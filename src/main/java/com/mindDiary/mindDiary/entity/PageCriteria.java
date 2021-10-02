package com.mindDiary.mindDiary.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageCriteria {

  private static final int ITEMS_PER_PAGE = 5;
  private int perPageNum;
  private int pageNumber;
  private int pageStart;


  public PageCriteria(int pageNumber) {
    this.pageNumber = pageNumber;
    this.pageStart = calcPageStart(pageNumber);
    this.perPageNum = ITEMS_PER_PAGE;
  }

  private int calcPageStart(int pageNumber) {
    return (pageNumber - 1) * perPageNum;
  }

}
