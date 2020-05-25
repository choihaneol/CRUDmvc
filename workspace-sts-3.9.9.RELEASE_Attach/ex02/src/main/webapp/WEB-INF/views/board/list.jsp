<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
	
	<%@include file="../includes/header.jsp" %> 
	<!-- header.jsp로 보낸후 추가 -->
	
	
            <div class="row">
                <div class="col-lg-12"> <!-- 큰 기기 데스크탑 열 12개  -->
                    <h1 class="page-header">Tables</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>  
            <!-- /.row -->   <!-- 여기까지는 list.jsp에 남겨놓기  -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">  <!-- 패널은 콘텐츠가 있는 박스 형태의 구성요소  -->
                        <div class="panel-heading">Board List Page  <!-- 콘텐츠 영역과 분리된 제목 영역 -->
                        <!-- 등록버튼  -->
                        <button id='regBtn' type="button" class="btn btn-xs pull-right">Register New Board</button> <!-- 등록 버튼 -->
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                  
                  
               <!-- #번호, 제목, 작성자, 작성일, 수정일 출력코드 (시작) -->
                            <table class="table table-striped table-bordered table-hover"> <!-- 마우스 닿는 행 배경색 변함  -->
                                <thead>
                                    <tr>
                                        <th>#번호</th>
                                        <th>제목</th>
                                        <th>작성자</th>
                                        <th>작성일</th>
                                        <th>수정일</th>
                                    </tr>
                                </thead>
                                
                       <c:forEach items="${list}" var="board"> <!-- board list안에 있는 값 모두 출력  -->
                       <tr>
							<td><c:out value="${board.bno}" /></td>
							<!-- <td><a href='/board/get?bno=<c:out value="${board.bno}" />'>  --> <!-- 목록에서 조회페이지로 이동(목록페이지에서 각 제목에 링크 설정) -->
							<td><a class='move' href='<c:out value="${board.bno}"/>'>  <!-- <a> 태그에 이동하려는 게시물의번호만을 가지게 수정 -->
							<c:out value="${board.title}" /></a></td>
							<td><c:out value="${board.writer}" /></td>
							<td><fmt:formatDate pattern="yyyy-MM-dd"
									value="${board.regdate}" /></td>
							<td><fmt:formatDate pattern="yyyy-MM-dd"
									value="${board.updateDate}" /></td>
						</tr>
					</c:forEach>
				</table>
				<!-- #번호, 제목, 작성자, 작성일, 수정일 출력코드 (끝) -->
				 
                            
				<!--검색조건과 키워드 보여지는 부분 처리-->
				<div class='row'>
					<div class="col-lg-12">

						<form id='searchForm' action="/board/list" method='get'>
							<select name='type'>
								<option value=""
									<c:out value="${pageMaker.cri.type == null?'selected':''}"/>>--</option>
								<option value="T"
									<c:out value="${pageMaker.cri.type eq 'T'?'selected':''}"/>>제목</option>
								<option value="C"
									<c:out value="${pageMaker.cri.type eq 'C'?'selected':''}"/>>내용</option>
								<option value="W"
									<c:out value="${pageMaker.cri.type eq 'W'?'selected':''}"/>>작성자</option>
								<option value="TC"
									<c:out value="${pageMaker.cri.type eq 'TC'?'selected':''}"/>>제목
									or 내용</option>
								<option value="TW"
									<c:out value="${pageMaker.cri.type eq 'TW'?'selected':''}"/>>제목
									or 작성자</option>
								<option value="TWC"
									<c:out value="${pageMaker.cri.type eq 'TWC'?'selected':''}"/>>제목
									or 내용 or 작성자</option>
							</select> <input type='text' name='keyword'
								value='<c:out value="${pageMaker.cri.keyword}"/>' /> <input
								type='hidden' name='pageNum'
								value='<c:out value="${pageMaker.cri.pageNum}"/>' /> <input
								type='hidden' name='amount'
								value='<c:out value="${pageMaker.cri.amount}"/>' />
							<button class='btn btn-default'>Search</button>
						</form>
					</div>
				</div>
				
				<!-- 페이지처리 버튼 생성 -->
                           <div class='pull-right'>
                           <ul class="pagination">
                           
                           <c:if test="${pageMaker.prev}">
                           <li class="paginate_button previous"><a href="${pageMaker.startPage -1}">Previous</a>  <!-- <a> 태그의 href속성값이 단순히 페이지번호만 가지게 함  -->
                           </li>
                           </c:if>
                           
                           <c:forEach var="num" begin="${pageMaker.startPage}" end="${pageMaker.endPage}" >
                           <li class="paginate_button ${pageMaker.cri.pageNum == num ? "active":""} "><a href="${num}">${num}</a> <!-- <a> 태그의 href속성값이 단순히 페이지번호만 가지게 함  -->
                           </li>
                           </c:forEach>
                           
                           <c:if test="${pageMaker.next}">
                           <li class="paginate_button next"><a href="${pageMaker.endPage +1}">Next</a> <!-- <a> 태그의 href속성값이 단순히 페이지번호만 가지게 함  -->
                           </li>
                           </c:if>
                           </ul>
                           </div>
				<!-- end pagination -->
                        </div>
                        
                <!-- 페이지번호를 클릭해서 이동할때도 페이지번호와 한페이지당 데이터개수와 함께 검색조건과 키워드도 같이 전달 -->
                <form id='actionForm' action="/board/list" method='get'>
				<input type='hidden' name='pageNum' value='${pageMaker.cri.pageNum}'>
				<input type='hidden' name='amount' value='${pageMaker.cri.amount}'>

				<input type='hidden' name='type'
					value='<c:out value="${ pageMaker.cri.type }"/>'> <input
					type='hidden' name='keyword'
					value='<c:out value="${ pageMaker.cri.keyword }"/>'>
				</form>
                        
                        
                <!-- 실제페이지를 클릭하면 동작하는 부분 <form> 태그 이용하여 처리 -->           
                <form id='actionForm' action="/board/list" method='get'> 
				<input type='hidden' name='pageNum' value='${pageMaker.cri.pageNum}'>
				<input type='hidden' name='amount' value='${pageMaker.cri.amount}'>
                </form>            
                                            
                            
             <!-- Modal  추가 -->
			<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
				aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal"
								aria-hidden="true">&times;</button>
							<h4 class="modal-title" id="myModalLabel">Modal title</h4>
						</div>
						<div class="modal-body">처리가 완료되었습니다.</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default"
								data-dismiss="modal">Close</button>
							<button type="button" class="btn btn-primary">Save
								changes</button>
						</div>
					</div>
					<!-- /.modal-content -->
				</div>
				<!-- /.modal-dialog -->
			</div>
			<!-- /.modal -->
                            
             
                        </div>
                    <!-- end panel-body -->
                </div>
                <!-- end panel -->
            </div>
         </div>
            <!-- /.row -->
            
            
           

<script type="text/javascript">
	$(document).ready(function() {   //모든 html 페이지가 화면에 뿌려지고 나서 ready안에 서술된 이벤트들이 동작준비
		
		//<script> 태그를 이용한 상황에 따른 메세지 확인 코드
		//var result = '<c:out value="${result}"/>';	
		/*
		/* 모달창 보여주는 작업 */
		//checkModal(result);  //새로운 게시글 작성되는 경우 RedirectAttributes로 게시물의 번호 전송 
		
		/* 뒤로가기 방지 history */
		//history.replaceState({}, null, null); //뒤로가기 방지 history객체로 표시 
		
		/* 수정/등록/삭제 시 모달창코드*/
		//function checkModal(result) {

		//			if (result === ''|| history.state) { //뒤로가기 방지 history . 등록된거 없으면 리스트로 되돌아감  
		//				return;
		//			}

		//			if (parseInt(result) > 0) {  //parseInt:문자열->정수 변환 
		//				$(".modal-body").html("게시글 " + parseInt(result) + " 번이 등록되었습니다."); //모달메세. 등록된게 있으면 모달에 내용전달 
		//			}

		//			$("#myModal").modal("show"); //모달 보여줌
		//		}
		
		
		/* 등록버튼 클릭시 동작 정의 */
		//$("#regBtn").on("click", function(){  
		//	self.location = "/board/register";
		//});
	 
		/*var formObj = $("form");
		
		$('button').on("click", function(e){ //button 클릭하면 다음 동작 
			
			e.preventDefault();  //기본동작을 막고 마지막에 직접 submit함 
			
			var operation = $(this).data("oper");  //버튼 태그의 'data-oper' 속성을 이용해서 세부 기능을 동작하도록 처리 
			
			console.log(operation);
			
			if(operation == 'remove'){
				formObj.attr("action", "/board/remove");  // "/board/remove" 으로  
			}else if (operation == 'list'){
				//move to list
				formObj.attr("action", "board/list").attr("method", "get"); // "/board/list" 으로 get방식으로 바꿔서
				formObj.empty();
				//self.location="/board/list";
				return;
			}
			formObj.submit();  //전송 
			}); 
}); */


var result = '<c:out value="${result}"/>';

checkModal(result);

history.replaceState({}, null, null);

function checkModal(result) {

	if (result === '' || history.state) {
		return;
	}

	if (parseInt(result) > 0) {
		$(".modal-body").html(
				"게시글 " + parseInt(result)
						+ " 번이 등록되었습니다.");
	}

	$("#myModal").modal("show");
}

$("#regBtn").on("click", function() {

	self.location = "/board/register";

});


//페이지번호를 클릭하면 처리하는 부분
var actionForm = $("#actionForm");

$(".paginate_button a").on(
		"click", function(e) {

			e.preventDefault(); //<a> 태그를 클릭해도 페이지 이동이 없도록 처리 

			console.log('click');

			actionForm.find("input[name='pageNum']")
					.val($(this).attr("href"));  //pageNum값을 href으로 변경 
			actionForm.submit(); 
		});
		
$(".move").on("click", function(e) { //게시물의 제목을 클릭했을때 bno값을 전달하면서 화면도 이동할수 있도록 javascript에서 클릭 이벤트처리
//<form> 태그에 추가로 bno값을 전송하기 위해서 <input>태그를 만들어 추가 
//<form> 태그의 action은 /board/get으로 변경 

			e.preventDefault(); 
			actionForm.append("<input type='hidden' name='bno' value='"+ 
					
$(this).attr("href")+ "'>");
			actionForm.attr("action", "/board/get");
			actionForm.submit();
		});
		
//검색버튼 이벤트처리
var searchForm = $("#searchForm");

$("#searchForm button").on(
		"click", function(e) {
//검색버튼을 클릭하면 <form>태그의 전송은 막고, 페이지번호는 1이 되도록처리.
//화면에 키워드가 없다면 검색을 하지 않도록 제어 

			if (!searchForm.find("option:selected")
					.val()) {
				alert("검색종류를 선택하세요");
				return false;
			}

			if (!searchForm.find(
					"input[name='keyword']").val()) {
				alert("키워드를 입력하세요");
				return false;
			}

			searchForm.find("input[name='pageNum']")
					.val("1");
			e.preventDefault();

			searchForm.submit();

		});
		
});
		
</script>
           
           
           
           
           
           
           
           
           
           
           
<%@include file="../includes/footer.jsp" %>
            
            







<!-- 화면처리 
- 화면처리를 위한 환경설정 
1)servlet-context.xml에 Mvc의 jsp처리 설정 되어있는지 확인
2)설정된경로 WEB-INF/views 에 board/list.jsp 생성후 jsp코드 삽입 
3)Web Modules 절대경로 설정 
4)list.jsp 파일에 스프링부트Admin2>page/tables.html 코드를 복사 후, jsp파일의 Page지시자만 남겨놓고 붙여넣기 > 화면에 깨져서 나오는것을 확인할수있음   
5)스프링부트Admin2 내 모든폴더를 현재프로젝트 내 src/main/webapp/resources 폴더에 모두 복사 
6)list.jsp 파일에서 CSS나 JS파일의 경로를 /resources/ 에서 /resources/ 으로 수정  
7)http://localhost:8080/board/list 확인시 Bootstrap Admin Theme 나옴 
8)views 폴더안에 includes폴더 및 footer.jsp header.jsp파일 생성  (많은양의 HTML코드 방지)
9)list.jsp에서 <div id='page-wrapper'> 라인까지 잘라낸다음, header.jsp에 붙여넣기 후 그자리에 해당코드 추가 (지시자는 남기기) 
  list.jsp에서<div id='page-wrapper'> 태그가 끝나는라인 을 제외한 나머지 코드 <--!jQuery-> 까지 잘라낸다음, footer.jsp에 붙여넣기 후 해당 코드 추가   
10) JSP페이지를 javascript로 브라우저 내에서 조작할수 있도록 하기 위해 jquery.min.js 파일 <scrip>태그 제거 후 header.jsp내에 
    <script src = "https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
11)반응형웹처리를 위해 footer.jsp에 기관련 코드 추가 
12)list.jsp 에는 부트스트랩 테마 코드들이 있으니 최소한의 코드로 수정 

- 등록/모달창,목록이동/조회 
13)board/list를 실행했을때 BoardController는 Model을 이용해서 게시물의 목록을 'list'라는 이름으로 담아서 전달 했으므로 list.jsp에서는 이를 출력한다. 
   출력은 JSTL을 이용해서 처리(list.jsp) 코드 수정  
14)게시물 등록작업은 POST방식으로 처리하지만, 화면에서 입력을 받아야만 하므로 GET방식으로 입력 펭지를 볼 수 있도록 BoardController에 메서드 추가. 
15) views/board/register.jsp 파일을 생성한뒤 includes를 적용한 입력페이지를 작성.   
16)브라우저상 한글깨짐현상은 1)브라우저에서 한글이 깨져서 전송되는지(개발자툴) 2)Controller나 데이터베이스 문제인지 확인후, 후자일경우 스프링 MVC쪽에 필터등록(web.xml) 
17)등록,수정,삭제 작업은 처리가완료된 후 페이지 목록으로 이동해야 중복작업을 방지할 수 있다. 
  모달창추가 : list.jsp 내에 <table>태그 아래쪽에 모달창 <div> 추가. 모달창을 보여주기 위해서 jQuery를 이용해서 처리. (addFlashAttribute의 resultf를 checkModal()에 전달하여 처리)
18)등록버튼 : list.jsp내에 HTML구조를 수정(등button 코드 추가) > list.jsp 하단에 jQuery를 이용하는 부분에서 해당버튼을 클릭했을때의 동작을 정의한다. 
19)조회페이지 작성 : views/board/get.jsp 생성 > register.jsp 코드 복사 > 게시물벚호를 보여줄 수 있는 필드 추가 및 readonly지정 >  
    <form>태그 삭제 및 수정/삭제 페이지로 이동하거나 원래의 페이지로 이동 할수 있는 버튼 및 이동링크 추가
20)제목링크연결 : list.jsp에서 목록페이지에서 각 제목에 링크 설정 
   뒤로가기 : window의 history객체를 이용해서 현재페이지는 모달창을 띄울 필요가 없다고 표시를 해두는 방식으로 처리(list.jsp에서 javascript)  
21)수정 및 삭제 : 조회페이지에서는 GET방식으로 처리되는 URL을 통해서 수정/삭제버튼이 존재하는 화면이 보여야 하고, 
   수정/삭제는 POST방식으로 처리되며 결과는 다시 화면으로 보여야 한다. 
   controller에서 get방식 수정(하나의 메서드에서 modify URL추가) > views/board/modify.jsp 파일 생성 > 제목이나 내용등이 readonly속성이 없도록 수정 
   > POST방식으로 처리하는 부분을 위해서 <form> 태그로 내용들을 감쌈 > 버튼코드 수정 
   > list.jsp의 javascript에서 위의 버튼에 따라서 다른동작 하는 기능 수정
22)<form> 처리 : get.jsp 에서 <form>태그를 이용해서 코드 수정 
   > 사용자가 버튼을 클릭하면 operForm이라는 id를 가진 <form> 태그를 전송해야 하므로 get.jsp 에서 javascript 처리 

-페이징 처리 
0) SQL에서 테이블 데이터 생성 > 정렬 > ROWNUM > inline view 
1) domain 패키지에 Criteria 클래스 생성 한 뒤 파라미터 받는코드 입력 > BoardMappper클래스에 Criteria 타입을 파라미터로 사용하는 getListWithPaging() 메서드 작성
   > BoardMapper.xml에 getListWithPaging에 해당하는 태그 추가 > XML설정이 제대로 동작하는지 BoardMapperTests를 만들어 테스트
   > BoardService는 Criteria를 파라미터로 처리하도록 BoardService, BoardServiceImpl, BoardServiceTests 클래스 파라미터 수정  
   > BoardContollerTests에서 pageNum과 amount를 파라미터로 처리하도록 > BoardController, BoardControllerTests도 Criteria를 파라미터로 처리
  
-페이징 화면 처리 
1)페이징처리를 위한 클래스 설계 : Domain>PageDTO 클래스생성 > 시작/끝/이전/이후 페이지 코드 작성 >BoardController클래스에서 PageDTO를 사용할수 있도록 Model에 담아서 화면에 전달 하는 코드 추가
2)JSP에서 페이지 번호 출력 : SB admin2의 pages폴더에 있는 table.html 페이지의 페이지처리 코드를 이용해서 list.jsp파일에 <table> 태그가 끝나는 직후에 페이지처리 코드를 추가 
3)페이지번호 이벤트 처리 : list.jsp에서 <a>태그의 href 속성값으로 페이지번호를 가지도록 수정하여 <a>태그가 href 속성값으로 단순히 페이지번호만 가지게 변경 
  > 실제페이지를 클릭하면 동작하는 부분은 별도의 <form> 태그를 이용하여 처리  > javascript에 페이지번호를 클릭하면 처리하는 부분 코드 추가 
4)게시글확인후 1페이지로 돌아가는 에러 수정 :
  4-1)게시물의 제목을 클릭했을때 pageNum과 amount 파라미터가 추가로 전달되도록 처리
      list.jsp에서 <a> 태그에 이동하려는 게시물의번호(bno)만을 가지게 수정 >  javascript에서 게시물의 제목을 클릭했을때 이동하도록 이벤트 처리 
      (<form> 태그에 추가로 bno값을 전송하기 위해서 <input>태그를 만들어 추가 > <form> 태그의 action은 /board/get으로 변경 )
  4-2)조회페이지에서 다시 목록페이지로 이동 할때 페이지번호 유지 
      버튼을 클릭하면 <form> 태그를 이용하는방식에 맞춰 get.jsp에서 필요한 데이터를 추가해서 이동 하도록 처리 > 
5)수정, 삭제 처리 : modify.jsp 에서 <form>태그 내에서 pageNum과 amount 라는 값 전송할수 있도록 수정 > 
  POST방식으로 진행하는 수정과 삭제처리 이므로 BoardController에서 modify,remove 메서드에 페이지관련 파라미터들(Criteria)을 처리할수 있도록 수정 > 
6)수정, 삭제를 취소하고 다시 목록 페이지로 이동 : modify.jsp의 javascript에서 목록 페이지는 오직 pageNum과 amount만을 사용하므로 <form> 태그의 다른내용들은 삭제하고 필요한 내용만 다시 추가 
7)데이터베이스에 있는 전체데이터의 개수 처리 : BoardMapper 인터페이스에서 파라미터를 받는 getTotalCount(Criteria cri) 메서드 추가
  > BoardMapper.xml 에도 getTotalCount 구현체추가 > BoardService, BoardServiceImpl에서 별도의 메서드(getTotal(Criteria cri)를 작성해서 BoardMapper의 getTotalCount()호출 처리 
  > BoardController 에서 BoardService 인터페이스를 통해서 getTotal()을 호출하도록 변경 

-검색 처리
1)검색기능과 SQL : 오라클은 페이징처리에 인라인뷰를 이용하기 때문에 실제 검색조건에 대한 처리는 인라인뷰 내부에서 처리. 
 1-1)다중항목 검색 : AND연산자가 OR연산자보다 우선순위가 높으므로 우선순위 연산자 ()로 OR조건처리
 1-2)Mtbatis의 동적 SQL : SQL을 파라미터들의 조건에 맞게 조정하는 기능 
2)검색조건처리에 따라서 Criteria를 확장할 필요가 있으므로 Criteria클래스에서 type,keyword변수,getTypeArr() 메서드 추가 
  -> BoardMapper.xml 에서 getListWithPaging()을 수정하여 동적 SQL처리 > BoardMapperTests로 테스트 
  2-1)<sql><includue>로 검색개수 맞추기 : <sql>태그로 SQL일부를 별도 보관후 필요한경우 inlcude시키는 형태로 사용. 즉,BoardMapper.xml에서 BoardMapper.xml 목록과 데이터 개수 관련된코드 수정 
3)화면에서 검색 조건 처리 : 목록화면(list.jsp)에서 검색조건과 키워드가 들어갈수 있게 HTML수정 > 브라우저 검색기능 한/영 모두 테스트 
4)3가지 에러처리 
  4-1)검색버튼 이벤트 처리: list.jsp에서 검색버튼을 클릭하면 검색은 1페이지를 하도록 수정하고, 화면에 검색 조건과 키워드가 보이게 처리하는작업을 우선 진행
      > 페이지번호를 클릭해서 이동할때에도 검색조건과 키워드는 같이 전달되어야 하므로 페이지이동에 사용한 <form>태그를 수정 
  4-2)조회페이지에서 검색처리 : 조회페이지에 Criteria의 type과 keyword에 대한 처리 > 
  4-3)수정/삭제 페이지에서 처리 : modify.jsp에서 기존<form>방식의 type과 keyword만 추가하는 방식으로 처리 
      > 수정/삭제 처리는 BoardController에서 redirect 방식으로 동작하므로 BoardController에서 type과 keyword조건을 같이 리다이렉트 시에 포함시킴 (GET방식)
      > modify.jsp에서 다시 목록으로 이동하는경우에 필요한 파라미터만 전송하기 위해서 <form>태그의 모든내용을 지우고 다시 추가하는 방식으로 이용했으므로 
        javascript에서 keyword와 type 역시 추가하도록 아래와 같이 관련된 코드 수정 
  4-4)쉽게 파라미터 유지:UriComponentsBuilder(javascript 사용할수 없는상황에서 유용) 클래스이용. 
      Criteria 클래스에 링크를 생성하는 기능 추가 > BoardController에 modify(), remove()메서드 코드 정리  
      
  
-->



