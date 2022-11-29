<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<div class="container my-3">
	<div class="row d-flex justify-content-center">
		<div class="col-sm-6">
			<form class="memberForm" method="post">
				<table class="table table-borderless">
					<tr>
						<th colspan="2" class="text-center">
							<h2>회원가입</h2>
						</th>
					</tr>
					<tr>
						<td>아이디</td>
						<td><input type="text" class="form-control" name="userId"></td>
					</tr>
					<tr>
						<td>비밀번호</td>
						<td><input type="password" class="form-control" name="userPwd" autocomplete="on" ></td>
					</tr>
					<tr>
						<td>이름</td>
						<td><input type="text" class="form-control" name="userName"></td>
					</tr>
					<tr>
						<td>이메일</td>
						<td><input type="text" class="form-control" name="email"></td>
					</tr>
					<tr>
						<td colspan="2" class="text-center">
							<button type="button" class="btn btn-primary joinMember">회원가입</button>
							<button type="button" class="btn btn-secondary">목록으로</button>
						</td>
					</tr>
				</table>
			</form>
		</div>	
	</div>
</div>
<%@ include file="../layout/footer.jsp" %>
<script>
$(function(){
	let memberForm = $('.memberForm');
	$('.joinMember').on('click',function(){
		memberForm.attr("action","${contextPath}/member/join")
		memberForm.submit();
	})
});
</script>