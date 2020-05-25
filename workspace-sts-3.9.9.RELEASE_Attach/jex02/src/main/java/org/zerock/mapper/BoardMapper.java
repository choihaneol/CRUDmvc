package org.zerock.mapper;

import java.util.List;

import org.zerock.domain.BoardVO;

public interface BoardMapper {

	//@Select("select * from tbl_board where bno > 0") //리스트 
	public List<BoardVO> getList();
	
	public void insert(BoardVO board); //pk값 없을때 row삽입 

	public Integer insertSelectKey(BoardVO board); //pk값 있을때 row삽입  
	
	public BoardVO read(Long bno); 

	public int delete(Long bno); //삭제되면 1이상, 해당게시물 없을시 0 

	public int update(BoardVO board); //수정되면      "


}
