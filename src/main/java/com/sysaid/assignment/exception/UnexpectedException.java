package com.sysaid.assignment.exception;

public class UnexpectedException extends RuntimeException  {
  public UnexpectedException() {
        super("Internal error. Should never get here");
    }
}
