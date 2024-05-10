package com.sysaid.assignment.service;

import com.sysaid.assignment.domain.User;
import com.sysaid.assignment.exception.DuplicatedUserException;

/******************************************************************************/

public interface IUserService {
    User createUser(String username) throws DuplicatedUserException;
}