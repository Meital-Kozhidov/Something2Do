package com.sysaid.assignment.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/******************************************************************************/

public class TaskManager {
  // private List<Task> completedTasks;
  private List<Task> wishlistTasks;
  private List<User> users;

  public TaskManager(List<Task> wishlistTasks) {
    this.wishlistTasks = wishlistTasks;
    this.users = new ArrayList<User>();
  }

  /****************************************************************************/

  public List<Task> getWishlistTasks () {
    return this.wishlistTasks;
  }
  public void setWishlistTasks(List<Task> wishlistTasks) {
    this.wishlistTasks = wishlistTasks;
  }

  /****************************************************************************/

  public List<Task> getRandomTasks(Integer amount , String type) {
    List<Task> filteredTasks = this.wishlistTasks.stream()
            .filter(item -> item.getType().equals(type))
            .toList();

		Collections.shuffle(filteredTasks);

    return filteredTasks.subList(0, amount);
  }

  public List<Task> getRatedTasks(Integer amount , String type) {
    //TODO
    return this.wishlistTasks;
  }

  public void updateTaskStatus(String key, String userName, String status)  throws Exception {
    User user = getUserByName(userName);

    Task task = this.wishlistTasks.stream()
            .filter(item -> item.getKey().equals(key)).limit(1)
            .toList()
            .get(0);

    switch (status) {
      case "complete":
        user.setTaskAsCompleted(task);
        break;
      case "wishlist":
        user.setTaskAsWishList(task);
        break;
      default:
        throw getInvalidStatusException();
    }
  }

  public List<Task> getUserTasks(String userName, String status) 
  throws Exception {
    User user = getUserByName(userName);

    switch (status) {
      case "complete":
        return user.getCompletedTasks();
      case "wishlist":
        return user.getWishlistTasks();
      case "":
        return Stream.concat(user.getCompletedTasks().stream(), 
                            user.getWishlistTasks().stream())
                     .toList();
      default:
        throw getInvalidStatusException();
    }
  }

  /****************************************************************************/

  private User getUserByName(String userName) {
    List<User> users = this.users.stream()
              .filter(item -> item.getName().equals(userName))
              .limit(1)
              .toList();

    User user;
    if (users.size() == 0) {
      user = new User(userName);
      this.users.add(user);
    } else {
      user = users.get(0);
    }

    return user;
  }

  private Exception getInvalidStatusException() {
    return new Exception("Status is not a valid option");
  }
}
