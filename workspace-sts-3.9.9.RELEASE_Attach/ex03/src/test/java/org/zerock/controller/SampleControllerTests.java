package org.zerock.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.beans.factory.annotation.Autowired;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.WebApplicationContext;
import org.zerock.domain.Ticket;

import com.google.gson.Gson;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)

//Test for Controller
@WebAppConfiguration

@ContextConfiguration({ "file:src/main/webapp/WEB-INF/spring/root-context.xml",
 "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml" })
//Java Config
//@ContextConfiguration(classes = {
//org.zerock.config.RootConfig.class,
//org.zerock.config.ServletConfig.class} )
@Log4j
public class SampleControllerTests { //SampleController에 convert() 메서드 테스트 

	@Setter(onMethod_ = { @Autowired })
	  private WebApplicationContext ctx; //서블릿에서 이용되는 Context, 빈들이 담겨있는 컨테이너 

	  private MockMvc mockMvc; //애플리케이션을 서버에 배포하지 않고도 MVC 동작을 재현할 수 있는 클래스 

	  @Before
	  public void setup() {
	    this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	  }

	  @Test  //@RequestBody 파라미터 반환 테스트(JSON 데이터 -> Ticket타입)
	  public void testConvert() throws Exception {

	    Ticket ticket = new Ticket();
	    ticket.setTno(123);
	    ticket.setOwner("Admin");
	    ticket.setGrade("AAA");
	    
	    String jsonStr = new Gson().toJson(ticket); //Gson():java object <-> JSON 문자열 변환. 
	    
	    log.info(jsonStr);
	    
	    mockMvc.perform(post("/sample/ticket")
	        .contentType(MediaType.APPLICATION_JSON)
	        .content(jsonStr))
	        .andExpect(status().is(200));
	  }

}
