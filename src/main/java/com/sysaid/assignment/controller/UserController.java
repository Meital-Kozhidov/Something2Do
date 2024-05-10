package com.sysaid.assignment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sysaid.assignment.domain.User;
import com.sysaid.assignment.exception.DuplicatedUserException;
import com.sysaid.assignment.service.UserServiceImpl;

/******************************************************************************/

@RestController
public class UserController {
  private final UserServiceImpl userService;

  public UserController(UserServiceImpl userService) {
    this.userService = userService;
  }

  /****************************************************************************/
  
  /**
     * Register a user with the given username to the system.
     *
     * @param username the requested username
     * @return the registered User
     * @throws DuplicatedUserException if the username is already in use
     */
    @PostMapping("/users")
    public ResponseEntity<User> registerUser(
      @RequestParam(name = "username") String username
    ) {
      User createdUser = userService.createUser(username);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
}
