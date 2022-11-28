<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<div class="container my-3">
<h1>게시글 목록</h1>
<table class="table">
	<tr>
		<th>번호</th>
		<th>제목</th>
		<th>작성자</th>
		<th>작성일</th>
	</tr>
	<c:choose>
		<c:when test="${empty listArticles}">
			<tr><td colspan="4"><p>등록된 글이 없습니다.</p></td></tr>
		</c:when>
		<c:otherwise>
		<c:forEach items="${listArticles}" var="b">
		<tr>
			<td>${b.articleNO}</td>
			<c:choose>
				<c:when test="${b.level > 1}">
					<td style="padding-left:${40*(b.level-1)}px">
						<a href="${contextPath}/board/viewArticle?articleNO=${b.articleNO}">[답변:]${b.title}</a>
					</td> 
				</c:when>
				<c:otherwise>
					<td>
						<a href="${contextPath}/board/viewArticle?articleNO=${b.articleNO}">${b.title}</a>
					</td>
				</c:otherwise>
			</c:choose>
			<td>${b.id}</td>
			<td>
				<fmt:formatDate value="${b.writeDate }" pattern="yyyy년MM월dd일"/>
			</td>
		</tr>
		</c:forEach>
		</c:otherwise>
	</c:choose>
</table>
<a href="${contextPath}/board/articleForm" class="btn btn-primary">글쓰기</a>
</div>
<%@ include file="../layout/footer.jsp" %>