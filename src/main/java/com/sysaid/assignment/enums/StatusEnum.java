package com.sysaid.assignment.enums;

public enum StatusEnum {
  COMPLETE("complete"),
  WISHLIST("wishlist"),
  ALL("all");

  private final String value;

  private StatusEnum(String value) {
    this.value = value;
  }

  public String get() {
    return value;
  }
}
