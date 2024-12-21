package com.example.goplanner;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PanelList extends Fragment {
    private List<TaskData> taskData = new ArrayList<>();
    private TaskAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_panel_list, container, false);

        TextView panelDateTV = view.findViewById(R.id.panelDateTV);
        RecyclerView recyclerView = view.findViewById(R.id.panelListRV);

        if (getArguments() != null) {
            String selectedDate = getArguments().getString("SELECTED_DATE");
            if (selectedDate != null) {

                panelDateTV.setText(formatDate(selectedDate));


                fetchTasksForDate(selectedDate);
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TaskAdapter(taskData);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private String formatDate(String selectedDate) {
        try {
            SimpleDateFormat originalDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            originalDateFormat.setLenient(false);
            Date parsedDate = originalDateFormat.parse(selectedDate);

            SimpleDateFormat desiredDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            return desiredDateFormat.format(parsedDate);
        } catch (ParseException | IllegalArgumentException e) {
            Log.w("PanelList", "Invalid date format: " + selectedDate, e);
            return "Invalid Date";
        }
    }

    private void fetchTasksForDate(String selectedDate) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("Firestore", "Fetching tasks for date: " + selectedDate);

        db.collection("reminders")
                .whereEqualTo("date", selectedDate) // Filter by the selected date
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<TaskData> fetchedData = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("Firestore", "Document fetched: " + document.getData());
                            TaskData taskItem = document.toObject(TaskData.class);
                            fetchedData.add(taskItem);
                            Log.d("Firestore", "Fetched data: " + fetchedData.size() + " items");
                        }
                        updateTaskData(fetchedData);
                    } else {
                        Log.e("Firestore", "Error fetching tasks", task.getException());
                    }
                });
    }


    private void updateTaskData(List<TaskData> fetchedData) {
        Log.d("Firestore", "Updating task data with: " + fetchedData.size() + " items");

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        Collections.sort(fetchedData, (task1, task2) -> {
            try {
                Date time1 = timeFormat.parse(task1.getTimeStart());
                Date time2 = timeFormat.parse(task2.getTimeStart());
                return time1.compareTo(time2);
            } catch (ParseException e) {
                Log.e("TaskSort", "Error parsing timeStart for sorting", e);
                return 0;
            }
        });

        taskData.clear();
        taskData.addAll(fetchedData);
        adapter.notifyDataSetChanged();
    }
}