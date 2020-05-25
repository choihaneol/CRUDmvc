package org.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyVO;

public interface ReplyMapper {

	
	public int insert(ReplyVO vo);
	
	public ReplyVO read(Long rno);
	
	public int delete(Long rno);

	public int update(ReplyVO reply);

	//페이징처리,게시물번호를 파라미터로 보내는 함수
	public List<ReplyVO> getListWithPaging(
			@Param("cri") Criteria cri, 
			@Param("bno") Long bno);
	
	//게시물의 댓글개수 파악
	public int getCountByBno(Long bno);
	
	

}
