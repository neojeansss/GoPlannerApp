package com.example.goplanner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScheduleFragment extends Fragment {

    private String selectedDate;
    private FirebaseFirestore firestore; // Firestore instance

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        MaterialButton mainAddBtn = view.findViewById(R.id.mainAddBtn);
        CalendarView calendarView = view.findViewById(R.id.calendarView2);


        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            selectedDate = year + "-" + String.format("%02d",month+1) + "-" + String.format("%02d", dayOfMonth);
            Log.d("ScheduleFragment", "Selected Date: " + selectedDate);

            // Fetch data from Firestore for the selected date
            fetchRemindersForDate(selectedDate);
        });

        mainAddBtn.setOnClickListener(v -> {
            Fragment addTaskFragment = new AddTaskFragment();
            Bundle bundle = new Bundle();
            bundle.putString("SELECTED_DATE", selectedDate);
            addTaskFragment.setArguments(bundle);

            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentCV, addTaskFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }

    private void fetchRemindersForDate(String date) {
        firestore.collection("reminders")
                .whereEqualTo("date", date)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<String> reminders = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("Firestore", "Document fetched: " + document.getData());
                            reminders.add(document.getString("title")); // Fetch "title"
                        }

                        if (reminders.isEmpty()) {
                            PanelNoList panelNoList = new PanelNoList();
                            showFragment(panelNoList, date);
                        } else {
                            PanelList panelList = new PanelList();
                            showFragment(panelList, date);
                        }
                    } else {
                        Log.e("Firestore", "Error fetching reminders", task.getException());
                    }
                });
    }

    private void showFragment(Fragment fragment, String date) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("SELECTED_DATE", date);
        fragment.setArguments(bundle);
        transaction.replace(R.id.childFragmentContainer, fragment);
        transaction.commitAllowingStateLoss();
    }
}
