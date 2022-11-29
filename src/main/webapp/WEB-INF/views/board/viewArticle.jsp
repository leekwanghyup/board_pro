<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<style>
.viewMode {display:none;}
</style>
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
history.forward();
</script>
<div class="container my-3">
    <h2 class="text-center">게시글 보기</h2>
    <form id="viewForm" action="${contextPath}/board/modArticle" enctype="multipart/form-data" method="post">
    	<input type="hidden" value="${article.articleNO}" name="articleNO">
		<table class="table">
		    <tr>
		        <th class="success">글번호</th>
		        <td>${article.articleNO}</td>
		        <th class="success">조회수</th>
		        <td>100</td>
		    </tr>
		    <tr>
		        <th class="success">작성자</th>
		        <td>${article.id}</td>
		        <th class="success">작성일</th>
		        <td>${article.writeDate}</td>
		    </tr>
		    <tr>
		        <th class="success">제목</th>
		        <td colspan="3">
		        	<input type="text" class="form-control" value="${article.title}" name="title" readonly="readonly" />
		        </td>
		    </tr>
		    <tr>
		        <th class="success">글 내용</th>
		        <td colspan="3">
		        	<textarea name="content" rows="10" class="form-control" readonly>${article.content}</textarea>
		        </td>
		    </tr>
		    <tr>
		        <th class="success">첨부 이미지</th>
		        <td colspan="3">
		        	<input type="file" name="imageFileName" class="form-control viewMode" onchange="readURL(this)" />
		        <div class="my-3">
		        	<c:if test="${not empty article.imageFileName}">
		        		<input type="hidden" name="originalFileName" value="${article.imageFileName}"/>
		        		<div id="preview">
							<img src="${contextPath}/fileDownload?articleNO=${article.articleNO}&imageFileName=${article.imageFileName}" class="originImg" />
		        		</div>
					</c:if>
					<c:if test="${empty article.imageFileName}">
						<p>등록된 이미지가 없습니다.</p>
					</c:if>
					</div>	
		        </td>
		    </tr>
		    <tr>
		        <td colspan="4" class="text-center">
		            <button type="button" class="btn btn-warning toModForm">수정</button>
		            <button type="button" class="btn btn-danger delArticle">삭제</button>
		            <button type="button" class="btn btn-secondary listArticles">목록</button>
		        </td>
		    </tr>
		    <tr class="viewMode">
		        <td colspan="4" class="text-center">
		            <button type="button" class="btn btn-warning modProcedd">수정하기</button>
		            <button type="button" class="btn btn-secondary backViewForm" >취소</button>
		        </td>
		    </tr>
		</table>
	</form>
</div>
<script>
let viewForm = $('#viewForm'); // 입력폼
let originImg = $('.originImg').clone(); // 수정전 이미지 복사

$('.toModForm').on('click',function(){ // 수정폼으로 변경
	$("input[name='title']").attr("readonly",false); // 제목입력폼 수정가능
	$("textarea[name='content']").attr("readonly",false); // 내용입력폼 수정가능
	$('input[type="file"]').show(); // 파일입력폼 보이기
	$(this).closest('tr').hide(); // 뷰모드 숨기기
	$(this).closest('tr').next().show(); // 수정모드 보이기
});

$('.backViewForm').on('click',function(){ // 뷰폼으로 변경
	$("input[name='title']").attr("readonly",true);
	$("textarea[name='content']").attr("readonly",true);
	$('input[type="file"]').val(''); // 파일입력폼 초기화
	$('input[type="file"]').hide(); // 파일입력폼 숨기기
	$('#preview').html(originImg); // 원본이미지 복원
	$(this).closest('tr').hide(); // 수정모드 숨기기
	$(this).closest('tr').prev().show(); // 뷰모드 보이기
});

$('.listArticles').on('click',function(){ // 목록으로 
	viewForm.attr("action","${contextPath}/board/listArticles")
	viewForm.empty();
	viewForm.submit();
});

$('.modProcedd').on('click',function(){
	viewForm.attr("action","${contextPath}/board/modArticle");
	viewForm.submit();
})

$('.delArticle').on('click',function(){
	viewForm.attr("action","${contextPath}/board/removeArticle");
	viewForm.submit();
});

</script>
<%@ include file="../layout/footer.jsp" %>