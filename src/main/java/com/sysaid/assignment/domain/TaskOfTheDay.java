package com.sysaid.assignment.domain;

/******************************************************************************/

public class TaskOfTheDay {
  private Task randomOption;
  private Task ratedOption;

  public TaskOfTheDay(Task randomOption) {
    this.randomOption = randomOption;
  }

  /****************************************************************************/


  public Task getRandomOption () {
    return this.randomOption;
  }
  public void setRandomOption(Task randomOption) {
    this.randomOption = randomOption;
  }

  public Task getRatedOption () {
    return this.ratedOption;
  }
  public void setRatedOption(Task ratedOption) {
    this.ratedOption = ratedOption;
  }
}
