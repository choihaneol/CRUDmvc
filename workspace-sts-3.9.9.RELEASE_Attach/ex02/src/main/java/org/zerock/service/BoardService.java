package org.zerock.service;

import java.util.List;

import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;

public interface BoardService {

	//동록 
	public void register(BoardVO board);

	//조회 
	public BoardVO get(Long bno);

	//수정 
	public boolean modify(BoardVO board);

	//삭제 
	public boolean remove(Long bno);

	//목록조회 
	public List<BoardVO> getList(Criteria cri); //Criteria를 파라미터로 처리

	public int getTotal(Criteria cri);
	
	
}
