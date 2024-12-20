package com.example.goplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Task_Detail extends Fragment {

    private DatabaseReference databaseReference;
    private String finalTaskId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference("reminders");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
            finalTaskId = getArguments().getString("ID");  // Get the task ID

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

                if (finalTaskId != null) {

                    String updatedDesc = detailDescET.getText().toString();
                    String updatedStartTime = detailStartTimeET.getText().toString();
                    String updatedEndTime = detailEndTimeET.getText().toString();
                    String updatedTitle = detailTitleTV.getText().toString();  // Assuming title is editable
                    String updatedDate = detailDateTV.getText().toString();    // Assuming date is editable
                    String updatedType = detailEventBtn.getText().toString();  // Assuming type is editable


                    TaskData updatedTask = new TaskData(finalTaskId, updatedTitle, updatedDate, updatedStartTime, updatedEndTime, updatedDesc, updatedType);

                    databaseReference.child(finalTaskId).setValue(updatedTask)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getContext(), "Task updated successfully!", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Failed to update task.", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Toast.makeText(getContext(), "Task ID is null. Cannot update task.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        detailDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (finalTaskId != null) {

                    databaseReference.child(finalTaskId).removeValue()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getContext(), "Task deleted successfully!", Toast.LENGTH_SHORT).show();
                                getActivity().onBackPressed();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Failed to delete task.", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Toast.makeText(getContext(), "Task ID is null. Cannot delete task.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
