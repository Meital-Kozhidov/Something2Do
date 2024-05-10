package com.sysaid.assignment.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sysaid.assignment.enums.OptionEnum;
import com.sysaid.assignment.enums.StatusEnum;
import com.sysaid.assignment.exception.InvalidAmountException;
import com.sysaid.assignment.exception.InvalidOptionException;
import com.sysaid.assignment.exception.InvalidStatusException;
import com.sysaid.assignment.exception.InvalidKeyException;

/******************************************************************************/

public class TaskManager {
  private static TaskManager instance;
  private final List<Task> wishlistTasks;
  private final List<User> users;
  private RatedOption ratedOption;

  private TaskManager(Supplier<Task> getNewTask) {
    this.users = new ArrayList<User>();

    this.wishlistTasks = new ArrayList<Task>();
    for (int i = 0; i < 20; ++i) {
			this.wishlistTasks.add(getNewTask.get());
		}

    this.ratedOption = new RatedOption(this.wishlistTasks);
  }

  public static TaskManager getInstance(Supplier<Task> getNewTask) {
    if (instance == null) {
      instance = new TaskManager(getNewTask);
    }

    return instance;
  }

  /****************************************************************************/

  public List<Task> getWishlistTasks () {
    return this.wishlistTasks;
  }

  public List<Task> getTasks(Integer amount , String type, String option) throws InvalidOptionException, InvalidAmountException {

    try {
      OptionEnum optionEnum = OptionEnum.valueOf(option.toUpperCase());

      switch (optionEnum) {
        case RANDOM:
          return this.getRandomTasks(amount, type);
        case RATED:
          return this.ratedOption.getRatedTasks(amount);
        default:
          throw new InvalidOptionException();
      }
    } catch (Exception e) {
      throw new InvalidOptionException();
    }
  }

  private List<Task> getRandomTasks(Integer amount , String type) 
  throws InvalidAmountException {
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

  public void updateTaskStatus(String key, String userName, String status)  throws InvalidKeyException, InvalidAmountException {
    User user = getUserByUsername(userName);

    Task task;
    try {
      task = this.wishlistTasks.stream()
              .filter(item -> item.getKey().equals(key)).limit(1)
              .collect(Collectors.toList())
              .get(0);
    } catch (Exception e) { //TODO: check what is thrown here
      throw new InvalidKeyException();
    }

    try {
      StatusEnum statusEnum = StatusEnum.valueOf(status.toUpperCase());
      switch (statusEnum) {
        case COMPLETE:
          user.setTaskAsCompleted(task);
          task.incrementRating(2);
          break;
        case WISHLIST:
          user.setTaskAsCompleted(task);
          task.incrementRating(2);
          break;
        default:
          throw new InvalidStatusException();
      }
    } catch (Exception e) {
        throw new InvalidStatusException();
    }
  }

  public List<Task> getUserTasks(String userName, String status) 
  throws InvalidStatusException {
    User user = getUserByUsername(userName);

    try {
      StatusEnum statusEnum = StatusEnum.valueOf(status.toUpperCase());
      switch (statusEnum) {
        case COMPLETE:
          return user.getCompletedTasks();
        case WISHLIST:
          return user.getWishlistTasks();
        case ALL:
          return Stream.concat(user.getCompletedTasks().stream(), 
                    user.getWishlistTasks().stream())
                    .toList();
        default:
          throw new InvalidStatusException();
      }
    } catch (Exception e) {
        throw new InvalidStatusException();
    }
  }

  /****************************************************************************/

  /**
   * Return 'User' instance of a user with the given username.
   * If already exists, returns the instance. else, creates it.
   * @param username
   * @return User with given username
   */
  private User getUserByUsername(String username) {
    List<User> users = this.users.stream()
              .filter(item -> item.getName().equals(username))
              .limit(1)
              .collect(Collectors.toList());

    User user;
    if (users.size() == 0) {
      user = new User(username);
      this.users.add(user);
    } else {
      user = users.get(0);
    }

    return user;
  }
}

