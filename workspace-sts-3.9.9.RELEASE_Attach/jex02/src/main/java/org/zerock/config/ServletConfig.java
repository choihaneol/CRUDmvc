package org.zerock.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@EnableWebMvc
@ComponentScan(basePackages = { "org.zerock.controller" })
public class ServletConfig implements WebMvcConfigurer { //mvc 기본 설정을 위해 WebMvcConfigurer 상속 

  @Override
  public void configureViewResolvers(ViewResolverRegistry registry) { 
  //ViewResolverRegistry 는 jsp를 이용하여 컨트롤러의 실행결과를 보여줌 
    InternalResourceViewResolver bean = new InternalResourceViewResolver(); //처리결과를 보여줄 viwe 결정 
    bean.setViewClass(JstlView.class);
    bean.setPrefix("/WEB-INF/views/");
    bean.setSuffix(".jsp");
    registry.viewResolver(bean);
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) { 
 //addResourceHandlers는 리소스 등록 및 핸들러를 관리하는 객체인 ResourceHandlerRegistry를 통해 
 //리소스 위치와 이 리소스와 매칭될 url을 등록
	  
    registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
  }

}



/* 

- DispatcherServlet : 클라이언트의 요청을 전달받는다. 
  Controller에게 클라이언트의 요청을 전달하고, Controller가 리턴한 결과값을 View에 전달하여 
  알맞은 응답을 생성하도록 한다.
- HandlerMapping : 클라이언트의 요청 URL을 어떤 Controller가 처리할지를 결정한다.
- Controller : 클라이언트의 요청을 처리한 뒤, 그 결과를 DispatcherServlet에 알려준다. 
- ViewResolver	: Commander의 처리 결과를 보여줄 View를 결정한다.
- View : Commander의 처리 결과를 보여줄 응답을 생성한다.


*/