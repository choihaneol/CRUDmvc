package org.zerock.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class) //프레임워크의 테스트 실행 방법을 확장 
@WebAppConfiguration //WebApplicationContext(ServletContext)사용 하기위해  
@ContextConfiguration({ "file:src/main/webapp/WEB-INF/spring/root-context.xml",
"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml" }) //자동으로 만들어줄 애플리케이션 컨텍스트의 설정파일위치를 지정
@Log4j
public class BoardControllerTests {
	
	//스프링테스트기능 주입 
	@Setter(onMethod_ = { @Autowired })
	private WebApplicationContext ctx; 

	//스프링테스트기능. 가짜Mvc(가짜파라미터/URL로 controller실행) 
	private MockMvc mockMvc; 

	//스프링테스트기능. 모든 테스트기능 실행전에 매번 실행됨. 
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}
	
	
	
	//목록 
	@Test
	public void testList() throws Exception { 

		log.info(
				mockMvc
				.perform(MockMvcRequestBuilders //MockMvcRequestBuilders:GET()방식 호출 
				.get("/board/list")) //BoardController의 getList()에서 반환된결과를 이용해서 
				.andReturn() //Model에 어떤 데이터들이 담겨있는지 확인 
				.getModelAndView() //핸들러에 의해 준비된 ModelAndView를 리턴
				.getModelMap());
	}
	
	
	
	//등록 
	@Test
	public void testRegister() throws Exception { 

		String resultPage = mockMvc
				.perform(MockMvcRequestBuilders //MockMvcRequestBuilders:POST()방식 전달 
				.post("/board/register") 
				.param("title", "테스트 새글 제목") //param:전달해야하는 파라미터 지정 
				.param("content", "테스트 새글 내용")
				.param("writer", "user00"))
				.andReturn().getModelAndView().getViewName();

		log.info(resultPage); 

	}
	
	
	
	//조회 
	@Test
	public void testGet() throws Exception { 

		log.info(mockMvc
				.perform(MockMvcRequestBuilders
				.get("/board/get")
				.param("bno", "4")) //bno 명시적 처리 
				.andReturn()
				.getModelAndView()
				.getModelMap());
	}
	
	
	
	//수정 
	@Test
	public void testModify() throws Exception { 

		String resultPage = mockMvc
				.perform(MockMvcRequestBuilders
				.post("/board/modify")
				.param("bno", "1")
				.param("title", "수정된 테스트 새글 제목")
				.param("content", "수정된 테스트 새글 내용")
				.param("writer", "user00"))
				.andReturn()
				.getModelAndView()
				.getViewName();

		log.info(resultPage);

	}

	
	
	//삭제 
	@Test
	public void testRemove() throws Exception { 
		// 삭제전 데이터베이스에 게시물 번호 확인할 것
		String resultPage = mockMvc
				.perform(MockMvcRequestBuilders
				.post("/board/remove")
				.param("bno", "22"))
				.andReturn()
				.getModelAndView()
				.getViewName();

		log.info(resultPage);
	}
	
	
	
	@Test //Criteria를 파라미터로 처리
	public void testListPaging() throws Exception {

		log.info(mockMvc.perform(
				MockMvcRequestBuilders.get("/board/list")
				.param("pageNum", "2")
				.param("amount", "50"))
				.andReturn().getModelAndView().getModelMap());
	}
	
}
