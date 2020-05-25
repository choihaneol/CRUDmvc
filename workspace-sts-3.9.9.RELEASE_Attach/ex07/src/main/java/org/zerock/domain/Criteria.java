package org.zerock.domain;
//Criteria 클래스는 페이지수와 데이터개수의 파라미터 값을 같이 전달 하는 역할을 한다.

import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
@Setter
@Getter
public class Criteria {

	private int pageNum; //페이지번호 
	private int amount; //한 페이지당 데이터 개수 	
	
	private String type;
	private String keyword;
	
	public Criteria() {
	    this(1, 10);
	 }

	public Criteria(int pageNum, int amount) {
	    this.pageNum = pageNum;
	    this.amount = amount;
	 }
	
	public String[] getTypeArr() { //검색조건이 각 글자(T,W,C)로 구성되어 있으므로 검색조건을 배열로 만들어서 한번에 처리 
	    
	    return type == null? new String[] {}: type.split("");
	 }
	
	//UriComponentsBuilder로 링크를 생성하는 기능
	public String getListLink() {
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("") 
				.queryParam("pageNum", this.pageNum)
				.queryParam("amount", this.getAmount())
				.queryParam("type", this.getType())
				.queryParam("keyword", this.getKeyword());
		
		return builder.toUriString();
		
	}
	
	
	
}