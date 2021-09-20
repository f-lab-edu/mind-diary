package com.mindDiary.mindDiary.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageCriteria {

  private int perPageNum;
  private int pageNumber;
  private int pageStart;

  public PageCriteria(int pageNumber, int perPageNum) {
    this.pageNumber = pageNumber;
    this.pageStart = calcPageStart(pageNumber);
    this.perPageNum = perPageNum;
  }

  private int calcPageStart(int pageNumber) {
    return (pageNumber - 1) * perPageNum;
  }

}
