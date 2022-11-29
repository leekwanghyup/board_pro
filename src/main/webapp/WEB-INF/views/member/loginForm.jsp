<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<div class="container my-3">
	<form action="${contextPath}/member/login" method="post">
		아이디 : <input type="text" name="userId"><br>
		비밀번호 : <input type="text" name="userPwd"><br>
		<button>로그인</button>
	</form>
</div>
<%@ include file="../layout/footer.jsp" %>

