<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 가입 페이지</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css">
</head>
<body>
	<form>
		<div class="form-group">
			<label>회원가입</label>
			<input type="text" name="memId" placeholder="영문 소문자 최대 12자" maxlength="12">
		</div>
		<div class="form-group">
			<label>비밀번호</label>
			<input type="password" name="memPass" placeholder="문자 숫자 조합 8~20자" maxlength="20" min="8">
		</div>
		<div class="form-group">
			<label>이름</label>
			<input type="text" name="memNm" placeholder="이름">
		</div>
		<div class="form-group">
			<label>전화번호</label>
			<select name="firstTelNo">
				<option value="+82" selected>+82</option>
				<option value="010">010</option>
				<option value="010">070</option>
			</select>
			<input type="number" name="memTel" placeholder="전화번호 입력" maxlength="15">
			<button type="button">인증요청</button>
		</div>
		<div class="form-group">
			<label>회원가입</label>
			<input type="text" name="authCode" placeholder="인증번호 6자 입력" maxlength="6">
		</div>
		<div>
			<button type="submit">확인</button>
			<button type="reset">취소</button>
		</div>
	</form>
</body>
</html>