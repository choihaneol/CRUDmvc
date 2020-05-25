package org.zerock.domain;

import java.util.Date;
import java.util.List;
import lombok.Data;


@Data //Lombok(getter,setter,toString) 
public class BoardVO { //테이블설계기준으로 작성 

  private Long bno;
  private String title;
  private String content;
  private String writer;
  private Date regdate;
  private Date updateDate;
  
  private int replyCnt;
  
  private List<BoardAttachVO> attachList; //첨부파일리스트 형태 
  
}
