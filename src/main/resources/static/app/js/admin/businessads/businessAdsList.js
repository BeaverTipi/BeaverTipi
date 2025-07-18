// src/main/webapp/app/js/admin/businessads/businessAdsList.js

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

// 모달 관련 JavaScript - 행 클릭 이벤트
$(document).ready(function() {
    $('.view-detail-btn').on('click', function() {
        const brdNo = $(this).data('brd-no');
        console.log("상세보기 클릭, 게시글 번호:", brdNo);

        $.ajax({
            url: contextPath + '/admin/businessAds/adsDetailModal.do',
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
                // $('#modalBrdVwCnt').text(data.brdVwCnt);

            },
            error: function(xhr, status, error) {
                console.error("AJAX 실패:", status, error, xhr.responseText);
                alert("상세 정보를 가져오는 데 실패했습니다.");
            }
        });
    });
});