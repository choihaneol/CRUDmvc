package org.zerock.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.log4j.Log4j;


@Controller
@Log4j
public class CommonController {
	

	@GetMapping("/accessError")
	public void accessDenied(Authentication auth, Model model) { //authentication파라미터로 받아서 사용자정보 확인 하도록함  

		log.info("access Denied : " + auth);

		model.addAttribute("msg", "Access Denied");
	}
	
	
	@GetMapping("/customLogin")
	public void loginInput(String error, String logout, Model model) { //에러메세지, 로그아웃메세지 파라미터로 받기 

		log.info("error: " + error);
		log.info("logout: " + logout);

		if (error != null) {
			model.addAttribute("error", "Login Error Check Your Account");
		}

		if (logout != null) {
			model.addAttribute("logout", "Logout!!");
		}
	}
	
	@GetMapping("/customLogout")
	public void logoutGET() {

		log.info("custom logout");
	}
	

}
