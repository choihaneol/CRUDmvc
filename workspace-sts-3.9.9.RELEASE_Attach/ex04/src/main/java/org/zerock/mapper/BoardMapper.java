package org.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;

public interface BoardMapper {

	//@Select("select * from tbl_board where bno > 0") //리스트 
	public List<BoardVO> getList();

	public List<BoardVO> getListWithPaging(Criteria cri); //페이지수, 한페이지당 데이터 수 
	
	public void insert(BoardVO board); //pk값 없을때 row삽입 

	public Integer insertSelectKey(BoardVO board); //pk값 있을때 row삽입  
	
	public BoardVO read(Long bno); 

	public int delete(Long bno); //삭제되면 1이상, 해당게시물 없을시 0 

	public int update(BoardVO board); //수정되면      "

	public int getTotalCount(Criteria cri);
	
	public void updateReplyCnt(@Param("bno") Long bno, @Param("amount") int amount); //댓글개수 

}
