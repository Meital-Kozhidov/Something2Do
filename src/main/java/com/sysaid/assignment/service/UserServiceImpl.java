package com.sysaid.assignment.service;

import org.springframework.stereotype.Service;

import com.sysaid.assignment.domain.User;
import com.sysaid.assignment.domain.UserManager;
/******************************************************************************/

@Service
public class UserServiceImpl implements IUserService {

    private final UserManager userManager;

    public UserServiceImpl() {
        this.userManager = UserManager.getInstance();
    }

    public User createUser(String username) {
        return userManager.createUser(username);
    }
}
