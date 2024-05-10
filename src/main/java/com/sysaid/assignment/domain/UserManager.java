package com.sysaid.assignment.domain;

import java.util.HashMap;
import java.util.Map;

import com.sysaid.assignment.exception.DuplicatedUserException;
import com.sysaid.assignment.exception.NotFoundUserException;

/******************************************************************************/

public class UserManager {
  private static UserManager instance;
  private final Map<String, User> usersByUsername;

  private UserManager() {
    this.usersByUsername = new HashMap<>();
  }

  public static UserManager getInstance() {
    if (instance == null) {
        instance = new UserManager();
    }
    return instance;
  }

  /****************************************************************************/

  public User createUser(String username) {
    if (usersByUsername.containsKey(username)) {
        throw new DuplicatedUserException();
    }

    User user = new User(username);
    usersByUsername.put(username, user);
    return user;
  }

  public User getUserByUsername(String username) {
    User user = usersByUsername.get(username);
    if (user == null) {
        throw new NotFoundUserException();
    }
    return user;
  }

  public boolean userExists(String username) {
    return usersByUsername.containsKey(username);
  }
}

