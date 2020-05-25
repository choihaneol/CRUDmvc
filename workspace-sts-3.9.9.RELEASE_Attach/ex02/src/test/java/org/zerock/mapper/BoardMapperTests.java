package org.zerock.mapper;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class) //프레임워크의 테스트 실행 방법을 확장 
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml") //자동으로 만들어줄 애플리케이션 컨텍스트의 설정파일위치를 지정
@Log4j //log
public class BoardMapperTests {
	
	//BoardMapper 주입 
	@Setter(onMethod_ = @Autowired) //자동으로 set,get 메서드를
	private BoardMapper mapper;

	
	//tbl_board 소환 테스트 
	@Test
	public void testGetList() {
		
		mapper.getList().forEach(board -> log.info(board)); //게시판로그 

	}
	
	
	//게시물생성 테스트(PK값X)
	@Test
	public void testInsert() {  

		BoardVO board = new BoardVO();
		board.setTitle("새로 작성하는 글");
		board.setContent("새로 작성하는 내용");
		board.setWriter("newbie");

		mapper.insert(board);

		log.info(board); //toString()을 이용하여 bno멤버(인스턴스)변수 값 확인 
	}
	
	
	//게시물생성 테스트(PK값O) 
	@Test
	public void testInsertSelectKey() { 

		BoardVO board = new BoardVO();
		board.setTitle("새로 작성하는 글 select key");
		board.setContent("새로 작성하는 내용 select key");
		board.setWriter("newbie");

		mapper.insertSelectKey(board);

		log.info(board);
	}
	
	
	//조회 테스트 
	@Test
	public void testRead() {

		// 존재하는 게시물 번호로 테스트
		BoardVO board = mapper.read(5L); //bno=5 조회 

		log.info(board);

	}
	
	
	//삭제 테스트 
	@Test
	public void testDelete() {

		log.info("DELETE COUNT: " + mapper.delete(3L));//bno=3 삭제 
	}
	
	
	//수정 테스트 
	@Test  
	public void testUpdate() {

		BoardVO board = new BoardVO();
		// 실행전 존재하는 Bno인지 확인할 것
		board.setBno(5L);
		board.setTitle("수정된 제목");
		board.setContent("수정된 내용");
		board.setWriter("user00");

		int count = mapper.update(board);
		log.info("UPDATE COUNT: " + count);

	}
	
	
	@Test
	public void testPaging() {

		Criteria cri = new Criteria();
		//10개씩 3페이지
		cri.setPageNum(3);
		cri.setAmount(10);

		List<BoardVO> list = mapper.getListWithPaging(cri);

		list.forEach(board -> log.info(board.getBno()));

	}
	
	@Test
	  public void testSearch() {

	    Criteria cri = new Criteria();
	    //검색조건
	    cri.setKeyword("키워드");
	    cri.setType("TCW");

	    List<BoardVO> list = mapper.getListWithPaging(cri);

	    list.forEach(board -> log.info(board));
	  }
	
	
	
	
}
