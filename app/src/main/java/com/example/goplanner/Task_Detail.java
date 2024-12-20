package com.example.goplanner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Task_Detail extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task__detail, container, false);

        EditText detailDescET = view.findViewById(R.id.detailDescET);
        TextView detailDateTV = view.findViewById(R.id.detailDateTV);
        EditText detailStartTimeET = view.findViewById(R.id.detailTimeStartET);
        EditText detailEndTimeET = view.findViewById(R.id.detailTimeEndET);
        TextView detailTitleTV = view.findViewById(R.id.detailTitleET);
        Button detailSaveBtn = view.findViewById(R.id.detailSaveBtn);
        Button detailDeleteBtn = view.findViewById(R.id.detailDeleteBtn);
        Button detailEventBtn = view.findViewById(R.id.detailEventBtn);

        if (getArguments() != null) {
            String title = getArguments().getString("TITLE");
            String date = getArguments().getString("DATE");
            String timeStart = getArguments().getString("TIME_START");
            String timeEnd = getArguments().getString("TIME_END");
            String desc = getArguments().getString("DESC");
            String type = getArguments().getString("TYPE");

            detailTitleTV.setText(title);
            detailDateTV.setText(date);
            detailDescET.setText(desc);
            detailStartTimeET.setText(timeStart);
            detailEndTimeET.setText(timeEnd);
            detailEventBtn.setText(type);


        }

        detailSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle save button click (e.g., update task details in database)
                // ... (code to save task details)
            }
        });

        detailDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete button click (e.g., delete task from database)
                // ... (code to delete task)
            }
        });




        return view;
    }
}