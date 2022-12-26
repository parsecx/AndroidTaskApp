package com.example.myapplication;

import java.util.ArrayList;

public class Tasks {
    private final ArrayList<Task> taskList;

    Tasks() {
        taskList = new ArrayList<>();
    }

    public void add(Task task) {
        taskList.add(task);
    }

    public void remove(int id) {
        taskList.remove(id);
    }

    public ArrayList<Task> getTaskList() {
        return taskList;
    }
}
