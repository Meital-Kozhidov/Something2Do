package com.sysaid.assignment.exception;

public class InvalidOptionException extends RuntimeException{
    public InvalidOptionException() {
        super("Invalid option - should be 'random' or 'rated'");
    }
}

