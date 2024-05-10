package com.sysaid.assignment.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sysaid.assignment.domain.User;
import com.sysaid.assignment.domain.UserManager;
import com.sysaid.assignment.exception.DuplicatedUserException;

/******************************************************************************/

@RestController
public class UsersController {
  private final UserManager userManager;

  public UsersController() {
    this.userManager = UserManager.getInstance();
  }

  /****************************************************************************/
  
  /**
   * Register a user with given username to the system
   * @param username the requested username
   * @return the registered User
   * @throws DuplicatedUserException
   */
  @PostMapping("/users")
	public User registerUser(
		@RequestParam(name = "username", required = true) String username) 
		throws DuplicatedUserException {
			return this.userManager.createUser(username);
	}
}
