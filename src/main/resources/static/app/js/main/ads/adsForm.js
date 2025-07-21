// src/main/resources/static/js/main/ads/adsForm.js

// --- 전역 변수 및 헬퍼 함수 ---
// 현재 선택된 파일들을 저장할 배열
let selectedFiles = [];
// 현재 미리보기 중인 파일의 인덱스
let currentPreviewIndex = 0;
// 캔버스 렌더링 중복 방지 플래그
let isRendering = false;

// PDF.js worker 소스 경로가 adsForm.jsp에 이미 설정되어 있지만,
// 만약을 위해 JS 파일에서도 한 번 더 설정해 줄 수 있습니다.
// pdfjsLib.GlobalWorkerOptions.workerSrc = 'https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.4.120/pdf.worker.min.js';

// 파일 크기를 읽기 쉬운 형식으로 변환 (bytes, KB, MB)
function formatBytes(bytes, decimals = 2) {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const dm = decimals < 0 ? 0 : decimals;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
}

// 전화번호 포맷팅 헬퍼 함수 (adsClientVO.adsPicTelno 필드 유효성 검사 등에서 사용 가능)
function formatPhoneNumber(phoneNumber) {
    if (!phoneNumber) return '';
    const cleaned = ('' + phoneNumber).replace(/\D/g, '');
    const match = cleaned.match(/^(\d{2,3})(\d{3,4})(\d{4})$/);
    if (match) {
        return `${match[1]}-${match[2]}-${match[3]}`;
    }
    return phoneNumber;
}

// --- 파일 미리보기 관련 함수 (businessAdsList.js에서 재활용/수정) ---

// 파일 목록 테이블 렌더링
function renderFileTable() {
    const tbody = document.querySelector("#fileTable tbody");
    tbody.innerHTML = ""; // 기존 목록 초기화

    if (selectedFiles.length === 0) {
        const row = document.createElement("tr");
        row.innerHTML = `<td colspan="3" class="text-center">첨부 예정 파일이 없습니다.</td>`;
        tbody.appendChild(row);
        updatePreviewControls(0); // 파일 없으므로 미리보기 컨트롤 비활성화 및 초기화
        return;
    }

    selectedFiles.forEach((file, index) => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${file.name}</td>
            <td>${formatBytes(file.size)}</td>
            <td>
                <button type="button" class="btn btn-danger btn-sm delete-file-btn" data-index="${index}">삭제</button>
            </td>
        `;
        tbody.appendChild(row);
    });

    // 삭제 버튼 이벤트 리스너 추가
    tbody.querySelectorAll(".delete-file-btn").forEach(button => {
        button.addEventListener("click", function() {
            const indexToRemove = parseInt(this.dataset.index);
            removeFile(indexToRemove);
        });
    });

    // 파일 목록이 변경될 때마다 현재 미리보기 인덱스 조정 및 미리보기 업데이트
    if (currentPreviewIndex >= selectedFiles.length) {
        currentPreviewIndex = selectedFiles.length > 0 ? selectedFiles.length - 1 : 0;
    }
    updatePreviewControls(selectedFiles.length); // 컨트롤 업데이트
    if (selectedFiles.length > 0) {
        fetchPdfOrImage(selectedFiles[currentPreviewIndex]); // 현재 인덱스 파일 미리보기
    } else {
        clearCanvas(); // 파일 없으면 캔버스 지우기
    }
}

// 파일 삭제 로직
function removeFile(indexToRemove) {
    selectedFiles.splice(indexToRemove, 1); // 배열에서 파일 제거
    renderFileTable(); // 테이블 다시 렌더링
}

// 캔버스 초기화
function clearCanvas() {
    const canvas = document.getElementById("pdfCanvas");
    const ctx = canvas.getContext("2d");
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    canvas.width = 1;
    canvas.height = 1;
}

// PDF 또는 이미지 미리보기
function fetchPdfOrImage(file) {
    const canvas = document.getElementById("pdfCanvas");
    const ctx = canvas.getContext("2d");

    if (!file || !file.type) {
        clearCanvas();
        alert("유효하지 않은 파일 정보입니다.");
        isRendering = false;
        return;
    }
    if (isRendering) return;
    isRendering = true;

    if (file.type === 'application/pdf') {
        fetchPdf(file);
    } else if (file.type.startsWith('image/')) {
        renderImage(file);
    } else {
        clearCanvas();
        alert("미리보기를 지원하지 않는 파일 형식입니다: " + file.name);
        isRendering = false;
    }
}

// PDF 파일 렌더링
function fetchPdf(file) {
    const canvas = document.getElementById("pdfCanvas");
    const ctx = canvas.getContext("2d");
    clearCanvas(); // PDF 로드 전 캔버스 클리어

    const fileReader = new FileReader();
    fileReader.onload = function() {
        const typedArray = new Uint8Array(this.result);
        pdfjsLib.getDocument(typedArray).promise
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
                alert("PDF 미리보기 중 오류가 발생했습니다: " + file.name);
                clearCanvas();
            })
            .finally(() => {
                isRendering = false;
            });
    };
    fileReader.readAsArrayBuffer(file); // Blob 대신 File 객체를 직접 읽음
}

// 이미지 파일 렌더링
function renderImage(file) {
    const canvas = document.getElementById("pdfCanvas");
    const ctx = canvas.getContext("2d");
    clearCanvas(); // 이미지 로드 전 캔버스 클리어

    const img = new Image();
    const reader = new FileReader();

    reader.onload = function(e) {
        img.src = e.target.result;
    };

    img.onload = () => {
        const maxWidth = 500; // 모달 내부에 맞게 조절
        const maxHeight = 400;

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
        isRendering = false;
    };
    img.onerror = () => {
        alert("이미지 로드 실패: " + file.name);
        clearCanvas();
        isRendering = false;
    };
    reader.readAsDataURL(file); // File 객체를 Data URL로 읽음
}

// 미리보기 컨트롤 (이전/다음 버튼, 페이지 인디케이터) 업데이트
function updatePreviewControls(total) {
    const fileIndexSpan = document.getElementById("fileIndex");
    const totalCountSpan = document.getElementById("totalCount");
    const prevBtn = document.getElementById("prevBtn");
    const nextBtn = document.getElementById("nextBtn");

    fileIndexSpan.innerText = total > 0 ? currentPreviewIndex + 1 : 0;
    totalCountSpan.innerText = total;

    prevBtn.disabled = currentPreviewIndex <= 0;
    nextBtn.disabled = currentPreviewIndex >= total - 1;

    // 파일이 없으면 버튼 비활성화
    if (total === 0) {
        prevBtn.disabled = true;
        nextBtn.disabled = true;
    }
}

// 다음 파일 미리보기
function nextFile() {
    if (currentPreviewIndex < selectedFiles.length - 1) {
        currentPreviewIndex++;
        fetchPdfOrImage(selectedFiles[currentPreviewIndex]);
        updatePreviewControls(selectedFiles.length);
    }
}

// 이전 파일 미리보기
function prevFile() {
    if (currentPreviewIndex > 0) {
        currentPreviewIndex--;
        fetchPdfOrImage(selectedFiles[currentPreviewIndex]);
        updatePreviewControls(selectedFiles.length);
    }
}

// --- DOMContentLoaded 이벤트 리스너 ---
$(document).ready(function() {
    // 1. 파일 입력 필드 변경 이벤트
    const attachFilesInput = document.getElementById("attachFiles");
    attachFilesInput.addEventListener("change", function(event) {
        // 새로 선택된 파일들을 기존 selectedFiles 배열에 추가
        for (const file of event.target.files) {
            selectedFiles.push(file);
        }
        renderFileTable(); // 파일 목록 테이블 다시 렌더링
        // 첫 파일이 선택되었거나, 기존에 파일이 없었다면 새로 추가된 첫 파일을 미리보기
        if (selectedFiles.length === event.target.files.length) { // 첫 파일 선택 시
             currentPreviewIndex = 0;
             fetchPdfOrImage(selectedFiles[currentPreviewIndex]);
        } else if (selectedFiles.length > 0 && currentPreviewIndex === 0) {
            // 이미 파일이 있었고, 새로운 파일을 추가했지만, 현재 인덱스가 0이라면 그냥 다시 렌더링 (동일 파일)
             // 또는 새로운 파일을 추가한 경우 가장 최근 추가된 파일로 미리보기를 변경할 수 있음
             // currentPreviewIndex = selectedFiles.length - 1; // 마지막 추가된 파일 미리보기
             // fetchPdfOrImage(selectedFiles[currentPreviewIndex]);
        }
        // input[type="file"]의 값 초기화 (동일 파일을 다시 선택해도 change 이벤트 발생시키기 위함)
        this.value = '';
    });

    // 2. 미리보기 컨트롤 버튼 이벤트 리스너
    document.getElementById("prevBtn").addEventListener("click", prevFile);
    document.getElementById("nextBtn").addEventListener("click", nextFile);

    // 초기 파일 목록 렌더링 (페이지 로드 시)
    renderFileTable();
    updatePreviewControls(0); // 처음에는 파일이 없으므로 0으로 초기화

    // 3. 폼 제출 이벤트 (Axios를 사용한 비동기 제출 예시)
    // 기존 form 태그의 action과 method를 사용해도 되지만,
    // 파일 업로드와 함께 비동기 처리(프로그레스 바 등)를 원하면 Axios 사용이 편리합니다.
    const adsRequestForm = document.getElementById("adsRequestForm");
    adsRequestForm.addEventListener("submit", function(event) {
        event.preventDefault(); // 기본 폼 제출 방지

        // 클라이언트 측 유효성 검사 (예시)
        if (!document.getElementById("brdTitlNm").value) {
            alert("광고 제목을 입력해주세요.");
            return;
        }
        if (!document.getElementById("brdCont").value) {
            alert("광고 내용을 입력해주세요.");
            return;
        }
        if (!document.getElementById("adsBp").value) {
            alert("사업장명을 입력해주세요.");
            return;
        }
        if (!document.getElementById("adsPic").value) {
            alert("담당자명을 입력해주세요.");
            return;
        }
        const telno = document.getElementById("adsPicTelno").value;
        if (!telno || !/^\d{10,11}$/.test(telno)) { // 숫자 10~11자리 확인
            alert("담당자 연락처는 하이픈 없이 숫자만 10~11자리로 입력해주세요.");
            return;
        }
        if (!document.getElementById("adsReqPblsStartDt").value) {
            alert("희망 게재 시작일을 선택해주세요.");
            return;
        }
        if (!document.getElementById("adsReqPblsEndDt").value) {
            alert("희망 게재 종료일을 선택해주세요.");
            return;
        }
        // 날짜 유효성 검사 (시작일 < 종료일)
        const startDate = new Date(document.getElementById("adsReqPblsStartDt").value);
        const endDate = new Date(document.getElementById("adsReqPblsEndDt").value);
        if (startDate > endDate) {
            alert("희망 게재 종료일은 시작일보다 빠를 수 없습니다.");
            return;
        }

        const formData = new FormData(this); // 폼의 모든 데이터를 FormData 객체로 생성

        // selectedFiles 배열에 있는 파일들을 FormData에 추가
        // <input type="file" name="attachFiles" multiple> 로 선택된 파일들은 이미 formData에 자동으로 포함됩니다.
        // 하지만 사용자가 파일 삭제 버튼을 눌러 selectedFiles에서 파일을 제거한 경우
        // 기존 input의 files 리스트에는 남아있을 수 있으므로, selectedFiles를 기반으로 FormData를 재구성하는 것이 안전합니다.
        formData.delete('attachFiles'); // 기존 input의 attachFiles 제거
        selectedFiles.forEach(file => {
            formData.append('attachFiles', file); // selectedFiles의 파일들을 다시 추가
        });


        // Axios를 사용하여 데이터 전송
        axios.post(this.action, formData, {
            headers: {
                'Content-Type': 'multipart/form-data' // 파일 업로드 시 필수
            },
            // onUploadProgress: progressEvent => { // 진행률 표시 (선택 사항)
            //     const percentCompleted = Math.round((progressEvent.loaded * 100) / progressEvent.total);
            //     console.log(`업로드 진행률: ${percentCompleted}%`);
            // }
        })
        .then(response => {
            console.log("응답:", response.data);
            if (response.data.message) { // 컨트롤러에서 message를 반환한다고 가정
                alert(response.data.message);
            } else {
                alert("광고 요청이 성공적으로 처리되었습니다.");
            }
            // 성공 시 페이지 이동 또는 폼 초기화
            location.href = contextPath + "/member/ads/requestSuccess"; // 예시: 성공 페이지로 이동
        })
        .catch(error => {
            console.error("오류 발생:", error.response || error);
            const errorMessage = error.response && error.response.data && error.response.data.message
                               ? error.response.data.message
                               : "광고 요청 처리 중 오류가 발생했습니다.";
            alert(errorMessage);
        });
    });
});