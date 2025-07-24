<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<head>
<meta charset="UTF-8">
<title>새 공지사항 등록</title>
</head>
<body>
	<h1>공지사항 등록</h1>
	<div class="card">
		<div class="card-body">
			<form:form modelAttribute="board" method="post">
    			<form:hidden path="brdDelYn" value="N" />
				<div>
				
		<div class="form-control">
			<label class="label" for="brdTitlNm">제목</label>
			<form:input path="brdTitlNm" placeholder="제목을 입력하세요" />
			<form:errors path="brdTitlNm" cssClass="text-danger" />
		</div>
		
		<div class="form-control">
		  <label class="label" for="brdCtgryGrpCd">게시판 유형</label>
		  <c:forEach items="${brdCodeList}" var="item">
			  <c:if test="${item.codeValue eq '007' or item.codeValue eq '008'or item.codeValue eq '009'}">
			 	 <form:radiobutton path="brdCtgryGrpCd" value="${item.codeValue }" />
			 	 ${item.codeName }
			  </c:if>
		  </c:forEach>
		  <form:errors path="brdCtgryGrpCd" cssClass="text-danger" />
		</div>
						
		  <div class="form-detail" id="noticeDetailBox" style="display:none">
			  <label class="label">상세설정</label>
			
			 <div>
			  <label class="label">공지사항 유형</label>
			  <c:forEach var="item" items="${noticeTypeList}">
			    <form:radiobutton path="noticeType" value="${item.codeValue}" />
			    ${item.codeName}
			  </c:forEach>
			  <form:errors path="noticeType" cssClass="text-danger" />
			</div>
			
			  <div>
			    <label class="label">종료일시</label>
			    <form:input path="brdEndDtm" type="date" />
			    <form:errors path="brdEndDtm" cssClass="text-danger" />
			  </div>
			
			  <div class="form-control">
			    <label class="label">내용</label>
			    <form:textarea path="brdCont" id="summernote-notice" />
			    <form:errors path="brdCont" cssClass="text-danger" />
			  </div>
			</div>

       <div class="form-detail" id="faqDetailBox" style="display:none">
		  <label class="label">상세설정</label>
		
		 <div>
		  <label class="label">FAQ 유형</label>
		  <c:forEach var="item" items="${faqCtgryList}">
			  <form:radiobutton path="faqCtgry" value="${item.codeValue}" />
			  ${item.codeName}
			</c:forEach>
		  <form:errors path="faqCtgry" cssClass="text-danger" />
		</div>
		
		  <div class="form-control">
		    <label class="label">내용</label>
		    <form:textarea path="brdCont" id="summernote-faq" />
		    <form:errors path="brdCont" cssClass="text-danger" />
		  </div>
		</div>
       

        <div class="form-detail" id="qnaDetailBox" style="display:none">
		  <label class="label">상세설정</label>
		
		 <div>
		  <label class="label">QnA 유형</label>
		  <c:forEach var="item" items="${qnaCtgryList}">
		    <form:radiobutton path="qnaCtgry" value="${item.codeValue}" />
		    ${item.codeName}
		  </c:forEach>
		  <form:errors path="qnaCtgry" cssClass="text-danger" />
		</div>

		
		  <div class="form-control">
		    <label class="label">내용</label>
		    <form:textarea path="brdCont" id="summernote-qna" />
		    <form:errors path="brdCont" cssClass="text-danger" />
		  </div>
		</div>

			
		<div class="card-footer">
			<div class="button-group">
				<button type="submit" class="btn btn-outline-success">등록</button>
				<a class="btn btn-outline-danger" href="${pageContext.request.contextPath}/admin/notice/list">취소</a>
			</div>
		</div>
			</form:form>
		</div>
	</div>
</body>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/admin/common_admin.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/admin/board/adminNotice.css">
	<script src="${pageContext.request.contextPath}/app/js/admin/board/boardToggle.js"></script>
	
	<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-lite.min.css" rel="stylesheet">
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-lite.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/lang/summernote-ko-KR.min.js"></script>
