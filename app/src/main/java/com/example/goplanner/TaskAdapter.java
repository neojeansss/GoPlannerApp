package com.example.goplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskHolder holder, int position) {
        String title = taskData.get(position).getTitle();
        String date = taskData.get(position).getDate();
        String timeStart = taskData.get(position).getTimeStart();
        String timeEnd = taskData.get(position).getTimeEnd();
        String desc = taskData.get(position).getDesc();
        String type = taskData.get(position).getType();

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
            todoDateTV = itemView.findViewById(R.id.toDoDateTV);
            todoStartTimeTV = itemView.findViewById(R.id.todoStartTimeTV);
            todoEndTimeTV = itemView.findViewById(R.id.todoEndTimeTV);
            todoTaskCL = itemView.findViewById(R.id.todoTaskCL);
        }
    }
}
