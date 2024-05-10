package com.sysaid.assignment.domain;

import org.springframework.scheduling.annotation.Scheduled;

import java.util.function.Supplier;
/******************************************************************************/

public class TaskOfTheDay {
  private Task task;
  private Supplier<Task> taskSupplier;
  private static TaskOfTheDay instance;

  private TaskOfTheDay(Supplier<Task> taskSupplier) {
    this.taskSupplier = taskSupplier;
    scheduleFixedDelayTask();
  }

  public static TaskOfTheDay getInstance(Supplier<Task> taskSupplier) {
    if (instance == null) {
      instance = new TaskOfTheDay(taskSupplier);
    }

    return instance;
  }

  /**
	 * Scheduled job to run everyday at midnight, fetching a new task 
	 * and updating the task of the day
	 */
	@Scheduled(cron = "*/10 * * * * *")
	public void scheduleFixedDelayTask() {
		this.task = this.taskSupplier.get();
	}

  /****************************************************************************/


  public Task getTask () {
    return this.task;
  }

}
