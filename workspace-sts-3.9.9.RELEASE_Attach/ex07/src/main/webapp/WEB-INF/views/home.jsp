<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>

<P>  The time on the server is ${serverTime}. </P>


<script type ="text/javascript"> <!-- 로그인후 목록페이지로 이동  -->
self.location ="/board/list";
</script>

</body>
</html>
