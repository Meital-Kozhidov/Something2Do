package com.sysaid.assignment.controller;

import com.sysaid.assignment.domain.Task;
import com.sysaid.assignment.enums.OptionEnum;
import com.sysaid.assignment.enums.StatusEnum;
import com.sysaid.assignment.exception.NotFoundKeyException;
import com.sysaid.assignment.exception.UnexpectedException;
import com.sysaid.assignment.exception.InvalidAmountException;
import com.sysaid.assignment.exception.InvalidOptionException;
import com.sysaid.assignment.exception.InvalidStatusException;
import com.sysaid.assignment.service.TaskServiceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/******************************************************************************/

/**
 * the controller is a basic structure and save some time on "dirty" work.
 */

@RestController
public class TaskController {

	private final TaskServiceImpl taskService;

	/**
	 * constructor for dependency injection
	 * @param taskService
	 */
	public TaskController(TaskServiceImpl taskService) {
		this.taskService = taskService;
	}

	/****************************************************************************/

	/**
	 * Return incomplete tasks, filtered to the requested type,
	 * in the requested amount.
	 * @param amount tasks amount - default is 10
	 * @param type task type to filter by - default is not filtered
	 * @return list of tasks
	 * @throws InvalidAmountException
	 * @throws InvalidOptionException
	 * @throws UnexpectedException
	 */
	@GetMapping("/tasks")
	public ResponseEntity<List<Task>> getIncompleteTasks(
		@RequestParam(name = "amount",required = false, defaultValue = "10") Integer amount,
		@RequestParam(name = "type",required = false, defaultValue = "") String type,
		@RequestParam(name = "option",required = false) String option
	) throws InvalidAmountException , InvalidOptionException {
		if (option == null) {
			option = OptionEnum.RANDOM.get();
		}
			return ResponseEntity.ok(this.taskService.getTasks(amount, type, option));
	}

	/**
	 * Update task status for user
	 * @param username name of the user to update the task for
	 * @param status status of the task - 'complete' or 'wishlist'
	 * @param key task's key
	 * @throws InvalidStatusException
	 * @throws InvalidKeyException 
	 */
	@PatchMapping("/tasks/{username}")
	public ResponseEntity<String> updateTaskStatus(
		@PathVariable ("username") String username, 
		@RequestParam(name = "status", required = true) String status,
		@RequestParam(name = "key", required = true) String key) 
		throws InvalidStatusException, NotFoundKeyException {
			this.taskService.updateTaskStatus(key, username, status);
			return ResponseEntity.status(HttpStatus.OK).body("updated");
	}


	/**
	 * Returns user's tasks, filtered by status
	 * @param user name of the user
	 * @param status status of the task - 'complete' or 'wishlist'.
	 * default is no filter.
	 * @return user's tasks
	 * @throws InvalidStatusException
	 */
	@GetMapping("/tasks/{username}")
	public ResponseEntity<List<Task>> getUserTasks(
		@PathVariable ("username") String username,
		@RequestParam(name = "status",required = false) String status) throws InvalidStatusException {
			if (status == null) {
				status = StatusEnum.ALL.get();
			}
			return ResponseEntity.ok(this.taskService.getUserTasks(username, status));
	}

	/****************************************************************************/
	
	/**
	 * Return the random task of the day
	 * @return task of the day
	 */
	@GetMapping("/tasks/task-of-the-day")
	public ResponseEntity<Task> getRandomTaskOfTheDay() {
		return ResponseEntity.ok(this.taskService.getTaskOfTheDay());
	}
}

