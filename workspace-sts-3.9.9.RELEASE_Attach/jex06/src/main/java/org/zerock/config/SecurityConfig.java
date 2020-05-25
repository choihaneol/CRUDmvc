package org.zerock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.zerock.security.CustomLoginSuccessHandler;

import lombok.extern.log4j.Log4j;



@Configuration
@EnableWebSecurity //스프링 MVC와 스프링 시큐리티 결합 
@Log4j
public class SecurityConfig extends WebSecurityConfigurerAdapter { //WebSecurityConfigurerAdapter 상속 
//SecurityConfig 클래스는 security-context.xml에 <security:http> 관련설정을 대신함 
	
	
	
	 @Override
	 protected void configure(AuthenticationManagerBuilder auth) throws Exception
	 {
	 log.info("configure ............................");

	 //auth.jdbcAuthentication().withUser("admin").password("{noop}admin").roles("ADMIN");
	 //auth.jdbcAuthentication().withUser("member").password("{noop}member").roles("MEMBER");
	 
	 //문자열 인코딩 
	 auth.inMemoryAuthentication().withUser("admin").password("{noop}admin").roles("ADMIN"); 
	 
	 auth.inMemoryAuthentication().withUser("member").password("$2a$10$dS9YfKUxCn1tsCjz.h6kze5Wp63DpgLIyTtg34CqMwC7h1PStD.H2").roles("MEMBER");	 
	 
	 }


	@Bean  //CustomLogfniSuccessHandler
	public AuthenticationSuccessHandler loginSuccessHandler() {
			return new CustomLoginSuccessHandler();
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		//configure()는 파라미터로 WebSecuriy와 HttpSecurity를 받는 메서드가 있으므로 주의해서 오버라이드함 
		
		/*//초기 동작확인 코드
		http.authorizeRequests()
		.antMatchers("/sample/all").permitAll()
		.antMatchers("/sample/admin").access("hasRole('ROLE_ADMIN')")
		.antMatchers("/sample/member").access("hasRole('ROLE_MEMBER')");
		} */

		//기본 POST방식으로 처리하는 경로가 xml에서는 /login, 
		//java에서는 loginPage()이므로 xml과 동일하게 동작시키는것이 목표이기 때문에 
		//loginProcessingUrl()을 이용해서 /login을 지정해줌
		
		//로그인페이지로 이동,로그인 설정 
		http.authorizeRequests()
		.antMatchers("/sample/all").permitAll()
		.antMatchers("/sample/admin")
		.access("hasRole('ROLE_ADMIN')")
		.antMatchers("/sample/member")
		.access("hasRole('ROLE_MEMBER')");

		http.formLogin()
		.loginPage("/customLogin")   
		.loginProcessingUrl("/login") 
		.successHandler(loginSuccessHandler()); //CustomLogfniSuccessHandler	
		
		//로그아웃페이지로 이동, 로그아웃 설정
		http.logout()
		.logoutUrl("/customLogout")
		.invalidateHttpSession(true) 
		.deleteCookies("remember-me","JSESSION_ID"); 
		
	}
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
}
