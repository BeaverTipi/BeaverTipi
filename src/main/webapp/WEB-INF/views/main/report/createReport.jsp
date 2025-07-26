<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>신고 작성</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <%-- 파일 미리보기 관련 CSS (광고 폼과 동일하게 사용) --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/main/ads/filePreview.css">
    
    <%-- PDF.js 라이브러리 (CDN) - 파일 미리보기에 사용 --%>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.4.120/pdf.min.js"></script>
    <%-- Axios 라이브러리 (CDN) - AJAX 폼 제출에 사용 --%>
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
</head>
<body>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script>
    var contextPath = '${pageContext.request.contextPath}';
    // PDF.js worker 소스 경로 설정
    pdfjsLib.GlobalWorkerOptions.workerSrc = 'https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.4.120/pdf.worker.min.js';
</script>

<div class="container mt-5">
    <h2 class="mb-4">신고 작성</h2>
    
    <%-- multipart/form-data 인코딩 타입 설정 및 commandName 지정 --%>
    <form:form id="createReportForm" action="${pageContext.request.contextPath}/main/report/create" 
               method="post" enctype="multipart/form-data" 
               modelAttribute="reportVO"> <%-- ReportVO를 modelAttribute로 설정 --%>
        
        <div class="card mb-4">
            <div class="card-header">신고 내용</div>
            <div class="card-body">
                <div class="form-group">
                    <label for="brdTitlNm">신고 제목 <span class="text-danger">*</span></label>
                    <%-- ReportVO는 BoardVO를 상속하므로 brdTitlNm 필드 직접 사용 --%>
                    <form:input path="brdTitlNm" class="form-control" id="brdTitlNm" placeholder="신고 제목을 입력하세요" required="true"/>
                    <form:errors path="brdTitlNm" cssClass="text-danger"/>
                </div>
                <div class="form-group">
                    <label for="brdCont">신고 상세 내용 <span class="text-danger">*</span></label>
                    <form:textarea path="brdCont" class="form-control" id="brdCont" rows="5" placeholder="신고 내용을 자세히 입력해주세요" required="true"/>
                    <form:errors path="brdCont" cssClass="text-danger"/>
                </div>
            </div>
        </div>

        <div class="card mb-4">
            <div class="card-header">신고 대상 정보</div>
            <div class="card-body">
                <div class="form-group">
                    <label for="rptCode">신고 유형 <span class="text-danger">*</span></label>
                    <%-- 신고 유형이 이미 설정되어 있다면 disabled 및 사용자에게 표시 --%>
                    <c:set var="isRptCodePredefined" value="${not empty reportVO.rptCode}"/>
                    <form:select path="rptCode" class="form-control" id="rptCode" required="true" ${isRptCodePredefined ? 'disabled' : ''}>
                        <form:option value="" label="-- 신고 유형 선택 --"/>
                        <form:option value="MEMB" label="회원"/>
                        <form:option value="LSTG" label="매물"/>
                    </form:select>
                    <form:errors path="rptCode" cssClass="text-danger"/>
                    <c:if test="${isRptCodePredefined}">
                        <small class="form-text text-muted">신고 유형이 자동으로 선택되었습니다.</small>
                        <input type="hidden" name="rptCode" value="${reportVO.rptCode}"/>
                    </c:if>
                </div>
                <div class="form-group">
                    <label for="rptTargetId">신고 대상 ID/게시글 번호 <span class="text-danger">*</span></label>
                    <%-- 신고 대상 ID가 이미 설정되어 있다면 readonly 및 사용자에게 표시 --%>
                    <c:set var="isRptTargetIdPredefined" value="${not empty reportVO.rptTargetId}" />
                    <form:input path="rptTargetId" class="form-control" id="rptTargetId" 
                                placeholder="신고 대상의 ID 또는 게시글 번호를 입력하세요" 
                                required="true" ${isRptTargetIdPredefined ? 'readonly' : ''}/>
                    <form:errors path="rptTargetId" cssClass="text-danger"/>
                    <c:if test="${isRptTargetIdPredefined}">
                        <small class="form-text text-muted">신고 대상 ID가 자동으로 입력되었습니다.</small>
                    </c:if>
                </div>
            </div>
        </div>

        <%-- 파일 첨부 영역 (광고 폼과 동일한 UI 및 input name/id 사용) --%>
        <div class="card mb-4">
            <div class="card-header">증거 자료 첨부</div>
            <div class="card-body">
                <div class="form-group">
                    <label for="attachFiles">파일 선택</label>
                    <%-- multiple 속성을 추가하여 여러 파일 선택 가능하도록 함 --%>
                    <input type="file" class="form-control-file" id="attachFiles" name="attachFiles" multiple>
                    <h4></h4>
                    <small class="form-text text-muted">신고 내용을 뒷받침할 이미지, PDF 등 증거 자료를 첨부해주세요.<br>최대 5개 파일, 각 파일 최대 10MB.</small>
                </div>

                <div class="file-preview-area mt-3">
                    <div class="file-list-section">
                        <h5>첨부 예정 파일 목록</h5>
                        <table border="1" id="fileTable" class="table table-bordered table-sm">
                            <thead>
                                <tr><th>파일명</th><th>크기</th><th></th></tr>
                            </thead>
                            <tbody>
                                <%-- 파일 목록이 동적으로 추가될 영역 --%>
                            </tbody>
                        </table>
                    </div>
                    <div class="file-display-section">
                        <h5>미리보기</h5>
                        <canvas id="pdfCanvas" class="img-fluid border"></canvas>
                        <div id="pdf-controls" class="d-flex justify-content-center mt-2">
                            <button id="prevBtn" type="button" class="btn btn-sm btn-outline-secondary mr-1">이전</button>
                            <span>페이지 <span id="fileIndex">0</span> / <span id="totalCount">0</span></span>
                            <button id="nextBtn" type="button" class="btn btn-sm btn-outline-secondary ml-1">다음</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="form-group text-center">
            <button type="submit" class="btn btn-primary mr-2">신고 접수</button>
            <button type="button" class="btn btn-secondary" onclick="history.back()">취소</button>
        </div>
    </form:form>
</div>

<script src="${pageContext.request.contextPath}/app/js/main/report/createReport.js"></script>
</body>
</html>