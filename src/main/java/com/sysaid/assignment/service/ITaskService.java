package com.sysaid.assignment.service;

import com.sysaid.assignment.domain.Task;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface ITaskService {
    public ResponseEntity<Task> getRandomTask();
    public List<Task> getTasks(Integer amount, String type, String option);
    public void updateTaskStatus(String key, String username, String status);
    public List<Task> getUserTasks(String username, String status);
    public Task getTaskOfTheDay();
}
