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

14. Spring Web Security를 이용한 로그인처리
   14-1) Spring Web Security를 소개 
       1) 환경설정 : ex06생성 -> ex05의 pom.xml을 그대로 가져온다. 그외에도 spring-security-web, spring-security-config, 
                  spring-security-core, spring-security-taglibs 등의 라이브러리의 버전을 수정 혹은 추가 
         (1)security-context.xml 생성 : src/main/webapp/WEB-INF/spring 위치에 Spring Bean Configuration File 로  security-context.xml 생성 
                                       -> 네임스페이스에 Bean, security 체크 -> security-context.xml의 전처리기 선언 
         (2)web.xml 설정 : web.xml에 springSecurityFilterChain 필터를 추가 ->  contextConfigLocation  추가 -> security-context.xml에 Authentication-manager 추가 
          * 정 안되면 web.xml, security-context.xml 복사/붙여넣기  
       2) 시큐리티가 필요한 URI설계 : 접근권한을 모든사용자(all)/로그인한사용자(member)/로그인한사용자중 관리자(admin)로 나누어 URI설계. 
                                org.zerock.controller패키지에서 SampleController 생성 후 각 URI에 맞는 메서드 doAll(), doMember(), doAdmin() 작성  
                                -> view폴더에 sample폴더 생성 후 각 URI에 맞는 all.jsp, member.jsp, admin.jsp파일 생성 후 브라우저에 간단히 출력될수 있는 테스트문구작성 
                                -> 서버 프로젝트 경로 /controller 에서 / 로 바꾼뒤 프로젝트 실행 -> 브라우저에서 각 URI 호출시 문구 출력되는지 확인   
       3) 인증과 권한부여 : 
       
   14-2) 로그인과 로그아웃 처리 : AuthenticationManager의 AuthenticationProvider,UserDetailService를 구현하는 방식
       1)접근제한설정 
         (1)접근제한설정 : security-context.xml에 기본설정, 접근제한설정, 권한체크추가 -> 브라우저에서 /sample/member 호출해보면 /sample/all과 다르게 로그인 페이지로 강제 이동됨
       2)단순로그인처리: 스프링시큐리티에서 user(인증정보와 권한을 가진 객체), userid(username). -> security-context.xml에서 AuthenticationManager내부에 UserDetailService 추가 
                        -> PasswordEncoder 대신에 설정한 password 앞에 {noop} 삽입 -> 브라우저에서 /sample/member 호출하여 로그인하면 member.jsp화면 호출됨 
         (1)로그아웃 확인 : (로그인상태로 계속 유지되고 있는상태이고 다시 로그아웃상태로 되려면) /sample/member 페이지에서 개발자도구>Apllication탭>Cookies> "JSESSIONID"와 같이 
                         세션을 유지하는데 사용되는 세션쿠키 오른쪽버튼 눌러서 강제삭제 -> 브라우저에서 /sample/member호출 시 로그인화면 호출됨                     
         (2)여러 권한을 가지는 사용자 설정 : (/sample/admin 처리) security-context.xml에서 ROLE_ADMIN이라는 권한을 가진 사용자가 접근할수 있도록 지정 
                                      -> 브라우저에서 admin 계정으로 /sample/member와 /sampler/admin 모두 접근(로그인)가능한것을 확인할수 있음.  
         (3)접근제한 메시지 : security-context.xml에 AccessDeniedHandler를 직접 구현하여 특정한 URI를 지정 -> org.zerock.controller에서 CommonController클래스 생성 하여 accessDenied()작성  
                          -> views폴더에 accessError.jsp 생성 하여 접근제한문구 작성 
         (4)AccessDeniedHandler 구현: org.zerock.security 패키지생성 -> CustomerAccssDeniedHandler 클래스 생성 하여 권한없는사용자 접근시 accessError()로 리다이렉트 처리하는 handle()메서드 작성 
                                     -> security-context.xml에서 CustomAccessDeniedHandle를 빈으로 등록 -> 브라우저상에서 /sample/admin에 member/member계정으로 로그인하면 
                                        이전과 달리 /accessError로 리다이레트 되는것을 확인 할수 있음 
       3)커스텀 로그인 페이지 : 디자인등의 문제로 대부분 도의 URi를 이용하여 로그인페이지를 다시 제자가여 사용. security-context.xml에 커스텀 로그인 페이지 태그 추가 -> CommonController에 loginInput()메서드 작성 
                           -> views폴더에 customLogin.jsp를 생성 후 지정한 URI에 접근하면 작성된 커스텀 로그인관련 문구가 출력되도록 jsp코드 작성->브라우저에서 잘못된 비번 입력시 Login Error Check Your Account 메세지 출력됨
       4)CSRF 공격과 토큰 : CSRF(사용자의 요청에대한 출처를 검증하지않고 처리하는 허점을 이용한 해킹방식). 
         (1)CSRF토큰 : 사용자가 임의로 변하는 특정한 토큰값을 서버에서 체크하는 방식. 데이터와함께 브라우저에 전송된 토큰값을 서버가 보관하고 있는 토큰의 값과 비교하여 다를경우 처리하지않음
         (2스프링-시큐리티의 CSRF설정 : security-context.xml에서 scrf 태그 추가
       5)로그인성공과 AuthenticationSuccessHandler : (로그인성공 이후 특정한동작을 하도록 제어) CustomLoginSuccessHandler클래스 생성 후 사용자권한별로 로그인성공 처리하는 onAuthenticationSuccess()메서드 추가
                                                  -> security-context.xml 에서 작성된 CustomLoginSuccessHandler를 빈으로 등록하고 로그인 성공후 처리를 담당하는 핸들러 지정 
                                                  -> 브라우저에서 기존과달리 /sample/customLogin을 시작해서 사용자의권한에 따라 다른 페이지들이 호출되는것을 확인할 수 있음
       6)로그아웃의 처리와 LogoutSuccessHandler : (로그아웃시 세션을 무효화 시키는 설정이나 특정한 쿠키를 지우는 작업 지정) security-context.xml에서 특정한URI를 지정하고, 로그아웃 처리 후 직접 로직을 처리 할수 있도록 /customLogout 핸들러 추가  
                                              CommonController에 로그아웃을 결정하는 페이지에 대한 메서드 logoutGET()추가 -> views폴더에 customLogout.jsp 파일 생성 후 로그아웃 문구를 작성함 (로그아웃 후 추가적인 처리 필요할경우 핸들러추가하여 처리) 
                                              -> 로그아웃을 테스트하기 위해서 views 밑에 /sample/admin.jsp 위칠에 로그아웃으로 이동하는 링크 추가 -> 브라우저상에서 로그인페이지(/sample/admin) > 로그인(/customLogin) > 제출버튼 누르면 (sample/admin) 로그인성공 > Logout 버튼누르면 (/customLogout) 로그아웃 페이지 > 로그아웃 버튼 누르면 (/customerLogin?logout) 로그아웃 과 동시에 Logout!! 문구 출력 되는지 확인 
      
   14-3) JDBC를 이용하는 간편 인증/권한 처리 : 데이터베이스가 존재하는 상황에서 MyBatis나 기타 프레임워크 없이 사용하는 방법   
       1)JDBC를 이용하기위한 테이블설정 : jdbc를 이용해서 인증/권한을 체크하는방식은 '지정된형식으로 테이블을 생성하는 방식' 과 '기존에 작성된 데이터베이스를 이용하는 방식' 
                                   ->기존에 작성된 데이터베이스를 이용하는 방식(스프링 시큐리티가 jdbc를 이용하는경우)은 jdbcUserDetailsManager클래스와 SQL오픈소스코드등을 이용할수 있다. 예제에서는 지정된형식으로 테이블을생성해서 사용하는방식(스프링 시큐리티가 지정된 SQL을 이용하는 방식)으로 함 
                                   ->SQL에서 지정된형식의 테이블을 생성해 준다(프로젝트소스코드 참고)-> root-context.xml의 'dataSource' 빈이 등록되었는지 확인 -> security-context.xml의 <security:authentication-manager> 내용 수정 
                                   -> 브라우저에서 /sample/admin 호출해서 id:admin00 password:pw00 로 로그인해보면 별도의 처리없이 필요한 쿼리문이 로그로 찍히지만, 비밀번호가 평문으로 처리되어 있어 비번관련에러뜸.                    
         (1)PasswordEncoder 문제해결 : org.zerock.security 패키지에 CustomeNoOpPasswordEncoder 클래스 생성후 encode(), matches() 메서드 추가 -> security-context.xml에는 CustomNoOpPasswordEncoder 클래스 빈으로 등록   
                                     -> 브라우저에서id:admin00 password:pw00 로 로그인해보면 jdbc를 이용해서 정상적으로 로그인처리가 되는것이 로그 찍혀서 보임                       

       2)기존의 테이블을 이용하는구조 (JDBC) 
         (1)인증/권한을 위한 테이블설계 : (회원관련테이블,권한테이블 따로 설계)SQL에서 각 tbl_member, tbl_member_auth 생성 
         (2)BCryptPasswordEncoder클래스를 이용한 패스워드보호 : (스프링시큐리티에서 제공하는 BCryptPasswordEncoder를 이용해서 패스워드암호화) security-context.xml에 BCryptPasswordEncoder채그 추가(CustomNuOpPassword는 주석처리) 
            - 인코딩된 패스워드를 가지는 사용자추가 : 테스트코드를 위해 pom.xml에 spring-test 추가 -> org.zerock.security 패키지에 MemberTests클래스 추가 -> PasswordEncoder, DataSource 주입후 100명의 회원정보를 기록하고 암호화된 문자열을 추가하는 testInserMember()메서드추가  
                                             ->TBL_MEMBER 조회시 100명의 암호가등록됨 
            - 생성된 사용자에 권한추가하기 : (tbl_member_auth 테이블에 사용자의 권한에 대한정보도 tbl_member_auth 테이블에 추가) MemberTests 클래스에서 testinsertAuth() 메서드추가 
         (3)쿼리를 이용하는 인증 : (위와같이 지정된방식이 아닌 테이블구조를 이용하는경우에는 '인증을 하는데 필요한쿼리(user-by-usernmae-query)' 와 '권한을 확인하는데 필요한 쿼리(authorities-by-query)'를 이용해서 처리) 
                              security-context.xml에 각각의 쿼리문을 PreparedStatement에서 사용하는 구문으로 바꾸고 <security:jdbc-user-service>태그 속성으로 지정 (dataSource 태그는 주석처리)  
                              -> 테스트코드 testInsertAuth()실행후, 브라우저에서 admin90/pw90 으로 로그인하면 콘솔에 정상처리 되는것이 찍힘
 
  14-4)커스텀 UserDetaillsService 활용 
       : JDBC를 이용하는 방식은 데이터베이스를 처리해서 편리하게 사용할수 있기는 하지만 사용자의정보를 제한적으로 사용. 이를 해결하기위해 UserDetailsService를 구현하는 방식이 좋음 (커스텀 UserDetailsService)
          (직접구현하는 방법도 있지만 예제에서는 커스텀 UserDetailsService를 이용하기위해 MyBatis를 이용하는 MemberMapper와 서비스를작서앟고 스프링시큐리티랑 연결하여 사용하는방식으로 진행 )      
      1)회원도메인, 회원 Mapper : (tbl_member, tbl_member_auth 테이블을 MyBatis를 이용하는 코드로 처리) org.zerock.domain 패키지에서 MemeberVO, AuthVO 클래스 생성 후 정보들 변수선언 
         (1)MemberMapper : (Member 객체를 가져오는경우에 한번에 tbl_member와 tbl_member_auth를 조인해서 1:N으로 데이터를 처리할수 있는방식으로 MyBatis에 ResultMap이라는 기능을 사용한다) 
                           org.zerock.mapper패키지에 MemberMapper 인터페이스를 추가 하여 read()선언 
                           ->src/main/resources 밑에 org.zerock.mapper폴더구조를 작성하여 MemberMapper.xml를 생성하고 ResultMap을 이용하여 데이터를 불러오는 read() 매퍼작성 
         (2)MemberMapper 테스트 : 먼저 root-context.xml은 이전예제에서 아용했던 파일을 재사용 -> src/test/java에 org.zerock.Mapper 패키지, MemberMapperTests클래스 생성후 
                                admin90에 대한 정보를 조회하는 코드작성 -> 콘솔창에 MemeberVO와 AuthVO가 구성된것을 확인할수 있음 
      2)CustomUserDetailsService 구성 : (CustomeUserDetailsService는 스프링시큐리티의 UserDetailService를 구현하고 MemberMapper타입의 인스턴스를 주입받아서 실제기능구현) 
                                       ->org.zerock.security패키지에 CustomUserDetailService클래스 생성 loadUserByUsername() 메서드로 일단 로그만 제대로 찍히는지확인
                                       ->security-context.xml에 CustomUserDetailsService클래스를 스프링의빈으로 등록 및 태그 추가(jdbc-user-service 주석처리) 
                                       -> 브라우저상에서 read() 테스트코드 실행후 로그인을 지도 했을때 지정된 로그 출력되었는지 확인 
         (1)MemberVO를 UserDetails 타입으로 변환 : (스프링 시큐리티의 UserDetailsService는 loadUserByUsername()이라는 하나의 추상메서드만 가지고 있으며 
                                               리턴타임은 UserDetails이므로 MemberVO의 인스턴스도 UserDetails 타입으로 반환해야함) org.zerock.security 패키지에 
                                               별도의 domain 패키지를 추가해서 CustomUser클래스 생성 후 MemberVO의 리턴타입을 변환해주는 CustomUser() 메서드 두개 작성
                                               -> CustomeUserDetailService에 loadUserByUsername()메서드에서 CustomUser타입의 객체로 반환 하는 부분 추가
                                               -> 브라우저에서 이를 테스트해보면 로그인시 CustomUserDetailsService가 동작하는 모습확인가능  
 
 14-5)스프링 시큐리티를 JSP에서 사용하기 : 사용자의 이름이나 이메일 같은 추가적인정보 이용 
     1)JSP에서 로그인한 사용자 정보 보여주기 : (스프링 시큐리티와 관련된 정보를 출력하거나 사용하려면) admin.jsp 상단에 스프링시큐리티 관련 태그 선언, <sec:authentiation>태그와 principal이라는 이름의 속성사용 
                                       ->브라우저에서 아용자가 admin90으로 로그인을 한 후에 admin.jsp에는 사용자에대한 정보들이 출력됨 
     2)표현식을 이용하는 동적 화면 구성 : 스프링 시큐리티의 표현식은 검새해보면 나옴. 예제에서는 사용자의 로그인상태에 따라 다른화면을 구성하는표현식을 all.jsp애 작성한다 ->브라우저에서 /sample/all 화면은 로그인한경우와 안한경우에 따라 다른결과를 만들어냄  
 
 14-6)자동로그인 : 스프링 시큐리티에서 자동로그인은 'remember-me' 기능을 사용하여 쿠키에 사용되는값을 암호화, 기존로그인정보 기록, 쿠키이름지정, 체크박스, 쿠키의 유효기간을 지정할수 있음
                remember-me 기능 또한 지정된 이름의 테이블을 생성하면 지정된 SQL문이 실행되면서 이를 처리하는 방식과 직접구현하는 방식이 있으나 예제에서는 지정된형식의 테이블을 생성하는방식으로 함. 
     1)데이터베이스를 이용하는 자동로그인
       - 로직 : 로그인이 되었던 정보를 데이터베이스를 이용해서 기록해 -> 재방문시 세션에 정보가 없으면 데이터베이스를 조회해서 사용하는방식 (서버의 메모리상에만 데이터를 저장하는방식보다 안정적)
       - SQL에서 persistent_logins 테이블생성 ->security-context.xml에서 'data-source-ref' 지정 
         (1)로그인화면에 자동 로그인 설정 : (자동로그인은 로그인화면에서 선택해서 처리하므로 체크박스로 구현)customLogin.jsp에서 자동로그인 화면처리 추가 
                                     ->브라우저 customLogin에서 'Remember-me'를 체크한 후 admin90/pw90으로 브라우저에서 로그인한뒤, 
                                       쿠키를 조사해보면 자동으로 'remember-me'라고 쿠키가 생긴기고 데이터베이스 persistent_logins 테이블에도 로그인정보 확인할수 있음.  
     2)로그아웃시 쿠키 삭제 : security-context.xml의 /customLogout에 로그아웃시 쿠키삭제 추가 
 
 14-7)자바를 이용한 스프링 시큐리티 설정 -> jex06
 
 14-8) 어노테이션을 이용하느 스프링 시큐리티설정 : (매번 url에 따라서 설정을 변겅하는것 보다 어노테이션으로 간단하게 필요한 스프링 시큐리티 설정을 할수 있음) SampleController에서 @Secured @PreAuthorize @PostAuthorize를 사용하는 메서드 doMember2() 추가
                                        -> (설정은 기존과 다르게 security-context.xml에서 하는것이 아닌 스프링 MVC설정을 담당하는 servlet-context.xml에서 추가)servlet-context.xml의 네임스페이스에 security 체크 (네이스페이스 추가할때 버전 5.0으로 추가될시 4.2로 바꾸던가 아예 삭제) 
                                           및 security네임스페이스를 이용해서 global-method-security를 지정(어노테이션은 기본으로 disabled로 되어 있는데 이것을 enabled로 설정. 

 14-9) 기존프로젝트에 스프링 시큐리티 접목하기 => ex07
 
 

*/







