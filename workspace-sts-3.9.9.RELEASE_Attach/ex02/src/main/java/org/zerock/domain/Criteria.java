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
	
	public String[] getTypeArr() { //검색조건 메서드. 이 각 글자(T,W,C)로 구성되어 있으므로 검색조건을 배열로 만들어서 한번에 처리 
	    
	    return type == null? new String[] {}: type.split(""); //type.split(""):문자열 분할
	 }
	
	//UriComponentsBuilder:파라미터가 추가될 때 마다 매번 링크를 생성하기 번거로울때 편리하게 해주는 기능. 주로 javascript를 사용하지 않고 링크처리시 사용. 
	public String getListLink() {
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
				.queryParam("pageNum", this.pageNum)
				.queryParam("amount", this.getAmount())
				.queryParam("type", this.getType())
				.queryParam("keyword", this.getKeyword());
		
		return builder.toUriString();
		
	}
	
	
	
}