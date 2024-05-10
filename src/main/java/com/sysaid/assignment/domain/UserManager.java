package com.sysaid.assignment.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.sysaid.assignment.exception.DuplicatedUserException;
import com.sysaid.assignment.exception.NotFoundUserException;

/******************************************************************************/

public class UserManager {
  private static UserManager instance;
  private final List<User> users;

  private UserManager() {
    this.users = new ArrayList<User>();
  }

  public static UserManager getInstance() {
    if (instance == null) {
      instance = new UserManager();
    }

    return instance;
  }

  /****************************************************************************/

  public List<User> getUsers () {
    return this.users;
  }

  public User createUser(String username) {
    if (isUserExist(username)) {
      throw new DuplicatedUserException();
    }

    User user = new User(username);
    this.users.add(user);

    return user;
  }

  public User getUserByUsername(String username) {
    try {
      return this.getMatchingUsers(username).get(0);
    } catch (Exception e) {
      throw new NotFoundUserException();
    }
  }

  /****************************************************************************/

  private List<User> getMatchingUsers(String username) {
   return this.users.stream()
                .filter(item -> item.getName().equals(username))
                .limit(1)
                .collect(Collectors.toList()); 
  }

  private Boolean isUserExist(String username) {
    return (this.getMatchingUsers(username).size() != 0);
  }

}
