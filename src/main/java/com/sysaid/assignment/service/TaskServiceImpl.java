package com.sysaid.assignment.service;

import com.sysaid.assignment.domain.Task;
import com.sysaid.assignment.domain.TaskManager;
import com.sysaid.assignment.domain.TaskOfTheDay;
import com.sysaid.assignment.exception.FetchTaskException;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/******************************************************************************/

@Service
public class TaskServiceImpl implements ITaskService {

    private final TaskOfTheDay taskOfTheDay;
    private final TaskManager taskManager;
    private final String baseUrl;

    public TaskServiceImpl(
        @Value("${external.boredapi.baseURL}") String baseUrl,
        @Value("${tasks.numberOfTasks}") Integer taskAmount
    ) {
        this.baseUrl = baseUrl;
        this.taskOfTheDay = TaskOfTheDay.getInstance(this::getRandomTask);
        this.taskManager = TaskManager.getInstance(this::getRandomTask, taskAmount);
    }

    public Task getRandomTask() {
        String endpointUrl = String.format("%s/activity", baseUrl);

        RestTemplate template = new RestTemplate();
        ResponseEntity<Task> responseEntity = template.getForEntity(endpointUrl, Task.class);

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new FetchTaskException();
        } 

        return responseEntity.getBody();
    }

    public List<Task> getTasks(Integer amount, String type, String option) {
        return this.taskManager.getTasks(amount, type, option);
    }

    public void updateTaskStatus(String key, String username, String status) {
        this.taskManager.updateTaskStatus(key, username, status);
    }

    public List<Task> getUserTasks(String username, String status) {
        return this.taskManager.getUserTasks(username, status);
    }

    public Task getTaskOfTheDay() {
        return this.taskOfTheDay.getTask();
    }
}

