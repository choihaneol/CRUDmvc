package org.zerock.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
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
 
<Java 설정을 이용하는 경우의 스프링 시큐리티 설정> 
 - 환경설정 : web.xml/root-context.xml/servlet-context.xml 삭제. log4jdbc.log4j2.properties/JDBC 드라이버 추가. 
           pom.xml에 스프링 시큐리티 관련 파일 및 태그 라이브러리 추가. -> 에러때문에 ex06 그대로 복사함 
           XML설정에 사용했던 코드들(ex06) 프로젝트에 포함. JSP파일들도 WEB_INF/views 폴더내에 추가.  
 1)Java설정 추가 및 동작확인 : org.zerock.config에 패키지 SecurityInitializer 클래스 추가 후 AbstractSecurityWebApplicationInitializer 상속
                          security-context.xml 대신 org.zerock.config에 SecurityConfig 클래스 추가 후  WebSecurityConfigurerAdapter 상속 및 @@EnableWebSecurity 추가  
                          security-context.xml의 <security:http> 를 대신하는 configure() 오버라이드 -> 런해서 springSecurityFilterChain 에러확인
   (1)WebConfig 클래스 : WebConfig 클래스 생성후 기존의코드(ex05)를 똑같이 작성한뒤 getRootConfigClasses() 오버라이드 부분 수정  
                       ServletConfig 클래스 생성후 기존의 코드(ex05)를 똑같이 작성
                       RootConfig                 "                       
                       서브에서 런하여 403 Access denied 에러가 확인됨(로그인인증매니저 없음)    
 2)로그인페이지 관련 설정 : securityConfig에서 로그인페이지로 이동하고 로그인할수 있는설정을 위해 configure(HttpSecurity http) 수정, 
                       configure(AuthenticationManagerBuilder auth) 추가 
   (1)로그인 성공 처리 : securityConfig에서 AuthenticaationSuccessHandler메서드추가, configure(HttpSecurity http) 메서드 내에 CustomLogfniSuccessHandler 추가 
   (2)로그아웃처리 : (로그인후에 생성되는 쿠키의값을 확인 > 로그아웃 이후에 기존의 쿠키값이 삭제 > 다른값으로 변경되었는지 확인 ) SecurityConfig에서 configure(HttpSecurity http) 메서드 내에 로그아웃페이지로 이동, 로그아웃 설정
 3)PasswordEncoder 지정 : (정상적으로 로그인 되는 상황을 테스트 하고 싶다면 패스워드를 PasswordEncoder를 이용해서 인코딩해야만함) SecurityConfig에 PasswordEncoder()메서드 추가
                         ->PasswordEncoderTests클래스 생성 -> member라는 문자열 인코딩하는 테스트코드 testEncode() 작성 
                         ->SecurityConfig 에서 configure(AuthenticationManagerBuilder auth)을 문자열 인코딩할수 있도록 수정 
                         ->브라우저를 통해 member/member 계정으로 정상적으로 로그인이 되는지 확인 
                         
                         !!!!!500 에러고치는 중 !!!!!
                         
 4)JDBC를 이용하는 java설정 : (스프링 시큐리티의 인증은 크게 1)username으로 사용자의 정보를 얻어오는 작업, 2)적당한 권한 등을 체크하는 과정 으로 처리) SecurityConfig에서 DataSource주입, 기존의 configure()메서드 주석처리 한 뒤, 
                          JDBC를 이용하여 스프링 시큐리티를 설정하는 새로 configure()메서드 작성 
                          -> 
 
 
 
  */
