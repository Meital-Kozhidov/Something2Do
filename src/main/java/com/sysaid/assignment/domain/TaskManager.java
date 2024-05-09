package com.sysaid.assignment.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sysaid.assignment.exception.InvalidAmountException;
import com.sysaid.assignment.exception.InvalidOptionException;
import com.sysaid.assignment.exception.InvalidStatusException;
import com.sysaid.assignment.exception.InvalidKeyException;

/******************************************************************************/

public class TaskManager {
  private List<Task> wishlistTasks;
  private List<User> users;

  public TaskManager() {
    this.wishlistTasks = new ArrayList<Task>();
    this.users = new ArrayList<User>();
  }

  /****************************************************************************/

  public List<Task> getWishlistTasks () {
    return this.wishlistTasks;
  }

  public void addWishlistTask(Task task) {
    this.wishlistTasks.add(task);
  }

  /** **************************************************************************/

  public List<Task> getTasks(Integer amount , String type, String option) throws Exception {
    switch (option) {
      case "random":
        return this.getRandomTasks(amount, type);
      case "rated":
        return RatedOption.getRatedTasks(this.wishlistTasks, amount);
      default:
        throw new InvalidOptionException();
       
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
      throw new InvalidAmountException();
    }
  }

  public void updateTaskStatus(String key, String userName, String status)  throws Exception {
    User user = getUserByName(userName);

    try {
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
          throw new InvalidStatusException();
      }
    } catch (Exception e) {
      throw new InvalidKeyException();
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
        throw new InvalidStatusException();
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
}

