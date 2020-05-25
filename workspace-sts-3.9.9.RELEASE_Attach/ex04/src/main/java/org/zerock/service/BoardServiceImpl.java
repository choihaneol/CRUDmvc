package org.zerock.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.mapper.BoardMapper;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@Service //비즈니스영역 
@AllArgsConstructor //모든 파라미터를 이용하는 생성자를 만듬 
public class BoardServiceImpl implements BoardService {
//Param into BoardServiceImple > (Param의 형태 BoardVO) > Mapper에서 처리 > BoardMapper.xml 
	
	private BoardMapper mapper;

	@Override
	public void register(BoardVO board) { 

		log.info("register......" + board);

		mapper.insertSelectKey(board);
	}

	
	@Override
	public BoardVO get(Long bno) { //게시물조회 

		log.info("get......" + bno);

		return mapper.read(bno);

	}
	

	@Override
	public boolean modify(BoardVO board) { //boolean으로 엄격하게 처리 

		log.info("modify......" + board);

		return mapper.update(board) == 1; //정상처리시 1반환 하므로 true/false 처리가능   
	}
	

	@Override
	public boolean remove(Long bno) { //게시물삭제 

		log.info("remove...." + bno);

		return mapper.delete(bno) == 1;
	}
	

	/*@Override
	public List<BoardVO> getList() { //게시물조회 
	
	log.info("getList..........");
	
	return mapper.getList();
	}*/	
	@Override
	public List<BoardVO> getList(Criteria cri) { //Criteria를 파라미터로 처리
	
	log.info("get List with criteria:" + cri);
	
	return mapper.getListWithPaging(cri);
	}
	
	
	@Override
	public int getTotal(Criteria cri) { //전체페이지 개수 

		log.info("get total count");
		return mapper.getTotalCount(cri);
	}
	
	
	
	
}
