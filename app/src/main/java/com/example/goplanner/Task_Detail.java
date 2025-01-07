package com.example.goplanner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Task_Detail extends Fragment {

    private FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance("reminders");
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
        String documentId = "";

        if (getArguments() != null) {
            documentId = getArguments().getString("DOCUMENT_ID");
            String title = getArguments().getString("TITLE");
            String date = getArguments().getString("DATE");
            String timeStart = getArguments().getString("TIME_START");
            String timeEnd = getArguments().getString("TIME_END");
            String desc = getArguments().getString("DESC");
            String type = getArguments().getString("TYPE");
            String formattedDate = formatDate(date);

            detailTitleTV.setText(title);
            detailDateTV.setText(formattedDate);
            detailDescET.setText(desc);
            detailStartTimeET.setText(timeStart);
            detailEndTimeET.setText(timeEnd);
            detailEventBtn.setText(type);

            if (getArguments() != null && getArguments().containsKey("DOCUMENT_ID")) {
                documentId = getArguments().getString("DOCUMENT_ID");
                Log.d("Firestore", "Received Document ID: " + documentId);
            } else {
                Log.e("Firestore", "DOCUMENT_ID is missing in arguments");
            }
        }

        String finalDocumentId1 = documentId;
        detailSaveBtn.setOnClickListener(v -> {
            if (finalDocumentId1 != null) {
                String updatedTitle = detailTitleTV.getText().toString();
                String updatedDate = detailDateTV.getText().toString();
                String updatedTimeStart = detailStartTimeET.getText().toString();
                String updatedTimeEnd = detailEndTimeET.getText().toString();
                String updatedDesc = detailDescET.getText().toString();
                String updatedType = detailEventBtn.getText().toString();

                if (updatedTitle.isEmpty() || updatedDate.isEmpty() || updatedTimeStart.isEmpty() ||
                        updatedTimeEnd.isEmpty() || updatedDesc.isEmpty() || updatedType.isEmpty()) {
                    Toast.makeText(getContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Object> updatedTask = new HashMap<>();
                updatedTask.put("title", updatedTitle);
                updatedTask.put("date", updatedDate);
                updatedTask.put("timeStart", updatedTimeStart);
                updatedTask.put("timeEnd", updatedTimeEnd);
                updatedTask.put("desc", updatedDesc);
                updatedTask.put("type", updatedType);
                String finalDocumentId = finalDocumentId1;
                db.collection("reminders")
                        .document(finalDocumentId)
                        .update(updatedTask)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(getContext(), "Task updated successfully", Toast.LENGTH_SHORT).show();
                            requireActivity().getSupportFragmentManager().popBackStack();
                        })
                        .addOnFailureListener(e -> {
                            Log.e("Firestore", "Error updating task", e);
                            Toast.makeText(getContext(), "Failed to update task", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(getContext(), "Error: Document ID is missing", Toast.LENGTH_SHORT).show();
            }
        });

        detailDeleteBtn.setOnClickListener(v -> {

        });

        return view;
    }

    private String formatDate(String dateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = inputFormat.parse(dateString);

            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }
    }
}
