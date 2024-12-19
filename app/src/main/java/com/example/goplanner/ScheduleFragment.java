package com.example.goplanner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.google.android.material.button.MaterialButton;

public class ScheduleFragment extends Fragment {

    private String selectedDate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);


        //Ini gak kerja



        MaterialButton mainAddBtn = view.findViewById(R.id.mainAddBtn);

        CalendarView calendarView = view.findViewById(R.id.calendarView2);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                //tanggal berubah, set di variable
                selectedDate = dayOfMonth + "-" + (month + 1) +  "-" + year;
            }
        });

        mainAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment addTaskFragment = new AddTaskFragment();
                Bundle bundle = new Bundle();
                bundle.putString("SELECTED_DATE", selectedDate); // Kirim tanggal
                addTaskFragment.setArguments(bundle);

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentCV, addTaskFragment);
                transaction.addToBackStack(null); // Tambahkan ke back stack untuk navigasi kembali
                transaction.commit();
            }
        });





        return view;

    }
}