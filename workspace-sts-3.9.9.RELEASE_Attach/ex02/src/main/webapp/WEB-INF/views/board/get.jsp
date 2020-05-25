<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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

      <div class="panel-heading">Board Read Page</div>
      <!-- /.panel-heading -->
      <div class="panel-body">

       
          <!-- 게시물벚호를 보여줄 수 있는 필드 추가 및 readonly지정  -->
          <div class="form-group">   <!-- 입력항목그룹화 -->
            <label>Bno</label> <input class="form-control" name='bno'
            value='<c:out value="${board.bno }"/>' readonly="readonly">
          </div>
       
           <div class="form-group">   
          <label>Title</label> <input class="form-control" name='title'
            value='<c:out value="${board.title }"/>' readonly="readonly">
        </div>

          <div class="form-group">
            <label>Text area</label>
            <textarea class="form-control" rows="3" name='content'
            readonly="readonly"><c:out value="${board.content }" /></textarea>
          </div>

          <div class="form-group">
          <label>Writer</label> <input class="form-control" name='writer'
            value='<c:out value="${board.writer }"/>' readonly="readonly">
        </div>
          
          <!-- <form>태그 삭제 및 수정/삭제 페이지로 이동하거나 원래의 페이지로 이동 할수 있는 버튼 및 이동링크 추가 -->
          <!-- <button data-oper='modify' class="btn btn-default"
          onclick="location.href='/board/modify?bno=<c:out value="${board.bno }"/>'">
          Modify</button>
          <button data-oper='list' class="btn btn-info" 
          onclick="location.href='/board/list'">
          List</button> -->
 
        <!-- <form> 처리  -->
        <button data-oper='modify' class="btn btn-default">Modify</button>
        <button data-oper='list' class="btn btn-info">List</button>
        
       <!-- 버튼을 클릭하면 <form>태그를 이용하는 방식에맞춰 데이터를 추가해서 이동하도록 함 -->
        <form id='operForm' action="/boad/modify" method="get">
        <input type='hidden' id='bno' name='bno' value='<c:out value="${board.bno}"/>'>  
        <input type='hidden' name='pageNum' value='<c:out value="${cri.pageNum}"/>'>   <!-- pageNum, amount 추가 -->
        <input type='hidden' name='amount' value='<c:out value="${cri.amount}"/>'>
        <!-- 조회페이지에서 Criteria의 type,keyword 처리 -->
         <input type='hidden' name='keyword' value='<c:out value="${cri.keyword}"/>'>
          <input type='hidden' name='type' value='<c:out value="${cri.type}"/>'>
        </form>
        

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
  
  var operForm = $("#operForm");  //사용자가 수정 버튼 클릭시, 해당 id의 <form>태그를 전송하도록 처리 
  
  //사용자가 버튼을 누르면 bno값이 전달되고 <form>태그를 submit 시켜서 처리 
  $("button[data-oper='modify']").on("click", function(e){  
    
    operForm.attr("action","/board/modify").submit();
    
  });
  
 //사용자가 list로 이동하면 <form>태그 내 bno를 지우고 submit 시켜서 리스트 페이지로 이동 
  $("button[data-oper='list']").on("click", function(e){
    
    operForm.find("#bno").remove();
    operForm.attr("action","/board/list")
    operForm.submit();
    
  });  
});
</script>

<%@include file="../includes/footer.jsp"%>
