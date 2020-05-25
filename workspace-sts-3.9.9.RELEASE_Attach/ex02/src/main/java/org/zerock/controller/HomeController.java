package org.zerock.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.zerock.domain.BoardVO;

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
 <웹 게시물 관리> - 전체구조 > 설정 및 테스트 > CRUD,list구현 > list화면 페이징처리 > 검색기능 페이지이동 
*목표 
 -스프링 MVC와 Mybatis를 이용해서 기본적인 CRUD
 -페이징처리 
 -검색기능 
 
 1. 환경설정 
   1)pom.xml : spring version5.0.7/Java1.8 수정, spring-tx/spring-jdbc/spring-test, HikariCP/MyBatis/mybatis-spring/Log4jdbc 라이브러리 추가, 
               jUnit4.12/lombok1.18.0/javax.servlet3.1.0/Maven-compiler-plugin2.5.1 버전변경 -> Maven update
               Build path및 Deployment Assembly에 Oracle JDBC Driver추가 
   2)데이터베이스 : (SQL Dummy data 생성) tbl_board 에 sequence,bno,title,content,writer,regdate,updatedate 생성 
   3)스프링 MVC : (데이터베이스 관련 설정 및 테스트) root-context.xml에 mybatis-spring 네임스페이스 추가, DataSource설정(HikariCP configuration), Mybatis설정(hikariConfig),Mapper스캔 라이브러리 추가 
                                           src/main/resources/Log4jdbc.log4j2.properties 생성 및 코드 추가 ->  
                                           (DataSource와 Mybatis를 연결하기위해) src/test/java/org.zerock/persistence에 JDBCTest,DataSourceTests 클래스 추가하여 연결되는지 테스트 

 2. 영속/비즈니스 계층에서의 CRUD 
   1) 영속계층과 구현준비 
      (1)VO클래스(테이블칼럼) 생성 : org.zerock.domain/BoardVO 생성 후 테이블 컬럼(bno,title,content 등)을 변수로 선언 
      (2)Mybatis의 Mapper 인터페이스 작성/XML처리 : (Mybatis의 Mapper 인터페이스) org.zerock.mapper패키지, BoardMapper인터페이스 생성 -> tbl_board 리스트 소환 getList() 코드 작성 ->
                                               (Mapper 인터페이스 테스트) src/test/java/org.zerock.mapper패키지, BoardMapperTests 클래스 생성 -> BoardMapper주입, tbl_board 소환 테스트 코드 testGetList() 작성  
                                               (Mapper XML 파일) src/main/resources내 org>zerock>mapper폴더를 단계별로 생성 BoardMapper.xml 파일 생성 -> 생성자, 조회처리 getList() 코드추가 
                                                                -> 테스트결과 XML에 SQL문이 처리되었으니 BoardMapper인터페이스에 SQL제거
   2) 영속영역의 CRUD 구현 
      (1)Create(isnert) : BoardMapper에 게시물생성(insert(),insertSelectKey()) 메서드 선언 -> BoardMapper.xml에도 게시물생성(insert(),insertSelectKey()) 처리 작성 -> BoardMapperTests에 게시물생성테스트 코드 testinsert() 추가  
      (2)Read : BoardMapper에 조회(read()) 메서드 선언 -> BoardMapper.xml에도 조회(read()) 처리 작성 -> BoardMapperTests에 조회테스트 코드 testRead() 추가
      (3)Delete : BoardMapper에 삭제(delete()) 메서드 선언 -> BoardMapper.xml에도 삭제(delete()) 처리 작성 ->  BoardMapperTests에 삭제테스트 코드 testDelete() 추가 
      (4)Update : BoardMapper에 수정(update()) 메서드 선언 -> BoardMapper.xml에도 수정(update()) 처리 작성 ->  BoardMapperTests에 수정테스트 코드 testUpdate() 추가 
      
 3. 비즈니스 계층
   1) 비즈니스 계층의 구현준비 
      (1)(비즈니스 패키지생성)org.zerock.service 패키지, BoardService 인터페이스, BoardServiceImpl클래스 생성 -> BoardMapper주입-> 등록/조회/수정/삭제/목록 메서드 대신 테스트용 로그  선언 
         ->BoardServieImple에 -> (스프링의 서비스객체 설정)root_context.xml의 네임스페이스탭에서 context항목을 추가 후 service스캔 코드 추가 -> 
   2) 비즈니스 계층으 구현과 테스트   
      : src/test/java밑에 BoardServiceTests 클래스 생성 및 BoardService 주입 -> 주입 테스트 testExist() 작성 -> 로그출력확인                                                               
      (1)등록 작업의 구현과 테스트 : BoardServiceImple에 등록(register()) 메서드 작성 -> BoardServiceTests에서 등록 테스트코드 testRegister() 작성 -> 로그확인 
      (2)목록 작업의 구현과 테스트 : BoardServiceImple에 목록조회(List<BoardVO> getList()) 메서드 작성 -> BoardServiceTests에서 목록조회 테스트코드 testGetList() 작성 -> 로그확인 
      (3)조회 작업의 구현과 테스트 : BoardServiceImple에 조회(get()) 메서드 작성 -> BoardServiceTests에서 조회 테스트코드 testGet() 작성 -> 로그확인 
      (4)삭제/수정 작업의 구현과 테스트 : BoardServiceImple에 수정(modify()), 삭제(remove()) 메서드 작성 -> BoardServiceTests에서 수정,삭제 테스트코드(testUpdate(),testDelete()) 작성 -> 로그확인                                           

 4. 프레젠테이션 계층
   1)BoardController 작성 
      :org.zerock.controller패지키 및 BoardController클래스 생성 -> BoardService주입 및 URL분기 외 필요한 어노테이션추가 
       -> BoardControllerTests클래스 생성 -> BoardControllerTests에 WebAppConfiguration 주입 -> 가짜 mvc를 위해 MockMvc 주입 등 필요한 어노테이션 추가 및 스프링테스트기능 설정  
      (1)목록 처리와 테스트 : BoardController에 목록 메서드 list() 작성 -> BoardControllerTests에 목록 테스트코드 getList() 작성 -> 로그확인 
      (2)등록 처리와 테스트 : BoardController에 등록 메서드 @PostMapping으로 register() 작성 -> BoardControllerTests에 등록 테스트코드 testRegister() 작성 -> 로그확인 
      (3)조회 처리와 테스트 : BoardController에 조회 메서드 get() 작성 -> BoardControllerTests에 조회 테스트코드 get() 작성 -> 로그확인 
      (4)수정 처리와 테스트 : BoardController에 수정 메서드 modify() 작성 -> BoardControllerTests에 수정 테스트코드 testModify() 작성 -> 로그확인 
      (5)삭제 처리와 테스트 : BoardController에 삭제 메서드 remove() 작성 -> BoardControllerTests에 삭제 테스트코드 testRemove() 작성 -> 로그확인
 
 5. 화면처리 
   1)목록 페이지작업과 includes
      (1) servlet-context.xml에 스프링 MVC의 화면설정에 해당되는 ViewResolver 객체 잘있는지 확인 ->
          게시물리스트의 URL경로인 /WEB-INF/views/board에 list.jsp파일 추가 후 지시자 및 간단한문구 출력하는 코드 추가  -> web modules 절대경로 설정 -> localhost:8080/board/list 호출되는지 확인 
          (SB Admin2 페이지 적용하기) SB Admin2의 pages폴더에 있는 tables.html 내용을 list.jsp의 내용으로 그대로 복사(지시자만 남겨놓고) ->
                                  SB Admin2의 압축을 풀어둔 모든 폴더를 프로젝트 내 webapp/resources 폴더로 복사 -> list.jsp파일에서 CSS나 JS파일의 경로를 변경 ( ../ -> /resources/ ) -> 브라우저에서 board/list 호출하여 확인 
          (includes 적용) views폴더에 includes폴더를 생성 후 그 안에 header.jsp, footer.jsp 파일 생성 
                         -> list.jsp 파일의 처음부터 ~ <div id='page-wrapper'> 라인까지 header.jsp로 이동 -> list.jsp의 코드가 이동된 자리에 지시자 <%include file="../includes/header.jsp" %> 추가 -> 브라우저확인 
                         -> list.jsp 파일의 <div id='page-wrapper'>부터 ~ 마지막라인까지 footer.jsp로 이동 -> list.jsp의 코드가 이동된 자리에 지시자 <%include file="../includes/footer.jsp" %> 추가 -> 브라우저확인
          (jQuery 라이브러리 변경) footer.jsp 에 jquery.mins.js파일 <script>태그 제거 -> jQuery링크 검색해서 header.jsp에 추가 
          (반응형 웹처리) footer.jsp에 기존의 코드대신 교재 p.235의 반응형 코드로 수정    
   2)목록화면 처리 
      : list.jsp에 코드는 p.256과 같이 최소한만 남겨 놓음 -> JSTL의 출력과 포맷을 적용할 수 있는 태그 라이브러리를 추가 -> 브라우저 확인 
     (1)Model에 담긴 데이터 출력 : list.jsp내에 <tbody>태그와 각 <tr> 로 #번호, 제목, 작성자, 작성일, 수정일 출력코드 추가 -> 브라우저 확인 
   3)등록입력 페이지와 등록 처리 
     (1)등록입력 페이지와 등록 처리 : BoardController에 GET방식 register()메서드 추가(별도처리x) -> views/board에 register.jsp 생성 -> <form>태그를 이용해서 필요한 데이터 전송 -> 브라우저에 /board/register 에서 화면깨짐 확인, 화면에서 게시물등록 테스트 
     (2)한글문제와 UTF-8 필터 처리 : 개발자도구에서 확인했을때 한글이 깨진상태로 전송되는것이 아니라면, 콘솔로그에서만 깨진다면, web.xml에 encoding 필터 추가  
     (3)재전송 처리(도배방지) : list.jsp 아래쪽에 <script> 태그를 이용하여 상황에 따른 메세지 확인 코드 추가 (addFlashAttribute와 함께 동작)
     (4)모달창 보여주기 : (SBAdmin2의 pages폴더 내 notification.html 참고) list.jsp의 <table>태그 아래쪽에 모달창 <div>추가 -> jQeury에 모달창 보여주는 작업 추가 ->
     (5)목록에서 버튼으로 이동하기 : list.jsp의 HTML 등록 버튼 구조 수정  -> jQuery에 등록버튼 클릭시 동작 정의 
   4)조회페이지와 이동 
     (1)조회페이지 작성 : view/board 폴더내 get.jsp 생성 -> register.jsp 내용 복사/붙여넣기
                      -> get.jsp에서 게시물번호 보여주는 필드추가 -> 모든데이터 읽기전용 readonly 설정 -> 수정/삭제 페이지로 이동 혹은 원래목록 페이지로 이동하는 버튼 추가 -> 그외 불필요한 코드 삭제 ex)<form>태그 
     (2)목록페이지와 뒤로가기 문제 
         (목록에서 조회페이지로 이동) list.jsp에서 <a> 태그를 이용하여 <tr>부분 수정  
         (뒤로가기의 문제) list.jsp의 javascript부분 window.history객체로 뒤로가기 시 모달창 띄울필요없다는 표시 
   5)게시물의 수정/삭제 처리  
     (1)수정/삭제 페이지로 이동 : BoardController에서 get()과 modify()를 하나의 메서드로 처리하도록 수정
         -> views폴더 내 modfiy.jsp에서 코드 수정. '제목','내용' 등이 readonly속성이 없도록 작성. POST방식으로 처리하는 부분을 위해서 <form>태그로 내용들을 감싸기. 등록일/수정일은 boardVO형태로 맞추기. 수정/삭제/목록 버튼 
         -> 브라우저상 수정페이지(Board Modify Page)화면 처리 확인 -> 브라우저상에서 게시물수정 후 로그 확인      
     (2)조회 페이지에서 <form>처리 : get.jsp에서 <form> 태그 이용하여 수정 -> 사용자가 버튼 클릭시, 해당 id의 <form>태그를 전송하도록 get.jsp의 javascript에서 처리
     (3)수정페이지에서 링크 처리 : modify.jsp에서 사용자가 다시 목록페이지로 이동 할 수 있도록 Javascript내용을 조금 수정 -> 수정된 내용은 클리간 버튼이 list일경우 action,method속성 변경
                             -> /board/list로 이동은 파라미터가 없기 때문에 <form>태그의 모든 내용은 삭제한 상태에서 submit()진행 후 코드는 실행되지 않도록 return 
 
 6. 오라클 데이터베이스 페이징처리 
   1)테이블생성 
     (1)bno 역순 정렬 rownum 처리 
     (2)inline view 게시글10개씩 20개 나오도록 처리
      
 7. 페이지처리(MyBatis)
   파라미터 클래스 생성 : org.zeroc.domain패키지에 Criteria클래스(검색기준) 생성-> Lombok을 이용하여 파라미터(전달할 페이지번호, 한 페이지당 데이터 개수) 생성자 코드작성  
   1) MyBatis 처리 : org.zerock.mapper패키지의 BoardMapper에서 위 Criteria 타입을 파라미터로 사용하는 getListWithPaging() 메서드 작성
                           -> src/main/resources의 org/zerock/mapper/BoardMapper.xml에 getListWithPaging에 해당되는 태그 추가                       
     (1)페이징 테스트와 수정 : src/test/java > org.zerock.mapper > BoardMapperTests 클래스생성 후 테스트메서드 testPaging() 추가   
                          -> 페이지번호(pageNum)과 데이터수(amount)를 변경할수 있게 수정 -> BoartMapperTests에 testPagiging()에 10개씩 3페이지 출력하도록 테스트코드 수정 
                          -> 로그와 SQL developer에서 실행된 결과와 동일한지 체크하고 페이지번호를 변경해서 정상적으로 번호가 처리되는지 확인 
   2)파라미터에 맞춰서 BoardContoroller, BoardService수정 : BoardService 인터페이스의 getList()메서드와 BoardServiceImpl 클채스에 getList()메서드에 Criteria cri 파라미터 추가
                                                      -> BoardServiceTests클래스에 testGetList()메서드 역시 Criteria 타입으로 임의의 페이지번호와 한 페이지당 데이터개수를 추가 
                                                      -> BoardController클래스에 list()메서드에도 Criteria cri 파라미터 추가 -> BoardControllerTests에 testListPaging()역시 Criteria 타입으로 임의의 페이지번호와 한 페이지당 데이터개수를 추가      
 8. 페이징 화면처리(페이지번호 버튼 표시 및 이벤트처리) 
   1)페이징화면처리를 위해 필요한 정보 (계산식 코드나 검색 참고)  
     (1)현재번호 
     (2)시작, 끝 번호 : 끝번호(올림), 시작번호(끝번호에서 보여줄페이지양 - (끝번호-1))  
     (3)이전, 다음 번호  : 이전(시작번호가 1보다 큰경우라면 존재), 다음( 실끝번호(실제 데이터갯수*10/한페이지당 데이터개수)의 올림 이 미리 계산해놓은 끝번호보다 크면 존재 
   2)페이징처리를 위한 클래스 설계 : org.zerock.domain에서 PageDTO생성 및 페이지처리관련 변수 선언 및 pageDTO() 생성자 작성 -> BoardController의 list()메서드에서 pagemaker라는 이름으로 pageDTO를 선언하여 Model에 담아서 화면에 전달 
   3)JSP에서 페이지번호 버튼 생성 : SB admin2이 pages폴더에 있는 tables.html 페이지의 페이지처리를 이용해서 구성하고자함. views/board/list.jsp에서 기존의 <table> 태그가 끝나는 직후에 페이지처리 버튼 생성코드 추가 
   4)페이지번호 이벤트처리 : list.jsp에서 바로 위에서 작성한 페이지버튼 생성 코드에 a 태그에 href속성값으로 페이지번호를 가지도록 수정 -> 실제페이지를 클릭하면 동작하는부분은 별도의 <form> 태그를 이용해서 처리 (actionform)    
                      -> javascript에서 페이지번호를 클릭하면 처리하는 부분 추가
   5)게시글 작업후 목록 버튼을 눌렀을때 항상 1페이지로 돌아가는 오류 해결
     (1)조회페이지 : 먼저 이를 해결하기 위해서는 조회페이지로 갈때 현재목록 페이지의 pageNum과 amount를 같이 전달해야함.
                     list.jsp에서 목록페이지 부분인 ${list}에서 <a>태그에 각 제목에 링크 설정 (<a>태그에 class속성을 부여하여 이동하려는 게시물의 번호만을 가지도록 수정)
                     -> 게시물의 제목을 실제로 클릭했을때 bno값을 전달하면서 pageNum, amount가 담긴 actionForm을 board/list로 제출하도록 javascript에서 클릭 이벤트처리를 새로 작성  
     (2)조회페이지에서 다시 목록페이지로 이동 - 페이지번호 유지 : 조회페이지에 pageNum과 amount가 전달이 되었다면, 다시 목록페이지로 되돌아갈때도 유지시킨다.  
                                                    -> get.jsp의 <form>태그를 통해 pageNum, amount를 인풋 받도록 코드 추가 
     (3)조회페이지에서 수정/삭제 페이지로 이동 : controller에서 modfiy는 get메서드와 같이 처리 하고 있으므로 별도의 수정 필요 없음 
     (4)수정/삭제 처리 : modify.jsp의 <form>에서 pageNum과 amount를 전달할수 있도록 코드 추가 
     (5)수정/삭제 처리 후 이동 : controller의 modify, remove 메서드에서 페이지 관련 새로운 파라미터 (Criteria cri, pageNum, amoun)를 추가    
     (6)수정/삭제페이지에서 목록페이지로 이동 : 수정/삭제처리후 다시 목록페이지로 이동하기 위해 javascript에서 pageNum, amount 파라미터 추가
     (7)Mybatis 에서 전체데이터 개수 처리 : Mapper에서 getCountTotal(Criteria cri)메서드 추가하여 xml에 getCountTotal() 생성하여 연결, service와 serviceImple에서도 
                 전체 데이터개수 계산하는 GetTotal()메서드 코드 추가 ->BoardController에서 전체데이터개수를  파라미터로 받는 코드 추가. 전체데이터 개수가 필수로 필요하지는 않지만 목록데이터와 함께 자주쓰일수 있기 때문에추가하였음. 
 9. 검색처리 
  1)Mybatis에서 데이터 처리 : 검색처리를 위해서는 검색조건(type)과 keyword가 필요함 
                          Criteria 클래스에 keyword와 type추가 lombok으로 생성자처리, getTypeArr()메서드로 검색조건을 배열로 처리하여 동적태그 활용하도록 함-> 
                         BoardMapper에서 키워드별 데이터를 검색하는 조건을 동적태그로 추가 -> BoardMapperTest에서 검색조건이 제대로 작동하고 있는지 확인           
  1-1) <include>,<sql>을 이용한 처리 : Mybatis는 <sql>태그를 이용해서 데이터를 보관한뒤 필요할때마다 <include>시켜서 데이터의일부를 사용하는 기능 있음.  
  2)화면에서 검색조건 처리 : 검색조건과 키워드는 항상 화면 이동시 같이 전송되어야 함
                        -> list.jsp에서 페이지처리 상단에 검색조건 코드 추가-> javascript에서 검색버튼 이벤트처리 -> list.jsp에서 검색조건선택칸과 키워드 보여지는 부분 처리 ->
                        페이지처리에서도 검색조건(type)과 키워드(keyword)는 전달되어야 하므로 페이지처리부분 코드수정 
                        (처리순서 :사용자가 화면에서 검색조건 입력>list.jsp에서 입력값 받고>xml에서 데이터처리)
  3)조회페이지에서 검색처리 : 검색이후에 페이지이동에 있어서 동일한 검색조건과 키워드가 유지되어야 한다. 조회페이지는 아직 Criteria의 type과 keyword에 대한 처리가 없기 때문에 get.jsp에서 keyword, type에 대한 인풋코드를 추가
  4)수정/삭제페에징서 검색처리 : modify.jsp에서 수정/삭제페이지역시 Criteria의 type과 keyword에대한 처리 추가. -> BoardController에서 type과 keyword조건을 같이 리다렉트시에 포함 시켜야함
                          ->modify.jsp에서 javascript에서 필요한 파라미터만 전송하기 위해서 <form>태그의 모든 내용을 지우고 다시 추가하는 방식으로 이용했기 때문에 keyword와 type역시 동일하게 전달하도록 추가한다. 
 
 10. REST방식과 Ajax를 이용하는 댓글 처리 -> ex03 부터 
 
 
 
 
 
 
 */