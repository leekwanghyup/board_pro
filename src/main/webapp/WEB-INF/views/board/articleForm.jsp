<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<script>
function readURL(input){
	if(input.files && input.files[0]){
		let reader = new FileReader();  // 파일 읽기 객체
		reader.onload = function(e){ // 파일을 읽으면 이벤트 발생
			let srcValue = e.target.result;
			if(srcValue.startsWith("data:image/")){
				let imgTag = "<img src='"+srcValue+"'>";
				$('#preview').html(imgTag);	
			} else {
				alert('이미지만 등록하세요');
				$('#imageFileName').val('');
				$('#preview').html('');
			}
		}
		reader.readAsDataURL(input.files[0]); // 파일 읽기 메서드 호출
	}
}
</script>
<div class="container my-4">
	<div class="jumbotron">
		<h1>새로운 글쓰기</h1>
	</div>
	<form  method="post" action="${contextPath}/board/addArticle" enctype="multipart/form-data">
		<div class="form-group">
			<label for="title">제목:</label>
			<input type="text" class="form-control" id="title" name="title">
		</div>
		<div class="form-group">
			<label for="content">내용:</label>
			<textarea rows="10" class="form-control" name="content" id="content"></textarea>
		</div>
		<div class="form-group">
			<label for="id">작성자:</label>
			<input type="text" class="form-control" id="id" name="id" value="hong">
		</div>
		<div class="form-group">
			<label for="imageFileName">첨부파일:</label>
			<input type="file" class="form-control" id="imageFileName" name="imageFileName" onchange="readURL(this)">
		</div>
		<div class="form-group" id="preview"></div>
		<button class="btn btn-primary">글쓰기</button>
		<a href="#" class="btn bt-secondary">목록보기</a>
 	</form>
</div>
<%@ include file="../layout/header.jsp" %>


