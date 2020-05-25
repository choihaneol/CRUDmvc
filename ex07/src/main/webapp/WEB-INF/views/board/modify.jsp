<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %> <!-- 스프링 시큐리티 태그 라이브러리 -->

<%@include file="../includes/header.jsp"%>

<!-- register.jsp에서는 <form>태그를 이용해서 필요한 데이터를 전송
<input>이나 <textarea>태그의 name속성은 BoardVO 클래스의 변수와 일치시켜 줘야함.  -->

<div class="row">
  <div class="col-lg-12">
    <h1 class="page-header">Board Read</h1>
  </div>
  <!-- /.col-lg-12 -->
</div>
<!-- /.row -->

<div class="row">
  <div class="col-lg-12">
    <div class="panel panel-default">

      <div class="panel-heading">Board Modify</div>
      <!-- /.panel-heading -->
      <div class="panel-body">
      
      
      
<form role="form" action="/board/modify" method="post">    
 <!-- CSRF 토큰  -->
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>  
<!-- <form>태그 내에서 pageNum과 amount 라는 값 전송  -->
<input type='hidden' name='pageNum' value='<c:out value="${cri.pageNum }"/>'>
<input type='hidden' name='amount' value='<c:out value="${cri.amount }"/>'> 
<!-- <form>태그에 type과 keyword 추가 -->
<input type='hidden' name='type' value='<c:out value="${cri.type }"/>'>
<input type='hidden' name='keyword' value='<c:out value="${cri.keyword }"/>'> 
 

      
          <!-- 게시물벚호를 보여줄 수 있는 필드 추가 및 readonly지정  -->
          <div class="form-group">
          <label>Bno</label> 
          <input class="form-control" name='bno' 
          value='<c:out value="${board.bno }"/>' readonly="readonly">
          </div>
          
          
          <div class="form-group">
          <label>Title</label> 
          <input class="form-control" name='title' 
          value='<c:out value="${board.title }"/>' >
         </div>
         
         <div class="form-group">
         <label>Text area</label>
         <textarea class="form-control" rows="3" name='content' ><c:out value="${board.content}"/></textarea>
         </div>
         
         <div class="form-group">
         <label>Writer</label> 
         <input class="form-control" name='writer'
         value='<c:out value="${board.writer}"/>' readonly="readonly">            
         </div>
         
         
         <div class="form-group"> <!-- 등록일/수정일은 boardVO로 수집되야 하므로 yyyy/MM/dd 형태 -->
         <label>RegDate</label> 
         <input class="form-control" name='regDate'
         value='<fmt:formatDate pattern = "yyyy/MM/dd" value = "${board.regdate}" />'  readonly="readonly">           
         </div>
          
    
         <div class="form-group">
         <label>Update Date</label> 
         <input class="form-control" name='updateDate'
         value='<fmt:formatDate pattern = "yyyy/MM/dd" value = "${board.updateDate}" />'  readonly="readonly">            
         </div>
           
                 
          <!-- 수정/삭제/목록 버튼  -->
          <!-- <button type="submit" data-oper='modify' class="btn btn-default">Modify</button>  -->
          <!--<button type="submit" data-oper='remove' class="btn btn-danger">Remove</button>  -->
          
          <!-- 현재 로그인한 사용자가 게시물의 작성자인 경우에만 수정과 삭제가 가능 -->
          <sec:authentication property="principal" var="pinfo"/>

          <sec:authorize access="isAuthenticated()">

          <c:if test="${pinfo.username eq board.writer}">

          <button type="submit" data-oper='modify' class="btn btn-default">Modify</button>
          <button type="submit" data-oper='remove' class="btn btn-danger">Remove</button>
          </c:if>
          </sec:authorize>
  
          <button type="submit" data-oper='list' class="btn btn-info">List</button>
        </form>
      </div>
      <!--  end panel-body -->

    </div>
    <!--  end panel-body -->
  </div>
  <!-- end panel -->
</div>
<!-- /.row -->



<!-- 첨부파일 보여지는 <div> -->
<div class='bigPictureWrapper'>
  <div class='bigPicture'>
  </div>
</div>

<!-- <style> get.jsp와 동일 -->

<style>
.uploadResult {
  width:100%;
  background-color: gray;
}
.uploadResult ul{
  display:flex;
  flex-flow: row;
  justify-content: center;
  align-items: center;
}
.uploadResult ul li {
  list-style: none;
  padding: 10px;
  align-content: center;
  text-align: center;
}
.uploadResult ul li img{
  width: 100px;
}
.uploadResult ul li span {
  color:white;
}
.bigPictureWrapper {
  position: absolute;
  display: none;
  justify-content: center;
  align-items: center;
  top:0%;
  width:100%;
  height:100%;
  background-color: gray; 
  z-index: 100;
  background:rgba(255,255,255,0.5);
}
.bigPicture {
  position: relative;
  display:flex;
  justify-content: center;
  align-items: center;
}

.bigPicture img {
  width:600px;
</style>


<!-- 첨부파일 보여지는 <div> -->
<div class="row">
  <div class="col-lg-12">
    <div class="panel panel-default">

      <div class="panel-heading">Files</div>
      <!-- /.panel-heading -->
      <div class="panel-body">
        <div class="form-group uploadDiv">
            <input type="file" name='uploadFile' multiple="multiple"> <!-- 게시물을 등록할때 사용했던 버튼과 파일을 교체하기위한 <input type="file"> 필요 -->
        </div>
        
        <div class='uploadResult'> 
          <ul>
          
          </ul>
        </div>
      </div>
      <!--  end panel-body -->
  
    </div>
    <!--  end panel-body -->
  </div>
  <!-- end panel -->
</div>
<!-- /.row -->





<script type="text/javascript">
$(document).ready(function() { 

	  var formObj = $("form");

	  $('button').on("click", function(e){
	    
	    e.preventDefault(); //<a> 태그를 클릭해도 페이지 이동이 없도록 처리 
	    
	    var operation = $(this).data("oper"); 
	    
	    console.log(operation);
	    
	    if(operation === 'remove'){
	      formObj.attr("action", "/board/remove");
	      
	    }else if(operation === 'list'){
	      //move to list
	      formObj.attr("action", "/board/list").attr("method","get");
	      
	      var pageNumTag = $("input[name='pageNum']").clone(); //<form>태그에서 필요한 부분만 잠시 복사(clone)해서 보관 
	      var amountTag = $("input[name='amount']").clone();
	      var keywordTag = $("input[name='keyword']").clone();  //type과 keyword 추가 
	      var typeTag = $("input[name='type']").clone();
	      
	      formObj.empty(); //<form>태그 내 모든내용 지움 
	      
	      formObj.append(pageNumTag); //이후에 다시 필요한 태그들만 추가해서 '/board/list'호출  
	      formObj.append(amountTag);    
	      formObj.append(keywordTag); //type과 keyword 추가   
	      formObj.append(typeTag);    
	    }else if(operation === 'modify'){ //수정버튼을 눌렀을때, 모든정보가 처리되도록 수정버튼의 이벤트처리 수정  
	        
	        console.log("submit clicked");
	        
	        var str = "";
	        
	        $(".uploadResult ul li").each(function(i, obj){
	          
	          var jobj = $(obj);
	          
	          console.dir(jobj);
	          
	          str += "<input type='hidden' name='attachList["+i+"].fileName' value='"+jobj.data("filename")+"'>";
	          str += "<input type='hidden' name='attachList["+i+"].uuid' value='"+jobj.data("uuid")+"'>";
	          str += "<input type='hidden' name='attachList["+i+"].uploadPath' value='"+jobj.data("path")+"'>";
	          str += "<input type='hidden' name='attachList["+i+"].fileType' value='"+ jobj.data("type")+"'>";
	          
	        });
	        formObj.append(str).submit();
        }
	    
	    formObj.submit();
	  });

});

</script>


<!-- jQuery 첨부파일을 보여주는 작업 처리 -->
<script>

$(document).ready(function() {
  (function(){
    
    var bno = '<c:out value="${board.bno}"/>';
    
    $.getJSON("/board/getAttachList", {bno: bno}, function(arr){
    
      console.log(arr);
      
      var str = "";


      $(arr).each(function(i, attach){
          
          //image type
          if(attach.fileType){
            var fileCallPath =  encodeURIComponent( attach.uploadPath+ "/s_"+attach.uuid +"_"+attach.fileName)
            
            //교체할 파일을 첨부하는 <input>태그와 첨부파일의 이름과 삭제x버튼이 보이는 부분
            str += "<li data-path='"+attach.uploadPath+"' data-uuid='"+attach.uuid+"' "
            str +=" data-filename='"+attach.fileName+"' data-type='"+attach.fileType+"' ><div>";
            str += "<span> "+ attach.fileName+"</span>";
            str += "<button type='button' data-file=\'"+fileCallPath+"\' data-type='image' "
            str += "class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
            str += "<img src='/display?fileName="+fileCallPath+"'>";
            str += "</div>";
            str +"</li>";
          }else{
              //교체할 파일을 첨부하는 <input>태그와 첨부파일의 이름과 삭제x버튼이 보이는 부분
        	  str += "<li data-path='"+attach.uploadPath+"' data-uuid='"+attach.uuid+"' "
              str += "data-filename='"+attach.fileName+"' data-type='"+attach.fileType+"' ><div>";
              str += "<span> "+ attach.fileName+"</span><br/>";
              str += "<button type='button' data-file=\'"+fileCallPath+"\' data-type='file' "
              str += " class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
              
              str += "<img src='/resources/img/attach.png'></a>";
              str += "</div>";
              str +"</li>";
          
          }
       });

      
      $(".uploadResult ul").html(str);
      
    });//end getjson
  })();//end function
  
  
  //'x'버튼을 클릭하면 사용자의 확인을 거쳐 화면상에 사라지도록 처리
  $(".uploadResult").on("click", "button", function(e){
	    
    console.log("delete file");
      
    if(confirm("Remove this file? ")){
    
      var targetLi = $(this).closest("li");
      targetLi.remove();
    }
  });  
  
  //서버에 파일을 업로드 (첨부파일추가)
  var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
  var maxSize = 5242880; //5MB
  
  function checkExtension(fileName, fileSize){
    
    if(fileSize >= maxSize){
      alert("파일 사이즈 초과");
      return false;
    }
    
    if(regex.test(fileName)){
      alert("해당 종류의 파일은 업로드할 수 없습니다.");
      return false;
    }
    return true;
  }
  //CSRF토큰 처리 
  var csrfHeaderName ="${_csrf.headerName}"; 
  var csrfTokenValue="${_csrf.token}";

  
  $("input[type='file']").change(function(e){

    var formData = new FormData();
    
    var inputFile = $("input[name='uploadFile']");
    
    var files = inputFile[0].files;
    
    for(var i = 0; i < files.length; i++){

      if(!checkExtension(files[i].name, files[i].size) ){
        return false;
      }
      formData.append("uploadFile", files[i]);
      
    }
    
    $.ajax({
      url: '/uploadAjaxAction',
      processData: false, 
      contentType: false,data: 
      formData,type: 'POST',
      beforeSend: function(xhr) { 
          xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
      },
      dataType:'json',
        success: function(result){
          console.log(result); 
		  showUploadResult(result); //업로드 결과 처리 함수 

      }
    }); //$.ajax
  
});
  //서버에업로드한 파일을 화면에 섬네일이나 파일의 아이콘으로 보이게 처리 (첨부파일추가)
  function showUploadResult(uploadResultArr){
	    
	    if(!uploadResultArr || uploadResultArr.length == 0){ return; }
	    
	    var uploadUL = $(".uploadResult ul");
	    
	    var str ="";
	    
	    $(uploadResultArr).each(function(i, obj){
			
			if(obj.image){
				var fileCallPath =  encodeURIComponent( obj.uploadPath+ "/s_"+obj.uuid +"_"+obj.fileName);
				str += "<li data-path='"+obj.uploadPath+"'";
				str +=" data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-type='"+obj.image+"'"
				str +" ><div>";
				str += "<span> "+ obj.fileName+"</span>";
				str += "<button type='button' data-file=\'"+fileCallPath+"\' "
				str += "data-type='image' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
				str += "<img src='/display?fileName="+fileCallPath+"'>";
				str += "</div>";
				str +"</li>";
			}else{
				var fileCallPath =  encodeURIComponent( obj.uploadPath+"/"+ obj.uuid +"_"+obj.fileName);			      
			    var fileLink = fileCallPath.replace(new RegExp(/\\/g),"/");
			      
				str += "<li "
				str += "data-path='"+obj.uploadPath+"' data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-type='"+obj.image+"' ><div>";
				str += "<span> "+ obj.fileName+"</span>";
				str += "<button type='button' data-file=\'"+fileCallPath+"\' data-type='file' " 
				str += "class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
				str += "<img src='/resources/img/attach.png'></a>";
				str += "</div>";
				str +"</li>";
			}

	    });
	    
	    uploadUL.append(str);
	  }
	  
	});
  
</script>
  


<%@include file="../includes/footer.jsp"%>
