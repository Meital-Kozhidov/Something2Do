package com.sysaid.assignment.exception;

public class InvalidKeyException extends RuntimeException{
    public InvalidKeyException() {
        super("Task with this key does not exist");
    }
}

