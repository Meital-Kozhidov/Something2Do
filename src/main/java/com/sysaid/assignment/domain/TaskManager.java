package com.sysaid.assignment.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/******************************************************************************/

public class TaskManager {
  private List<Task> completedTasks;
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

  public List<Task> getCompletedTasks () {
    return this.completedTasks;
  }
   public void setCompletedTasks(List<Task> completedTasks) {
    this.completedTasks = completedTasks;
  }

  /****************************************************************************/

  public List<Task> getRandomTasks(Integer amount , String type) {
    List<Task> filteredTasks = this.wishlistTasks.stream()
            .filter(item -> item.getType().equals(type))
            .collect(Collectors.toList());

		Collections.shuffle(filteredTasks);

    return this.wishlistTasks.subList(0, amount);
  }

  public void updateTaskStatus(String key, String userName, String status) {
     User user = getUserByName(userName);

    Task task = this.wishlistTasks.stream()
            .filter(item -> item.getKey().equals(key)).limit(1)
            .collect(Collectors.toList())
            .get(0);

    if (status.equals("complete")) {
      user.setTaskAsCompleted(task);
    } else if (status.equals("wishlist")) {
      user.setTaskAsWishList(task);
    }

    //throw
  }

  public List<Task> getUserTasks(String userName, String status) {
    User user = getUserByName(userName);

    if (status.equals("complete")) {
      return user.getCompletedTasks();
    } else if (status.equals("wishlist")) {
      return user.getWishlistTasks();
    }
    return Stream.concat(user.getCompletedTasks().stream(), 
                         user.getWishlistTasks().stream())
                 .toList();
   
  }

  private User getUserByName(String userName) {
    List<User> users = this.users.stream()
              .filter(item -> item.getName().equals(userName))
              .limit(1)
              .collect(Collectors.toList());

    User user;
    if (users.size() == 0) {
      user = new User(userName);
      this.users.add(user);
    } else {
      user = users.get(0);
    }

    return user;
  }
}
