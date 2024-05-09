package com.sysaid.assignment.domain;

import java.util.ArrayList;
import java.util.List;

/******************************************************************************/

public class User {

  private String name;
  private List<Task> wishlistTasks;
  private List<Task> completedTasks;

  public User(String name) {
    this.name = name;
    this.wishlistTasks = new ArrayList<Task>();
    this.completedTasks = new ArrayList<Task>();
  }

  /****************************************************************************/

  public String getName () {
    return this.name;
  }
  public void setName(String name) {
    this.name = name;
  }

  public List<Task> getWishlistTasks () {
    return this.wishlistTasks;
  }

  public List<Task> getCompletedTasks () {
    return this.completedTasks;
  }

  /****************************************************************************/

  public void setTaskAsCompleted(Task task) {
    this.completedTasks.add(task);
  }

  public void setTaskAsWishList(Task task) {
    this.wishlistTasks.add(task);
  }
}
