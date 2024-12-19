package com.example.goplanner;

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

import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

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
        databaseReference = FirebaseDatabase.getInstance().getReference("tasks");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_add_task, container, false);
        final String[] selectedType = {""};
        editTitleET = view.findViewById(R.id.editTitleET);
        editDateET = view.findViewById(R.id.editDateET);
        editTimeStartET = view.findViewById(R.id.editTimeStartET);
        editTimeEndET = view.findViewById(R.id.editTimeEndET);
        editDescET = view.findViewById(R.id.editDescET);
        editEventBtn = view.findViewById(R.id.editEventBtn);
        editTaskBtn = view.findViewById(R.id.editTaskBtn);
        editSubmitBtn = view.findViewById(R.id.editSubmitBtn);
        editCalendarView = view.findViewById(R.id.editCalendarView);

        Bundle args = getArguments();
        if (args != null && args.containsKey("SELECTED_DATE")) {
            String selectedDate = args.getString("SELECTED_DATE");
            editDateET.setText(selectedDate);

            String[] dateParts = selectedDate.split("-");
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]) - 1; // Bulan dimulai dari 0 di Calendar
            int year = Integer.parseInt(dateParts[2]);

            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.set(year, month, day);
            editCalendarView.setDate(calendar.getTimeInMillis());
        }

        editCalendarView.setOnDateChangeListener((v, year, month, dayOfMonth) -> {
                String selectedDate = dayOfMonth + "-" + (month+1) + "-" + year;
                editDateET.setText(selectedDate);
        });

        editEventBtn.setOnClickListener(v->{
            selectedType[0] ="Event";
            editEventBtn.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            editEventBtn.setTextColor(Color.WHITE);
            editTaskBtn.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            editTaskBtn.setTextColor(Color.BLACK);

        });

        editTaskBtn.setOnClickListener(v->{
            selectedType[0] ="Task";
            editTaskBtn.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            editTaskBtn.setTextColor(Color.WHITE);
            editEventBtn.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            editEventBtn.setTextColor(Color.BLACK);
        });


        editSubmitBtn.setOnClickListener(v->{
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
                        String uniqueId = documentReference.getId();
                        Log.d("Firestore", "Reminder added: " + uniqueId);
                        Toast.makeText(getContext(), "Reminder Added", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();

                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Error adding reminder", e);
                    });


        });
        return view;
    }



}