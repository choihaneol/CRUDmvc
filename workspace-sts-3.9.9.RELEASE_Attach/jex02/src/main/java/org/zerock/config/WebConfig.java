package org.zerock.config;

import javax.servlet.ServletRegistration;

import org.springframework.web.servlet.support.
AbstractAnnotationConfigDispatcherServletInitializer;

public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer {
//AbstractAnnotationConfigDispatcherServletInitializer 는 WebApplicationInitializer 인터페이스의 구현체 
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { RootConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { ServletConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	@Override
	protected void customizeRegistration(
			ServletRegistration.Dynamic registration) { 
	//ServletRegistration.Dynamic:Servlet인터페이스중 등록된 하나를 쓸수 있음. 

		registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");
        //setInitParameter:서블릿 오브젝트 지정 
	}
	

}


