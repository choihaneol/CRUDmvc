package org.zerock.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
}


  
 /*

10. REST방식과 Ajax를 이용하는 댓글처리
   1)환경설정 : pom.xml 에서 스프링버전 5.0.7, 자바와 Maven 버전 1.8 설정 후 프로젝트 업데이트. jackson_databind 와 jackson-dataformat-xml 추가, gson 라이브러리 추가, servlet 버전 수정, Lombok관련 설정 추. jUnit 버전 변경, spring-test 관련 모듈 추가 
   2)REST방식으로 전환 : 하나의 고유한 리소스를 대표하도록 설계된다는 개념에 전송방식을 결합해서 원하는 작업을 지정 ex) URI + GET/POST/PUT/DELETE 
                      @RESTController에서는 다양한 포맷의 데이터를 전송할 수 있음(xml,JSON,문자열 등).
      (1)@RestController : REST API 어노테이션(사용자정의한 클래스 타입을 반환) 추가. 
      (1-1)단순문자열반환 : SampleController 패키지 생성 후 @RestController 임포트 후 단순문자열 반환하는 getText() 메서드 작성 -> 브라우저에서 /sample/getText 를 호출하여 문자열 반환되는지 확인 
      (2)객체의 반환(xml, JSON) : org.zerock.domain에서 SampleVO 클래스 생성 후, 사용할 변수 선언 한뒤-> SampleController에서 sampleVO 타입을 반환하는 getSample() 메서드 선언 ->브라우저에서 XML/JSON 각 타입으로 데이터가 전달되는지 확인
      (3)컬렉션타입의 반환 : SampleController에서 리스트 타입의 객체를 반환하는 getList()메서드 작성 -> 맵을 반환하는 getMap()메서드 작성 
      (4)ResponseEntity타입 : ResponseEntity는 데이터와 함께 HTTP헤더의 상태메세지 등을 같이 전달하는 용도이므로 데이터를 전달 받는 입장에서는 확실하게 결과를 알수 있다. SampleController에 파라미터에 따라 데이터와 상태메세지까지 전달하는 check()메서드 작성   
      (5)RESTcontroller와 함께자주 쓰이는 어노테이션 : @PahtVariable 는 getPath(), @RequestBody는 domain에서 ticket클래스 작성 후 SampleController에서 convert()메서드 예제작성 
      (6)RESTcontroller 테스트(Junit) : src/test/java에서 SampleControllerTests 클래스생성 및 테스트코드작성 
      (7)RESTcontroller 테스트(확장프로그램) : 톰캣을 구동 시킨뒤 크롬확장프로그램을 다운 받아서 테스트 하는 방식도 있음. (Talend API tester)
     
11. Ajax댓글처리(조회페이지에서 별도의 화면 이동 없이 처리)
   1)Part3에서 작성한 ex02의 코드 내용을 모두 붙여넣기 -> src/main/resources에서 log4jdbc-log4j2를 이용하기 위해서 log4jdbc.log4j2.properties 파일을 추가, MyBatis관련 xml라이브러리 추가, JDBC드라이버추가, ex02에서 작성한 src/main/werbapp/WEB-INF/view 폴더 안에 있는 모든 파일 복사 -> 톰캣 실행경로 절대경로'/'로 확인       
   2)댓글처리를 위한 영속 영역
      (1)SQL에서 댓글 처리를 위한 테이블 tbl_reply 생성(별도의 PK부여, 외래키FK 참조) ->  tbl_reply 테이블을 참고해서 도메인에 ReplyVO 클래스 생성 및 데이터변수 작성 
         -> ReplyMapper인터페이스 생성 -> src/main/resources에 org/zerock/mapper/ReplyMapper.xml 생성 후 문두에 namespace 관련 전처리문 선언 -> SQL에서 가장 최신 게시물 몇개 확인 해놓기 (tbl_board와 tbl_reply의 bno가 같아야하므로 체크)
      (2)ReplyMapper 테스트 : src/test/java에서 org.zerock.mapper에서 ReplyMapperTests 클래스 생성 후 테스트코드 testMapper()작성                      
   3)C(외래키 사용하여 등록) : ReplyMapper인터페이스에서 등록 메서드 insert() 추가 -> ReplyMapper.xml에서 데이터에 등록처리 하는 insert 태그추가 -> ReplyMapperTests에서 (실제존재하는 bno에) 댓글등록 테스트 testCreate()작성 -> SQL에서 댓글 등록 되었는지 확인
   4)R(조회) : ReplyMapper 인터페이스에 조회 read() 추가 -> ReplyMapper.xml에 조회 read 태그 추가 -> ReplyMapperTests에서 tbl_reply에 존재하는 Rno를 이용하여 조회 테스트 메서드 testRead() 추가 하여 확인 
   5)U(삭제) : ReplyMapper 인터페이스에 삭제 delete() 추가 -> ReplyMapper.xml에 삭제 delete 태그 추가 -> ReplyMappeTests에서 tbl_reply에 존재하는 Rno를 이용하여 삭제 테스트 메서드 testDelete() 추가 하여 확인 -> SQL에서 삭제되었는지 확인
   6)D(수정) : ReplyMapper 인터페이스에 수정 update() 추가 -> ReplyMapper.xml에 수정 update 태그 추가 -> ReplyMapperTests에 tbl_reply에 존재하는 Rno를 이용하여 댓글의 내용(reply),최종수정시간(updatedate)를 수정하는 메서드 testUpdate()를 추가하여 확인 
   7)댓글목록 처리 : 댓글의 목록과 페이징처리는 기존의 게시물 페이징 처리와 유사하지만 추가적으로 게시물번호(Bno)가 필요함 
      (1)MyBatis에서 두개이상의 데이터를 파라미터로 전달하는 방법은 1)객체로 구성 2)Map이용 3)@Param 하는 방법이 있음. 예제에서는 @Param사용
      (2)Criteria, Bno전달 : ReplyMapper 인터페이스에서 @Param을 이용하여 Criteria, Bno 전달하는 메서드 getListWithPaging()추가 
      (3)@Param 어노테이션 : ReplyMapper.xml에서 특정게시물의 댓글 가져오는 태그 getListWithPaging 추가( #{bno}가 ReplyMapper 인터페이스의 @Param("bno")와 매칭되어 사용되는점 주목 ) 
      (4)테스트 : ReplyMapperTests에서 실제 데이터베이스에 추가되어 있는 댓글들의 게시물번호로 확인 하는 메서드 testList()작성 하여 bnoArr[0]번째 게시물의 댓글목록 불러들여지는지 확인 
   8)서비스영역과 Controller 처리 
      (1)인터페이스,클래스 생성 : org.zerck.service에서 ReplyService 인터페이스 생성 후 register(), get(), modify(), remove(), getList() 메서드 선언 -> ReplyServiceImple클래스 생성 후 ReplyService 인터페이스에서 선언한 메서드들의 구현부 작성
      (2)Controller의 설계 : org.zerock.controller에서 ReplyController 클래스 생성 후 @RestController를 사용. ReplyService 객체를 불러와서 코딩 작성 
      (3)등록작업과 테스트 : ReplyController에 consume,produces로 브라우저에서는 데이터를 보낼때는 Json으로 전송하고 서버에서 데이터가 잘 처리되었는지 결과가 보여질때는 문자열로 보여주도록 하고 @PostMapping을 사용하여 create()메서드 작성 -> 크록확장프로그램으로 등록후 SQL에서 확인 
      (4)특정게시물의 댓글목록 조회 : ReplyController에 @GetMapping을 사용하여 댓글목록을 가져오는 getList()메서드 작성 -> 브라우저에서 잘 출력되는지 /replies/pages/게시물번호/댓글번호 로 확인 
      (5)조회와 삭제 : ReplyController에 @GetMapping을 사용하여 get(),@DeleteMapping을 사용하여 remove() 메서드 작성
      (6)수정 : ReplyController에 @ResquestMapping을 사용하여 update() 메서드작성. -> 크록확장프로그램과 SQL에서 확인 
   9)자바스크립트 준비 
      (1)자바스크립트 모듈화 : webapp/resources/js 폴더내에 reply.js(javascript source file) 생성 -> 간단한 출력테스트 코드 작성하여 확인 -> 여기까지 동작코드만 구현해본거고 실제사용할 코드는 reply.js는 조회페이지에서 사용하기 위해서 작성된것이므로 views/board/gets.jsp파일에 추가  
                          ->get.jsp에서 이전에 만든 이벤트 처리 바로 위쪽에 reply.js 경로 추가 -> 브라우저에서 /board/get?bno=xxx 를 호출하여 개발자도구에서 reply.js가 문제없이 로딩되는지 확인 
      (2)모듈 구성하기 : (reply.js의 결과는 reply.js를 사용하는 get.jsp를 이용해서 확인) reply.js에서 간단한 출력속성이 있는 객체 ReplyService 작성후 get.jsp에서 위 reply.js경로 바로 밑에 객체 실행코드 작성 -> 개발자도구에서 ReplyService의 출력메세지가 정상적으로 표시되는지 확인 
      (3)등록처리 : reply.js에서 Ajax를 이용하여 POST방식으로 호출하는 add()메서드 작성 -> 테스트를 위해 get.jsp에서 replyService.add() 로 호출하는 코드 작성 -> 톰캣실행하여 개발자도구에서 replyService.add()내용이 출력되는지, 브라우저상 데이터타입이 Json인지, 서버에서는 Json데이터가 ReplyVO형태로 제대로 변환되는지 확인      
      (4)댓글목록처리 : 우선적으로 xml이나 json 데이터로 출력되는지 브라우저에서 /replies/pages/게시물번호/페이지번호.xml혹json 으로 확인
      (5)getJson 사용 : reply.js에서 param을 사용하여 json데이터 호출 하여 getList() 메서드 작성 -> get.jsp에서 getList()를 호출하여 확인 -> 개발자 도구에서 댓글목록을 정상적으로 불러 들이느지 확인 
      (6)댓글삭제와 갱신 : reply.js에서 remove()메서드 작성 -> get.jsp 에서 실제 존재하는 rno를 사용하여 remove() 호출코드 작성하여 확인 -> SQL에서 삭제되었는지 확인 
      (7)댓글수정 : reply.js에서 update() 메서드 작성 ->get.jsp에서 실제 존재하는 rno를 사용하여 댓글내용을 수정하도록 update()메서드 작성  
      (8)댓글조회 처리 : reply.js에서 get()메서드 작성 -> get.jsp에서도 단순하게 get() 메서드를 호출하는 코드 작성 
      (9)댓글목록 화면 HTML처리 : 버튼등에서 이벤트를 감지하고 Ajax처리 결과를 화면에 보여주는 단계. get.jsp에서 <div>로 댓글화면 처리. rno는 꼭 보여줘야하는 정보임.   
      (10)댓글이벤트 처리 : get.jsp에서 조회페이지가 열리면 자동으로 댓글목록이 보이도록 처리 !!!!!이벤트처리 코드 이해안됨 다시 볼것
      (11)시간 처리 : reply.js와 displayTime()메서드 작성(시간기준은 국가마다 다르니 화면에서 포맷처리 해주는것이 좋음) 후 get.jsp에서 댓글전체의 시간을 배열 형태로받도록 처리 
      (12)새로운댓글 처리 : get.jsp에서 댓글목록 상단에 새로운댓글 등록버튼 추가 -> SBAadmin2 에서 pages폴더 내 notifications.html안에 모달코드 사용하여 새로운댓글 모달창 코드 추가 
      (13)새로운댓글의 추가 버튼 이벤트처리 : get.jsp의 javascript에서 모달과 관련된객체들은 여러함수에서 사용할 것이므로 바깥쪽으로 빼서 새로운댓글 버튼 클릭시 모달창에서 댓글 등록할 수 있도록 새로운댓글 모달창 이벤트 처리 코드 추가 
      (14)댓글등록과 댓글갱신 : get.jsp의 javascript에서 작성자, 댓글내용만 남겨두고 나머지정보는 안보이게처리 
      (15)특정댓글의 클릭 이벤트차리 :get.jsp의 javascript에서 jQeury 이벤트위임 방식으로 특정 댓글클릭 이벤트처리 추가 -> 특정댓글 클릭시 모달창에서 보여지는 코드 추가 
      (16)댓글의 수정/삭제처리 : get.jsp에서 댓글 수정 이벤트처리 코드 추가 -> get.jsp에서 댓글 삭제 이벤트처리 코드 추가 
  10)댓글 페이징처리 
      (1)데이터베스의 인덱스 설계(SQL&XML) : SQL 에서 tbl_reply의 인덱스 생성 -> ReplyMapper.xml에 게시물당 10개씩 댓글데이터를 가져오는 페이징처리 쿼리 작성 -> ReplyMapperTests에 testList2()메서드로 확인 
      (2)댓글의 숫자파악(Mapper) : 댓글의 페이징처리를 위해서는 댓글목록과 전체댓글개수 전달 필요. ReplyMapper에서 getCountByBno() 메서드 추가 -> ReplyMapper.xml 에서 id속성값이 getCountByBno인 <select>추가 
      (3)댓글과 댓글개수 처리(Service) : org.zerock.domain패키지에서 ReplyPageDTO 클래스 생성 후 목록과 댓글카운트변수 선언 -> ReplyService에서 댓글목록과개수 전달하는 getListPage() 메서드 선언 후 ReplyServiceImpl에서 구현부작성 
      (4)댓글과 댓글개수 처리(Controller) : 추가된 getListPage()를 호출하고 데이터를 전송하는 형태로 수정  
  11)댓글 페이지의 화면 처리 
      (1)댓글 페이지의 화면처리 : (구조)조회페이지에 들어오면 가장 오래된댓글을 가져와서 1페이지에 보여줌 -> 1페이지를 가져올때 해당 게시물의 댓글의 숫자를 파악해서 페이지번호 출력 -> 댓글이 추가되면 댓글의 숫자만 가져와서 최종페이지에 찾아서 이동 -> 수정과삭제 후에는 다시 동일 페이지 호출 
      (2)댓글페이지 계산과 출력 : Ajax로 댓글목록과 댓글개수 를 가져오는 처리는 reply.js의 getList()에서 처리 -> 이에대한 댓글페에지를 호출 하는 화면 처리는 get.jsp에 showList() 작성
                            -> get.jsp에서 댓글은 화면상에서 댓글이 출력되는 영역의 아래쪽에 <div class='panel-footer'>추가 후 여기에 페이지번호 출력하는 showReplyPage()메서드 추가 
                            -> showList()의 마지막에 페이지를 출력하도록 수정 ->get.jsp에서 javascript에서 페이지번호를 클릭했을때 새로운 댓글 가져오도록 하는 부분 replyPageFooter.on 작성 
      (3)댓글 수정/삭제 : 댓글이 수정/삭제되면 현재 댓글이 포함된 페이지로 이동 하도록 수정 -> get.jsp에서 현재댓글이 포함된 페이지로 이동하도록 modalModBtn과 modalRemoveBtn 수정 

12. AOP와 트랜젝션 
   -> ex04 











*/