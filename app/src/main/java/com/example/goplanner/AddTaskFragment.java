package com.example.goplanner;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class AddTaskFragment extends Fragment {
    EditText editTitleET, editDateET, editTimeStartET, editTimeEndET, editDescET;
    Button editEventBtn, editTaskBtn;
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

        editEventBtn.setOnClickListener(v->{
            selectedType[0] ="Event";
            editEventBtn.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
            editEventBtn.setTextColor(Color.WHITE);
            editTaskBtn.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
            editTaskBtn.setTextColor(Color.BLACK);

        });

        editTaskBtn.setOnClickListener(v->{
            selectedType[0] ="Task";
            editTaskBtn.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
            editTaskBtn.setTextColor(Color.WHITE);
            editEventBtn.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
            editTaskBtn.setTextColor(Color.BLACK);
        });
        return view;
    }

    private void saveTaskToFirebase(){
        String title = editTitleET.getText().toString();
        String date = editDateET.getText().toString();
        String timeStart = editTimeStartET.getText().toString();
        String timeEnd = editTimeEndET.getText().toString();
        String desc = editDescET.getText().toString();

        if(title.isEmpty() || date.isEmpty() || timeStart.isEmpty() || timeEnd.isEmpty() || desc.isEmpty()){
            Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

    }

}