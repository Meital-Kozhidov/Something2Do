package com.sysaid.assignment.enums;

public enum OptionEnum {
  RANDOM("random"),
  RATED("rated");

  private final String value;

  private OptionEnum(String value) {
    this.value = value;
  }

  public String get() {
    return value;
  }
}
