package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

public class SecondActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private Tasks taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        linearLayout = findViewById(R.id.linear_layout);
        ImageView addButton = findViewById(R.id.button_add_new_element);

        SharedPreferences preferencesLogin = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        String login = preferencesLogin.getString("LOGIN", null);
        TextView hello = findViewById(R.id.hello_title_tv);
        hello.setText(login);
        hello.setOnClickListener(v -> {
            SharedPreferences.Editor editor = preferencesLogin.edit();
            editor.putString("LOGIN", null);
            editor.apply();

            Intent startActivityIntent = new Intent(this, MainActivity.class);
            startActivity(startActivityIntent);
            finish();
        });


        addButton.setOnClickListener(v -> {
            Intent startActivityIntent = new Intent(this, TaskActivity.class);
            startActivityForResult(startActivityIntent, TaskActivity.REQUEST_CODE);
        });

        SharedPreferences preferences = getSharedPreferences("task", Context.MODE_PRIVATE);
        String tasksAsString = preferences.getString(TaskActivity.KEY_TASK, null);

        if (tasksAsString != null) {
            updateActivity(tasksAsString);
        } else {
            taskList = new Tasks();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) return;

        String taskAsStringOne = data.getStringExtra(TaskActivity.KEY_TASK);
        Task task = new Gson().fromJson(taskAsStringOne, Task.class);


        if (resultCode == TaskActivity.REQUEST_OK_CODE) {
            if (task.getName().equals("") || task.getDescription().equals("")) return;
            taskList.add(task);
        }
        else if(resultCode == TaskActivity.REQUEST_DELETE_CODE){
            taskList.remove(task.getId());
        }

        String taskAsString = new Gson().toJson(taskList);
        SharedPreferences sharedPreferences = getSharedPreferences("task", Context.MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();

        preferencesEditor.putString(TaskActivity.KEY_TASK, taskAsString);
        preferencesEditor.apply();

        updateActivity(taskAsString);
    }

    public void updateActivity(String tasksAsString) {
        linearLayout.removeAllViews();
        taskList = new Gson().fromJson(tasksAsString, Tasks.class);
        for (int i = 0; i < taskList.getTaskList().size(); i++) {
            Task task = taskList.getTaskList().get(i);
            task.setId(i);

            TextView textView = new TextView(this);
            textView.setText(task.getName());
            linearLayout.addView(textView);

            textView.setOnClickListener(v -> {
                String taskAsString = new Gson().toJson(task);
                Intent startActivityIntent = new Intent(this, TaskActivity.class);
                startActivityIntent.putExtra(TaskActivity.KEY_TASK, taskAsString);
                startActivityForResult(startActivityIntent, TaskActivity.REQUEST_CODE);
            });
        }
    }
}

