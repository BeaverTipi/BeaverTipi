<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %> <%-- ⭐ 이 태그 라이브러리 사용 ⭐ --%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%-- fmt 태그 라이브러리 제거 또는 사용하지 않음 (이전 해결책에 따라) --%>
<%-- <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> --%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>광고 관리</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/admin/common_admin.css">
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <style>
        /* 기존 스타일 유지 및 userList.css와 유사하게 조정 */
        .board-title {
            text-align: center;
            margin-top: 30px;
            margin-bottom: 30px;
            color: #333;
            font-size: 2em;
        }
        .container-wrapper {
            display: flex;
            justify-content: center;
            width: 100%;
            padding: 20px 0;
        }
        .container {
            width: 90%; /* userList.jsp의 main.container와 유사하게 너비 조정 */
            max-width: 1200px; /* 최대 너비 설정 */
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .search-area {
            margin-bottom: 20px;
            padding: 15px;
            border: 1px solid #ddd;
            background-color: #f9f9f9;
            border-radius: 5px;
        }
        .search-form .search-conditions {
            display: flex;
            flex-wrap: wrap;
            gap: 15px; /* 검색 항목 간의 간격 */
            margin-bottom: 15px;
        }
        .search-form .search-item {
            display: flex;
            align-items: center;
        }
        .search-form label {
            font-weight: bold;
            margin-right: 10px;
            min-width: 80px; /* 라벨 너비 고정 */
        }
        .search-form .input-field,
        .search-form .select-field {
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 4px;
            width: 200px; /* 적절히 조절 */
            box-sizing: border-box; /* 패딩, 보더 포함 너비 계산 */
        }
        .search-form .date-range-group {
            display: flex;
            align-items: center;
            gap: 5px;
        }
        .search-button-area {
            text-align: right; /* 버튼을 오른쪽으로 정렬 */
        }
        .search-button-area button {
            padding: 8px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
            margin-left: 10px;
        }
        .btn-primary { background-color: #007bff; color: white; }
        .btn-primary:hover { background-color: #0056b3; }
        .btn-secondary { background-color: #6c757d; color: white; }
        .btn-secondary:hover { background-color: #5a6268; }
        .btn-warning { background-color: #ffc107; color: #212529; }
        .btn-warning:hover { background-color: #e0a800; }
        .btn-dark { background-color: #343a40; color: white; }
        .btn-dark:hover { background-color: #23272b; }

        .table-container {
            width: 100%;
            overflow-x: auto;
            margin-top: 20px;
        }
        .table { /* Bootstrap table class */
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        .table th, .table td {
            border: 1px solid #dee2e6;
            padding: 10px;
            text-align: center;
            vertical-align: middle;
        }
        .table thead th {
            background-color: #e9ecef;
            font-weight: bold;
        }
        .table tbody tr:hover {
            background-color: #f2f2f2;
        }
        .no-data-center {
            text-align: center;
            padding: 20px;
            color: #777;
        }

        .pagination-wrapper {
            margin-top: 20px;
            text-align: center;
        }
        /* Bootstrap pagination classes will handle this */
        .pagination-wrapper .pagination .page-item .page-link {
            color: #007bff;
        }
        .pagination-wrapper .pagination .page-item.active .page-link {
            background-color: #007bff;
            border-color: #007bff;
            color: white;
        }
        /* 모달 상세 내용 스타일 */
        #adsDetailModal .modal-body table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }
        #adsDetailModal .modal-body th,
        #adsDetailModal .modal-body td {
            border: 1px solid #eee;
            padding: 8px;
            text-align: left;
        }
        #adsDetailModal .modal-body th {
            background-color: #f8f8f8;
            width: 120px;
        }
        #adsDetailModal .modal-body .full-content {
            white-space: pre-wrap; /* 줄 바꿈 및 공백 유지 */
        }
    </style>
</head>
<body>

<h2 class="board-title">광고 관리</h2>

<div class="container-wrapper">
  <main class="container">

    <%-- 검색 폼 --%>
    <div class="search-area">
      <%-- ⭐ form:form 태그로 변경 ⭐ --%>
      <form:form modelAttribute="detailSearch" action="${pageContext.request.contextPath}/admin/businessAds/businessAdsList" method="get" id="searchForm" class="search-form">
        <input type="hidden" name="page" value="${pagingVO.currentPageNo}" id="currentPageNoInput">

        <div class="search-conditions">
          <div class="search-item">
            <label for="searchAdsStatusCode">광고 상태:</label>
            <%-- ⭐ form:select 태그 사용 ⭐ --%>
            <form:select path="searchAdsStatusCode" id="searchAdsStatusCode" class="select-field">
                <form:option value="">전체</form:option>
                <form:option value="ADS001" label="심사대기"/>
                <form:option value="ADS002" label="심사반려"/>
                <form:option value="ADS003" label="게재중"/>
                <form:option value="ADS004" label="게재종료"/>
            </form:select>
          </div>

          <div class="search-item">
            <label for="searchAdsBp">사업장명:</label>
            <%-- ⭐ form:input 태그 사용 ⭐ --%>
            <form:input path="searchAdsBp" id="searchAdsBp" placeholder="사업장명" class="input-field"/>
          </div>

          <div class="search-item">
            <label for="searchAdsPic">담당자명:</label>
            <%-- ⭐ form:input 태그 사용 ⭐ --%>
            <form:input path="searchAdsPic" id="searchAdsPic" placeholder="담당자명" class="input-field"/>
          </div>

          <div class="search-item">
            <label for="searchAdsPicTelno">담당자 연락처:</label>
            <%-- ⭐ form:input 태그 사용 ⭐ --%>
            <form:input path="searchAdsPicTelno" id="searchAdsPicTelno" placeholder="담당자 연락처" class="input-field"/>
          </div>
        </div>

        <div class="search-button-area">
          <button type="reset" id="resetBtn" class="btn btn-warning">초기화</button>
          <button type="submit" id="searchBtn" class="btn btn-dark">검색</button>
        </div>
      </form:form> <%-- ⭐ 닫는 태그도 form:form으로 변경 ⭐ --%>
    </div>

    <%-- 광고 목록 테이블 (이하 동일) --%>
    <div class="table-container">
        <table class="table" id="businessAdsTable">
            <thead>
                <tr>
                    <th>번호</th>
                    <th>광고 상태</th>
                    <th>사업장명</th>
                    <th>광고 제목</th>
                    <th>광고 내용</th>
                    <th>담당자명</th>
                    <th>담당자 연락처</th>
                    <th>게재 시작일</th>
                    <th>게재 종료일</th>
                    <th>조회수</th>
                    <th>상세보기</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty businessAdsList}">
                        <c:forEach var="ad" items="${businessAdsList}" varStatus="status">
                            <tr class="ads-row" data-brd-no="${ad.brdNo}">
                                <td>${pagingVO.firstRecordIndex + status.index + 1}</td>
                                <td>${ad.adsClient.adsStatusCode}</td>
                                <td>${ad.adsClient.adsBp}</td>
                                <td>${ad.brdTitlNm}</td>
                                <td>${ad.brdCont}</td>
                                <td>${ad.adsClient.adsPic}</td>
                                <td>${ad.adsClient.adsPicTelno}</td>
                                <td class="ads-start-dt" data-date="${ad.adsClient.adsReqPblsStartDt}"></td>
                                <td class="ads-end-dt" data-date="${ad.adsClient.adsReqPblsEndDt}"></td>
                                <td>${ad.brdVwCnt}</td>
                                <td>
                                    <button type="button" class="btn btn-info btn-sm view-detail-btn" data-toggle="modal" data-target="#adsDetailModal" data-brd-no="${ad.brdNo}">상세</button>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="11" class="no-data-center">조회된 광고 내역이 없습니다.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>

    <%-- 페이징 영역 (이하 동일) --%>
    <div class="pagination-wrapper">
        <nav aria-label="Page navigation example">
            <ul class="pagination justify-content-center">
                ${pagingHTML}
            </ul>
        </nav>
    </div>

  </main>
</div>

<div class="modal fade" id="adsDetailModal" tabindex="-1" aria-labelledby="adsDetailModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="adsDetailModalLabel">광고 상세 정보</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <table class="table table-bordered">
                    <tbody>
                        <tr>
                            <th>광고 번호</th>
                            <td id="modalBrdNo"></td>
                        </tr>
                        <tr>
                            <th>광고 상태</th>
                            <td id="modalAdsStatusCode"></td>
                        </tr>
                        <tr>
                            <th>사업장명</th>
                            <td id="modalAdsBp"></td>
                        </tr>
                        <tr>
                            <th>광고 제목</th>
                            <td id="modalBrdTitlNm"></td>
                        </tr>
                        <tr>
                            <th>광고 내용</th>
                            <td id="modalBrdCont" class="full-content"></td>
                        </tr>
                        <tr>
                            <th>담당자명</th>
                            <td id="modalAdsPic"></td>
                        </tr>
                        <tr>
                            <th>담당자 연락처</th>
                            <td id="modalAdsPicTelno"></td>
                        </tr>
                        <tr>
                            <th>게재 시작일</th>
                            <td id="modalAdsReqPblsStartDt"></td>
                        </tr>
                        <tr>
                            <th>게재 종료일</th>
                            <td id="modalAdsReqPblsEndDt"></td>
                        </tr>
                        <tr>
                            <th>작성자 ID</th>
                            <td id="modalMbrCd"></td>
                        </tr>
                        <tr>
                            <th>작성일시</th>
                            <td id="modalBrdPblsDtm"></td>
                        </tr>
                        <tr>
                            <th>조회수</th>
                            <td id="modalBrdVwCnt"></td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script>
    // 날짜 포맷팅 헬퍼 함수
    function formatDateString(dateString) {
        if (!dateString) return 'N/A';
        try {
            const [year, month, day] = dateString.split('-').map(Number);
            const date = new Date(year, month - 1, day);
            return date.toLocaleDateString('ko-KR', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit'
            });
        } catch (e) {
            console.error("날짜 파싱 오류:", dateString, e);
            return dateString;
        }
    }

    function formatDateTimeString(dateTimeString) {
        if (!dateTimeString) return 'N/A';
        try {
            const date = new Date(dateTimeString);
            return date.toLocaleString('ko-KR', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
                hour: '2-digit',
                minute: '2-digit',
                second: '2-digit',
                hour12: false
            });
        } catch (e) {
            console.error("날짜시간 파싱 오류:", dateTimeString, e);
            return dateTimeString;
        }
    }


    // 페이지 로드 시 테이블의 날짜 컬럼 포맷팅
    $(document).ready(function() {
        $('.ads-start-dt').each(function() {
            const dateVal = $(this).data('date');
            $(this).text(formatDateString(dateVal));
        });

        $('.ads-end-dt').each(function() {
            const dateVal = $(this).data('date');
            $(this).text(formatDateString(dateVal));
        });
    });


    function fn_paging(pageNo) {
        $('#currentPageNoInput').val(pageNo);
        $('#searchForm').submit();
    }

    // ⭐ 모달 관련 JavaScript ⭐
    $(document).ready(function() {
        $('.view-detail-btn').on('click', function() {
            const brdNo = $(this).data('brd-no');
            console.log("상세보기 클릭, 게시글 번호:", brdNo);

            $.ajax({
                url: '${pageContext.request.contextPath}/admin/businessAds/adsDetailModal.do',
                type: 'GET',
                data: { brdNo: brdNo },
                dataType: 'json',
                success: function(data) {
                    console.log("AJAX 성공, 데이터:", data);
                    $('#modalBrdNo').text(data.brdNo);
                    $('#modalAdsStatusCode').text(data.adsClient ? data.adsClient.adsStatusCode : 'N/A');
                    $('#modalAdsBp').text(data.adsClient ? data.adsClient.adsBp : 'N/A');
                    $('#modalBrdTitlNm').text(data.brdTitlNm);
                    $('#modalBrdCont').text(data.brdCont);
                    $('#modalAdsPic').text(data.adsClient ? data.adsClient.adsPic : 'N/A');
                    $('#modalAdsPicTelno').text(data.adsClient ? data.adsClient.adsPicTelno : 'N/A');

                    $('#modalAdsReqPblsStartDt').text(formatDateString(data.adsClient ? data.adsClient.adsReqPblsStartDt : null));
                    $('#modalAdsReqPblsEndDt').text(formatDateString(data.adsClient ? data.adsClient.adsReqPblsEndDt : null));
                    $('#modalBrdPblsDtm').text(formatDateTimeString(data.brdPblsDtm));

                    $('#modalMbrCd').text(data.mbrCd);
                    $('#modalBrdVwCnt').text(data.brdVwCnt);

                },
                error: function(xhr, status, error) {
                    console.error("AJAX 실패:", status, error, xhr.responseText);
                    alert("상세 정보를 가져오는 데 실패했습니다.");
                }
            });
        });
    });
</script>
</body>
</html>