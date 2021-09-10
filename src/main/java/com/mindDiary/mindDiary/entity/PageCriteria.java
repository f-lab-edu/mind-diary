package com.mindDiary.mindDiary.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageCriteria {

  private final int perPageNum = 5;
  private int pageNumber;
  private int pageStart;

  public PageCriteria(int pageNumber) {
    this.pageNumber = pageNumber;
    this.pageStart = calcPageStart(pageNumber);
  }

  private int calcPageStart(int pageNumber) {
    return (pageNumber - 1) * perPageNum;
  }

}
