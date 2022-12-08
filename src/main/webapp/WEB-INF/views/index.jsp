<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="layout/header.jsp" %>
<div class="container my-3">
	<h1>메인페이지</h1>
	<c:if test="${not empty auth}">
		<p>${auth.loginId}님, 반갑습니다.</p>
	</c:if>
</div>
<%@ include file="layout/footer.jsp" %>