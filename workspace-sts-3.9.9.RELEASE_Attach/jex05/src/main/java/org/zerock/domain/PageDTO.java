package org.zerock.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PageDTO {

  private int startPage;
  private int endPage;
  private boolean prev, next;

  private int total;
  private Criteria cri;

  public PageDTO(Criteria cri, int total) {

    this.cri = cri; //Criteria안에는 PageNum, amount 있음 
    this.total = total; //전체 데이터 수 

    this.endPage = (int) (Math.ceil(cri.getPageNum() / 10.0)) * 10; 

    this.startPage = this.endPage - 9;

    int realEnd = (int) (Math.ceil((total * 1.0) / cri.getAmount())); //실제끝페이지 

    if (realEnd <= this.endPage) { //실제끝페이지가 미리정해놓은 끝페이지보다 작다면 실제페이지가 끝페이지가 됨 
      this.endPage = realEnd;
    }

    this.prev = this.startPage > 1; //현재페이지 기준 앞페이지 

    this.next = this.endPage < realEnd; //현재페이지 기준 뒤페이지 
  }
  
}

