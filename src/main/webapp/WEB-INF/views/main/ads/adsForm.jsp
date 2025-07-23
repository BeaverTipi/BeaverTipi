<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>광고 요청 작성</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/main/ads/filePreview.css"> <%-- 파일 미리보기 관련 CSS (별도 파일로 관리 추천) --%>
    
    <%-- PDF.js 라이브러리 (CDN) --%>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.4.120/pdf.min.js"></script>
    <%-- Axios 라이브러리 (CDN) - 파일 업로드 및 데이터 전송에 사용 --%>
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
    <h2 class="mb-4">광고 요청 작성</h2>
    
    <%-- multipart/form-data 인코딩 타입 설정 및 commandName 지정 --%>
    <form:form id="adsRequestForm" action="${pageContext.request.contextPath}/member/ads/request.do" 
               method="post" enctype="multipart/form-data" 
               modelAttribute="boardVO"> <%-- BoardVO를 기본 modelAttribute로 설정 --%>
        
        <div class="card mb-4">
            <div class="card-header">광고 내용</div>
            <div class="card-body">
                <div class="form-group">
                    <label for="brdTitlNm">광고 제목 <span class="text-danger">*</span></label>
                    <form:input path="brdTitlNm" class="form-control" id="brdTitlNm" placeholder="광고 제목을 입력하세요" required="true"/>
                    <form:errors path="brdTitlNm" cssClass="text-danger"/>
                </div>
                <div class="form-group">
                    <label for="brdCont">광고 내용 <span class="text-danger">*</span></label>
                    <form:textarea path="brdCont" class="form-control" id="brdCont" rows="1" placeholder="광고 멘트를 입력해주세요" required="true"/>
                    <form:errors path="brdCont" cssClass="text-danger"/>
                </div>
            </div>
        </div>

        <div class="card mb-4">
            <div class="card-header">광고주 정보</div>
            <div class="card-body">
                <div class="form-group">
                    <label for="adsBp">사업장명 <span class="text-danger">*</span></label>
                    <%-- AdsClientVO 필드는 modelAttribute가 adsClientVO로 바인딩될 때 접근합니다. --%>
                    <input type="text" name="adsClientVO.adsBp" class="form-control" id="adsBp" placeholder="사업장명을 입력하세요" required="true"/>
                </div>
                <div class="form-group">
                    <label for="adsPic">담당자명 <span class="text-danger">*</span></label>
                    <input type="text" name="adsClientVO.adsPic" class="form-control" id="adsPic" placeholder="담당자명을 입력하세요" required="true"/>
                </div>
                <div class="form-group">
                    <label for="adsPicTelno">담당자 연락처 <span class="text-danger">*</span></label>
                    <input type="text" name="adsClientVO.adsPicTelno" class="form-control" id="adsPicTelno" placeholder="하이픈 없이 숫자만 입력하세요 (예: 01012345678)" required="true"/>
                </div>
                <div class="form-group">
                    <label for="adsReqPblsStartDt">희망 게재 시작일 <span class="text-danger">*</span></label>
                    <input type="date" name="adsClientVO.adsReqPblsStartDt" class="form-control" id="adsReqPblsStartDt" required="true"/>
                </div>
                <div class="form-group">
                    <label for="adsReqPblsEndDt">희망 게재 종료일 <span class="text-danger">*</span></label>
                    <input type="date" name="adsClientVO.adsReqPblsEndDt" class="form-control" id="adsReqPblsEndDt" required="true"/>
                </div>
            </div>
        </div>

        <%-- 파일 첨부 영역 --%>
        <div class="card mb-4">
            <div class="card-header">첨부 파일</div>
            <div class="card-body">
                <div class="form-group">
                    <label for="attachFiles">파일 선택</label>
                    <%-- multiple 속성을 추가하여 여러 파일 선택 가능하도록 함 --%>
                    <input type="file" class="form-control-file" id="attachFiles" name="attachFiles" multiple>
                    <h4></h4>
                    <small class="form-text text-muted">로고사진 1장, 메인 광고 사진 1장을 첨부해주세요!<br>이미지 (JPG, PNG), PDF 파일을 첨부할 수 있습니다.</small>
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
            <button type="submit" class="btn btn-primary mr-2">광고 요청</button>
            <button type="button" class="btn btn-secondary" onclick="location.href='${pageContext.request.contextPath}/'">취소</button>
        </div>
    </form:form>
</div>

<%-- JavaScript 파일 로드 --%>
<script src="${pageContext.request.contextPath}/app/js/main/ads/adsForm.js"></script>
</body>
</html>