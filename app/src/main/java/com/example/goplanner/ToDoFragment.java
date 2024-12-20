package com.example.goplanner;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ToDoFragment extends Fragment {

    private List<TaskData> taskData;
    private TaskAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskData = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_to_do, container, false);

        RecyclerView fragToDoRV = view.findViewById(R.id.fragToDoRV);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        fragToDoRV.setLayoutManager(layoutManager);

        adapter = new TaskAdapter(taskData);
        fragToDoRV.setAdapter(adapter);

        fetchTasksFromFirestore();

        return view;
    }

    private void fetchTasksFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Calculate start and end of the current week
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        Date startOfWeek = calendar.getTime(); // Start of the week
        calendar.add(Calendar.DAY_OF_WEEK, 6);
        Date endOfWeek = calendar.getTime(); // End of the week

        // Format dates to match Firestore's date format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Adjust to Firestore format
        String startOfWeekStr = sdf.format(startOfWeek);
        String endOfWeekStr = sdf.format(endOfWeek);

        Log.d("Firestore", "Querying dates from: " + startOfWeekStr + " to " + endOfWeekStr);

        db.collection("reminders")
                .whereGreaterThanOrEqualTo("date", startOfWeekStr) // Start of the week
                .whereLessThanOrEqualTo("date", endOfWeekStr) // End of the week
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    taskData.clear(); // Clear the list before adding new data
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        TaskData task = document.toObject(TaskData.class);
                        Log.d("Firestore", "Fetched task: " + task.getTitle());
                        taskData.add(task); // Add task to the list
                    }
                    adapter.notifyDataSetChanged(); // Notify the adapter to update the RecyclerView
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching tasks", e));
    }

}
