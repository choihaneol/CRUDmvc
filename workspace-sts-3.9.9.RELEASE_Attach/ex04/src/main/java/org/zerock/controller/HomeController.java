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

11. AOP와 트랜젝션
  11-1. AOP 패러다임
    1)AOP 용어 : Target(순수 비즈니스로직), JointPoint(Tartget객체가 가진 메서드), Proxy(중간에 필요한 관심사들을 거쳐 자동/수동으로 Target을 호출),
                PointCut(어떤 메서드에 관심사를 결정할지 결정), Advice(Aspect(추상적인관심사)를 구현한 코드) 
    2)AOP 실습 (1.서비스계층에서 호출시 모든파라미터들을 로그로 기록 2.메서드들의 실행시간을 기록) 
      (1)환경설정 : 스프링 5.0.x, 스프링 AOP버전 1.9.0, AspectJ 1.9.0, spring-test, lombok, Junit 4.12, Aspect Weaver 추가
      (2)서비스 계층 설계 : org.zerock.service 패키지생성, SampleService 인터페이스 추가 -> doAdd() 메서드 추가 -> SampleServiceImple 클래스에 @Service 추가 및 doAdd()구현체 작성 
      (3)Advice 작성 : 로그기록은 '반복적이면서도 핵심 로직도 아니지만 필요하기는 한 기능'이므로 '관심사(Advice)'로 간주하여 LogAdvice로 설계 
                      org.zerock.aop 패키지생성 -> LogAdvice 클래스생성 -> 필요한 어노테이션 및 로그기록 메서드 logBefore() 작성 
    3)AOP설정   
      (1)프로젝트의 root_context.xml의 네임스페이스에서 aop, context 추가 -> root_context.xml에 어노테이션활성화, service/aop 스캔, autoproxy 태그 추가 
    4)AOP테스트 : 테스트폴더 org.zerock.service패키지 생성 -> SampleServiceTests 클래스 생성 -> 테스트 코드(AOP설정을 한 target에 대해서 proxy객체가 정상적으로 만들어졌는지 확인) 작성
                 -> LogAdvice에 설정문제가 없다면 service변수의 클래스 proxy 인스턴스가 되어 'com.sun.proxy.$Proxy20' 형태로 로그가 찍힘
                 -> SampleSertiveTests에 testAdd() 메서드로 doAdd() 테스트코드 작성 후 확인 
    5)args를 이용한 파라미터 추적 
      (1) LogAdvice클래스에 logBeforeWithParam() 메서드 작성 -> 다시 doAdd() 테스트코드를 실행하여 @Before("excution~을 통해 파라미터까지 출력되는것 확인 
    6)@AfterThrowing : LogAdvice클래스에 예외알리는 logException()메서드 작성 -> SampleServiceTests에서 고의적으로 에러코드메서드 testAddError() 작성 하여 doAdd()에 파라미터로 문자열이 전달되었을때 예외로그 출력되는지 확인
    7)@Around와 ProceedingJointPoint : LogAdvice에 실행되는데 걸리는시간을 출력하는 logTime()메서드 작성 -> 테스트코드 testAdd()를 실행하여 확인  !!!logTime()정확히 이해안됨 
  
 11-2. 스프링에서 트랜잭션 관리 
    1)데이터베이스설계와 트랜잭션 : 트랜젝션(비즈니스에서 더이상 쪼개질 수 없는 하나의 단위 작업), 정규화(중복된 데이터를 제거하여 효율을 올리자)
    2)환경설정 : (트랜잭션매니저오 @Transactional 어노테이션으로 쉽게 설정가능) pom.xml에 spring-jdbc, psring-tx, mybatis, mybatis-spring, hikari 라이브러리 추가 
               ->root-context.xml의 네임스페이스에서 aop,beans,context,mybatis-spring,tx 체크 
               ->root-context.xml에 트랙잭션관리 Hikari관련, sqlSessionFactory, transaction Manager, tx, mapper 스캔 태그 추가
    3)예제테이블 : SQL에서 테이블 2개 생성 ->org.zerock.mapper 패키지 생성-> sample1Mapper, sample2Mapper인터페이스 생성 -> Sample1Mapper는 tbl_sample1(char[500])에 Sample2Mapper는 tbl_sample2(char[50])에 insert 하도록 코드 작성  
    4)비즈니스계층과 트랜잭션 설정 : 트랜잭션은 비즈니스계층에서 이루어지므로 org.zerock.service에서 SampleTxService인터페이스 생성하여 addData()메서드 선언, SampleTxServiceImpl클래스 생성 하여 addData() 구현부 작성
                              -> SampleTxServieTests클래스생성, 테스트코드 testLong()메서드 작성 
                              -> 결과확인시 tbl_sampele1은 문자열이 정상적으로 입력되고 tbl_sample2는 입력안됨  (tbl_sample1, tbl_sample2 트랜잭션처리X)
                              -> SampleTxServiceImpl에 @Transactional 어노테이션추가 (트랜잭션처리) -> 정확한 트랜잭션처리 확인을 위해 SQL에서 tbl_sample1,2를 지움
                              -> 다시 testLong() 실행시 트랜젝션처리전과 다르게 rollback()처리가 된것을 확인할수 있음 -> SQL에서 다시 tbl_sample1,2 확인시 두 테이블 모두 데이터가 입력되지 않은것을 확인할 수 있음
       
12. 댓글과 댓글수에 대한 처리 
  (1)로직 : 댓글이 추가되면 tbl_reply 에 추가되고 tbl_board에 replycnt라는 칼럼을 하나 추가해서 댓글이 추가될때마다 댓글수를 업데이트 하는 방식. 코드는 ex03을 참고하여 작성  
  (2)데이터베이스 :SQL에서 replyCnt 칼럼추가 
  (3)BoardVO, BoardMapper 수정 : ex03의 org.zerock.domain 패키지를 그대로 가져오기, BoardVO클래스에 replyCnt 변수 추가 
                          ->    "    org.zerock.mapper 패키지를 그대로 가져오기, BoardMapper 인터페이스에 updateReplyCnt()선언 
                          ->    "    src/main/resources에서 org>zerock>mapper>BoardMapper.xml, ReplyMapper.xml 그대로 가져오기-> BoardMapper.xml에 updateReplyCnt 메서드 추가, getListWithPaging에도 replycnt 추가     
                          ->    "    org.zerock.service 패키지를 그대로 가져오기, ReplyServiceImple에 boardMapper 주입 -> ReplyServiceImpl에 등록,삭제 메서드에 @Transactional 추가 
  (4)화면수정 ; 게시물목록화면에서 댓글의 숫자가 출력되도록 수정해야함. 
             -> ex03의 src/main/webapp/resources 내 모든 파일과폴더 가져오기 
             ->  "    org.zerock.controlle내 BoardController, ReplyController, SampleController 가져오기 
	         ->  "    pom.xml에서 jackson-databind, jackson-dataformat-xml 라이브러리 추가 
	         ->  "    views폴더안에 board,includes폴더 가져오기 -> list.jsp에서 댓글의 개수가 출력되는 부분 replyCnt 추가 
 
13. 파일 업로드 처리
    => ex05 	
	

*/