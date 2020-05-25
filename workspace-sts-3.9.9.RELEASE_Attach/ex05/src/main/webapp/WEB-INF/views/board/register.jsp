<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@include file="../includes/header.jsp"%>

<!-- register.jsp에서는 <form>태그를 이용해서 필요한 데이터를 전송
<input>이나 <textarea>태그의 name속성은 BoardVO 클래스의 변수와 일치시켜 줘야함.  -->

<div class="row">
  <div class="col-lg-12"> 
    <h1 class="page-header">Board Register</h1>
  </div>
  <!-- /.col-lg-12 -->
</div>
<!-- /.row -->

<div class="row">
  <div class="col-lg-12">
    <div class="panel panel-default"> 

      <div class="panel-heading">Board Register</div> 
      <!-- /.panel-heading -->
      <div class="panel-body">

        <form role="form" action="/board/register" method="post">
          <div class="form-group">
            <label>Title</label> <input class="form-control" name='title'>
          </div>

          <div class="form-group">
            <label>Text area</label>
            <textarea class="form-control" rows="3" name='content'></textarea>
          </div>

          <div class="form-group">
            <label>Writer</label> <input class="form-control" name='writer'>
          </div>
          <button type="submit" class="btn btn-default">Submit
            Button</button>
          <button type="reset" class="btn btn-default">Reset Button</button>
        </form>

      </div>
      <!--  end panel-body -->

    </div>
    <!--  end panel-body -->
  </div>
  <!-- end panel -->
</div>
<!-- /.row -->


<!-- 게시물의 제목이나 내용을 입력하는 <div> -->

<div class="row">
  <div class="col-lg-12">
    <div class="panel panel-default">

      <div class="panel-heading">File Attach</div>
      <!-- /.panel-heading -->
      <div class="panel-body">
        <div class="form-group uploadDiv">
            <input type="file" name='uploadFile' multiple>
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


<script>
//등록버튼을 눌렀을때 가장먼저 첨부파일 관련된 처리를 할 수 있도록 기본동작을 막는 작업
//브라우저에서 게시물등록 선택시 이미 업로드된 항목들은 내부적으로 <input type='hidden'>태그를 만들어서 
//<form>태그가 submit될때 같이 전송됨 
$(document).ready(function(e){
	
  var formObj = $("form[role='form']");
  
  $("button[type='submit']").on("click", function(e){ //form태그 전송하는 부분. 
	 
    e.preventDefault();  
    
    console.log("submit clicked");
    
    var str = "";
    
    $(".uploadResult ul li").each(function(i, obj){
        
        var jobj = $(obj);
        
        console.dir(jobj);
        console.log("-------------------------");
        console.log(jobj.data("filename"));
        
        //첨부파일 정보는 BoardVO에서 attachList 형태로 수집하기 때문에 <input type='hidden'>의 name은 
        //'attachList[인덱스번호]'와 같은 이름을 사용하도록 한다. 
        str += "<input type='hidden' name='attachList["+i+"].fileName' value='"+jobj.data("filename")+"'>";
        str += "<input type='hidden' name='attachList["+i+"].uuid' value='"+jobj.data("uuid")+"'>";
        str += "<input type='hidden' name='attachList["+i+"].uploadPath' value='"+jobj.data("path")+"'>";
        str += "<input type='hidden' name='attachList["+i+"].fileType' value='"+ jobj.data("type")+"'>";
        
      });
    formObj.append(str).submit();
  }); 
  
 
  
  var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$"); 
  var maxSize = 5242880; //5MB
  
  function checkExtension(fileName, fileSize){
    
    if(fileSize >= maxSize){ //대용량사이즈 업로드방지 
      alert("파일 사이즈 초과");
      return false;
    }
    
    if(regex.test(fileName)){ //특정 확장자 업로드방지 
      alert("해당 종류의 파일은 업로드할 수 없습니다.");
      return false;
    }
    return true;
  }
  
  $("input[type='file']").change(function(e){ //별도의 업로드 버튼을 두지않고 내용이 변경되는것을 감지해서 처리 하도록함 

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
      dataType:'json',
        success: function(result){
          console.log(result); 
		  showUploadResult(result); //업로드 결과 처리 함수 

      }
    }); //$.ajax
  });  
  
  
  function showUploadResult(uploadResultArr){
	    
	    if(!uploadResultArr || uploadResultArr.length == 0){ return; }
	    
	    var uploadUL = $(".uploadResult ul");
	    
	    var str ="";
	    
	    $(uploadResultArr).each(function(i, obj){
	    
	    	/* //image type
	        if(obj.image){ //이미지일경우 보여지는 화면내용(HTML)
	          var fileCallPath =  encodeURIComponent( obj.uploadPath+ "/s_"+obj.uuid +"_"+obj.fileName);
	          str += "<li><div>";
	          str += "<span> "+ obj.fileName+"</span>";
	          //<button>태그에 'data-file' 과 'data-type' 정보 추가 
	          str += "<button type='button' data-file=\'"+fileCallPath+"\' data-type='image' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
	          str += "<img src='/display?fileName="+fileCallPath+"'>";
	          str += "</div>";
	          str +"</li>";
	        }else{ //일반파일일 경우 보여지는 화면내용(HTML)
	          var fileCallPath =  encodeURIComponent( obj.uploadPath+"/"+ obj.uuid +"_"+obj.fileName);            
	            var fileLink = fileCallPath.replace(new RegExp(/\\/g),"/");
	              
	          str += "<li><div>";
	          str += "<span> "+ obj.fileName+"</span>";
   	          //<button>태그에 'data-file' 과 'data-type' 정보 추가 
	          str += "<button type='button' data-file=\'"+fileCallPath+"\' data-type='file' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
	          str += "<img src='/resources/img/attach.png'></a>";
	          str += "</div>";
	          str +"</li>";
	        } */
			//image type
	    	if(obj.image){
				var fileCallPath =  encodeURIComponent( obj.uploadPath+ "/s_"+obj.uuid +"_"+obj.fileName);
				str += "<li data-path='"+obj.uploadPath+"'";
				str +=" data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-type='"+obj.image+"'" //태그에 첨부파일과 관련된 정보 data-uuid, data-filename, data-type추가 
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
				str += "data-path='"+obj.uploadPath+"' data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-type='"+obj.image+"' ><div>"; //태그에 첨부파일과 관련된 정보 data-uuid, data-filename, data-type추가 
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
  
  
  //x버튼 클릭시 선택된 첨부파일 삭제 
  $(".uploadResult").on("click", "button", function(e){
	    
	    console.log("delete file");
	      
	    var targetFile = $(this).data("file");
	    var type = $(this).data("type");
	    
	    var targetLi = $(this).closest("li");
	    
	    $.ajax({
	      url: '/deleteFile',
	      data: {fileName: targetFile, type:type},
	      dataType:'text',
	      type: 'POST',
	        success: function(result){
	           alert(result);
	           
	           targetLi.remove();
	         }
	    }); //$.ajax
	   });

  
});

</script>





<%@include file="../includes/footer.jsp"%>
