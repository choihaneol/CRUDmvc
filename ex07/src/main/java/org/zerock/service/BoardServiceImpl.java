package org.zerock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.mapper.BoardAttachMapper;
import org.zerock.mapper.BoardMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;


@Log4j
@Service //비즈니스영역 
//@AllArgsConstructor //모든 파라미터를 이용하는 생성자를 만듬 
public class BoardServiceImpl implements BoardService {
//Param into BoardServiceImpl > (Param의 형태 BoardVO) > Mapper에서 처리 > BoardMapper.xml 
	
	@Setter(onMethod_ = @Autowired)
	private BoardMapper mapper;

	@Setter(onMethod_ = @Autowired)
	private BoardAttachMapper attachMapper;

	//로그만 테스트  
	/*@Override
	public void register(BoardVO board) { 

		log.info("register......" + board);

		mapper.insertSelectKey(board);
	}  */
	@Transactional
	@Override
	public void register(BoardVO board) {

		log.info("register......" + board);

		mapper.insertSelectKey(board); //먼저 tbl_board에 등록 

		if (board.getAttachList() == null || board.getAttachList().size() <= 0) {
			return;
		}

		board.getAttachList().forEach(attach -> {

			attach.setBno(board.getBno()); //attach_table(첨부파일)에 bno 셋 
			attachMapper.insert(attach); //attach_table에 데이터 추가 
		});
	}

	
	@Override
	public BoardVO get(Long bno) { //게시물조회 

		log.info("get......" + bno);

		return mapper.read(bno);

	}
	

	@Transactional
	@Override
	public boolean modify(BoardVO board) { //boolean으로 엄격하게 처리 

		log.info("modify......" + board);

		attachMapper.deleteAll(board.getBno()); //첨부파일목록 삭제 후 

		boolean modifyResult = mapper.update(board) == 1;
		
		//if (modifyResult && board.getAttachList().size() > 0) {
		if (modifyResult && board.getAttachList() != null 
				&& board.getAttachList().size() > 0) {

			board.getAttachList().forEach(attach -> {

				attach.setBno(board.getBno());
				attachMapper.insert(attach); //다시 첨부파일목록에 데이터저장 
			});
		}
		//return mapper.update(board) == 1; //정상처리시 1반환 하므로 true/false 처리가능   
		return modifyResult;
	}
	
	
	@Transactional //tbl_board, attach_tbl 둘다 삭제이므로 트랜젝션처리 
	@Override
	public boolean remove(Long bno) { //게시물삭제 

		log.info("remove...." + bno);

		attachMapper.deleteAll(bno);
		
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
	
	@Override
	public List<BoardAttachVO> getAttachList(Long bno) { //게시물의 첨부파일 목록 

		log.info("get Attach list by bno" + bno);

		return attachMapper.findByBno(bno);
	}
	
	
	
	
	
}
