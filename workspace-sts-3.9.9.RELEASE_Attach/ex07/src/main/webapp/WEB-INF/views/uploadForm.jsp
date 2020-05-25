<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">  
<title>Insert title here</title>
</head>
<body>


<form action="uploadFormAction" method="post" enctype="multipart/form-data">  <!-- 실제 전송경로. enctype="multipart/from-data" 주의 -->

<input type='file' name='uploadFile' multiple> <!-- input type 'file'의 속성 multiple : 하나의 input태그로 여러개의 파일 한번에 업로드  -->

<button>Submit</button>

</form>

</body>
</html>
