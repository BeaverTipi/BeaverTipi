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

// 날짜+시간 포맷팅 헬퍼 함수
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

// 전화번호 포맷팅 헬퍼 함수
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

// --- 파일 미리보기 관련 전역 변수 (filePopup.js에서 가져옴) ---
let currentFileList = [];
let currentFileIds = [];
let currentIndex = 0;
let isRendering = false;
// --- 파일 미리보기 관련 전역 변수 끝 ---


// --- 파일 미리보기 관련 함수 (filePopup.js에서 가져옴) ---
function renderFileTable() {
    const tbody = document.querySelector("#adsDetailModal #fileTable tbody"); // 모달 내부의 테이블
    tbody.innerHTML = "";
    if (currentFileList.length === 0) {
        const row = document.createElement("tr");
        row.innerHTML = `<td colspan="2" class="text-center">첨부파일이 없습니다.</td>`;
        tbody.appendChild(row);
        return;
    }
    currentFileList.forEach(file => {
        const row = document.createElement("tr");
        row.innerHTML = `<td>${file.fileOriginalname || '파일명 없음'}</td><td>${file.fileSize || 0} bytes</td>`;
        tbody.appendChild(row);
    });
}

function fetchPdfOrImage(file) {
    const canvas = document.querySelector("#adsDetailModal #pdfCanvas");
    const ctx = canvas.getContext("2d");

    if (!file || !file.fileMime) {
        // 파일 정보가 없으면 캔버스 초기화
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        canvas.width = 1; // 캔버스 크기를 최소화
        canvas.height = 1;
        alert("유효하지 않은 파일 정보입니다.");
        isRendering = false;
        return;
    }
    if (isRendering) return;
    isRendering = true;

    if (file.fileMime === 'application/pdf') {
        fetchPdf(file.fileId);
    } else if (file.fileMime.startsWith('image/')) {
        renderImage(file.fileId);
    } else {
        ctx.clearRect(0, 0, canvas.width, canvas.height); // 캔버스 초기화
        canvas.width = 1;
        canvas.height = 1;
        alert("미리보기 지원하지 않는 파일 형식입니다.");
        isRendering = false;
    }
}

function fetchPdf(fileId) {
    const canvas = document.querySelector("#adsDetailModal #pdfCanvas");
    const ctx = canvas.getContext("2d");
    ctx.clearRect(0, 0, canvas.width, canvas.height); // PDF 로드 전 캔버스 클리어

    // axios 라이브러리가 필요합니다. <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
    // businessAdsList.jsp <head> 또는 <body> 하단에 추가해주세요.
    axios({
        method: 'get',
        url: contextPath + `/admin/business/file/preview/${fileId}`,
        responseType: 'blob'
    })
    .then(response => {
        const contentType = response.headers['content-type'];
        if (!contentType || !contentType.includes('pdf')) {
            throw new Error('PDF 파일이 아닙니다.');
        }
        const blob = response.data;
        const url = URL.createObjectURL(blob);
        // pdfjsLib는 businessAdsList.jsp에 로드되어 있어야 합니다.
        return pdfjsLib.getDocument(url).promise;
    })
    .then(pdf => pdf.getPage(1))
    .then(page => {
        const scale = 1.5;
        const viewport = page.getViewport({ scale });
        canvas.width = viewport.width;
        canvas.height = viewport.height;
        return page.render({ canvasContext: ctx, viewport }).promise;
    })
    .catch(err => {
        console.error("PDF 로딩 오류:", err);
        alert("PDF 미리보기 중 오류가 발생했습니다.");
        ctx.clearRect(0, 0, canvas.width, canvas.height); // 오류 시 캔버스 초기화
        canvas.width = 1;
        canvas.height = 1;
    })
    .finally(() => {
        updatePageIndicator();
        isRendering = false;
    });
}

function renderImage(fileId) {
    const canvas = document.querySelector("#adsDetailModal #pdfCanvas");
    const ctx = canvas.getContext("2d");
    ctx.clearRect(0, 0, canvas.width, canvas.height); // 이미지 로드 전 캔버스 클리어

    const img = new Image();
    img.onload = () => {
        const maxWidth = 800;
        const maxHeight = 600;

        let width = img.width;
        let height = img.height;

        if (width > maxWidth) {
            height = height * (maxWidth / width);
            width = maxWidth;
        }
        if (height > maxHeight) {
            width = width * (maxHeight / height);
            height = maxHeight;
        }
        // 캔버스 크기를 이미지에 맞게 조절
        canvas.width = width;
        canvas.height = height;
        ctx.drawImage(img, 0, 0, width, height);

        updatePageIndicator();
        isRendering = false;
    };
    img.onerror = () => {
        alert("이미지 로드 실패");
        ctx.clearRect(0, 0, canvas.width, canvas.height); // 오류 시 캔버스 초기화
        canvas.width = 1;
        canvas.height = 1;
        isRendering = false;
    };
    // contextPath를 사용하여 절대 경로로 요청
    img.src = contextPath + `/admin/business/file/preview/${fileId}`;
}

function nextFile() { // 함수명 충돌 방지를 위해 next -> nextFile 변경
    if (currentIndex < currentFileIds.length - 1) {
        currentIndex++;
        fetchPdfOrImage(currentFileList[currentIndex]);
    }
}

function prevFile() { // 함수명 충돌 방지를 위해 prev -> prevFile 변경
    if (currentIndex > 0) {
        currentIndex--;
        fetchPdfOrImage(currentFileList[currentIndex]);
    }
}

function updatePageIndicator() {
    const indicator = document.querySelector("#adsDetailModal #fileIndex");
    const total = document.querySelector("#adsDetailModal #totalCount");
    if (indicator && total) {
        indicator.innerText = currentFileIds.length > 0 ? currentIndex + 1 : 0;
        total.innerText = currentFileIds.length;
    }
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
				
				 // 파일 미리보기 초기화 로직 추가
                const fileDataHolder = document.querySelector("#adsDetailModal #fileDataHolder");
                const fileListJson = JSON.stringify(data.attachFiles || []); // attachFiles가 null일 경우 빈 배열
                fileDataHolder.setAttribute("data-filelist", fileListJson);

                // 파일 미리보기 전역 변수 초기화
                currentFileList = JSON.parse(fileListJson);
                currentFileIds = currentFileList.map(f => f.fileId);
                currentIndex = 0;
                isRendering = false; // 렌더링 상태 초기화

                // 파일 미리보기 UI 초기화 및 첫 파일 렌더링
                renderFileTable(); // 파일 목록 테이블 렌더링
                updatePageIndicator(); // 페이지 인디케이터 업데이트

                // 파일이 있을 경우 첫 번째 파일 미리보기
                if (currentFileIds.length > 0) {
                    fetchPdfOrImage(currentFileList[currentIndex]);
                } else {
                    // 파일이 없을 경우 캔버스 초기화
                    const canvas = document.querySelector("#adsDetailModal #pdfCanvas");
                    const ctx = canvas.getContext("2d");
                    ctx.clearRect(0, 0, canvas.width, canvas.height);
                    canvas.width = 1;
                    canvas.height = 1;
                }

                // 파일 미리보기 컨트롤 버튼 이벤트 리스너 재등록 (모달이 열릴 때마다)
                // 기존에 이미 등록되어 있을 수 있으므로, .off()로 제거 후 .on()으로 다시 등록
                $('#adsDetailModal #prevBtn').off('click').on('click', prevFile);
                $('#adsDetailModal #nextBtn').off('click').on('click', nextFile);
                
                const toggleBtn = document.querySelector("#adsDetailModal #toggleFileListBtn");
                const fileTable = document.querySelector("#adsDetailModal #fileTable");
                // 초기 상태는 숨김
                fileTable.style.display = "none";
                toggleBtn.innerText = "첨부파일 목록 보기";

                $(toggleBtn).off('click').on('click', () => {
                    if (fileTable.style.display === "none" || fileTable.style.display === "") {
                        fileTable.style.display = "table";
                        toggleBtn.innerText = "첨부파일 목록 숨기기";
                    } else {
                        fileTable.style.display = "none";
                        toggleBtn.innerText = "첨부파일 목록 보기";
                    }
                });

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
	
	// 모달이 닫힐 때 파일 미리보기 상태 초기화
    $('#adsDetailModal').on('hidden.bs.modal', function () {
        currentFileList = [];
        currentFileIds = [];
        currentIndex = 0;
        isRendering = false;
        // 캔버스 내용 지우기
        const canvas = document.querySelector("#adsDetailModal #pdfCanvas");
        const ctx = canvas.getContext("2d");
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        canvas.width = 1; // 캔버스 크기 초기화
        canvas.height = 1;
        renderFileTable(); // 파일 목록 테이블도 초기화
        updatePageIndicator(); // 페이지 인디케이터 초기화
    });
});