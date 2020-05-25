package org.zerock.domain;


import lombok.Data;

@Data
public class AuthVO {

  //tbl_member_auth 칼럼ㅁ을 그대로 반영 
  private String userid;
  private String auth;
  
}


