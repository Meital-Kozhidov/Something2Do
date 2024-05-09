package com.sysaid.assignment.domain;

/******************************************************************************/

public class TaskOfTheDay {
  private Task task;

  public TaskOfTheDay(Task task) {
    this.task = task;
  }

  /****************************************************************************/


  public Task getTask () {
    return this.task;
  }
  public void setTask(Task task) {
    this.task = task;
  }
}
