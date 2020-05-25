package org.zerock.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.domain.SampleVO;
import org.zerock.domain.Ticket;

import lombok.extern.log4j.Log4j;

@RestController //사용자정의 클래스타입을 리턴으로 반환. REST API 
@RequestMapping("/sample") //URL
@Log4j //로그찍기 
public class SampleController {
	
	//단순문자열반환
	@GetMapping(value = "/getText", produces = "text/plain; charset=UTF-8") 
	//@GetMapping(value = "/getText" ; : URL 
	//produces = "text/plain; : 반환타입, charset=UTF-8" : 한글깨짐방지. 
	//produces:메서드가 생산하는 MIME타입. 문자열로 직접 지정도 가능하며, MediaType클래스 이용할수있음
	public String getText() {

		log.info("MIME TYPE: " + MediaType.TEXT_PLAIN_VALUE);

		return "안녕하세요";

	}
	
	//SampleVO 리턴하는 메서드. XML,JSON방식의 데이터를 생성 
	@GetMapping(value = "/getSample", 
			produces = { MediaType.APPLICATION_JSON_UTF8_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public SampleVO getSample() {

		return new SampleVO(112, "스타", "로드");

	}
	
	@GetMapping(value = "/getSample2")
	public SampleVO getSample2() {
		return new SampleVO(113, "로켓", "라쿤");
	}
	
	//컬렉션 타입의 객체 반환(배열,리스트)
	@GetMapping(value = "/getList")
	public List<SampleVO> getList() {

		return IntStream.range(1, 10).mapToObj(i -> new SampleVO(i, i + "First", i + " Last")) //1~10루프돌며 SampleVO객체 만들어서 List<SampleVO>로 만들어냄. mapToObj 함수에서 반환하는 타입대로 객체 변환  
				.collect(Collectors.toList()); //Collectors : 객체를 원하는 처리 후 원하는 타입으로 반환 
 
	}
	
	//컬렉션 타입의 객체 반환(맵) 
	@GetMapping(value = "/getMap")
	public Map<String, SampleVO> getMap() {

		Map<String, SampleVO> map = new HashMap<>(); //Map을 이용하는 경우 키(First)에 속하는 데이터는 XML로 변환되는 경우에 태그의 이름이 되기 때문에 문자열을 지정 
		map.put("First", new SampleVO(111, "그루트", "주니어"));

		return map;
	}
	
	
	//ResponseEntity: 정상적인 데이터인지 비정상적인 데이터인지 구분
	@GetMapping(value = "/check", params = { "height", "weight" })
	public ResponseEntity<SampleVO> check(Double height, Double weight) {

		SampleVO vo = new SampleVO(000, "" + height, "" + weight);

		ResponseEntity<SampleVO> result = null;

		if (height < 150) {
			result = ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(vo); //파라미터 height에따라 Bad 메세지 출력 
		} else {
			result = ResponseEntity.status(HttpStatus.OK).body(vo); //데이터와 OK 메세지 출력 
		}

		return result; //개발자도구->http://localhost:8080/sample/check.json?height=140&weight=60 (height<150이면,502)
	}
	

	@GetMapping("/product/{cat}/{pid}") //URL안에서 {}안에 입력된 값은 변수로 전달되어 컨트롤러 처리 
	public String[] getPath(@PathVariable("cat") String cat, @PathVariable("pid") Integer pid) { //@PathVariable : URL의 일부를 변수{}로 사용하고 싶을때 

		return new String[] { "category: " + cat, "productid: " + pid }; //http://localhost:8080/sample/product/bags/1234 이중에서 category:product, pid:1234는 변수로 전달 받을 수 있음 
	}

	
	//JSON 데이터 -> ticket타입으로 변환 
	@PostMapping("/ticket")  //@PostMapping : @RequestBody가 말그대로 요청(request)한 내용(body)을 처리 하기 때문에 일반적인 파라미터 전달 방식 사용X 
	public Ticket convert(@RequestBody Ticket ticket) { // @RequestBody : JSON데이터를 원하는 객체로 변환 

		log.info("convert.......ticket" + ticket);

		return ticket;

	}
	
	
	
}





/* REST방식과 Ajax를 이용하는 댓글처리 

1. REST(Represntational State Transfer) 방식의 데이터처리를 위한 여러 종류의 어노테이션
* @RestController : 리턴타입으로 사용자가 정의한 클래스 타입을 사용할 수 있고, 이를 JSON이나 XML로 자동으로 처리 될 수 있음
   - 환경셋팅 : pom.xml -> 스프링 버전 5.0.7, 자바버전 1.8, Maven compile버전 1.8 -> project update
             -> JSON 데이터를 처리하기 위한 Jackson-databind 라이브러리(브라우저에 객체를 JSON이라는 포맷의 문자열로 변환시켜 전송할때 필요함) 추가 
             ->테스트때 직접 자바 인스턴스를 JSON타입의 문자열로 변환해야 하는 일이 있으므로 gson라이브러리 추가 
             -> 서블릿버전수정, Lombok추가 ,Junit 버전 변경, spring-test관련 모듈 추가 
   - 단순문자열반환 : SampleController에 @GetMapping을 통해 단순 문자열 반환 코드 작성 
   - 객체의반환 : domain패키지 생성 -> SampleVO클래스 생성 및 변수 작성 -> SampleConstructor에 SampleVO 리턴하는 메서드(XML,JSON방식의 데이터) 추가 
   - 컬렉션타입의객체 반환(배열,리스트) : SampleController에 1~10루프돌며 SampleVO객체 만들어서 List<SampleVO>로 만들어내는 메서드 (XML,JSON방식의 데이터) 추가 
   - 컬렉션타입의객체 반환(키,맵) : SampleController에 맵을 이용하여 SampleVO객체를 생성여 전송하는 메서드 (XML,JSON방식의 데이터) 추가         
   - ResponseEntity 타입 : REST방식으로 호출하는 경우 데이터자체를 전송하는 방식이므로 데이터를 요청한 쪽에서는 정상적인 데이터인지 비정산적인 데이터인지를 구분할수 있는 확실한 방법을 제공해야한다.
                          ResponseEntity는 데이터와 함께 HTTP 헤더의 상태코드와 에러메세지와 함께 데이터를 전달하므로 확실하게 데이터상태 확인가능함. 
                          SampleController에 데이터를 요청한쪽에서 정상적인 데이터인지 비정상적인 데이터인지 구분하는 메서드 작성 
   - @RestController가 사용하는 클래스에서 사용할 수 있는 몇가지 어노테이션 :
      ㄴ @PathVariable - 일반 컨트롤러에서도 사용이 가능하지만 REST방식에서 자주 사용됨. URL경로의 일부를 파라미터로 사용할 때 이용 -> SampleController 예제                        
      ㄴ @RequestBody - JSON데이터를 원하는 타입의 객체로 변환해야 하는 경우. HttpMessageConverter 타입의 객체 이용하여 다양한 포맷의 입력데이터를 변환
                     domain패키지에 Ticker클래스 생성 -> 변수선언 -> SampleController에 Ticket 메서드 추가 
   - REST방식의 테스트 : 
      ㄴ JUnit 방식 - test/java 폴더아래 SampleControllerTests 클래스 생성 > 예제코드 
      ㄴ spring-test 방식 등 
   - 다양한 전송방식 : 
   
2. Ajax 댓글처리 
   - 환경설정 : part3 참고 
   - 댓글처리를 위한 영속처리 : SQL 테이블 생성과처리 -> domain패키지에 ReplyVO 클래스 생성 
                         (댓글에 대한 처리는 화면상에서페이지처리가 필요할수 있으므로 Criteria를 이용) 
   - ReplyMapper클래스와 xml처리 : mapper패키지에 ReplyMapper 인터페이스생성, ReplyMapper.xml 생성 -> xml파일에 네임스페이스 작성 
                                (xml에서는 tbl_reply 테이블에 필요한 SQL을 작성) 
                                -> tbl_reply 테이블이 tbl_board 테이블과 FK(외래키)의 관계로 처리 되어 있어 bno값이 정확하게 일치 해야함. tbl_board의 bno값 확인. 
                                   SQL에서 select * from tbl_board where rownum < 10 order by bno desc 
                                -> ReplyMapper사용가능한지 테스트 하기 윈해 ReplyMapperTests 클래스 생성 후 테스트코드 작성
   - CRUD 작업 : 
      등록 : ReplyMapper 인터페이스에 등록 메서드 추가 -> ReplyMapper.xml 에 등록 코드 추가 -> ReplyMapperTests에 bno를 사용해서 테스트코드 작성 
            ->tbl_reply에 bno 존재하는지 확인 select * from tbl_reply order by rno desc;
      조회 : ReplyaMapper 클래스에 조회(read)클래스 생성 및 xml파일에 read 구현코드 추가 -> ReplyMapperTests에 댓글 조회 테스트 추가 
      삭제 : ReplyaMapper 클래스에 삭제(delete)클래스 생성 및 xml파일에 delete 구현코드 추가 -> ReplyMapperTests에 댓글 삭제 테스트 추가 
      수정 : (tbl_reply에서 댓글내용과 최종수정시간을 수정) ReplyaMapper 클래스에 수정(update) 추가 및 xml파일에 update 구현코드 추가 -> ReplyMapperTests에 댓글 수정 테스트 추가 
      
   - @Param 으로 두개이상 데이터를 파라미터로 전달 : ReplyMapper에 페이징처리,게시물번호를 파라미터로 보내는 getListWithPaging 함수 및 xml파일에 구현코드 추가 -> ReplyMapperTests에서 댓글이 있는 게시물 번호 확인 코드 추가 
     -> 서비스영역처리 : ReplyService 인터페이스 생성 및 register,get,modify,remove,getList 메서드 선언 -> 그에 상응하는 ReplySetvieImpl 구현코드 작성  
     -> 컨트롤러영역처리 : ReplyController 클래스 생성 후 @RestController방식으로 URL기준으로 동작하며 ReplyService와 ReplyServiceImpl을 주입받도록 설계 
     -> 등록작업과 테스트 : 브라우저에서 서버 호출시 데이터 포맷과 서버에서 보내주는 데이터타입을 명확히 설계 및 코드 추가 -> 서버 절대경로 설정 "/" 
        -> 브라우저에서 크롬확장프로그램(Talend API tester)에서 존재하는 게시물번호,댓글내용,댓글작성자를 JSON문법에 맞게 출력되는지 확인 -> 데이터베이스에 정상적으로 추가되었는지 확인 
        -> 특정게시물의 댓글 목록 확인 : ReplyController에 특정게시물댓글목록 확인코드 추가 -> @PathVariable을 이용해서 파라미터처리 후 브라우저에서 테스트 (/replies/pages/10/1) ->XML타입으로 댓글데이터들이 나옴
        -> 댓글 삭제/조회/수정 : ReplyController 삭제/조회/수정 코드 추가 -> JavaScript의 모듈화 : webapp/resoures/js/reply.js 생성  
        -> 로그 코드 작성 -> veiws/board/get.jsp 파일로 이동하여 Javascript 코드 작성 -> 브라우저상에서 reply.js가 문제없이 로딩되고 있는지 확인(개발자도구) 
        -> 모듈 구성 하기 :reply.js에 실행결과 찍히는 함수 선언후 get.jsp에 JavaScript 코드 수정 후 개발자도구에서 출력되는지 확인
        -> reply.js 등록 처리 : reply.js에서 즉시 실행하는 함수 내부에서 필요한 메서드를 구성해서 객체를 구성하는 방식의 코드 작성 -> 개발자도구에서 메서드 출력되는지 확인
                              -> reply.js에서 add 함수는 Ajax를 이용해서 POST방식으로 호출하는 코드 작성 ->  reply.jsp를 이용하는 get.jsp에 테스트를위해 replyService.add() 작성 후 호출 
                              -> 데이터베이스에 정상적으로 댓글 추가가 되는지, 브라우저에 경고창이 제대로 뜨는지, 브라우저에서 JSON형태로 데이터가 전송되고 있는지, 전송되는 데이터 역시 JSON형태로 전송되는지, 콘솔창에 JSON데이터가 ReplyVO 타입으로 변환되는지 확인해야함
        -> 댓글의 목록처리 : 브라우저에서 특정 게시물의 데이터 조회(/replies/pages/게시물번호/페이지번호.xml or .json) -> reply.js에서는 Ajax 호출을 담당하므로 jQuery의 getJSON()을 이용해서 처리 할 수 있음. reply.js에 getJSON()을 이용한 목록처리 구현 코드 작성
                         -> get.jsp에서 해당 게시물의 모든댓글을 가져오는지 확인하는 코드 작성 
        -> 댓글삭제와 갱신 : reply.jsp에 remove() 함수 추가 -> DELETE방식으로 데이터를 전달하므로 $.ajax()를 이용하여 구체적으로 type속성으로 delete 지정 (get.jsp) 
        -> 댓글수정 : reply.jsp에 update() 함수 추가 -> get.jsp에 replyService.update함수 추가
        -> 댓글조회 :       "     get()     "     ->     "      replyService.get함수 추가 
        -> 이벤트처리와 HTML처리 : get.jsp에 목록을 위해 <div>를 게시글과 관련된 화면 아래쪽에 추가, 댓글 목록을 위한 <ul>, 댓글을위한 <li> , rno속성등과 관련된코드 추가
                               -> 조회페이지가 열리면 자동으로 댓글목록을 가져와서 <il>태그를 구성해야 함.이에대한처리는 get.jsp에 $(document).ready() 내에서 코드추가 
        -> 시간에대한처리 : reply.jsp에 당일업데이트 '시/분/초', 당일업데이트 '년/월/일' 전날업데이트 displayTime(timevalue) 함수 추가 -> get.jsp에도 ajax에서 데이터를 가져와서 HTML을 만들어주는 부분에 
                        replyService.displayTime(list[i].replyDate')의 형태로 적용 
        -> 새로운 댓글처리 : get.jsp에 New reply 버튼 생성 코드 추가 -> 댓글추가는 모달창을 이용하는데, get.jsp에 <script>태그 시작전에 추가(모달창코드는 SBadmin2의 pages폴더내 notifications.html에 있음)                        
                         -> 새로운 댓글 추가 버튼 이벤트처리 : 모달과 관련된 객체들은 여러함수에서 사용할 것이므로 바깥쪽으로 빼두어 매번 jQeury를 호출하지 않도록 함 -> 
          
        ->댓글등록 및 목록갱신 : get.jsp에 필요한 댓글내용과 댓글의 작성자 항목만으로 추가해서 모달창 아래쪽의 Register버튼을 클릭해서 처리 하도록 코드 추가 -> 추가후 목록리스트 갱신하는 코드도 작성
        ->특정 댓글의 클릭이벤트 처리(수정,삭제) : get.jsp에서 이미 존재하는 요소에 이벤트를 걸어주고, 나중에 이벤트의 대상을 변경해 주는 방식
                                         -> 댓글조회 클릭 이벤트 처리 (모달창) : get.jsp에서 rno로 조회 후 수정및 삭제하도록 처리 후 'data-rno'속성을 추가하여 특정댓글을 클릭하면 아래 모습처럼 필요한 내용들만 보이도록 함   
        ->댓글삭제 : get.jsp에 댓글삭제정 코드 추가-> 
     -> 댓글 페이징처리 : 전체댓글을 게시물번호(bno)중심으로 가져와서 화면에 출력해야 한다. SQL에서 인덱스생성 -> 특정게시물의 rno순서대로 데이터 조회설정 -> 댓글 10개씩 2페이지씩 불러들이는 코드 작성
         -> ReplyMapper.xml에 getListWithPaging에 SQL설정 반영 -> ReplyMapperTests에 데스트코드 testList2 로 최종결과 확인 
         -> 해당 게시물의 댓글 숫자파악 : ReplyMapper인터페이스의 일부에 댓글개수 세는 코드 추가 -> ReplyMapper.xml에서는 id 속성값이 getCountByBno인 <select>추가
                                   -> 댓글페이징처리는 댓글목록과 전체댓글개수를 같이전달해야하기 때문에, ReplyPageDTO클래스 생성후 replyCnt, List<ReplyVO> list선언후, 
                                   -> ReplyService 인터페이스에 getListPage(Criteria cri, Long bno), 구현클래스에 List<ReplyVO>와 댓글의수를 같이 전달하는 코드 작성.
                                   -> ReplyController 에서 getListPage()를 호출하고 데이터 전송하도록함 
         -> 댓글의 페이지계산과 출력 : Ajax로 가져오는 데이터가 replyCount와 list라는 데이터로 구성되므로, 이를 처리하는 reply.jsp의 내용도 그에맞게 게시물의 댓글수도 전달하도록 구조수정. 
                                   -> get.jsp에서 파라미터로 전달되는 page변수로 원하는 댓글페이지 가져온뒤, page번호가 -1로 전달되면, 마지막페이지호출. 
                                      새로운댓글을 추가하면 showList(-1);을 호출하여 우선 전체댓글의 숫자파악 후 다시 마지막페이지를 호출해서 이동 -> 화면처리는 get.jsp 의 댓글이 출력되는 영역아래쪽에 <div class='pannel-footer'>처리
                                   -> 페이지번호 클릭시 새로운댓글 가져오도록 get.jsp에서 <a>태그의 기본동작을 제한(preventDefault())하고 댓글 페이지번호를 변경한 후 해당 페이지의 댓글을 가져오도록함
        -> 댓글의 수정과 삭제 : get.jsp에서 댓글의 수정,삭제 시에도 현재 댓글이 포함된 페이지로 이동하도록 수정                               
                                               
 */