package com.sysaid.assignment.exception;

public class FetchTaskException extends RuntimeException {
  public FetchTaskException() {
        super("Failed to fetch random task");
    }
}
