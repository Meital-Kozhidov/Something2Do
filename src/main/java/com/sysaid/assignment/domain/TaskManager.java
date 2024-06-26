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
import com.sysaid.assignment.exception.NotFoundKeyException;

/******************************************************************************/

public class TaskManager {
  private static TaskManager instance;
  private final List<Task> wishlistTasks;
  private final UserManager userManager;
  private final RatedOption ratedOption;

  private TaskManager(Supplier<Task> taskSupplier, Integer taskAmount) {
    this.wishlistTasks = new ArrayList<Task>();

    for (int i = 0; i < taskAmount; ++i) {
      this.wishlistTasks.add(taskSupplier.get());
    }

    this.ratedOption = new RatedOption(this.wishlistTasks);
    this.userManager = UserManager.getInstance();
  }

  public static TaskManager getInstance(Supplier<Task> taskSupplier, Integer tasksAmount) {
    if (instance == null) {
      instance = new TaskManager(taskSupplier, tasksAmount);
    }

    return instance;
  }

  /****************************************************************************/

  public List<Task> getTasks(Integer amount , String type, String option) {
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

  public void updateTaskStatus(String key, String username, String status) {
    User user = this.userManager.getUserByUsername(username);

    Task task = this.findTaskByKey(key);

    try {
      StatusEnum statusEnum = StatusEnum.valueOf(status.toUpperCase());
      switch (statusEnum) {
        case COMPLETE:
          user.setTaskAsCompleted(task);
          task.incrementRating(2);
          break;
        case WISHLIST:
          user.setTaskAsWishList(task);
          task.incrementRating(2);
          break;
        default:
          throw new InvalidStatusException();
      }
    } catch (Exception e) {
        throw new InvalidStatusException();
    }
  }

  public List<Task> getUserTasks(String username, String status) {
    User user = this.userManager.getUserByUsername(username);

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
                    .collect(Collectors.toList());
        default:
          throw new InvalidStatusException();
      }
    } catch (Exception e) {
        throw new InvalidStatusException();
    }
  }

  /****************************************************************************/
  
  private Task findTaskByKey(String key) {
    return this.wishlistTasks
            .stream()
            .filter(item -> item.getKey().equals(key))
            .findFirst()
            .orElseThrow(() -> new NotFoundKeyException());
  }
  
  private List<Task> getRandomTasks(Integer amount , String type) {
    List<Task> filteredTasks = this.wishlistTasks;
    if (!type.equals("")) {
      filteredTasks = this.wishlistTasks.stream()
              .filter(item -> item.getType().equals(type))
              .collect(Collectors.toList());
    }

		Collections.shuffle(filteredTasks);

    try {
      return filteredTasks.subList(0, amount);
    } catch (Exception e) {
      throw new InvalidAmountException();
    }
  }
}

