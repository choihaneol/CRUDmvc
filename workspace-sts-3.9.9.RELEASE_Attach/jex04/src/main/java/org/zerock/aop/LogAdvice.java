package org.zerock.aop;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j;


@Aspect //Aspect임 
@Log4j //로그 
@Component //빈(객체)로 인식 
public class LogAdvice {

	 @Before( "execution(* org.zerock.service.SampleService*.*(..))") //@Before( "execution ~ :Advice를 적용할 메서드 지정(Pointcut)
	  public void logBefore() {

	    log.info("========================");
	  }	  
	  /*  execution(* some.package..*.*(..)) : 파라미터가 0개 이상인 모든 메서드 호출  */
	 
	 
	 @Before("execution(* org.zerock.service.SampleService*.doAdd(String, String)) && args(str1, str2)") 
	  public void logBeforeWithParam(String str1, String str2) {

	    log.info("str1: " + str1);
	    log.info("str2: " + str2);
	  }  
    /*  .doAdd(String String)) && args(str1, str2) : 테스트코드 확인시 단순한로그 뿐만아니라 파라미터로 파악할수 있음
	     Pointcut부분에 doAdd()메서드를 명시, 파라미터타입 String 지정, && args( 뒤에 변수명 str1, str2 지정하여
	     이 2종류의 정보를 이용해서 logBeforeWithPara()메서드의 파라미터 설정.  */
	 
	 @AfterThrowing(pointcut = "execution(* org.zerock.service.SampleService*.*(..))", throwing="exception")
	  public void logException(Exception exception) {
	    
	    log.info("Exception....!!!!");
	    log.info("exception: "+ exception);
	  
	  } 
	  /* @AfterThrowing : 예외상황 발생 시 문제를 찾을수 있도록 도와줌  
	     Pointcut과 throwing 속성지정, 변수이름 exception으로 지정 */
	 
	 @Around("execution(* org.zerock.service.SampleService*.*(..))")
	  public Object logTime( ProceedingJoinPoint pjp) {
	    
	    long start = System.currentTimeMillis();
	    
	    log.info("Target: " + pjp.getTarget());
	    log.info("Param: " + Arrays.toString(pjp.getArgs()));
	    
	    
	    //invoke method 
	    Object result = null;
	    
	    try {
	      result = pjp.proceed();
	    } catch (Throwable e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }
	    
	    long end = System.currentTimeMillis();
	    
	    log.info("TIME: "  + (end - start));
	    
	    return result;   //@Around : 리턴타입이 void가아닌 지정해줘야함
	  }
	   /* ProceedingJointPoin : @Around와 결합해서 파라미터나 예외처리 가능. AOP의 대상이 되는 Target이나 파라미터 등을 파악 하거나 직접실행. 
	      Pointcut 설정 후 메서드의 파라미터에 ProceedingJointPoint 지정  */
	 
	 
	 
}
