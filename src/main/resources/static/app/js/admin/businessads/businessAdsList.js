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

        return `${formattedYear}-${formattedMonth}-${formattedDay}`; //  이 부분이 YYYY-MM-DD 형식으로 변경됩니다. 

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
    
     $('#resetBtn').on('click', function() {
        // 모든 텍스트/셀렉트 입력 필드 초기화
        $('#searchForm').find('input[type="text"], select').val('');

        // 숨겨진 page 필드를 1로 설정
        $('#currentPageNoInput').val('1');

        // 필요하다면 다른 숨겨진 검색 필드도 초기화
        // $('#searchRptCodeInput').val(''); // 예시 (만약 다른 숨겨진 필드가 있다면)

        // 폼 제출 (초기화된 상태로 다시 리스트를 불러옴)
        $('#searchForm').submit();
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
                $('#modalAdsStatusCodeSelect').val(data.adsClient ? data.adsClient.adsStatusCode : ''); 
                $('#modalAdsBp').text(data.adsClient ? data.adsClient.adsBp : 'N/A');
                $('#modalBrdTitlNm').text(data.brdTitlNm);
                $('#modalBrdCont').text(data.brdCont);
                $('#modalAdsPic').text(data.adsClient ? data.adsClient.adsPic : 'N/A');
                //  담당자 연락처에 formatPhoneNumber 함수 적용 
                $('#modalAdsPicTelno').text(formatPhoneNumber(data.adsClient ? data.adsClient.adsPicTelno : null));

                //  희망 게재 시작일/종료일에 formatDateString 함수 적용 (함수 내부 변경으로 하이픈 적용) 
                $('#modalAdsReqPblsStartDt').text(formatDateString(data.adsClient ? data.adsClient.adsReqPblsStartDt : null));
                $('#modalAdsReqPblsEndDt').text(formatDateString(data.adsClient ? data.adsClient.adsReqPblsEndDt : null));
                
                //  작성일시는 서버에서 formattedBrdPblsDtm으로 받는다고 가정하고, 없으면 기존 brdPblsDtm으로 포맷팅 
                // 이 부분은 서버에서 formattedBrdPblsDtm을 보내주는 것이 가장 좋습니다.
                $('#modalBrdPblsDtm').text(data.formattedBrdPblsDtm || formatDateTimeString(data.brdPblsDtm));

                $('#modalMbrCd').text(data.mbrCd);
                
				$('#adsDetailModal').data('brdNo', data.brdNo); 

            },
            error: function(xhr, status, error) {
                console.error("AJAX 실패:", status, error, xhr.responseText);
                alert("상세 정보를 가져오는 데 실패했습니다.");
            }
        });
    });
    
    // 저장 버튼 클릭 이벤트
    $('#saveAdsStatusBtn').on('click', function() {
	    const brdNo = $('#adsDetailModal').data('brdNo'); // 모달에 저장된 brdNo 가져오기
	    const newStatusCode = $('#modalAdsStatusCodeSelect').val(); // 변경된 상태 값 가져오기
	
	    if (!brdNo) {
	        alert("광고 번호를 찾을 수 없습니다.");
	        return;
	    }
	
	    if (confirm("정말 광고 상태를 바꾸시겠습니까?")) {
	        $.ajax({
	            url: contextPath + '/admin/businessAds/updateAdsStatus.do',
	            type: 'POST',
	            contentType: 'application/json',
	            data: JSON.stringify({
	                brdNo: brdNo,
	                adsStatusCode: newStatusCode
	            }),
	            //  dataType을 'json'으로 설정 
	            dataType: 'json',
	            success: function(response) {
	                console.log("서버 응답:", response); // {success: true, message: "..."} 또는 {success: false, message: "..."}
	                if (response.success) { //  응답 객체의 'success' 속성 확인 
	                    alert(response.message); // 서버에서 받은 메시지 표시
	                    $('#adsDetailModal').modal('hide'); // 모달 닫기
	                    $('#searchForm').submit(); // 리스트 새로고침
	                } else {
	                    alert("광고 상태 업데이트에 실패했습니다: " + response.message); // 실패 메시지 표시
	                }
	            },
	            error: function(xhr, status, error) {
	                console.error("광고 상태 업데이트 AJAX 실패:", status, error, xhr.responseText);
	                alert("광고 상태 업데이트 중 통신 오류가 발생했습니다.");
	            }
	        });
	    }
	});
});