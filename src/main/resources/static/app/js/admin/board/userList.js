// userList.js

// --- 헬퍼 함수 (날짜, 전화번호 포맷팅) ---
function formatDateString(dateString) {
    if (!dateString) return 'N/A';
    try {
        const [year, month, day] = dateString.split('-').map(Number);
        const date = new Date(year, month - 1, day);
        const formattedYear = date.getFullYear();
        const formattedMonth = (date.getMonth() + 1).toString().padStart(2, '0');
        const formattedDay = date.getDate().toString().padStart(2, '0');
        return `${formattedYear}-${formattedMonth}-${formattedDay}`;
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

function formatPhoneNumber(phoneNumber) {
    if (!phoneNumber) return 'N/A';
    const cleaned = ('' + phoneNumber).replace(/\D/g, '');
    const match = cleaned.match(/^(\d{2,3})(\d{3,4})(\d{4})$/);
    if (match) {
        return `${match[1]}-${match[2]}-${match[3]}`;
    }
    return phoneNumber;
}


// --- 파일 미리보기 관련 전역 변수 ---
let currentFileList = [];
let currentIndex = 0;
let isRendering = false;

// --- 파일 미리보기 관련 함수 (businessAdsList.js에서 가져옴, #adsDetailModal -> #reportDetailModal 변경) ---
function renderFileTable() {
    // #reportDetailModal 내의 #fileTable tbody를 선택
    const tbody = document.querySelector("#reportDetailModal #fileTable tbody");
    tbody.innerHTML = ""; // 기존 내용 초기화

    if (currentFileList.length === 0) {
        const row = document.createElement("tr");
        row.innerHTML = `<td colspan="2" class="text-center">첨부파일이 없습니다.</td>`;
        tbody.appendChild(row);
        return;
    }

    currentFileList.forEach(file => {
        const row = document.createElement("tr");
        // 파일명과 크기를 표시
        row.innerHTML = `<td>${file.fileOriginalname || '파일명 없음'}</td><td>${file.fileSize || 0} bytes</td>`;
        tbody.appendChild(row);
    });
}

function fetchPdfOrImage(file) {
    // #reportDetailModal 내의 #pdfCanvas를 선택
    const canvas = document.querySelector("#reportDetailModal #pdfCanvas");
    const ctx = canvas.getContext("2d");

    if (!file || !file.fileMime) {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        canvas.width = 1;
        canvas.height = 1;
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
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        canvas.width = 1;
        canvas.height = 1;
        alert("미리보기를 지원하지 않는 파일 형식입니다.");
        isRendering = false;
    }
}

function fetchPdf(fileId) {
    // #reportDetailModal 내의 #pdfCanvas를 선택
    const canvas = document.querySelector("#reportDetailModal #pdfCanvas");
    const ctx = canvas.getContext("2d");
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    axios({
        method: 'get',
        // 신고 관련 파일 미리보기 URL로 변경
        url: contextPath + `/admin/report/file/preview/${fileId}`,
        responseType: 'blob'
    })
    .then(response => {
        const contentType = response.headers['content-type'];
        if (!contentType || !contentType.includes('pdf')) {
            throw new Error('PDF 파일이 아닙니다.');
        }
        const blob = response.data;
        const url = URL.createObjectURL(blob);
        return pdfjsLib.getDocument({ url: url }).promise;
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
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        canvas.width = 1;
        canvas.height = 1;
    })
    .finally(() => {
        updatePageIndicator();
        isRendering = false;
    });
}

function renderImage(fileId) {
    // #reportDetailModal 내의 #pdfCanvas를 선택
    const canvas = document.querySelector("#reportDetailModal #pdfCanvas");
    const ctx = canvas.getContext("2d");
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    const img = new Image();
    img.onload = () => {
        const maxWidth = 800; // 최대 너비 (조절 가능)
        const maxHeight = 600; // 최대 높이 (조절 가능)

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

        canvas.width = width;
        canvas.height = height;
        ctx.drawImage(img, 0, 0, width, height);

        updatePageIndicator();
        isRendering = false;
    };
    img.onerror = () => {
        alert("이미지 로드 실패");
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        canvas.width = 1;
        canvas.height = 1;
        isRendering = false;
    };
    // 신고 관련 파일 미리보기 URL로 변경
    img.src = contextPath + `/admin/report/file/preview/${fileId}`;
}

function nextFile() {
    if (currentIndex < currentFileList.length - 1) {
        currentIndex++;
        fetchPdfOrImage(currentFileList[currentIndex]);
    }
}

function prevFile() {
    if (currentIndex > 0) {
        currentIndex--;
        fetchPdfOrImage(currentFileList[currentIndex]);
    }
}

function updatePageIndicator() {
    // #reportDetailModal 내의 #fileIndex와 #totalCount를 선택
    const indicator = document.querySelector("#reportDetailModal #fileIndex");
    const total = document.querySelector("#reportDetailModal #totalCount");
    if (indicator && total) {
        indicator.innerText = currentFileList.length > 0 ? currentIndex + 1 : 0;
        total.innerText = currentFileList.length;
    }
}


document.addEventListener('DOMContentLoaded', function() {
    const searchForm = document.getElementById('searchForm');
    const resetButton = document.getElementById('resetBtn');
    const currentPageNoInput = document.getElementById('currentPageNoInput');
    const searchRptCodeInput = document.getElementById('searchRptCodeInput');

    const labelReportedTargetId = document.getElementById('labelReportedTargetId');
    const searchReportedTargetIdInput = document.getElementById('searchReportedTargetId');
    const reportedTargetHeader = document.getElementById('reportedTarget');

    // --- 유틸리티 함수 ---
    function updateReportedTargetLabels(rptCode) {
        let labelText = '피신고 ID';
        let placeholderText = '피신고 ID';
        let thText = '신고된 대상';

        if (rptCode === 'MEMB') {
            labelText = '피신고자ID';
            placeholderText = '피신고자ID';
            thText = '피신고자 ID';
        } else if (rptCode === 'LSTG') {
            labelText = '피신고매물 ID';
            placeholderText = '피신고매물 ID';
            thText = '피신고매물 ID';
        }

        if (labelReportedTargetId) {
            labelReportedTargetId.textContent = labelText;
        }
        if (searchReportedTargetIdInput) {
            searchReportedTargetIdInput.placeholder = placeholderText;
        }
        if (reportedTargetHeader) {
            reportedTargetHeader.textContent = thText;
        }
    }

    // --- 이벤트 리스너 설정 ---

    window.fn_paging = function(pageNo) {
        if (currentPageNoInput) {
            currentPageNoInput.value = pageNo;
        }
        searchForm.submit();
    };

    if (searchForm) {
        searchForm.addEventListener('submit', function(event) {
            // 여기에 폼 제출 관련 추가 로직이 필요하다면 작성
            // 현재는 특별히 추가할 내용이 없어서 주석 처리
        });
    }

    if (resetButton) {
        resetButton.addEventListener('click', function() {
            document.getElementById('searchTitle').value = '';
            document.getElementById('searchWriter').value = '';
            document.getElementById('searchReportedTargetId').value = '';
            document.getElementById('brdPblsDtmFrom').value = '';
            document.getElementById('brdPblsDtmTo').value = '';
            document.getElementById('searchRptStatusCode').value = '';

            if (currentPageNoInput) {
                currentPageNoInput.value = 1;
            }

            const activeTab = document.querySelector('#reportTabs .nav-link.active');
            const currentRptCode = activeTab ? activeTab.dataset.rptCode : 'MEMB';
            updateReportedTargetLabels(currentRptCode);

            searchForm.submit();
        });
    }

    $('#reportTabs .nav-link').on('shown.bs.tab', function(e) {
        const rptCode = $(this).data('rpt-code');
        searchRptCodeInput.value = rptCode;
        currentPageNoInput.value = 1;

        updateReportedTargetLabels(rptCode);

        searchForm.submit();
    });

    const initialActiveTab = document.querySelector('#reportTabs .nav-link.active');
    const initialRptCode = initialActiveTab ? initialActiveTab.dataset.rptCode : 'MEMB';
    updateReportedTargetLabels(initialRptCode);


    $(document).on('click', '.report-row', function(e) {
        if ($(e.target).closest('button, select').length) {
            return;
        }

        e.preventDefault();
        const reportId = $(this).data('report-id');

        axios.get(`${contextPath}/axios/admin/report/detail/${reportId}`) // contextPath 사용
            .then(response => {
                const data = response.data; // data는 이제 ReportVO 객체입니다.
                console.log("신고 상세 정보:", data); // 데이터 확인용 로그

                $('#modalReportId').text(data.rptId || 'null');
                // ReportVO에 brdTitlNm이 직접 있다면 data.brdTitlNm, boardVO 내에 있다면 data.boardVO.brdTitlNm 확인 필요
                $('#modalBrdTitlNm').text(data.brdTitlNm  ? data.brdTitlNm : '제목 없음');
                $('#modalBrdCont').html(data.brdCont ? data.brdCont.replace(/\n/g, '<br>') : '내용 없음');

                const isListingReport = (data.rptCode === 'LSTG');
                const $modalTargetIdLabel = $('#modalTargetIdLabel');
                const $modalRptTargetId = $('#modalRptTargetId'); // 이 요소를 변경합니다.

                const $memberSpecificInfo = $('#memberSpecificInfo');
                const $modalNewMbrStatus = $('#modalNewMbrStatus');
                const $listingSpecificInfo = $('#listingSpecificInfo');
                const $modalNewLtsgDel = $('#modalNewLtsgDel');

                $memberSpecificInfo.hide();
                $listingSpecificInfo.hide();

                // --- 매물 신고일 경우 링크 추가 로직 시작 ---
                if (isListingReport) {
                    $modalTargetIdLabel.text('피신고매물 ID : ');
                    const lstgId = data.rptTargetId; // 매물 ID를 가져옴

                    // <span id="modalRptTargetId"> 대신 클릭 가능한 <a> 태그를 생성
                    const listingLinkHtml = `<a href="#" class="listing-detail-link" data-lstg-id="${lstgId}">${lstgId || 'N/A'}</a>`;
                    $modalRptTargetId.html(listingLinkHtml); // HTML로 설정
                    $modalRptTargetId.data('lstg-id', lstgId); // jQuery data()에도 저장

                    // 동적으로 생성된 <a> 태그(클래스: .listing-detail-link)에 클릭 이벤트 리스너 추가
                    // .off()를 사용하여 기존 이벤트 리스너 중복 등록 방지
                    $modalRptTargetId.off('click', '.listing-detail-link').on('click', '.listing-detail-link', function(event) {
                        event.preventDefault(); // 링크 기본 동작(페이지 이동) 방지
                        event.stopPropagation(); // 부모 요소 클릭 이벤트(report-row) 전파 방지
                        const clickedLstgId = $(this).data('lstg-id');
                        console.log("매물 상세 모달 열기 요청:", clickedLstgId);

                        // mainKakaoMap.js 또는 listRenderer.js에 정의된 window.openDetailModal 함수 호출
                        if (typeof window.openDetailModal === 'function') {
                            window.openDetailModal(clickedLstgId);
                        } else {
                            console.error("window.openDetailModal 함수를 찾을 수 없습니다. 스크립트 로드 순서를 확인해주세요.");
                            alert("매물 상세 페이지를 불러올 수 없습니다.");
                        }
                    });

                    $listingSpecificInfo.show();
                    // ReportVO에 lstgDel이 직접 있다면 data.lstgDel, ListingVO 내에 있다면 data.listingVO.lstgDel 확인 필요
                    $('#modalNewLtsgDel').val(data.lstgDel);

                    $('#btnProcessAllChanges').data('original-lstg-del', data.lstgDel);
                    $('#btnProcessAllChanges').data('lstg-id', data.rptTargetId);
                    $('#btnProcessAllChanges').removeData('original-mbr-status');
                    $('#btnProcessAllChanges').removeData('mbr-cd');

                } else { // 회원 신고일 경우 (매물 신고가 아님)
                    $modalTargetIdLabel.text('피신고자 ID : ');
                    $modalRptTargetId.html(data.rptTargetId || 'N/A'); // 다시 일반 텍스트로 설정
                    $modalRptTargetId.removeData('lstg-id'); // 매물 데이터 제거
                    $modalRptTargetId.off('click', '.listing-detail-link'); // 혹시 모를 이벤트 리스너 제거

                    $memberSpecificInfo.show();
                    // ReportVO에 rptTargetMbrStatus가 직접 있다면 data.rptTargetMbrStatus, MemberVO 내에 있다면 data.memberVO.mbrStatus 확인 필요
                    $('#modalNewMbrStatus').val(data.rptTargetMbrStatus);

                    $('#btnProcessAllChanges').data('original-mbr-status', data.rptTargetMbrStatus);
                    $('#btnProcessAllChanges').data('mbr-cd', data.rptTargetMbrCd);
                    $('#btnProcessAllChanges').removeData('original-lstg-del');
                    $('#btnProcessAllChanges').removeData('lstg-id');
                }
                // --- 매물 신고일 경우 링크 추가 로직 끝 ---

                $('#modalRptStatusCode').val(data.rptStatusCode);
                $('#btnProcessAllChanges').data('report-id', data.rptId);
                $('#btnProcessAllChanges').data('original-rpt-status', data.rptStatusCode);

                // --- 첨부파일 관련 로직 시작 ---
                // ReportVO에 attachFileList라는 이름으로 첨부파일 목록이 넘어온다고 가정
                const fileDataHolder = document.querySelector("#reportDetailModal #fileDataHolder");
                // data.attachFileList가 null이거나 정의되지 않았다면 빈 배열 사용
                const fileListJson = JSON.stringify(data.attachFiles || []);
				fileDataHolder.setAttribute("data-filelist", fileListJson);

                currentFileList = JSON.parse(fileListJson);
                currentIndex = 0; // 항상 첫 번째 파일부터 시작

                renderFileTable(); // 파일 목록 테이블 렌더링
                updatePageIndicator(); // 페이지 인디케이터 업데이트

                // 파일이 있을 경우 첫 번째 파일 미리보기
                if (currentFileList.length > 0) {
                    fetchPdfOrImage(currentFileList[currentIndex]);
                } else {
                    // 파일이 없을 경우 캔버스 초기화
                    const canvas = document.querySelector("#reportDetailModal #pdfCanvas");
                    const ctx = canvas.getContext("2d");
                    ctx.clearRect(0, 0, canvas.width, canvas.height);
                    canvas.width = 1;
                    canvas.height = 1;
                }

                // 파일 미리보기 컨트롤 버튼 이벤트 리스너 재등록
                // #reportDetailModal 내의 버튼들을 선택
                $('#reportDetailModal #prevBtn').off('click').on('click', prevFile);
                $('#reportDetailModal #nextBtn').off('click').on('click', nextFile);

                // 첨부파일 목록 보기/숨기기 버튼 이벤트 리스너 재등록
                const toggleBtn = document.querySelector("#reportDetailModal #toggleFileListBtn");
                const fileTable = document.querySelector("#reportDetailModal #fileTable");
                fileTable.style.display = "none"; // 초기 상태는 숨김
                toggleBtn.innerText = "첨부파일 목록 보기"; // 초기 텍스트 설정

                $(toggleBtn).off('click').on('click', () => {
                    if (fileTable.style.display === "none" || fileTable.style.display === "") {
                        fileTable.style.display = "table";
                        toggleBtn.innerText = "첨부파일 목록 숨기기";
                    } else {
                        fileTable.style.display = "none";
                        toggleBtn.innerText = "첨부파일 목록 보기";
                    }
                });
                // --- 첨부파일 관련 로직 끝 ---

                $('#reportDetailModal').modal('show'); // 신고 상세 모달 표시
            })
            .catch(error => {
                console.error('신고 상세 정보 로드 실패:', error);
                alert('신고 상세 정보를 불러오는 데 실패했습니다.');
            });
    });

    $('#closeReportDetailModalBtn').on('click', function() {
        $('#reportDetailModal').modal('hide');
    });

    $('#btnProcessAllChanges').on('click', function() {
        const $thisBtn = $(this);
        const reportId = $thisBtn.data('report-id');
        const originalRptStatus = $thisBtn.data('original-rpt-status');
        const newRptStatusCode = $('#modalRptStatusCode').val();

        const originalMbrStatus = $thisBtn.data('original-mbr-status');
        const newMbrStatus = $('#modalNewMbrStatus').val();
        const mbrCd = $thisBtn.data('mbr-cd');

        const originalLstgDel = $thisBtn.data('original-lstg-del');
        const newLstgDel = $('#modalNewLtsgDel').val();
        const lstgId = $thisBtn.data('lstg-id');

        let changesMade = false;
        let successMessages = [];
        let errorMessages = [];
        const promises = [];

        if (newRptStatusCode !== originalRptStatus) {
            changesMade = true;
            promises.push(
                axios.post(`${contextPath}/axios/admin/report/updateStatuses`, [{ rptId: reportId, rptStatusCode: newRptStatusCode }])
                    .then(response => {
                        if (response.data.status === 'success') {
                            successMessages.push('신고 처리 상태가 성공적으로 변경되었습니다.');
                        } else {
                            errorMessages.push('신고 처리 상태 변경 실패: ' + response.data.message);
                        }
                    })
                    .catch(error => {
                        console.error('신고 상태 변경 AJAX 오류:', error);
                        errorMessages.push('신고 상태 변경 중 오류가 발생했습니다.');
                    })
            );
        }

        if (mbrCd && newMbrStatus !== originalMbrStatus) {
            changesMade = true;
            promises.push(
                axios.post(`${contextPath}/axios/admin/report/updateMemberStatus`, null, {
                    params: { mbrCd: mbrCd, mbrStatus: newMbrStatus }
                })
                .then(response => {
                    if (response.data === 'SUCCESS') {
                        successMessages.push(`회원 (${mbrCd}) 상태가 성공적으로 변경되었습니다.`);
                    } else {
                        errorMessages.push(`회원 (${mbrCd}) 상태 변경 실패.`);
                    }
                })
                .catch(error => {
                    console.error('회원 상태 변경 AJAX 오류:', error);
                    errorMessages.push(`회원 (${mbrCd}) 상태 변경 중 오류가 발생했습니다.`);
                })
            );
        }

        if (lstgId && newLstgDel !== originalLstgDel) {
            changesMade = true;
            promises.push(
                axios.post(`${contextPath}/axios/admin/report/updateListingDeleteStatus`, null, {
                    params: { lstgId: lstgId, lstgDel: newLstgDel }
                })
                .then(response => {
                    if (response.data === 'SUCCESS') {
                        successMessages.push(`매물 (${lstgId}) 삭제 상태가 성공적으로 변경되었습니다.`);
                    } else {
                        errorMessages.push(`매물 (${lstgId}) 삭제 상태 변경 실패.`);
                    }
                })
                .catch(error => {
                        console.error('매물 삭제 상태 변경 AJAX 오류:', error);
                        errorMessages.push(`매물 (${lstgId}) 삭제 상태 변경 중 오류가 발생했습니다.`);
                })
            );
        }

        if (!changesMade) {
            alert('변경할 내용이 없습니다.');
            return;
        }

        if (confirm('모든 변경 사항을 저장하시겠습니까?')) {
            Promise.all(promises)
                .then(() => {
                    let finalMessage = "";
                    if (successMessages.length > 0) {
                        finalMessage += "✅ 성공:\n" + successMessages.join('\n');
                    }
                    if (errorMessages.length > 0) {
                        if (finalMessage !== "") finalMessage += "\n\n";
                        finalMessage += "❌ 실패:\n" + errorMessages.join('\n');
                    }
                    alert(finalMessage || "처리 완료 (메시지 없음)");
                    $('#reportDetailModal').modal('hide');
                    window.location.reload(); // 페이지 새로고침하여 변경사항 반영
                })
                .catch(allErrors => {
                    console.error('모든 Promise 처리 중 오류 발생:', allErrors);
                    alert('일부 변경 사항 처리 중 오류가 발생했습니다.');
                    $('#reportDetailModal').modal('hide');
                    window.location.reload(); // 페이지 새로고침하여 변경사항 반영
                });
        }
    });

    // 모달이 닫힐 때 파일 미리보기 상태 초기화
    $('#reportDetailModal').on('hidden.bs.modal', function () {
        currentFileList = [];
        currentIndex = 0;
        isRendering = false;
        // 캔버스 내용 지우기
        const canvas = document.querySelector("#reportDetailModal #pdfCanvas");
        const ctx = canvas.getContext("2d");
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        canvas.width = 1; // 캔버스 크기 초기화
        canvas.height = 1;
        renderFileTable(); // 파일 목록 테이블도 초기화
        updatePageIndicator(); // 페이지 인디케이터 초기화
    });
});