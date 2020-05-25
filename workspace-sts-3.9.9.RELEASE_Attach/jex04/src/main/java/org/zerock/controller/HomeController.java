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
 
11. AOP와 트랜젝션 (자바에서 처리 하기) 
11-1. AOP 패러다임
  1)AOP 용어 : Target(순수 비즈니스로직), JointPoint(Tartget객체가 가진 메서드), Proxy(중간에 필요한 관심사들을 거쳐 자동/수동으로 Target을 호출),
              PointCut(어떤 메서드에 관심사를 결정할지 결정), Advice(Aspect(추상적인관심사)를 구현한 코드) 
  2)AOP 실습 (1.서비스계층에서 호출시 모든파라미터들을 로그로 기록 2.메서드들의 실행시간을 기록) 
    (1)환경설정 : 스프링 5.0.x, 스프링 AOP버전 1.9.0, AspectJ 1.9.0, spring-test, lombok, Junit 4.12, Aspect Weaver 추가
    (2)서비스 계층 설계 : org.zerock.service 패키지생성, SampleService 인터페이스 추가 -> doAdd() 메서드 추가 -> SampleServiceImple 클래스에 @Service 추가 및 doAdd()구현체 작성 
    (3)Advice 작성 : 로그기록은 '반복적이면서도 핵심 로직도 아니지만 필요하기는 한 기능'이므로 '관심사(Advice)'로 간주하여 LogAdvice로 설계 
                    org.zerock.aop 패키지생성 -> LogAdvice 클래스생성 -> 필요한 어노테이션 및 로그기록 메서드 logBefore() 작성 
  3)AOP설정   
    (1)org.zerock.config패키지 생성 -> RootConfig 클래스 생성-> xml 네임스페이스 추가와 xml 태그 추가하는 대신 자바설정코드 추가  
  4)AOP테스트 : 테스트폴더 org.zerock.service패키지 생성 -> SampleServiceTests 클래스 생성 -> 테스트 코드(AOP설정을 한 target에 대해서 proxy객체가 정상적으로 만들어졌는지 확인) 작성
               -> LogAdvice에 설정문제가 없다면 service변수의 클래스 proxy 인스턴스가 되어 'com.sun.proxy.$Proxy20' 형태로 로그가 찍힘
               -> SampleSertiveTests에 testAdd() 메서드로 doAdd() 테스트코드 작성 후 확인 
  5)args를 이용한 파라미터 추적 
    (1) LogAdvice클래스에 logBeforeWithParam() 메서드 작성 -> 다시 doAdd() 테스트코드를 실행하여 @Before("excution~을 통해 파라미터까지 출력되는것 확인 
  6)@AfterThrowing : LogAdvice클래스에 예외알리는 logException()메서드 작성 -> SampleServiceTests에서 고의적으로 에러코드메서드 testAddError() 작성 하여 doAdd()에 파라미터로 문자열이 전달되었을때 예외로그 출력되는지 확인
  7)@Around와 ProceedingJointPoint : LogAdvice에 실행되는데 걸리는시간을 출력하는 logTime()메서드 작성 -> 테스트코드 testAdd()를 실행하여 확인  !!!logTime()정확히 이해안됨 

11-2. 스프링에서 트랜잭션 관리 
  1)데이터베이스설계와 트랜잭션 : 트랜젝션(비즈니스에서 더이상 쪼개질 수 없는 하나의 단위 작업), 정규화(중복된 데이터를 제거하여 효율을 올리자)
  2)환경설정 : RootConfig에 xml 네임스페이스 추가와 xml 태그 추가하는 대신 자바설정코드 추가  

  
  
  

*/