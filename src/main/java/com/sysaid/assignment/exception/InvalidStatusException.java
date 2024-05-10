package com.sysaid.assignment.exception;

public class InvalidStatusException extends RuntimeException{
    public InvalidStatusException() {
        super("Invalid status - should be 'complete' or 'wishlist'");
    }
}
