package com.example.goplanner;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    Context activityContext;
    List<TaskData>taskData;


    @NonNull
    @Override
    public TaskAdapter.TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        activityContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(activityContext);
        View view = inflater.inflate(R.layout.task_detail,parent,false);
        TaskHolder holder = new TaskHolder(view);
        return holder;
    }

    public TaskAdapter(List<TaskData> taskDataList) {
        this.taskData = taskDataList;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskHolder holder, int position) {
        String title = taskData.get(position).getTitle();
        String date = taskData.get(position).getDate();
        String timeStart = taskData.get(position).getTimeStart();
        String timeEnd = taskData.get(position).getTimeEnd();
        String type = taskData.get(position).getType();
        String desc = taskData.get(position).getDesc();
        String documentId = taskData.get(position).getDocumentId();

        holder.todoTitleTV.setText(title);
        holder.todoDateTV.setText(date);
        holder.todoStartTimeTV.setText(timeStart);
        holder.todoEndTimeTV.setText(timeEnd);
        holder.toDoTypeTV.setText(type);

        holder.todoTaskCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Prepare a Bundle to pass all task details
                Bundle bundle = new Bundle();
                bundle.putString("DOCUMENT_ID", documentId);
                bundle.putString("TITLE", title);
                bundle.putString("DATE", date);
                bundle.putString("DESC", desc);
                bundle.putString("TIME_START", timeStart);
                bundle.putString("TIME_END", timeEnd);
                bundle.putString("TYPE", type);
                 // Pass the document ID")

                // Navigate to Task_detail fragment
                Fragment taskDetailFragment = new Task_Detail();
                taskDetailFragment.setArguments(bundle);

                FragmentTransaction transaction = ((AppCompatActivity)activityContext).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentCV, taskDetailFragment);
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();
            }
        });


    }

    @Override
    public int getItemCount() {
        return taskData.size();
    }

    public class TaskHolder extends RecyclerView.ViewHolder{
        TextView todoTitleTV, todoDateTV, todoStartTimeTV, todoEndTimeTV, toDoTypeTV;
        ConstraintLayout todoTaskCL;
        public TaskHolder(@NonNull View itemView) {
            super(itemView);

            todoTitleTV = itemView.findViewById(R.id.todoTitleTV);
            todoDateTV = itemView.findViewById(R.id.todoDateTV);
            todoStartTimeTV = itemView.findViewById(R.id.todoStartTimeTV);
            todoEndTimeTV = itemView.findViewById(R.id.todoEndTimeTV);
            todoTaskCL = itemView.findViewById(R.id.todoTaskCL);
            toDoTypeTV = itemView.findViewById(R.id.toDoStatusTV);
        }
    }
}
