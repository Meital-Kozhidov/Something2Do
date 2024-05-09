package com.sysaid.assignment.controller;

import com.sysaid.assignment.domain.Task;
import com.sysaid.assignment.domain.TaskManager;
import com.sysaid.assignment.domain.TaskOfTheDay;
import com.sysaid.assignment.service.TaskServiceImpl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.ArrayList;

/******************************************************************************/

/**
 * the controller is a basic structure and save some time on "dirty" work.
 */

@RestController
public class TaskController {

	private final TaskServiceImpl taskService;
	private TaskOfTheDay taskOfTheDay;
	private TaskManager taskManager;

	/**
	 * constructor for dependency injection
	 * @param taskService
	 */
	public TaskController(TaskServiceImpl taskService) {
		this.taskService = taskService;
		this.taskOfTheDay = new TaskOfTheDay(getNewRandomTask());

		List<Task> incompleteTasks = new ArrayList<Task>();

		//TODO: get pool number as param
		for (int i = 0; i < 20; ++i) {
			incompleteTasks.add(getNewRandomTask());
		}
		this.taskManager = new TaskManager(incompleteTasks);
	}

	private Task getNewRandomTask() {
		return this.taskService.getRandomTask().getBody();
	}

	//TODO: better location
	/**
	 * Scheduled job to run everyday at midnight, fetching a new task 
	 * and updating the task of the day
	 */
	@Scheduled(cron = "0 0 0 * * *")
	public void scheduleFixedDelayTask() {
		this.taskOfTheDay.setTask(getNewRandomTask());
	}

	/****************************************************************************/

	/**
	 * Return incomplete tasks, filtered to the requested type,
	 * in the requested amount.
	 * @param amount tasks amount - default is 10
	 * @param type task type to filter by - default is not filtered
	 * @return list of tasks
	 * @throws Exception 
	 */
	@GetMapping("/tasks")
	public List<Task> getIncompleteTasks(
		@RequestParam(name = "amount",required = false, defaultValue = "10") Integer amount,
		@RequestParam(name = "type",required = false, defaultValue = "") String type,
		@RequestParam(name = "option",required = false, defaultValue = "random") String option
	) throws Exception{
			return this.taskManager.getTasks(amount, type, option);
	}

	/**
	 * Update task status for user
	 * @param username name of the user to update the task for
	 * @param status status of the task - 'complete' or 'wishlist'
	 * @param key task's key
	 * @throws Exception 
	 */
	@PatchMapping("/tasks/{username}")
	public void updateTaskStatus(
		@PathVariable ("username") String username, 
		@RequestParam(name = "status", required = true) String status,
		@RequestParam(name = "key", required = true) String key) 
		throws Exception {
			this.taskManager.updateTaskStatus(key, username, status);
	}


	/**
	 * Returns user's tasks, filtered by status
	 * @param user name of the user
	 * @param status status of the task - 'complete' or 'wishlist'.
	 * default is no filter.
	 * @return user's tasks
	 */
	@GetMapping("/tasks/{username}")
	public List<Task> getAllTasks(
		@PathVariable ("username") String username,
		@RequestParam(name = "status",required = false, defaultValue = "") String status) throws Exception {
			return this.taskManager.getUserTasks(username, status);
	}

	/****************************************************************************/
	
	/**
	 * Return the random task of the day
	 * @param rated should get the rated task of the day instead the random
	 * @return
	 * @throws Exception 
	 */
	@GetMapping("/tasks/task-of-the-day")
	public  Task getRandomTaskOfTheDay(
		@RequestParam(name = "option",required = false, defaultValue = "random") String option) throws Exception {
			return this.taskOfTheDay.getTask();
	}
}

