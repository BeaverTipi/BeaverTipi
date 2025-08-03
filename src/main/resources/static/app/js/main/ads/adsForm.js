// src/main/resources/static/js/main/ads/adsForm.js


document.addEventListener("DOMContentLoaded",()=>{
	const writeBtn = document.querySelector("#writeBtn");
	writeBtn.addEventListener("click",()=>{
		const title = document.querySelector("#brdTitlNm");
		const cont = document.querySelector("#brdCont");
		const adsBp = document.querySelector("#adsBp");
		const adsPic = document.querySelector("#adsPic");
		const adsPicTelno = document.querySelector("#adsPicTelno");
		const adsReqPblsStartDt = document.querySelector("#adsReqPblsStartDt");
		const adsReqPblsEndDt = document.querySelector("#adsReqPblsEndDt");
		
		title.value="여운선";
		cont.value="나베가 맛있고 술이 맛있는 언덕위의 작은 요리주점";
		adsBp.value="여운선";
		adsPic.value="이학범";
		adsPicTelno.value="010-5674-3422";
		adsReqPblsStartDt.value="2025-08-05";
		adsReqPblsEndDt.value="2025-09-30";
	})
})
// --- 전역 변수 및 헬퍼 함수 ---
// 현재 선택된 파일들을 저장할 배열
let selectedFiles = [];
// 현재 미리보기 중인 파일의 인덱스
let currentPreviewIndex = 0;
// 캔버스 렌더링 중복 방지 플래그
let isRendering = false;

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

// --- 파일 미리보기 관련 함수 ---

// 미리보기 요소들의 가시성을 토글하는 헬퍼 함수
function togglePreviewVisibility(isVisible) {
    const canvas = document.getElementById("pdfCanvas");
    const controls = document.getElementById("pdf-controls");
    const previewTitle = document.querySelector(".file-display-section h5"); // H5 제목 가져오기

    if (isVisible) {
        canvas.style.display = 'block';
        controls.style.display = 'flex'; // CSS에 정의된 대로 flex 사용
        previewTitle.style.display = 'block'; // 제목도 보이도록
    } else {
        canvas.style.display = 'none'; // 캔버스 숨김
        controls.style.display = 'none'; // 컨트롤 숨김
        previewTitle.style.display = 'none'; // 미리보기 없을 때 제목도 숨김
        // 파일이 없을 때 보여줄 메시지 (선택 사항)
        const displaySection = document.querySelector(".file-display-section");
        let noFileMessage = displaySection.querySelector(".no-preview-message");
        if (!noFileMessage) {
            noFileMessage = document.createElement("p");
            noFileMessage.classList.add("no-preview-message");
            noFileMessage.innerText = "파일을 선택하여 미리보기를 확인하세요.";
            displaySection.appendChild(noFileMessage);
        }
        noFileMessage.style.display = 'block';
    }
}

// 파일 목록 테이블 렌더링
function renderFileTable() {
    const tbody = document.querySelector("#fileTable tbody");
    tbody.innerHTML = ""; // 기존 목록 초기화

    if (selectedFiles.length === 0) {
        const row = document.createElement("tr");
        row.innerHTML = `<td colspan="3" class="text-center">첨부 예정 파일이 없습니다.</td>`;
        tbody.appendChild(row);
        updatePreviewControls(0); // 파일 없으므로 미리보기 컨트롤 비활성화 및 초기화
        clearCanvas(); // 파일 없으면 캔버스 초기화
        togglePreviewVisibility(false); // 파일이 없으면 미리보기 요소 숨김
        return;
    }

    // 파일이 있으면 "파일을 선택하여 미리보기를 확인하세요." 메시지 숨김
    const noFileMessage = document.querySelector(".file-display-section .no-preview-message");
    if (noFileMessage) {
        noFileMessage.style.display = 'none';
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
        togglePreviewVisibility(true); // 파일이 있으면 미리보기 요소 표시
        fetchPdfOrImage(selectedFiles[currentPreviewIndex]); // 현재 인덱스 파일 미리보기
    } else {
        clearCanvas(); // 파일 없으면 캔버스 지우기
        togglePreviewVisibility(false); // 파일 없으면 미리보기 요소 숨김
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
    // 캔버스의 실제 그리기 영역을 현재 CSS에 의해 결정된 크기로 설정
    canvas.width = canvas.offsetWidth;
    canvas.height = canvas.offsetHeight;
    ctx.clearRect(0, 0, canvas.width, canvas.height);
}

// PDF 또는 이미지 미리보기
function fetchPdfOrImage(file) {
    if (!file || !file.type) {
        clearCanvas();
        alert("유효하지 않은 파일 정보입니다.");
        isRendering = false;
        togglePreviewVisibility(false); // 유효하지 않은 파일이면 숨김
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
        togglePreviewVisibility(false); // 지원하지 않는 파일이면 숨김
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
                const parentDiv = canvas.parentElement; // .file-display-section
                const containerWidth = parentDiv.offsetWidth - (parseInt(getComputedStyle(parentDiv).paddingLeft) || 0) - (parseInt(getComputedStyle(parentDiv).paddingRight) || 0);
                const containerHeight = parentDiv.offsetHeight - (parseInt(getComputedStyle(parentDiv).paddingTop) || 0) - (parseInt(getComputedStyle(parentDiv).paddingBottom) || 0);

                // H5와 버튼 영역의 높이를 고려하여 캔버스에 할당할 실제 사용 가능 높이 계산
                const h5Height = document.querySelector(".file-display-section h5").offsetHeight + parseInt(getComputedStyle(document.querySelector(".file-display-section h5")).marginBottom || 0);
                const controlsHeight = document.getElementById("pdf-controls").offsetHeight + parseInt(getComputedStyle(document.getElementById("pdf-controls")).marginTop || 0);
                const availableHeightForCanvas = containerHeight - h5Height - controlsHeight;

                const viewport = page.getViewport({ scale: 1 });

                // 캔버스를 사용 가능한 영역에 맞추기 위한 스케일 계산 (비율 유지)
                let scale = Math.min(containerWidth / viewport.width, availableHeightForCanvas / viewport.height);
                // 너무 크게 확대되지 않도록 제한 (선택 사항)
                if (scale > 1.0) scale = 1.0; 

                const scaledViewport = page.getViewport({ scale });

                canvas.width = scaledViewport.width;
                canvas.height = scaledViewport.height;

                // 캔버스 요소를 수평 중앙에 배치 (수직 중앙은 flexbox가 처리)
                canvas.style.marginLeft = `${(containerWidth - canvas.width) / 2}px`;
                // 캔버스 자체의 수직 중앙 정렬을 위해 margin-top 사용
                // 여기서는 flex-direction: column과 align-items: center로 인해 이미 수평 중앙 정렬이 되므로,
                // 수직 정렬은 flex-grow: 1과 margin: auto로 처리됩니다.
                canvas.style.marginTop = 'auto'; // flex-grow와 함께 사용 시 유용
                canvas.style.marginBottom = 'auto'; // flex-grow와 함께 사용 시 유용

                canvas.style.display = 'block';

                return page.render({ canvasContext: ctx, viewport: scaledViewport }).promise;
            })
            .catch(err => {
                console.error("PDF 로딩 오류:", err);
                alert("PDF 미리보기 중 오류가 발생했습니다: " + file.name);
                clearCanvas();
                togglePreviewVisibility(false); // 오류 발생 시 숨김
            })
            .finally(() => {
                isRendering = false;
            });
    };
    fileReader.readAsArrayBuffer(file);
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
        const parentDiv = canvas.parentElement; // .file-display-section
        const containerWidth = parentDiv.offsetWidth - (parseInt(getComputedStyle(parentDiv).paddingLeft) || 0) - (parseInt(getComputedStyle(parentDiv).paddingRight) || 0);
        const containerHeight = parentDiv.offsetHeight - (parseInt(getComputedStyle(parentDiv).paddingTop) || 0) - (parseInt(getComputedStyle(parentDiv).paddingBottom) || 0);

        // H5와 버튼 영역의 높이를 고려하여 캔버스에 할당할 실제 사용 가능 높이 계산
        const h5Height = document.querySelector(".file-display-section h5").offsetHeight + parseInt(getComputedStyle(document.querySelector(".file-display-section h5")).marginBottom || 0);
        const controlsHeight = document.getElementById("pdf-controls").offsetHeight + parseInt(getComputedStyle(document.getElementById("pdf-controls")).marginTop || 0);
        const availableHeightForCanvas = containerHeight - h5Height - controlsHeight;

        let width = img.width;
        let height = img.height;

        const aspectRatio = width / height;
        const containerAspectRatio = containerWidth / availableHeightForCanvas; // 캔버스 사용 가능 높이 기준

        if (aspectRatio > containerAspectRatio) {
            // 이미지가 컨테이너보다 넓을 경우 (너비에 맞춤)
            width = containerWidth;
            height = containerWidth / aspectRatio;
        } else {
            // 이미지가 컨테이너보다 높거나 같은 비율일 경우 (높이에 맞춤)
            height = availableHeightForCanvas;
            width = availableHeightForCanvas * aspectRatio;
        }

        canvas.width = width;
        canvas.height = height;

        // 캔버스 요소를 수평 중앙에 배치 (수직 중앙은 flexbox가 처리)
        canvas.style.marginLeft = `${(containerWidth - canvas.width) / 2}px`;
        canvas.style.marginTop = 'auto'; // flex-grow와 함께 사용 시 유용
        canvas.style.marginBottom = 'auto'; // flex-grow와 함께 사용 시 유용
        canvas.style.display = 'block';

        ctx.drawImage(img, 0, 0, width, height);
        isRendering = false;
    };
    img.onerror = () => {
        alert("이미지 로드 실패: " + file.name);
        clearCanvas();
        isRendering = false;
        togglePreviewVisibility(false); // 오류 발생 시 숨김
    };
    reader.readAsDataURL(file);
}

// 미리보기 컨트롤 (이전/다음 버튼, 페이지 인디케이터) 업데이트
function updatePreviewControls(total) {
	console.log("updatePreviewControls 호출. total:", total, "currentPreviewIndex:", currentPreviewIndex);
    const fileIndexSpan = document.getElementById("fileIndex");
    const totalCountSpan = document.getElementById("totalCount");
    const prevBtn = document.getElementById("prevBtn");
    const nextBtn = document.getElementById("nextBtn");

    fileIndexSpan.innerText = total > 0 ? currentPreviewIndex + 1 : 0;
    totalCountSpan.innerText = total;

    prevBtn.disabled = currentPreviewIndex <= 0;
    nextBtn.disabled = currentPreviewIndex >= total - 1;

	console.log("prevBtn disabled:", prevBtn.disabled, "nextBtn disabled:", nextBtn.disabled);

    if (total === 0) {
        prevBtn.disabled = true;
        nextBtn.disabled = true;
    }
}

function nextFile() {
    if (currentPreviewIndex < selectedFiles.length - 1) {
        currentPreviewIndex++;
        fetchPdfOrImage(selectedFiles[currentPreviewIndex]);
        updatePreviewControls(selectedFiles.length);
    }
}

function prevFile() {
    if (currentPreviewIndex > 0) {
        currentPreviewIndex--;
        fetchPdfOrImage(selectedFiles[currentPreviewIndex]);
        updatePreviewControls(selectedFiles.length);
    }
}

// --- DOMContentLoaded 이벤트 리스너 ---
$(document).ready(function() {
    const attachFilesInput = document.getElementById("attachFiles");
    attachFilesInput.addEventListener("change", function(event) {
        // 새로 선택된 파일들을 기존 selectedFiles 배열에 추가
        for (const file of event.target.files) {
            selectedFiles.push(file);
        }
        renderFileTable();
        // 첫 파일이 선택되었거나, 기존에 파일이 없었다면 새로 추가된 첫 파일을 미리보기
        // 또는 새로운 파일 추가 시 마지막 추가된 파일로 미리보기
        if (selectedFiles.length > 0) {
            currentPreviewIndex = 0;
            fetchPdfOrImage(selectedFiles[currentPreviewIndex]);
        }
        this.value = ''; // input[type="file"]의 값 초기화 (동일 파일을 다시 선택해도 change 이벤트 발생시키기 위함)
    });

    // 2. 미리보기 컨트롤 버튼 이벤트 리스너
    document.getElementById("prevBtn").addEventListener("click", prevFile);
    document.getElementById("nextBtn").addEventListener("click", nextFile);

    // 초기 파일 목록 렌더링 (페이지 로드 시)
    renderFileTable();
    updatePreviewControls(0); // 처음에는 파일이 없으므로 0으로 초기화
    togglePreviewVisibility(false); // 초기에는 미리보기 관련 요소 숨김

    // 3. 폼 제출 이벤트 (Axios를 사용한 비동기 제출 예시)
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
        if (selectedFiles.length === 0) { // 파일 첨부 필수 유효성 검사 추가
            alert("광고 파일을 최소 1개 이상 첨부해야 합니다.");
            return;
        }

        const formData = new FormData(this); // 폼의 모든 데이터를 FormData 객체로 생성

        // selectedFiles 배열에 있는 파일들을 FormData에 추가
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
            location.href = contextPath + "/"; // 예시: 성공 페이지로 이동
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