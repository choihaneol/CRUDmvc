package org.zerock.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;

import lombok.Setter;
import lombok.extern.log4j.Log4j;


@RunWith(SpringJUnit4ClassRunner.class) //스프링의 테스트 컨텍스트 프레임워크의 JUnit 확장기능 지정
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml") //테스트 컨텍스트가 자동으로 만들어줄 애플리케이션 컨텍스트의 위치 지정
@Log4j
public class BoardServiceTests {
	
	//BoardService 주입 
	@Setter(onMethod_ = { @Autowired })
	private BoardService service; 

	
	//BoardService가 정상적으로 주입됬는지확인 
	@Test 
	public void testExist() {

		log.info(service);
		assertNotNull(service);
	}
	
	
	//게시글등록 및 확인 테스트 
	@Test 
	public void testRegister() {

		BoardVO board = new BoardVO();
		board.setTitle("새로 작성하는 글");
		board.setContent("새로 작성하는 내용");
		board.setWriter("newbie");

		service.register(board);

		log.info("생성된 게시물의 번호: " + board.getBno());
	}
	
	
	//목록조회 테스트 
	@Test
	public void testGetList() {

		//service.getList().forEach(board -> log.info(board));
		/* mapper.getList().forEach() = getList() 객체의 결과 값을 가지고 forEach 함수의 내장 for문 메서드를 실행한다.
		 * forEach( board -> log.info(board) ) getList() 함수의 결과 값을 하나씩 BoardVO 타입의 board 에 넘긴다 
		 * + log.info(board)를 실행한다. getList() 결과 값이 하나도 없을 때까지 */
		service.getList(new Criteria(2,10)).forEach(board->log.info(board));  //Criteria를 파라미터로 처리
	
	}
	
	
	//조회 
	@Test //bno=1 조회 
	public void testGet() {

		log.info(service.get(1L)); 
	}

	
	
	//삭제 
	@Test //bno=2 삭제 
	public void testDelete() { 

		// 게시물 번호의 존재 여부를 확인하고 테스트할 것
		log.info("REMOVE RESULT: " + service.remove(2L)); //REMOVE RESULT:bno=2 있을시 true반환 및 삭제  

	}

	
	//수정 
	@Test //bno=1 여부 확인후 제목수정  
	public void testUpdate() {

		BoardVO board = service.get(1L); //먼저 특정 게시물 존재하는지 확인후 제목수정하는 테스트코드 

		if (board == null) {
			return;
		}

		board.setTitle("제목 수정합니다.");
		log.info("MODIFY RESULT: " + service.modify(board));
	}
	
}
