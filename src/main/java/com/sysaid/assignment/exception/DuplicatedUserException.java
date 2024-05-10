package com.sysaid.assignment.exception;

public class DuplicatedUserException extends RuntimeException{
    public DuplicatedUserException() {
        super("This username already exists");
    }
}
