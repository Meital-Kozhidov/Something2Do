package com.sysaid.assignment.exception;

public class InvalidAmountException extends RuntimeException{
    public InvalidAmountException() {
        super("This amount of tasks of requested type do not exist (did you specify amount? default is 10)");
    }
}

