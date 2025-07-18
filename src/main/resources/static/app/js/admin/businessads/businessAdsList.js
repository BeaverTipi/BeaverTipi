// src/main/webapp/app/js/admin/businessads/businessAdsList.js

// 날짜 포맷팅 헬퍼 함수
function formatDateString(dateString) {
    if (!dateString) return 'N/A';
    try {
        const [year, month, day] = dateString.split('-').map(Number);
        const date = new Date(year, month - 1, day);

        // 연, 월, 일을 추출하여 "YYYY-MM-DD" 형식으로 직접 조합
        const formattedYear = date.getFullYear();
        const formattedMonth = (date.getMonth() + 1).toString().padStart(2, '0'); // 월은 0부터 시작하므로 +1
        const formattedDay = date.getDate().toString().padStart(2, '0');

        return `${formattedYear}-${formattedMonth}-${formattedDay}`; // ⭐ 이 부분이 YYYY-MM-DD 형식으로 변경됩니다. ⭐

    } catch (e) {
        console.error("날짜 파싱 오류:", dateString, e);
        return dateString; // 파싱 실패 시 원본 문자열 반환
    }
}

// 날짜+시간 포맷팅 헬퍼 함수 (기존과 동일)
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

// 전화번호 포맷팅 헬퍼 함수 추가
function formatPhoneNumber(phoneNumber) {
    if (!phoneNumber) return 'N/A';
    // 숫자만 추출
    const cleaned = ('' + phoneNumber).replace(/\D/g, '');
    // 2-3자리 (지역번호/국번), 3-4자리 (국번), 4자리 (뒷자리) 패턴 매칭
    const match = cleaned.match(/^(\d{2,3})(\d{3,4})(\d{4})$/);
    if (match) {
        return `${match[1]}-${match[2]}-${match[3]}`;
    }
    return phoneNumber; // 매칭되지 않으면 원본 반환
}

// 페이지 로드 시 테이블의 날짜 및 담당자 연락처 컬럼 포맷팅
$(document).ready(function() {
    $('.ads-start-dt').each(function() {
        const dateVal = $(this).data('date');
        $(this).text(formatDateString(dateVal)); // formatDateString 변경으로 하이픈 적용
    });

    $('.ads-end-dt').each(function() {
        const dateVal = $(this).data('date');
        $(this).text(formatDateString(dateVal)); // formatDateString 변경으로 하이픈 적용
    });
    
    $('.ads-pic-telno').each(function() {
        const telNoVal = $(this).data('telno');
        $(this).text(formatPhoneNumber(telNoVal)); // formatDateString 변경으로 하이픈 적용
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
                // ⭐ 담당자 연락처에 formatPhoneNumber 함수 적용 ⭐
                $('#modalAdsPicTelno').text(formatPhoneNumber(data.adsClient ? data.adsClient.adsPicTelno : null));

                // ⭐ 희망 게재 시작일/종료일에 formatDateString 함수 적용 (함수 내부 변경으로 하이픈 적용) ⭐
                $('#modalAdsReqPblsStartDt').text(formatDateString(data.adsClient ? data.adsClient.adsReqPblsStartDt : null));
                $('#modalAdsReqPblsEndDt').text(formatDateString(data.adsClient ? data.adsClient.adsReqPblsEndDt : null));
                
                // ⭐ 작성일시는 서버에서 formattedBrdPblsDtm으로 받는다고 가정하고, 없으면 기존 brdPblsDtm으로 포맷팅 ⭐
                // 이 부분은 서버에서 formattedBrdPblsDtm을 보내주는 것이 가장 좋습니다.
                $('#modalBrdPblsDtm').text(data.formattedBrdPblsDtm || formatDateTimeString(data.brdPblsDtm));

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