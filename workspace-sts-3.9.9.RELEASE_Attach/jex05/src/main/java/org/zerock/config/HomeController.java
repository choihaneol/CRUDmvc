package org.zerock.config;

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
*java로 설정하는경우(xml설정외에 모든내용 ex05와 동일)

 13-9) 잘못 업로드된 파일 삭제 
    - Ajax처리시 사용자가 비정상적인종료를 하거나 페이지를 빠져나가는 경우 다음과같은 문제발생. 첨부파일만 서버에 업로드 되고 게시물은 등록되지 않은경우, 게시물을 수정할때 파일을 삭제했지만(데이터베이스에서) 실제로 폴더에서 삭제되지 않은경우
       이를해결하기 위해, 정상적으로 사용자의 게시물에 첨부된파일인지 아니면 사용자가 게시물을 수정할때 업로드했지만 최종적으로 사용되는 파일인지아닌지 파악하는것이 중요.
     1)잚못 업로드된 파일의 정리 
       - 로직 : (데이터베이스와 비교하는 작업을 통해 업로드만된 파일의목록을 찾기) 어제날짜로 등록된 첨부파일목록을 구한다 -> 어제 업로드가 되었지만 데이터베이스에는 존재하지 않는 파일들을 찾는다 
               -> 데이터베이스와 비교해서 필요없는파일들을 삭제
     2) Quartz 라이브러리 설정 : 주기적으로 특정한 프로그램을 실행할때 사용. pom.xml에 라이브러리 추가 하고 root-context.xml 네임스페이스와 소스코드로 추가하는대신 
                             RootConfig에서 필요한 어노테이션을 추가해준다(그외 나머지는 모두 동일).
           
   
 


*/


