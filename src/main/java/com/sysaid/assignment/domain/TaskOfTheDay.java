package com.sysaid.assignment.domain;

import org.springframework.scheduling.annotation.Scheduled;

import java.util.function.Supplier;
/******************************************************************************/

public class TaskOfTheDay {
  private Task task;
  private Supplier<Task> getNewTask;
  private static TaskOfTheDay instance;

  private TaskOfTheDay(Supplier<Task> getNewTask) {
    this.getNewTask = getNewTask;
    scheduleFixedDelayTask();
  }

  public static TaskOfTheDay getInstance(Supplier<Task> getNewTask) {
    if (instance == null) {
      instance = new TaskOfTheDay(getNewTask);
    }

    return instance;
  }

  /**
	 * Scheduled job to run everyday at midnight, fetching a new task 
	 * and updating the task of the day
	 */
	@Scheduled(cron = "*/10 * * * * *")
	public void scheduleFixedDelayTask() {
		this.task = this.getNewTask.get();
	}

  /****************************************************************************/


  public Task getTask () {
    return this.task;
  }

}
