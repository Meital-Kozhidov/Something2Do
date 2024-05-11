package com.sysaid.assignment.exception;

public class NotFoundUserException extends RuntimeException{
    public NotFoundUserException() {
        super("This username does not exist. Please register in /users route");
    }
}
