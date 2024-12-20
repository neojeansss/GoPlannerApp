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

import java.util.ArrayList;
import java.util.List;

public class PanelList extends Fragment {
    private List<TaskData> taskData = new ArrayList<>();
    private TaskAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_panel_list, container, false);

        // Initialize views
        TextView panelDateTV = view.findViewById(R.id.panelDateTV);
        RecyclerView recyclerView = view.findViewById(R.id.panelListRV);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TaskAdapter(taskData); // Initialize adapter with an empty list
        recyclerView.setAdapter(adapter);

        // Get the selected date from arguments
        if (getArguments() != null) {
            String selectedDate = getArguments().getString("SELECTED_DATE");
            if (selectedDate != null) {
                // Display the selected date
                panelDateTV.setText(selectedDate);

                // Fetch tasks for the selected date
                fetchTasksForDate(selectedDate);
            }
        }

        return view;
    }

    private void fetchTasksForDate(String selectedDate) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("Firestore", "Fetching tasks for date: " + selectedDate);

        db.collection("tasks")
                .whereEqualTo("date", selectedDate) // Filter by the selected date
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<TaskData> fetchedData = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("Firestore", "Document fetched: " + document.getData());
                            TaskData taskItem = document.toObject(TaskData.class);
                            fetchedData.add(taskItem);
                        }
                        updateTaskData(fetchedData);
                    } else {
                        Log.e("Firestore", "Error fetching tasks", task.getException());
                    }
                });
    }


    private void updateTaskData(List<TaskData> fetchedData) {
        Log.d("Firestore", "Updating task data with: " + fetchedData.size() + " items");
        taskData.clear();
        taskData.addAll(fetchedData);
        adapter.notifyDataSetChanged();
    }
}
