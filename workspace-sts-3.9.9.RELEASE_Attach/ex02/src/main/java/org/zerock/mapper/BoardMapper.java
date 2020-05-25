package org.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;

public interface BoardMapper {

	//tbl_board 리스트 소환
	//@Select("select * from tbl_board where bno > 0")  
	public List<BoardVO> getList();

	public List<BoardVO> getListWithPaging(Criteria cri); //페이지수, 한페이지당 데이터 수 
	
	//게시물생성(pk값 없을때) 
	public void insert(BoardVO board); 

	//게시물생성(pk값 있을때)
	public Integer insertSelectKey(BoardVO board);   
	
	//조회 
	public BoardVO read(Long bno); 

	//삭제 
	public int delete(Long bno); //삭제되면 1이상, 해당게시물 없을시 0 

	//수정 
	public int update(BoardVO board); //수정되면      "

	public int getTotalCount(Criteria cri);

}
