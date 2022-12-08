<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:if test="${not empty sessionScope.auth}">
	<c:set var="auth" value="${auth}"/>
</c:if>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>계층형게시판</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<script>
$(function(){
	$('.logout').on('click',function(e){
		e.preventDefault();
		let logoutForm = document.createElement("form");
		logoutForm.setAttribute("method", "post");
		logoutForm.setAttribute("action", "${contextPath}/member/logout");
		document.body.appendChild(logoutForm);
		logoutForm.submit();
	})
});
</script>
</head>
<body>
<c:if test="${not empty loginedMember}">
	${auth.loginId}님 로그인 중 .....
</c:if>
<nav class="navbar navbar-expand-sm bg-dark navbar-dark d-flex justify-content-between">
  <!-- Brand/logo -->
  
  <ul class="navbar-nav">
  	<li class="nav-item">
  		<a class="navbar-brand" href="#">Logo</a>
  	</li>
    <li class="nav-item">
      <a class="nav-link" href="${contextPath}/board">자유게시판</a>
    </li>
    <li class="nav-item">
      <a class="nav-link" href="#">Link 2</a>
    </li>
    <li class="nav-item">
      <a class="nav-link" href="#">Link 3</a>
    </li>
  </ul>
  <ul class="navbar-nav">
	<c:if test="${not empty auth}">
		<li class="nav-item"><a class="nav-link" href="${contextPath}/member/myPage">나의정보보기</a></li>
		<li class="nav-item"><a class="nav-link logout" href="#" >로그아웃</a></li>
		<c:if test="${auth.loginId eq 'admin'}">
			<li class="nav-item"><a class="nav-link logout" href="#" >관리자메뉴</a></li>
		</c:if>
 	</c:if>
 	<c:if test="${empty auth}">
  	  <li class="nav-item"><a class="nav-link" href="${contextPath}/member/joinForm">회원가입</a></li>
  	  <li class="nav-item"><a class="nav-link" href="${contextPath}/member/loginForm">로그인</a></li>
  	</c:if>
  </ul>
</nav>
