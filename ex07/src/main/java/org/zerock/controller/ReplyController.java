package org.zerock.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyPageDTO;
import org.zerock.domain.ReplyVO;
import org.zerock.service.ReplyService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@RequestMapping("/replies/") //@RestController
@RestController //@RestController
@Log4j
@AllArgsConstructor //@Setter 주입 
public class ReplyController {
	
	private ReplyService service;

	//댓글등록 
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/new",  
			consumes = "application/json", 
			produces = { MediaType.TEXT_PLAIN_VALUE })//POST방식으로만 동작 
	//consumes,produces : JSON방식으로 처리, 문자열 반환(@PostMapping와 함께사용) 
	//MediaType.TEXT_PLAIN_VALUE : 요청을 TEXT TYPE의 데이터만 담고있는 요청을 처리하겠다
	public ResponseEntity<String> create(@RequestBody ReplyVO vo) { //@ResquestBody : JSON데이터->ReplyVO타입 변환 

		log.info("ReplyVO: " + vo);

		int insertCount = service.register(vo);

		log.info("Reply INSERT COUNT: " + insertCount);

		return insertCount == 1  
				?  new ResponseEntity<>("success", HttpStatus.OK)//"200 OK" 반환 
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);//"500 Internal Server Error" 반환 
	}
	
	
	 //댓글조회 
	@GetMapping(value = "/{rno}", 
			produces = { MediaType.APPLICATION_XML_VALUE, 
					     MediaType.APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<ReplyVO> get(@PathVariable("rno") Long rno) {

		log.info("get: " + rno);

		return new ResponseEntity<>(service.get(rno), HttpStatus.OK);
 }
	
	
	
	//댓글수정 
	@PreAuthorize("principal.username == #vo.replyer") //댓글작성자도 같이 전송 
	@RequestMapping(method = { RequestMethod.PUT, 
			RequestMethod.PATCH }, value = "/{rno}", 
			consumes = "application/json")
			//produces = {MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<String> modify(
			 @RequestBody ReplyVO vo,  //실제처리되는 데이터는 Reply타입이므로  @RequestBody: JSON데이터->ReplyVO타입 변환 
			 @PathVariable("rno") Long rno) {

		vo.setRno(rno);

		log.info("rno: " + rno);
		log.info("modify: " + vo);

		return service.modify(vo) == 1 
				? new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	
	 //댓글삭제 
	@PreAuthorize("principal.username == #vo.replyer") //JSON으로 전송하는데이터 아래와같이 수정 
	@DeleteMapping("/{rno}")
	//@DeleteMapping(value = "/{rno}", produces = { MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<String> remove(@RequestBody ReplyVO vo, @PathVariable("rno") Long rno) {

		log.info("remove: " + rno);

		return service.remove(rno) == 1 
				? new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	

	/*
	 //특정 게시물의 댓글 목록 확인 
	 @GetMapping(value = "/pages/{bno}/{page}", 
	 produces = {
			 MediaType.APPLICATION_XML_VALUE,  //요청을 XML TYPE의 데이터만 담고있는 요청을 처리하겠다 
			 MediaType.APPLICATION_JSON_UTF8_VALUE }) //요청을 JSON TYPE의 데이터만 담고있는 요청을 처리하겠다
     public ResponseEntity<List<ReplyVO>> getList(
	 @PathVariable("page") int page, // @PathVariable : 게시물번호 파라미터 처리 
	 @PathVariable("bno") Long bno) {


     log.info("getList.................");
     Criteria cri = new Criteria(page, 10);
     log.info(cri);

     return new ResponseEntity<>(service.getList(cri, bno), HttpStatus.OK);
     } 
	*/

	 //댓글페이징처리
	 @GetMapping(value = "/pages/{bno}/{page}", 
				produces = { MediaType.APPLICATION_XML_VALUE,   //요청을 XML TYPE의 데이터만 담고있는 요청을 처리하겠다 
				MediaType.APPLICATION_JSON_UTF8_VALUE })   //요청을 JSON TYPE의 데이터만 담고있는 요청을 처리하겠다
		public ResponseEntity<ReplyPageDTO> getList(@PathVariable("page") int page, 
				@PathVariable("bno") Long bno) {  // @PathVariable : 게시물번호 파라미터 처리

			Criteria cri = new Criteria(page, 10);
			
			log.info("get Reply List bno: " + bno);

			log.info("cri:" + cri);

			return new ResponseEntity<>(service.getListPage(cri, bno), HttpStatus.OK); //getListPage() 호출 
		}
		
		
	
	
	

}
