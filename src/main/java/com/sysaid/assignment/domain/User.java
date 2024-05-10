package com.sysaid.assignment.domain;

import java.util.ArrayList;
import java.util.List;

/******************************************************************************/

public class User {

  private String username;
  private final List<Task> wishlistTasks;
  private final List<Task> completedTasks;

  public User(String username) {
    this.username = username;
    this.wishlistTasks = new ArrayList<Task>();
    this.completedTasks = new ArrayList<Task>();
  }

  /****************************************************************************/

  public String getName () {
    return this.username;
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
