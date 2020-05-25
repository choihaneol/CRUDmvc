package org.zerock.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
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

 14-9) 기존프로젝트에 스프링 시큐리티 접목하기
    - 작업순서: 로그인과 회원가입페이지 작성 -> 기존화면과 컨트롤러에 시큐리티 관련 내용 추가 -> Ajax부분 변경 (기존의 ex05 예제에 ex06(스프링시큐리티)를 접목 시키는방식)
    - 접목시 체크해야할 항목: security-context.xml 추가/org.zerock.security 및 이하 패키지의 추가/org.zerock.domain내에 MemberVO와 AuthVO클래스 추가
                   web.xml에서 security-context.xml설정과 필터추가
                   MemberVO 인터페이스(MemberMapper.java) 와 MemberMapper.xml의 추가
                   org.zerock.controller패키지에 CommonConrotller의 추가 
                   pom.xml에 시큐리티 관련 의존성주입 
    1)로그인 페이지 처리 : (로그인페이지의 기본 HTML 코드는 webapp/resources 폴더 내의 pages/login.html 페이지를 이용) ex06의 customLogin.jsp를 복사
                       -> CSS나 Javascript 파일의 링크는 '/vendor 나 /dist'로 되어 있는 모든링크를 '/resources/vendor나 /resources/dist'로 수정 
                       -> customLogin.jsp 작성시 신경써야 하는것들 
                          - JSTL이나 스프링 시큐리티의 태그를 사용할수 있도록 선언                         
                          - CSS파일이나 JS파일의 링크는 절대 경로를 쓰도록 수정 
                          - <form>태그 내의 <input> 태그의 name속성을 스프링 시큐리티에 맞게 수정 (특히주의)
                          - CSRF 토큰 항목 추가 
                          - JavaScript를 통한 로그인 전송 
                       -> 브라우저에서 로그인이 정상적으로 이루어지는지 확인 -> admin90/pw90으로 로그인시 404 에러가 발생하는것을 확인할수 있음 
                       ->security-context.xml에서 CustomLoginSuccesshandler를 추석처리 한뒤 /customLogin 태그 추라 
    2)게시물 작성시 스프링 시큐리티 처리 : (게시물 작성시 로그인한 사용자에 한해서 처리)servlet-context.xml에서 어노테이션을 위한 설정으로 네임스페이스에서 security 체크 및 
                                   global-method-security 태그를 enabled로 추가. 상단에 spring-security-5.0 일시 4.2혹은 버전 삭제 
                                   -> BoardController에서 register()메서드에 로그인에 성공한 사용자만이 해당 기능을 사용할수 있는 @PreAuthorize("isAuthenticated()") 추가 
      (1)게시물 작성시 로그인한 사용자의 아이디출력 : (게시물작성 페이지는 로그인한 사용자들만 호출할수 있으며, 게시물작성페이지 호출시 작성자항목에 현재 사용자의 아이디를 출력) 
                                            register.jsp에서 작성자항목에 현재사용자의 아이디출력 코드 추가 -> 브라우저 목록페이지에서 게시물등록 버튼 클릭시 로그인화면이 뜨고 
                                            로그인에 성공하면 게시물등록 화면이 나오고, 작성자란의 사용자의 아이디가 나오는지 확인 
      (2)CSRF 토큰 설정(보안) : (스프링 시큐리티 사용시 POST방식의 전송은 반드시 CSRF토큰응 사용해야함) register.jsp에서 CSRF 토큰값을 <form>태그내에 추가 
      (3)스프링 시큐리티 한글 처리 : (스프링 시큐리티 사용시 한글이 깨지는문제) web.xml에서 필터순서를 주의하여 적용해야 함. 인코딩 필터 먼저 적용 후 스프링시큐리티 적용
    3)게시물조회와 로그인처리 : (게시물조회화면에서 현재 게시물의 작성자와 로그인한 사용자정보가 같을경우에만 수정/삭제 작업을 할수 있는 기능) get.jsp에서 button data-oper='modify'주석처리 후 
                          작성자와 사용자의 정보 비교하여 modify버튼 활성화 하는 코드 추가 
      (1)조회 화면에서 댓글 추가 버튼 : (게시물조회화면에서 로그인한 사용자만이 댓글버튼 활성화)get.jsp에서 <sec:authorize>이용하여 로그인한 사람만 댓글버튼 활성화하도록 처리  
    4)게시물의 수정/삭제 : (수정/삭제 페이지에 URL조작으로 접근하는경우를 방지하기 위해 CSRF토큰과 스프링 시큐리티를 적용하기. 현재사용자와 게시물의 작성자가 동일한 경우에만 수정/삭제 활성화)
      (1)브라우저 화면에서 설정 : modify.jsp 상단에 스프링시큐리티의 태그 라이브러리 추가 -> /board/modify를 post방식으로 처리하는 부분에 CSRF 토큰 추가 
                            -> 현재 로그인한 사용자가 게시물의 작성자인 경우에만 수정과 삭제가 가능하도록 제어 
      (2)BoardController에서 제어 : (메서드를실행하기전 로그인한 사용자와 현재 파라미터로 전달되는 작성자가 일치하는지 체크)
    5)Ajax와 스프링 시큐리티 처리 : Ajax를 이용하는경우에 스프링시큐리티가 적용되면 POST,PUT,PATCH,DELETE와 같은 방식으로 데이터를 전송하는경우에는 
                              반드시 X-CSRF-TOKEN와 같은 헤더정보를 추가해서 CSRF토큰값을 전달하도록 수정해야만 함    
      (1)게시물등록 시 첨부파일의 처리: (게시물등록이 POST방식으로 전송되기 때문에 파일첨부가 정상적으로 작동하지 않는것을 알수있음) register.jsp에서 Javscript부분을 
                            csrHeaderName, scrTokenValue 변수 추가하여 beforeSend()를 이용해서 추가적인 해더를 지정하여 전송      
        -첨부파일의 제거: (첨부된 파일을 삭제하는 경우에도 POST방식 으로 동작하므로 CSRF 토큰처리)register.jsp에서 첨부파일 삭제하는 부분에도 CSRF토큰처리
        -UploadController 수정:(서버쪽에서도 어노테이션 등을 이용하여 업로드시 보안확인)UploadController에서 uploadAjax()메서드와 deleteFile() 메서드에 
                              @PreAuthorize 이용하여 외부 로그인한 사용자만 등록/삭제 가능케함   
      (2)게시물수정/삭제에서 첨부파일의 처리 : (게시물의 수정/삭제 시에도 첨부파일은 추가/삭제 될수 있음) modify.jsp에서 CSRF토큰 처리 
      (3)댓글기능에서의 Ajax : (서버입장)로그인한 사용자만이 댓글 추가>로그인한 사용자와 댓글작성자의 아이디를 비교하여 댓글 수정/삭제 활성화 
                           (브라우저입장)댓글등록시 CSRF토큰을 같이 전송>댓글번호 뿐만아니라 작성자도 같이 전송 
                           -댓글등록: get.jsp에서 스프링 시큐리티 태그 라이브러리 추가 -> 댓글작성자를 Javascript의 변수로 설정 -> 모달창에서는 현재 로그인한 사용자이름으로 replyer항목이 고정되도록 수정
                                   -> ajaxSend()를 이용하여 모든 Ajax전송 CSRF 토큰 같이 전송 -> ReplyController에서 댓글이 로그인한 사용자인지 확인하기위해 create() 메서드에 @PreAuthorize추가 
                                   -> 이클립스에서 에러처럼 보일수 있으나 정상작동 하고 있는것임 
                           -댓글삭제: (댓글작성자와 현재사용자비교해서 Ajax로 삭제처리) get.jsp 에서 modalRemoveBtn에서 댓글작성자 같이 전송하도록 코드 수정 -> reply.js 의 remove함수에서도 댓글 작성자 같이 전송하고 데이터타입은 JSON으로 전송하도록 코드 수정 
                                    ->ReplyController의 remove()메서드에서 @PreAuthorize 추가, JSON으로 전송하는데이터 수정, @RequestBody 추가 -> 브라우저를 통해 댓글삭제처리 되는지 확인 
                           -댓글수정: (댓글작성자와 현재사용자비교해서 Ajax로 수정처리) get.jsp 에서 modalModBtn에서 댓글작성자 같이 전송하도록 코드 수정 -> ReplyController에서 modify()메서드에서 MediaType을 주석처리 하고 @PreAuthorize 추가 
                                    -> 브라우저를 통해 데이터가 전달되는지, 서버에서 로그가 찍히는지 확인 
                            
  14-10) 로그아웃처리: 프로젝트에 적용된 템플릿에 로그인페이지의 링크느 views/includes폴더 내에 header.jsp에 정의되어 있음. header.jsp에서 스프링 시큐리티를 설정하고 로그인(/customLogin) 과 로그아웃(/customLogout) 페이지로 이동 하는 처리 추가 
                    ->브라우저에서 확인해 보면 로그아웃/로그인 버튼이 로그인상태에 따라 다르게 활성화됨                                  
    1)로그아웃 페이지: views폴더에 customeLogout.jsp 파일 생성 -> 부트스트랩 테마의 로그인 페이지를 가져옴 -> 가져온 코드를 /customLogout으로 이동하도록 수정 -> 브라우저에서 로그아웃시 로그아웃페이지로 이동하면서 경고창을 보여줌
    2)로그인후 목록페이지로 이동: home.jsp에서 '/board/list'로 이동하도록 추가 -> 브라우저에서 로그인후 목록페이지로 이동하는지 확인                   
                            
                            
        
                            
                            
- controller의 Exception처리 (ex01)
  org.zorock.exception 패키지생성 -> CommonExceptionAdvice 클래스생성 -> @ControllerAdvice와 @ExceptionHandler 사용하여 
  별도의 로직처리없이 "error_page"(JSP파일의경로) 반환 하는 메서드 작성 -> servlet-context.xml에 exception패키지 스캐너 등록 
  -> /WEB_INF/views 위치에 error_page.jsp 파일 생성 -> 에러출려코드 작성 
  (자바로 설정하는 경우)servlet-context.xml에 스캐너 등록하는 대신 ServletConfig클래스에 exception패키지 추가
- 404 에러 페이지 
  web.xml파일에 throwExceptionIfNoHandlerFound 추가 -> CommonExeptionAdvice 클래스에 @ExceptionHandler, @ResponseStatus를 추가하고
  "custom404"를 반환하는 handle404() 메서드 출력 -> WEB_INF/views 위치에 custom404.jsp파일 생성 -> 에러메세지 출력하는 코드 작성
  -> 브라우저에서 URL일부로 잘못 호출해서 에러메세지 출력되는지 확인  
  (자바로 설정하는 경우) web.xml대신 WebConfig클래스에서 throwExceptionIfNoHandlerFound를 처리하는 메서드 customizeRegisteration()메서드 추가 
                        
                            
                            
*/