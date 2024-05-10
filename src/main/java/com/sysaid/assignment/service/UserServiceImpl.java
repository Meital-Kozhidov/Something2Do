package com.sysaid.assignment.service;

import org.springframework.stereotype.Service;

import com.sysaid.assignment.domain.User;
import com.sysaid.assignment.domain.UserManager;
import com.sysaid.assignment.exception.DuplicatedUserException;

/******************************************************************************/

@Service
public class UserServiceImpl implements IUserService {

    private final UserManager userManager;

    public UserServiceImpl() {
        this.userManager = UserManager.getInstance();
    }

    @Override
    public User createUser(String username) throws DuplicatedUserException {
        return userManager.createUser(username);
    }
}
