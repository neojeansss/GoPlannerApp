package com.example.goplanner;

import android.app.TimePickerDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddTaskFragment extends Fragment {
    EditText editTitleET, editDateET, editTimeStartET, editTimeEndET, editDescET;
    Button editEventBtn, editTaskBtn, editSubmitBtn;

    CalendarView editCalendarView;


    private DatabaseReference databaseReference;

    public AddTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference("reminders");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_task, container, false);
        final String[] selectedType = {""};

        editTitleET = view.findViewById(R.id.editTitleET);
        editDateET = view.findViewById(R.id.editDateET);
        editTimeStartET = view.findViewById(R.id.detailTimeStartET);
        editTimeEndET = view.findViewById(R.id.editTimeEndET);
        editDescET = view.findViewById(R.id.editDescET);
        editEventBtn = view.findViewById(R.id.detailEventBtn);
        editTaskBtn = view.findViewById(R.id.editTaskBtn);
        editSubmitBtn = view.findViewById(R.id.editSubmitBtn);
        editCalendarView = view.findViewById(R.id.editCalendarView);

        Bundle args = getArguments();
        if (args != null && args.containsKey("SELECTED_DATE")) {
            String selectedDate = args.getString("SELECTED_DATE");
            editDateET.setText(selectedDate);

            String[] dateParts = selectedDate.split("-");
            int day = Integer.parseInt(dateParts[2]);
            int month = Integer.parseInt(dateParts[1]) - 1; // Month is 0-based in Calendar
            int year = Integer.parseInt(dateParts[0]);

            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.set(year, month, day);

            editCalendarView.setDate(calendar.getTimeInMillis());
            Log.d("Calendar", "Date Back to normal");
        } else {
            // Set CalendarView to today's date
            java.util.Calendar today = java.util.Calendar.getInstance();
            Log.d("Calendar", "Date Back to today");
            editCalendarView.setDate(today.getTimeInMillis());
        }

        editCalendarView.setOnDateChangeListener((v, year, month, dayOfMonth) -> {
            String selectedDate = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth);
            editDateET.setText(selectedDate);
        });

        editEventBtn.setOnClickListener(v -> {
            selectedType[0] = "Event";
            editEventBtn.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            editEventBtn.setTextColor(Color.WHITE);
            editTaskBtn.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            editTaskBtn.setTextColor(Color.BLACK);
        });

        editTaskBtn.setOnClickListener(v -> {
            selectedType[0] = "Task";
            editTaskBtn.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            editTaskBtn.setTextColor(Color.WHITE);
            editEventBtn.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            editEventBtn.setTextColor(Color.BLACK);
        });

        editTimeStartET.setOnClickListener(v -> {
            showTimePickerDialog((time) -> editTimeStartET.setText(time));
        });

        // Time Picker for End Time
        editTimeEndET.setOnClickListener(v -> {
            showTimePickerDialog((time) -> editTimeEndET.setText(time));
        });

        editSubmitBtn.setOnClickListener(v -> {
            String title = editTitleET.getText().toString();
            String date = editDateET.getText().toString();
            String timeStart = editTimeStartET.getText().toString();
            String timeEnd = editTimeEndET.getText().toString();
            String desc = editDescET.getText().toString();
            String type = selectedType[0];
            if (title.isEmpty() || date.isEmpty() || timeStart.isEmpty() || timeEnd.isEmpty() || desc.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }





            // validation
            Time start = Time.valueOf(timeStart + ":00");
            Time end = Time.valueOf(timeEnd + ":00");
            if (start.after(end)) {
                Toast.makeText(getActivity(), "Start time must be earlier than End time", Toast.LENGTH_SHORT).show();
                return;
            }


            if (type.isEmpty()) {
                Toast.makeText(getActivity(), "Please select Event or Task", Toast.LENGTH_SHORT).show();
                return;
            }
            Map<String, Object> task = new HashMap<>();

            task.put("title", title);
            task.put("date", date);
            task.put("timeStart", timeStart);
            task.put("timeEnd", timeEnd);
            task.put("desc", desc);
            task.put("type", type);
            task.put("timestamp", new Date().getTime());

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("reminders")
                    .add(task)
                    .addOnSuccessListener(documentReference -> {
                        String uniqueId = documentReference.getId(); // Get the Document ID
                        Log.d("Firestore", "Generated Document ID: " + uniqueId);
                        task.put("documentId", uniqueId); // Add the documentId to the task map

                        // Now pass the documentId to the Task_Detail Fragment
                        Bundle bundle = new Bundle();
                        bundle.putString("DOCUMENT_ID", uniqueId);
                        bundle.putString("TITLE", title);
                        bundle.putString("DATE", date);
                        bundle.putString("TIME_START", timeStart);
                        bundle.putString("TIME_END", timeEnd);
                        bundle.putString("DESC", desc);
                        bundle.putString("TYPE", type);

                        Fragment taskDetailFragment = new Task_Detail();
                        taskDetailFragment.setArguments(bundle);

                        // Navigate to Task_Detail Fragment
                        requireActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentCV, taskDetailFragment)
                                .addToBackStack(null)
                                .commit();

                        Toast.makeText(getContext(), "Task added successfully", Toast.LENGTH_SHORT).show();

                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Error adding reminder", e);
                    });


        });

        return view;
    }



    private void showTimePickerDialog(TimeSetCallback callback) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
        int minute = calendar.get(java.util.Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                (view, selectedHour, selectedMinute) -> {
                    String formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                    callback.onTimeSet(formattedTime);
                },
                hour,
                minute,
                true // Use true for 24-hour format, false for AM/PM format
        );
        timePickerDialog.show();
    }

    // Define a callback interface for time selection
    interface TimeSetCallback {
        void onTimeSet(String time);
    }




}