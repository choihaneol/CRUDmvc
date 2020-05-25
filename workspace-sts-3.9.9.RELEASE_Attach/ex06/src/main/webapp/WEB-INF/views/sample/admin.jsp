<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %> <!-- 스프링시큐리티 관련 태그 선언 -->
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>/sample/admin page</h1>

<!-- principal 속성 -->
<p>principal : <sec:authentication property="principal"/></p>  <!-- UserDatailsService에서 반환된 객체.  -->
<p>MemberVO : <sec:authentication property="principal.member"/></p>
<p>사용자이름 : <sec:authentication property="principal.member.userName"/></p>
<p>사용자아이디 : <sec:authentication property="principal.username"/></p>
<p>사용자 권한 리스트  : <sec:authentication property="principal.member.authList"/></p>
<!-- Principal = CustomUser 이므로 principal.member 는 CustomUser객체의 getMember()를 호출하겠다 -->

<a href="/customLogout">Logout</a>


</body>
</html>
