package com.example.goplanner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ToDoFragment extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_to_do, container, false);
        RecyclerView fragToDoRV = view.findViewById(R.id.fragToDoRV);
        LinearLayoutManager layoutManager =new LinearLayoutManager(requireContext());
        fragToDoRV.setLayoutManager(layoutManager);
        TaskAdapter adapter = new TaskAdapter();
        fragToDoRV.setAdapter(adapter);

        return view;
    }


}