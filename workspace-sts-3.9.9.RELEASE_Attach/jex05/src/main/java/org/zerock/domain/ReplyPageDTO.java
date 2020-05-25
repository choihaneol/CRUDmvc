package org.zerock.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor //객체생성 
@Getter
public class ReplyPageDTO {
	
	private int replyCnt;
	private List<ReplyVO> list;

}
