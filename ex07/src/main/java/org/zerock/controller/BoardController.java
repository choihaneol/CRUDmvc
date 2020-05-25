package org.zerock.controller;
//org.zerock.controller 패키지는 servlet-context.xml에 기본으로 설정되어 있음 

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.PageDTO;
import org.zerock.service.BoardService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

//@InitBInder:파라미터 타입변환
@Controller //servlet-context.xml -> 스프링 Bean으로 인식
@Log4j
@RequestMapping("/board/*") //모든 /board로 시작하는 요청 Controller가 처리. GET/POST방식 모두 지원   
@AllArgsConstructor //BoardService에 의존적 이므로 생성자 주입 
public class BoardController {
	
	private BoardService service;

	@GetMapping("/register") //화면에서 GET방식 으로 입력받음.
	@PreAuthorize("isAuthenticated()") //어떤사용자든 로그인에 성공한 사용자만이 해당 기능을 사용할수 있음 
	public void register() {

	}
	
	@GetMapping("/list") //게시물조회 
	public void list(Criteria cri, Model model) { //게시물 목록으로 전달하므로 Model 타입 파라미터사용. //Criteria를 파라미터로 처리
	
	log.info("list" + cri);
	model.addAttribute("list", service.getList(cri)); //model을 BoardServiceImpl객체의 getList()결과를 담아 전달(addAtribute)  
	model.addAttribute("pageMaker", new PageDTO(cri, 123)); //(pageMaker로 Model에 담아서)PageDTO 화면에 전달
	
	int total = service.getTotal(cri);
	
	log.info("total: " + total);
	
	model.addAttribute("pageMaker", new PageDTO(cri, total));
	
	}
	
	
	/*@PostMapping("/register") //게시물등록 
	public String register(BoardVO board, RedirectAttributes rttr) { //
		//RedirectAttributes:등록후 목록으로 이동 및 새게시글번호 전달 
		//리턴 시 redirect: :MVC가 내부적으로 response.endRedirect()처리. 삭제후 목록으로 이동 

		log.info("register: " + board);

		service.register(board);

		rttr.addFlashAttribute("result", board.getBno()); //addFlashAttribute:임시로 값 지정해서 처리 (모달에 result전달)

		return "redirect:/board/list"; //처리후 목록으로 이동 
	}*/
	
	@PostMapping("/register")
	@PreAuthorize("isAuthenticated()")
	public String register(BoardVO board, RedirectAttributes rttr) {

		log.info("==========================");

		log.info("register: " + board);

		if (board.getAttachList() != null) {

			board.getAttachList().forEach(attach -> log.info(attach));

		}

		log.info("==========================");

		service.register(board);

		rttr.addFlashAttribute("result", board.getBno());

		return "redirect:/board/list"; //redirectAttribute:데이터를 일회성으로 전달 
	}
	
	
	@GetMapping({ "/get", "/modify" }) //게시물조회 및 수정 
    public void get(@RequestParam("bno") Long bno, 
    		@ModelAttribute("cri") Criteria cri, Model model) { //@RequestParam:bno값을 좀더 명시적으로 처리.생략가능. Model:게시물 전달 
	//@GetMapping @PostMapping은 URL을 배열처리 할수 있으므로 하나의 메서드로 여러개의 URL처리 가능 	
	//@ModelAttribute : 화면까지 전달되지 않는자료형을 화면까지 전달되도록 Model객체로 전달 
	log.info("/get or modify");
		
	model.addAttribute("board", service.get(bno));
	}
	

	@PreAuthorize("principal.username == #board.writer") //현재사용자와 작성자 검열. remove와 달리 writer를 board 타입 객체로 받음 
	@PostMapping("/modify") //게시물수정, 시작하는화면:GET방식/실제작업:POST방식 
	public String modify(BoardVO board, 
			//@ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
			Criteria cri, RedirectAttributes rttr) {
		//RedirectAttributes:URL 뒤에 원래의 페이지로 이동하기 위해서 pageNum과 amount값을 가지고 이동 
		log.info("modify:" + board);

		if (service.modify(board)) {
			rttr.addFlashAttribute("result", "success");
		}

		//rttr.addAttribute("pageNum", cri.getPageNum());  //페이지관련 파라미터 수정(Criteria형태의 pageNum,amount) 
		//rttr.addAttribute("amount", cri.getAmount());
		//rttr.addAttribute("type", cri.getType());  //type과 keyword조건을 같이 리다이렉트 시에 포함
		//rttr.addAttribute("keyword", cri.getKeyword());
		
		//return "redirect:/board/list"; //처리후 목록으로 이동
		return "redirect:/board/list" + cri.getListLink(); //UriComponentsBuilder에 맞게 정리 
	}
	
	/*
	@PostMapping("/remove") //게시물삭제 
	public String remove(@RequestParam("bno") Long bno, 
			@ModelAttribute("cri") Criteria cri, RedirectAttributes rttr){
		
	    log.info("remove..." + bno);
	    if (service.remove(bno)) {
	    	rttr.addFlashAttribute("result", "success"); 
		}
	    
	    //rttr.addAttribute("pageNum", cri.getPageNum());
	    //rttr.addAttribute("amount", cri.getAmount());
	    //rttr.addAttribute("type", cri.getType());  
		//rttr.addAttribute("keyword", cri.getKeyword());
	    
	    //return "redirect:/board/list"; //처리후 목록으로 이동 
	    return "redirect:/board/list" + cri.getListLink();
	}
	*/
	
	@PreAuthorize("principal.username == #writer") //@PreAutorize:문자열로 표현식을 지정과 동시에 파라미터로 사용 가능. 현재사용자과 작성자 같은지 검열   
	@PostMapping("/remove") //게시물삭제 
	public String remove(@RequestParam("bno") Long bno, Criteria cri,  //@RequestParam:기본자료형이나 문자열 등의 파라미터의 타입만 맞게 선언해주는방식. 파라미터로 선언된 변수명과 전달되는 파라미터의 이름이 다른경우 유용함 
			RedirectAttributes rttr, String writer) { 

		log.info("remove..." + bno);

		List<BoardAttachVO> attachList = service.getAttachList(bno); //첨부파일목록 

		if (service.remove(bno)) { //게시물 삭제 

			// delete Attach Files
			deleteFiles(attachList); 

			rttr.addFlashAttribute("result", "success");
		}
		return "redirect:/board/list" + cri.getListLink();
	}

	
	private void deleteFiles(List<BoardAttachVO> attachList) { //첨부파일삭제 
	    
	    if(attachList == null || attachList.size() == 0) {
	      return;
	    }
	    
	    log.info("delete attach files...................");
	    log.info(attachList);
	    
	    attachList.forEach(attach -> {
	      try {        
	        Path file  = Paths.get("/Users/angela/upload/"+attach.getUploadPath()+"/" + attach.getUuid()+"_"+ attach.getFileName());
	        
	        Files.deleteIfExists(file); //파일삭제 
	        
	        if(Files.probeContentType(file).startsWith("image")) {  //만일 이미지파일이면 
	        
	          Path thumbNail = Paths.get("/Users/angela/upload/"+attach.getUploadPath()+"/s_" + attach.getUuid()+"_"+ attach.getFileName());
	          
	          Files.delete(thumbNail); //섬네일파일삭제 
	        }
	
	      }catch(Exception e) {
	        log.error("delete file error" + e.getMessage());
	      }//end catch
	    });//end foreachd
	  }
	   
	
   @GetMapping(value = "/getAttachList",
		    produces = MediaType.APPLICATION_JSON_UTF8_VALUE) //JSON데이터로 반환 
   @ResponseBody
   public ResponseEntity<List<BoardAttachVO>> getAttachList(Long bno) {

	log.info("getAttachList " + bno);

	 return new ResponseEntity<>(service.getAttachList(bno), HttpStatus.OK); //ResponseEntity: 원하는 헤더정보나 데이터 전달  

   }
	
   

	
}
