package com.sysaid.assignment.exception;

public class NotFoundKeyException extends RuntimeException{
    public NotFoundKeyException() {
        super("Task with this key does not exist");
    }
}

