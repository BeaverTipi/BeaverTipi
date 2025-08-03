<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
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
    <h2 class="mb-4">신고 작성</h2><button id="writeDummyBtn">매물</button><button id="writeBtn">회원</button>
    
    <%-- multipart/form-data 인코딩 타입 설정 및 commandName 지정 --%>
    <form:form id="createReportForm" action="${pageContext.request.contextPath}/member/report/create" 
               method="post" enctype="multipart/form-data" 
               modelAttribute="reportVO"> <%-- ReportVO를 modelAttribute로 설정 --%>
        
        <div class="card mb-4">
            <div class="card-header">신고 내용</div>
            <div class="card-body">
                <div class="form-group">
                    <label for="brdTitlNm">신고 제목 <span class="text-danger">*</span></label>
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
					<!-- 신고 유형이 이미 설정되어 있다면 disabled 및 사용자에게 표시 -->
                    <c:set var="isRptCodePredefined" value="${not empty reportVO.rptCode}"/>
                    
                    <form:select path="rptCode" class="form-control" id="rptCode" required="true">					    
                        <form:option value="MEMB" label="회원"/>
                        <form:option value="LSTG" label="매물"/>
                    </form:select>
                    <form:errors path="rptCode" cssClass="text-danger"/>
                    <c:if test="${isRptCodePredefined}">
                        <small class="form-text text-muted">신고 유형이 자동으로 선택되었습니다.</small>
                        <input type="hidden" name="rptCode" value="${reportVO.rptCode}"/>
                    </c:if>
                </div>
                <div class="form-group" id="rptTargetIdGroup" 
                     style="display: ${reportVO.rptCode eq 'MEMB' ? 'block' : 'none'};">
                    <label for="rptTargetId">신고 대상 <span class="text-danger">*</span></label>
                    <c:set var="isRptTargetIdPredefined" value="${not empty reportVO.rptTargetId}" />
   
                    <form:input path="rptTargetId" class="form-control" id="rptTargetId"
				                placeholder="신고 대상의 ID를 입력하세요"
				                required="true"/>
                    <form:errors path="rptTargetId" cssClass="text-danger"/>
                    <c:if test="${isRptTargetIdPredefined}">
                        <small class="form-text text-muted">신고 대상이 자동으로 입력되었습니다.</small>
                    </c:if>
                </div>
                
                
			    <!--신고 대상 이름 입력 필드 (LSTG 유형일 때만 표시) -->
                <div class="form-group" id="rptTargetNmGroup" 
                     style="display: ${reportVO.rptCode eq 'LSTG' ? 'block' : 'none'};">
                    <label for="rptTargetNm">신고할 매물명 <span class="text-danger">*</span></label>
                    <c:set var="isRptTargetNmPredefined" value="${reportVO.rptCode eq 'LSTG' and not empty rptTargetNmFromUrl}" />
					<!-- rptTargetNm을 form:input으로 직접 바인딩 -->
                    <form:input path="rptTargetNm" class="form-control" id="rptTargetNm"
                                placeholder="신고할 매물명을 입력하세요"
                                required="true"/>
                    <form:errors path="rptTargetNm" cssClass="text-danger"/>
                    <c:if test="${isRptTargetNmPredefined}">
                        <small class="form-text text-muted">신고 대상 이름(매물명)이 자동으로 입력되었습니다.</small>
                    </c:if>
                </div>
            </div>
        </div>

        <div class="card mb-4">
            <div class="card-header">증거 자료 첨부</div>
            <div class="card-body">
                <div class="form-group">
                    <label for="attachFiles">파일 선택</label>
					<!-- multiple 속성을 추가하여 여러 파일 선택 가능하도록 했음 -->
                    <input type="file" class="form-control-file" id="attachFiles" name="attachFiles" multiple>
                    <h4></h4>
                    <small class="form-text text-muted">신고 내용을 뒷받침할 이미지, PDF 등 증거 자료를 첨부해주세요.</small>
                </div>

                <div class="file-preview-area mt-3">
                    <div class="file-list-section">
                        <h5>첨부 예정 파일 목록</h5>
                        <table border="1" id="fileTable" class="table table-bordered table-sm">
                            <thead>
                                <tr><th>파일명</th><th>크기</th><th></th></tr>
                            </thead>
                            <tbody>

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

<script>
$(function(){
	var isRptCodePredefined = ${isRptCodePredefined};
	$("#rptCode").prop("disabled", isRptCodePredefined);
	
	var isRptTargetIdPredefined = ${isRptTargetIdPredefined};
    $("#rptTargetId").prop("readonly", isRptTargetIdPredefined);
    
	var isRptTargetNmPredefined = ${isRptTargetNmPredefined};
    $("#rptTargetNm").prop("readonly", isRptTargetNmPredefined);
    
    var rptCodeFromModel = "${reportVO.rptCode}"; // 컨트롤러에서 넘겨받은 rptCode 값
    var targetIdFromUrl = "${targetIdFromUrl}"; // URL 파라미터 targetId (실제 ID)
    var rptTargetNmFromUrl = "${rptTargetNmFromUrl}"; // URL 파라미터 rptTargetNm (매물명);

    // 초기 로드 시 필드 가시성 및 값 설정
    if (rptCodeFromModel === 'MEMB') {
        $("#rptTargetId").val(targetIdFromUrl).prop("readonly", targetIdFromUrl !== "");
        $("#rptTargetNmGroup").hide(); // rptTargetNmGroup 숨김
        $("#rptTargetNm").prop("required", false); // rptTargetNm 필수 해제
    } else if (rptCodeFromModel === 'LSTG') {
        $("#rptTargetIdGroup").hide(); // rptTargetIdGroup 숨김
        $("#rptTargetNmGroup").show(); // rptTargetNmGroup 보임
        $("#rptTargetNm").val(rptTargetNmFromUrl).prop("readonly", rptTargetNmFromUrl !== "").prop("required", true); // rptTargetNm에 값 설정 및 필수 설정
    } else {
        // 기본 상태 (선택되지 않았을 때)
        $("#rptTargetNmGroup").hide(); // 기본적으로 rptTargetNmGroup 숨김
        $("#rptTargetNm").prop("required", false); // rptTargetNm 필수 해제
        $("#rptTargetId").prop("readonly", false); // rptTargetId readonly 해제
    }

    // 신고 유형(rptCode) 변경 시 동적 처리 (사용자가 직접 선택 가능할 때)
    $("#rptCode").on('change', function() {
        var selectedRptCode = $(this).val();
        // rptTargetId 필드는 항상 보이므로 값과 readonly만 변경
        $("#rptTargetId").val(""); // 신고 유형 변경 시 ID 초기화
        $("#rptTargetId").prop("readonly", false).prop("required", true); 

        if (selectedRptCode === 'MEMB') {
            $("#rptTargetNmGroup").hide();
            $("#rptTargetNm").val("").prop("required", false).prop("readonly", false);
        } else if (selectedRptCode === 'LSTG') {
            $("#rptTargetNmGroup").show();
            $("#rptTargetNm").val("").prop("required", true).prop("readonly", false);
        } else {
            // 다른 값 선택 시 (만약 있다면) - 기본 상태로 복원
            $("#rptTargetNmGroup").hide();
            $("#rptTargetNm").val("").prop("required", false).prop("readonly", false);
        }
    });

    // 폼 제출 시 required 속성 관리 (중요)
    $("#createReportForm").on('submit', function() {
        var currentRptCode = $("#rptCode").val();
        if (currentRptCode === 'MEMB') {
            $("#rptTargetId").prop("required", true);
            $("#rptTargetNm").prop("required", false); // 매물명 필드는 필수 아님
        } else if (currentRptCode === 'LSTG') {
            $("#rptTargetId").prop("required", true); // 매물 ID도 필수
            $("#rptTargetNm").prop("required", true); // 매물명 필드 필수
        }
        return true; // 폼 제출 계속
    });
}) 
</script>


<script src="${pageContext.request.contextPath}/app/js/main/report/createReport.js"></script>
</body>
</html>