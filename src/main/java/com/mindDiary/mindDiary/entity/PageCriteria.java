package com.mindDiary.mindDiary.entity;

import com.mindDiary.mindDiary.exception.businessException.InvalidPageNumberException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageCriteria {

  public static final int ITEMS_PER_PAGE = 5;
  private int perPageNum;
  private int pageNumber;
  private int pageStart;


  public PageCriteria(int pageNumber) {
    validate(pageNumber);
    this.perPageNum = ITEMS_PER_PAGE;
    this.pageNumber = pageNumber;
    this.pageStart = calcPageStart(pageNumber);
  }

  private void validate(int pageNumber) {
    if (pageNumber <= 0) {
      throw new InvalidPageNumberException();
    }

  }

  private int calcPageStart(int pageNumber) {
    return (pageNumber - 1) * perPageNum;
  }

}
