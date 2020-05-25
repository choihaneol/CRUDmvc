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
   무슨환경설정이든 pom.xml과 자바버전 주의할것! 
    
  13-1) 파일 업로드 방식 
       -환경설정(xml) : c드라이브위치에 파일을 업로드 받을 폴더를 upload/temp를 준비 -> ex05 새로운 프로젝트 생성 -> pom.xml 버전변경(java버전,서블릿 버전변경,롬복추가)
                 -> web.xml 상단에 XML 네임스페이스 수정 -> web.xml의 <servlet>태그 내에 <multipart-config>태그 추가 -> servlet-context.sml에 multipartResolver라는 이름의 빈 설정 추가 
       -환경설정(java) : pom.xml에서 <plugin>추가 -> org.zerock.config 패티지생성 -> org.zerock.config에 RootConfig, WebConfig, ServletConfig 클래스 생성 ->       
     1) <Form>방식의 파일 업로드   
        (1)jsp파일과 컨트롤러 : org.zerock.controller에 UploadController 클래스 생성 후 업로드 맵핑해주는 uploadForm()클래스 작성 -> UploadController클래스 선언부에 @RequestMapping이 적용되지 않았으므로 
                            WEB-INF/views폴더에 uploadForm.jsp 파일생성 후 uploadFormAction 폼작성 -> 브라우저 /uploadForm 에서 파일버튼 제대로 나오는지확인 
        (2)MultipartFile : UploadController에서 MultipartFile타입을 이용하는 uploadFormPost()메서드 작성 -> 브라우저상 /uploadForm에서 파일여러개 업로드 했을때 uploadFormAction.jsp가 없기때문에 에러뜨지만 컨트롤러에서 파일데이터가 감지 되는지 확인 
          -파일저장 : UploadController에서 업로드될폴더명과 transferTo()이용하여 파일을 저장하는방법 추가 
     2)Ajax를 이용하여 파일저장 : (실행순서:uploadAjax.jsp upload버튼(#uploadBtn) 클릭 > uploadAjax.jsp에서 자바스크립트에서 #uploadBtn 이벤트처리하는데 formData로 인풋데이터저장 > Ajax로 /uploadAjacAction (컨트롤러) 주소로 formData전송 -> 컨트롤러에서 데이터저장경로에 파일저장  
                             UploadController에 GET방식으로 파일 업로드하는 메서드 uploadAjax()추가 -> WEB-INF/views폴더에 uploadAjax.jsp생성한뒤 jQuery라이브러리 경로 설정, <script>처리로 첨부파일 설정 ->         
        (1)jQuery를 이용하여 첨부파일전송 : uploadAjax.jsp에서 filedata를 formdata에 추가하는 부분과 Ajax로 formdata를 컨트롤러로 넘기는부분 추가 
                                      ->UploadController에서 uploadAjaxPost() 메서드작성 -> 브라우저에서 /uploadAjax을 통해 저장경로로 파일업로드 되는지 확인 
        (2)그외 파일업로드시 고려해야하는 점 : 동일이름파일/대용량이미지 섬네일/이미지파일과 일반파일 구분하여 따로처리/첨부파일공격 대비 -> 14.파일업로드 상세처리에서
  13-2) 파일업로드 상세처리 
     1)파일의 확장자나 크기의 사전처리 
        (1)첨부파일사이즈/확장자 제한 : uploadAjax.jsp에서 파일의 확장자크기를 설정하고, 이를 검사하는 함수 checkExtension()을 작성 -> uploadAJax.jsp에 첨부파일사이즈/확장자제한 메서드 checkExtension() 추가, formData에 checkExtension()메서드 삽입 
        (2)너무 파일이 많을때 : 년/월/일 단위의 폴더를 생성하여 파일저장(java.io.File의 mkdirs()이용하여 필요한 상위폴더까지 한번에 생성할 수 있도록처리).
                                -> UploadController에서 폴더경로를 문자열로 생성하고 폴더경로로 반환하는 getFolder()메서드 작성 -> 필요한 상위 폴더까지 한번에 생성하고 생성된 폴더로 저장하도록 uploadAjaxPost()메서드에 코드 추가 -> 브라우저 /uploadAjax에서 오늘날짜로 생성된 폴더에 업로드되는지 확인 
        (3)파일 동일이름 업로드 : UploadController의 uploadAjaxPost()에 java.util.UUID를 이용하여 난수발생 뒤 원본파일명에 덧붙여 저장하는 방식 의 코드 추가 -> 브라우저에서 같은이름으로 파일을 업로드 했을때 파일명_난수 로 저장되는지 확인         

     2)섬네일 이미지 생성(일반파일과 이미지파일 구분) 
       - 환경설정 : pom.xml에 Thumbnailator 라이브러리 추가 
       - 로직 : 업로드된파일이 이미지파일인지 확인 -> 이미지파일인 경우에만 섬네일이미지 생성 및 저장 
       (1)이미지파일의 판단 및 섬네일파일처리  
          : Uploadzcontroller에 이미지파일인지 아닌지 확인하는 checkImgtype() 메서드 추가 -> uploadAjaxPost()메서드에 이미지타입이라면 섬네일을 생성하는 코드 추가 -> 브라우저에서 사진파일은 파일명 앞에 s_가 붙는 섬네일파일로 저장됨 
     3)업로드된 파일의 데이터반환(화면처리)   
       -브라우저로 전송해야 되는 데이터 : 업로드된 파일의이름과 원본파일의이름/파일이 저장된 경로/업로드된파일이 이미지인지 아닌지에대한 정보. 이 정보들을 별도의 객체를 생성해서 처리. 
       -환경설정: pom.xml jackson-databind 라이브러리 추가 
       (1)AttachFileDTO클래스 : org.zerock.domain 패키지에 첨부파일의 정보들을 저장하는 AttachFileDTO클래스 작성 -> UploadController에 uploadAjaxPost()에 AttachFileDTO를 리스트로 반환하는 부분 추가 
       (2)Ajax 화면처리 : uploadAjax.jsp 에서 결과데이터를 javascript로 JSON데이터로 반환 하도록처리
   
   13-3) 브라우저에서 섬네일 처리 
     1) <input type='file'> (첨부파일)의 초기화 : uploadAjax.jsp 파일에서 첨부파일 초기화 코드 추가 ->  브라우저에서 파일업로드 후 '선택된파일없음' 메세지로 초기화 되는지 확인 
     2) 업로드된 이미지 처리 
       (1)파일 이름 출력(첨부파일 목록으로 보여주기) : uploadAjax.jsp 에서 <ul>태그로 javascript에 첨부파일이름을 목록으로 처리 -> 첨부파일목록을 보여주는 별도의 함수 추가 -> 브라우저에서 업로드 후 파일명 목록으로 보여지는지 확인 
       (2)일반파일의 파일처리 : webapp/resources 폴더안에 기존의 모든폴더와파일을 가져오기 -> webapp/resources에 img 폴더생성 후 해당폴더에 attach.png 파일추가 -> maven project update 
                           -> uploadAjax.jsp에서 일반파일에 경우에는 attach.png이미지가 보이도록 코드 수정, 화면에는 약간의 <style> 로 첨부파일영역처리 추가 ->브라우저상 업로드시 스타일이 적용되는지 확인
       (3)섬네일이미지 보여주기 
          -로직: 서버에서 GET방식이용. 특정한 URI뒤에 파일이름을 추가하면 이미지데이터를 가져와서 <img>태그를 작성하여 처리 
          -UploadController에서 섬네일 데이터 전송하기: UploadController에서 특정한 파일이름을 받아서 이미지데이터를 전송하는 getFIle() 메서드 작성 -> 브라우저에서 upload/2020/04/17 폴더의 이미지파일을 호출해본다 ( 8080/display?fileName=/2020/04/17/flower.jpeg )
          -javascript 처리: GET방식으로 첨부파일의 이름을 사용할 때에는 항상 파일이름에 포함된 공백문자나 한글이름등이 문제가 생길수 있다. 이를 방지하기위해 uploadAjax.jsp에서 encodeURIComponent()를 사용하여 URI호출에 적합한 문자열로 인코딩처리 
 
   13-4)첨부파일의 다운로드 혹은 원본 보여주기 
     1) 첨부파일의 다운로드 : UploadController에서 downloadFile()메서드 작성 -> /Users/angela/upload 에 영문파일을 하나두고 브라우저에서 'download?fileName=파일이름' 형태로 호출 ->브라우저상에서는 아무것도 안뜨지만 ResponseEntity 는 처리됨(콘솔에로그 찍임)
                         -> HttpHeaders 객체를 이용해서 다운로드시 파일의 이름을 처리하도록 uploadController에 donwloadFile() 코드 수정 -> 브라우저에서 'download?fileName=파일이름' 형태로 호출 -> 브라우저에서 파일이 다운로드됨. IE계역에서는 한글파일명일경우 제대로 다운되지 않음 
       (1)IE/Edge 브라우저에서 한글이름파일 다운로드 안되는 에러 해결 : uploadController에서 HttpServletRequest에 포함된 헤더정보들을 이용해서 요청이 발생한 브라우저가 IE계열인지 확인해서 다르게 처리하는 방식으로 코드수정 (User-Agent이용)
                                                       -> IE테스트를 위해 URLEncode website를 웹페이지에서 검색하여 해당싸이트에서 파일명.jpg 의 인코딩결과를 IE에서 'download?fileName=파일이름' 파일이름부분에 붙여넣기 하여 브라우저에서 호충해본다 -> 정상적으로 다운로드 되는지 확인 
       (2)업로드된 후 다운로드 처리(파일이미지 클릭시 다운로드) : uploadAjax.jsp에 javascript부분에서 attach.png를 클릭하면 다운로드에 필요한 결로와 UUID가 붙은 파일이름을 이용해서 다운로드 가능하도록 <a>태그를 이용하여 코드 수정 -> 브라우저에서 <img>태그를 클릭해서 다운로드 되는지 확인 (아직 하단 다운로드창에 파일명은 깨짐)  
                                                      ->UploadController의 downloadFile()에서 파일이름에 UUID가 붙은부분을 제거하고 순수하게 다운로드되는 파일의 이름으로 저장할수 있도록 처리  ->브라우저에서 일반파일 업로드후 <img>를 클릭하여 다시 다운받으면 하단 다운로드창에 실제 파일명으로 표시되어 다운됨을 확인 
     2) 원본 이미지 보여주기 
       (1)원본이미지를 보여줄 <div> 처리 : 섬네일 이미지 클릭시 다운로드 될수 있도록 uploadAjax.jsp의 javascript에서 showImage()를 작성 -> 섬네일 이미지를 보여주도록 처리하는 javascript코드에서는 섬네일 클릭시 showImage()가 호출 되도록 코드 작성 
                                     -> 브라우저에서 서네일을 클릭하면 showIamgea()가 호출되는지 확인 (알림창에 showUploadedFile()의 <a>태그에 추가된 내용 호출됨)
          -CSS와 HTML 처리 : uploadAjax.jsp에 실제 원본이미지 보여주는 영역 HTML 추가 -> uploadAjax.jsp에서 showImage()함수내에 실제로 보여주는 영역추가 -> 브라우저에서 섬네일 클릭시 원본이미지창이 뜨는것을확인                              
          -<div> 이벤트처리 : 원본이미지 다시한번 클릭하여 사라지는 처리. uploadAjax.jsp에서 다시 섬네일 원본이미지 클릭시 원본이미지가 화면중앙으로 점차사라지는 코드 추가 -> 브라우저에서 섬네일을 클릭하여 나온 원본이미지를 클릭하여 화면중앙으로 서서히 이미지가 사라지는지 확인 
     3) 첨부파일 삭제
       (1)일반파일과 이미지파일의 삭제 
          -로직 : 파일의 확장자나 파라미터로 파일의종류를 파악 한뒤 일반파일은 그냥 삭제, 이미지파일인 경우 섬네일까지 삭제 
          -화면에서 삭제 기능 : 첨부파일이 업로드된 후에 생기는 이미지파일 옆에 x표시 추가하도록 uploadAjax.jsp의 showUploadedFile()에서 <span>이용하여 코드수정 -> uploadAjax.jsp에서 <span>을 이용한 x표시에 대한 이벤트처리 코드 추가 
          -서버에서 첨부차일의 삭제 : uploadController에서 전달되는 파라미터의 이름과 종류(이미지파일 or 일반파일)를 구분하여 처리하는 deleteFile()메서드 작성 ->    
       (2)첨부파일의 삭제 고민 
          : 사용자가 강제종료시 파일삭제처리 할지는 이후에 계속. 
    
   13-5) 프로젝트의 첨부파일-등록 :기존의 게시물과 첨부파일작업을 합치기 (기존의 파일 불러오기)
     1)첨부파일 정보를 위한 준비 : 게시물과 첨부파일의 관계를 저장하는 테이블 tbl_attach 설계. SQL에서 tbl_attach 테이블생성 -> org.zerock.domain 에서 BoardAttachV클래스 생성 -> BoardVO 클래스에 첨부파일 리스트형태로 추가 
                             ->org.zerock.mapper패키지에서 BoardAttachMapper 인터페이스 생성 및 insert(), delete(), findBybno() 메서드 작성 -> src/main/resources에 org/zerock/mapper에 BoardAttachMapper.xml 파일 생성 후 BoardAttachMapper에서 정의한 메서드들의 처리 메서드들 작성 
     2)등록을 위한 화면처리
         - 로직 : 첨부파일 자체의 처리는 게시물의 정보와 같이 전송해야함. 게시물등록 버튼을 클릭했을때 현재 서버에 업로드된 파일의 정보를 <input type='hidden'>으로 만들어서 한번에 전송하는 방식 
         - /board/resgister.jsp 파일 아래부분에 게시물의 제목이나 내용을 입력하는 새로운 <div>추가 -> 브라우저의 게시물등록화면 하단에 File attach 부분이 생성된것을 확인                            
       (1)Javascript 처리 : (uploadAjax.jsp의 CSS부분도 register.jsp내에 추가) register.jsp에 등록패이지에서 등록버튼을 눌렀을때 가장먼저 첨부파일 관련된 처리를 할 수 있도록 기본동작을 막는 작업과 별도의 업로드 버튼을 두지않고 내용이 변경되는것을 감지해서 처리 하는 코드를 추가한다. 
                             -> register.jsp에 업로드된 결과를 화면에 섬네일 등을 만들어서 처리하는 부분은 register.jsp에 showUploadResult()메서스작성. 함수내에 이미지파일인경우와 일반파일인경우 보여지는 화면 처리내용도 작성 
                             -> showUploadAjax()는 Ajax호출후에 업로드된 결과를 처리하는 함수로 &.ajax 호출부분에 업로드결과처리함수를 추가한다.              
                             -> 브라우저에서 등록페이지에 첨부파일을 선택했을때 x표시가된 섬네일이 보이는지 확인  
       (2)첨부파일의 변경처리 : uploadAjax.jsp 에서 x버튼 클릭시 선택된 첨부파일 삭제되는 코드 작성 -> //image type 부분의 <button>태그에 'data-file' 과 'data-type' 정보 추가 -> 브라우저에서 등록페이지에서 업로드로 선택된 파일의 x버튼을 눌렀을때 업로드된파일도 같이 삭제 되는지 확인 
       (3)게시물 등록과 첨부파일의 데이터베이스 처리 : (게시물이 등록될때 선택된 첨부파일과 함께 관련된자료를 같이 전송하고 이를 데이터베이스에 등록) register.jsp에서 첨부파일과 관련된 정보(data-uuid,data-filename,data-type)를 태그로 생성하는 부분(//image type)에 을 추가  
                                             -> 게시물등록은 <form>태그를 통해서 이루어지고 이미 업로드된 첨부파일의 정보는 별도의 <input type='hidden'>태그를 생성해서 처리하며 첨부파일관련 정보는 BoardVO에 attachList[]형태로 저장되기 때문에 
                                                register.jsp에 <form>태그를 전송하는 부분에 <input type='hidden'>의 이름도 attachList[인덱스번호]형태로 통일시킨다.  
     3)컨트롤러, 서비스처리 : Boardcontroller에서 먼저 register()메서스에서 로그로 데이터가 제대로 수집되었는지 확인하는 코드작성 -> 브라우저에서 첨부파일 및 게시물을 등록하면서 로그에 file name/type/bno가 잘 찍히는지확인
       (1)BoardServiceImpl 처리 : BoardServiceImple에 Setter메서드로 BoardMapper,BoardAttachMapper주입 
       
   13-6) 게시물조회와 첨부파일
    - 로직 : jsp에서 첨부파일 정보를 AJax를 이용해서 처리하는 방식. 
     1)BoardService와 BoardController 수정 : (Ajax로 처리하기로 했다면 먼저 서버측에서 json데이터를 만들어서 화면에 올바르게 전송하는 작업을먼저 해야함) BoardService인터페이스와 BoardServiceImpl에 게시물의 첨부파일목록을 가져오는 getAttachList()메서드(BoardServiceImpl의 findByBno()호출) 작성 
     2)BoardController의 변경과 화면처리 : (특정한 게시물번호를 이용해서 첨부파일과 관련된 데이터를 JSON으로 반환하도록 처리) BoardController에서 getAttachList()를 작성 -> 
       (1)게시물조회 화면처리 : (가장먼저 해당 게시물의 댓글을 가져오는부분이 자동으로 동작하게 처리) get.jsp에서 자동으로 <script>를추가하고 댓글가져오는부분 추가 -> 브라우저에서 첨부파일이 추가된 게시물을 선택하면 개발자도구에서 해당게시물의 첨부파일목록이 불러들여 지는지 확인 
                           -> (첨부파일 데이터를 보여줄수 있도록 <div>생성) get.jsp에서 기존의 게시물이 보여지는 <div class='row'>아래쪽에 하나더 추가하여 첨부파일 목록 보여주는 부분 bigPictureWrapper 코드 작성  
       (2)첨부파일 보여주기 : (JSON으로 가져온 데이터는 작성된 <div>안에서 보이도록처) get.jsp에서 첨부파일이 화면에 보여주는 부분 작성
       (3)첨부파일 클릭시 이벤트 처리 : (첨부파일 목록이 보인다면 이미지일경우 원본이미지가 일반파일일경우 다운로드처리가 필요) get.jsp에서 uploadResult, showImage 작성   
       (4)원본 이미지 창 닫기 : (<div>를 감추는 형태로 원본이미지가 보이는 창 닫는작업) get.jsp에서 원본이미지창 닫기 코드 추가 
   
   13-7) 게시물의 삭제와 첨부파일
     1)첨부파일 삭제처리 : BoardAttachMapper와 BoardAttachMapper.xml에서 deleteAll()메서드 추가 
       (1)BoardServiceImpl에서 delete()수정 
       (2)BoardController의 파일삭제
          - 로직 : 해당게시물의 첨부파일 정보 미리준비 -> 데이터베이스상에서 해당게시물과 첨부파일 데이터삭제 -> 첨부파일목록을 이용하여 해당 폴더에서 섬네일이미지와 일반파일을 삭제 
          - Criteria 수정(첨부파일정보 준비) :Criteria클래스에서 게시물삭제후 페이지번호나 검색조건을 유지하면서 이동하기 위하여 getListLink() 메서드 수정 
          - 파일삭제처리 : BoardController에서 첨부파일 삭제처리의 deleteFiles()추가 -> BoardController에서 게시물삭제처리의 remove()메서드 수정   
   13-8) 게시물의 수정과 첨부파일 
     - 로직 : 수정이라는 개념보다는 삭제 후 다시 추가한다는 개념으로 접근. 기존의 게시물 테이블을 수정하는 작업과 변경(새롭게추가된)된 첨부파일을 등록하는 작업으로 이루어짐
     1) 화면에서 첨부파일 수정 : 조회와 비슷하지만 원본이미지 확대 다운로드기능이 필요하지 않다는점, 삭제버튼이 있어야 한다는점이 다름. 
       (1) 첨부파일 데이터 보여주기 : (페이지가 로딩되면 첨부파일을 가져오는 작업먼저 처리) modify.jsp에서 첨부파일을 보여주는 부분의 <div>를 추가하고, get.jsp에서 사용한 <style>태그내용 그대로 가져옴 (보여줄공간) 
                                -> jQuery의 &(document).ready()를 이용해서 첨부파일을 보여주는 작업 처리 -> 브라우저에서 게시물의 조회화면에서 수정/삭제화면으로 이동하여 첨부된 파일아이콘이 보이는지 확인 
                                -> (첨부파일을 수정하기위해서는 게시물을 등록할때 사용했던 버튼과 파일을 교체하기위한 <input type="file">이 필요하다는것 기억)
                                -> modify.jsp에서 Ajax로 첨부파일의 데이터를 가져온 부분에 교체하는 파일을 첨부하는 <input>태그와 첨부파일의 이름과 삭제x버튼이 보이게 코드 추가 
                                -> 브라우저에서 게시물조회 페이지의 modify버튼을 누르면 화면하단에 수정할 첨부파일 아이콘과 이름(<input>태그), 삭제버튼 x 이 보이는지 확인 
       (2) 첨부파일 삭제 이벤트 
           - 로직 : 삭제처리에서 주의해야할점은 사용자가 이미 있던 첨부파일 중에 일부를 삭제한 상태에서 게시물수정 버튼을 누르지않고 빠져나가는상황. 
                   이것을 방지하기 위하여 사용자가 특정 첨부파일을 삭제했을때 화면에서만 삭제하고, 최종적으로 게시물을 수정했을때 이를 반영하는 방식
           - 먼저 modify.jsp에서 첨부파일의 'x'버튼을 클릭하면 사용자의 확인을 거쳐 화면상에 사라지도록 처리 -> 삭제하는파일에 대한 정보보관
             (정보는 <li>태그내 저장되어 있으므로, 첨부파일추가 코드 작성후, 이를이용해 <input type ="hidden"> 태그 생성해) -> 실제 삭제처리는 수정버튼을 누르고 처릭되는 과정에서 구현 
       (3) 첨부파일 추가 : 기존의 게시물등록시 처리와 동일. modify.jsp에서 서버에 파일을 업로드하고, 이를 화면에 섬네일이나 파일의 아이콘으로 보이게 처리 
       (4) 게시물 수정 이벤트 처리 : (게시물을 수정할 때는 모든 첨부파일 정보를 같이 전송) modify.jsp에서 수정버튼을 눌렀을때, 모든정보가 처리되도록 수정버튼의 이벤트처리 수정              
     2) 서버 측 게시물 수정과 첨부파일 
       - 로직 : 게시물의 모든 첨부파일목록을 삭제 하고, 다시 첨부파일목록을 추가하는 형태. 이 경우 데이터베이스상에는 문제가 없는데 업로드폴더에는 삭제된 파일이 남아있는문제 발생. 
               이에 대한 처리는 파일과 데이터베이스를 주기적으로 비교하는등의 방법을 활용해서 처리      
       (1) BoardService(Impl)의 수정 : (기존의 첨부파일 관련된 데이터를 삭제한 후 첨부파일 데이터를 추가하는 방식) BoardServiceImpl에서 modify()메서드 수정 -> 브라우저상와 데이터베이스상 제대로 처리되는지 확인
       
   13-9) 잘못 업로드된 파일 삭제 
     - Ajax처리시 사용자가 비정상적인종료를 하거나 페이지를 빠져나가는 경우 다음과같은 문제발생. 첨부파일만 서버에 업로드 되고 게시물은 등록되지 않은경우, 게시물을 수정할때 파일을 삭제했지만(데이터베이스에서) 실제로 폴더에서 삭제되지 않은경우
       이를해결하기 위해, 정상적으로 사용자의 게시물에 첨부된파일인지 아니면 사용자가 게시물을 수정할때 업로드했지만 최종적으로 사용되는 파일인지아닌지 파악하는것이 중요.
     1)잚못 업로드된 파일의 정리 
       - 로직 : (데이터베이스와 비교하는 작업을 통해 업로드만된 파일의목록을 찾기) 어제날짜로 등록된 첨부파일목록을 구한다 -> 어제 업로드가 되었지만 데이터베이스에는 존재하지 않는 파일들을 찾는다 
               -> 데이터베이스와 비교해서 필요없는파일들을 삭제
     2) Quartz 라이브러리 설정 : 주기적으로 특정한 프로그램을 실행할때 사용. pom.xml에 quartz, quartz-jobs 라이브러리 추가 -> root-context.xml 네임스페이스에 task항목 체크 
                             -> 소스코드에는 <task:annotation-driven> 추가   
       (1)자바설정을 이용할경우 -> jex05 
       (2)Task작업의 처리 : org.zerock.task패키지, FileCheckTask클래스생성 하여 corn을 이용해 주기부여 메서드 checkFiles() 작성 -> root-context.xml에 FileCheckTask를 스프링의빈으로 설정 -> @Scheduled(corn)으로 설정한 주기대로 로그찍히는지 확인 
       (3)BoardAttachMapper 수정 : (파일목록 가져오기) BoardAttachMapper에서 어제 등록된 파일목록 가져오는 getOldFiles()선언 -> BoardAttaachMapper.xml 구현부작성 
       (4)파일의 목럭처리 
          - 로직 : 데이터베이스에서 어제 사용된 파일목록을 가져옴 -> 해당폴더의 파읾목록에서 데이터베이스에 없는 파일 찾이냄 -> 데이터베이스에 없는 파일들을 삭제
          - FileCheckTask클래스에서 업로드폴더에 어제 등록된 첨부파일목록을 가져오는 getFolderYesterday()메서드를 추가 
            -> checkFIles()에서 데이터베이스에 어제 등록된 첨부파일목록을 준비하고 업로드폴더 담겨있는 파일중 데이터베이스에 없는 파일들을 찾아 삭제하는 코드 추가 -> 로드확인
       
            
            
14. Spring Web Security를 이용한 로그인처리 -> ex06
            
       




 */