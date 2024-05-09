package com.sysaid.assignment.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/******************************************************************************/

public class TaskManager {
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

  /**
   * @throws Exception **************************************************************************/

  public List<Task> getTasks(Integer amount , String type, String option) throws Exception {
    switch (option) {
      case "random":
        return this.getRandomTasks(amount, type);
      case "rated":
        return RatedOption.getRatedTasks(this.wishlistTasks, amount);
      default:
        throw new Exception("Invalid option - random or rated");
       
    }
  }

  private List<Task> getRandomTasks(Integer amount , String type) throws Exception {
    List<Task> filteredTasks = this.wishlistTasks;
    if (!type.equals("")) {
      filteredTasks = this.wishlistTasks.stream()
              .filter(item -> type.equals(""))
              .collect(Collectors.toList());
    }

		Collections.shuffle(filteredTasks);

    try {
      return filteredTasks.subList(0, amount);
    } catch (Exception e) {
      throw new Exception("This amount of tasks of requested type do not exist (did you specify amount? default is 10)");
    }
  }

  public void updateTaskStatus(String key, String userName, String status)  throws Exception {
    User user = getUserByName(userName);

    Task task = this.wishlistTasks.stream()
            .filter(item -> item.getKey().equals(key)).limit(1)
            .collect(Collectors.toList())
            .get(0);

    switch (status) {
      case "complete":
        user.setTaskAsCompleted(task);
        task.incrementRating(2);
        break;
      case "wishlist":
        user.setTaskAsWishList(task);
        task.incrementRating(1);
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

  private Exception getInvalidStatusException() {
    return new Exception("Status is not a valid option");
  }
}
