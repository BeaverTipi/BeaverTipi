// 전역 변수 (contextPath는 스크립트 로드 전에 정의되어야 함)
// 예: <script>var contextPath = "${pageContext.request.contextPath}";</script>

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
        row.innerHTML = `<td>${file.fileOriginalname || '파일명 없음'}</td><td>${file.fileSize || 0} bytes</td>`;
        tbody.appendChild(row);
    });
}

function fetchPdfOrImage(file) {
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
        Swal.fire({
            icon: 'info',
            title: '알림',
            text: '미리보기를 지원하지 않는 파일 형식입니다.',
            confirmButtonText: '확인'
        });
        isRendering = false;
    }
}

function fetchPdf(fileId) {
    const canvas = document.querySelector("#reportDetailModal #pdfCanvas");
    const ctx = canvas.getContext("2d");
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    axios({
        method: 'get',
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
        Swal.fire({
            icon: 'error',
            title: '오류',
            text: 'PDF 미리보기 중 오류가 발생했습니다.',
            confirmButtonText: '확인'
        });
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
    const canvas = document.querySelector("#reportDetailModal #pdfCanvas");
    const ctx = canvas.getContext("2d");
    ctx.clearRect(0, 0, canvas.width, canvas.height);

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

        canvas.width = width;
        canvas.height = height;
        ctx.drawImage(img, 0, 0, width, height);

        updatePageIndicator();
        isRendering = false;
    };
    img.onerror = () => {
        Swal.fire({
            icon: 'error',
            title: '오류',
            text: '이미지 로드 실패',
            confirmButtonText: '확인'
        });
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        canvas.width = 1;
        canvas.height = 1;
        isRendering = false;
    };
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
    const indicator = document.querySelector("#reportDetailModal #fileIndex");
    const total = document.querySelector("#reportDetailModal #totalCount");
    if (indicator && total) {
        indicator.innerText = currentFileList.length > 0 ? currentIndex + 1 : 0;
        total.innerText = currentFileList.length;
    }
}

// =====================================================================================================
// 매물 상세 모달 관련 함수 (listRenderer.js의 로직을 userList.js에 맞춰 수정)
// =====================================================================================================

// 매물 갤러리 이미지 URL 저장 배열 (listRenderer.js에서 사용)
let listingGalleryImages = [];
// 현재 보고 있는 이미지 인덱스 (listRenderer.js에서 사용)
let currentGalleryIndex = 0;

// listRenderer.js의 getDealType 함수 복사
const getDealType = (code) => ({
    '001': '전세',
    '002': '월세',
    '003': '매매'
}[code] || '미정');

// listRenderer.js의 getDepositText 함수 복사
const getDepositText = (item) => {
    const type = String(item.lstgTypeSale);
    const lease = item.lstgLease || 0;
    const leaseM = item.lstgLeaseM || 0;
    const leaseAmt = item.lstgLeaseAmt || 0;

    const format = (num) => !isNaN(num) ? Math.round(Number(num)).toLocaleString() + '원' : '-';

    switch (type) {
        case '001':
            return `전세금: ${format(lease)}`;
        case '002':
            return `보증금: ${format(leaseAmt)} / 월세: ${format(leaseM)}`;
        case '003':
            return `매매가: ${format(leaseAmt)}`;
        default:
            return '-';
    }
};

// listRenderer.js의 renderFacilityOptions 함수 복사 (facilityOptionList 대신 facOptions 사용)
const renderFacilityOptions = (options = []) => {
    if (!Array.isArray(options) || options.length === 0) return '<p>선택된 옵션 없음</p>';

    return `
    <ul class="facility-options" style="list-style: none; padding: 0;">
        ${options.map(opt => `
          <li style="display: inline-block; margin-right: 15px; background: #e9ecef; padding: 5px 10px; border-radius: 5px; margin-bottom: 5px;"><strong>${opt.facOptNm}</strong></li>
        `).join('')}
      </ul>
      `;
};

// userList.js의 renderImageGallery 함수 수정
const renderImageGallery = (fileList) => {
    const fallback = `${contextPath}/volt/assets/img/illustrations/no-image.png`;

    let imageUrls = [];
    if (fileList && Array.isArray(fileList) && fileList.length > 0) {
        fileList.sort((a, b) => (a.fileOrd || 0) - (b.fileOrd || 0));
        fileList.forEach(file => {
            imageUrls.push(file.filePathUrl);
        });
    }

    listingGalleryImages = imageUrls;

    const totalImages = imageUrls.length;
    const thumbnailsToShow = imageUrls.slice(1, 5);

    const hiddenCount = totalImages > 5 ? totalImages - 5 : 0;

    return `
    <div class="image-gallery" style="
        display: grid;
        grid-template-columns: 2fr 1fr;
        grid-template-rows: repeat(2, 1fr);
        gap: 12px;
        width: 100%;
        height: 240px;
    ">
        <div class="main-image" style="
            grid-row: 1 / span 2;
            grid-column: 1 / 2;
            border-radius: 8px;
            overflow: hidden;
            border: 1px solid #ddd;
            height: auto; /* 이미지 비율에 따라 높이 자동 조정 */
            display: flex;
            align-items: center;
            justify-content: center;
        ">
            <img src="${imageUrls.length > 0 ? imageUrls [0] : fallback}" alt="대표 이미지" onerror="this.src='${fallback}'"
                 style="cursor: pointer;
                        width: 100%;
                        max-height: 100%; /* 최대 높이 설정 */
                        object-fit: contain; /* 이미지 비율 유지하며 모두 보이게 */
                        border-radius: 8px;
                        display: block;
                 " />
        </div>

        <div class="thumbnail-grid" style="
            grid-row: 1 / span 2;
            grid-column: 2 / 3;
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            grid-template-rows: repeat(2, 1fr);
            gap: 6px;
            width: 100%;
            height: 100%;
        ">
            ${thumbnailsToShow.map((url, i) => {
                const isLastThumbnailSlot = (i === thumbnailsToShow.length - 1);

                const thumbnailItemStyle = `
                    border-radius: 6px;
                    overflow: hidden;
                    border: 1px solid #ddd;
                    position: relative;
                `;

                const imageTag = `<img src="${url}" alt="썸네일" onerror="this.src='${fallback}'" data-index="${i + 1}"
                                     style="width: 100%; height: 100%; object-fit: cover; border-radius: 6px; cursor: pointer; display: block;" />`;

                if (isLastThumbnailSlot && hiddenCount > 0) {
                    return `
                        <div class="image-item thumbnail-more" style="${thumbnailItemStyle}">
                            ${imageTag}
                            <div class="more-count" style="
                                position: absolute; top: 0; left: 0;
                                width: 100%; height: 100%;
                                background-color: rgba(0, 0, 0, 0.5);
                                color: #fff;
                                font-weight: 600; font-size: 16px;
                                display: flex; align-items: center; justify-content: center;
                                pointer-events: none; border-radius: 6px; z-index: 10;
                            ">+${hiddenCount}</div>
                        </div>
                    `;
                }

                return `
                    <div class="image-item" style="${thumbnailItemStyle}">
                        ${imageTag}
                    </div>
                `;
            }).join('')}

            ${imageUrls.length === 0 ? `
                <div class="image-item" style="
                    border-radius: 6px; overflow: hidden; border: 1px solid #ddd;
                ">
                    <img src="${fallback}" alt="No Image" style="width:100%; height:100%; object-fit:cover; border-radius: 6px;">
                </div>
            ` : ''}
        </div>
    </div>
    `;
};


// 갤러리 확대 모달 열기 (기존 userList.js 함수 유지)
function openGalleryModal(index) {
    const modal = document.getElementById('imageGalleryModal');
    const imgEl = document.getElementById('galleryFullImage');
    const fallback = `${contextPath}/volt/assets/img/illustrations/no-image.png`;

    if (typeof index !== 'number' || index < 0 || index >= listingGalleryImages.length) {
        index = 0;
    }
    currentGalleryIndex = index;

    imgEl.onerror = null;
    imgEl.src = listingGalleryImages[currentGalleryIndex] || fallback;
    imgEl.onerror = function() {
        if (imgEl.src !== fallback) {
            imgEl.src = fallback;
        }
    };

    $(modal).modal('show');
    updateGalleryControls();
}

// 갤러리 이미지 변경 (이전/다음 버튼) (기존 userList.js 함수 유지)
function changeGalleryImage(delta) {
    currentGalleryIndex += delta;
    if (currentGalleryIndex < 0) currentGalleryIndex = listingGalleryImages.length - 1;
    if (currentGalleryIndex >= listingGalleryImages.length) currentGalleryIndex = 0;

    const imgEl = document.getElementById('galleryFullImage');
    const fallback = `${contextPath}/volt/assets/img/illustrations/no-image.png`;

    imgEl.onerror = null;
    imgEl.src = listingGalleryImages[currentGalleryIndex] || fallback;
    imgEl.onerror = function() {
        if (imgEl.src !== fallback) {
            imgEl.src = fallback;
        }
    };
    updateGalleryControls();
}

// 갤러리 컨트롤 (이전/다음 버튼) 가시성 업데이트 및 이벤트 바인딩 (기존 userList.js 함수 유지)
function updateGalleryControls() {
    const prevBtn = document.getElementById('galleryPrevBtn');
    const nextBtn = document.getElementById('galleryNextBtn');

    if (listingGalleryImages.length <= 1) {
        if (prevBtn) $(prevBtn).hide();
        if (nextBtn) $(nextBtn).hide();
    } else {
        if (prevBtn) $(prevBtn).show().off('click').on('click', (e) => { e.preventDefault(); changeGalleryImage(-1); });
        if (nextBtn) $(nextBtn).show().off('click').on('click', (e) => { e.preventDefault(); changeGalleryImage(1); });
    }
}

// 매물 상세 정보를 모달에 표시하는 함수 (listRenderer.js의 showDetailModal을 기반으로 수정)
window.showDetailModal = function(data) {
    // userList.jsp의 매물 상세 모달 ID는 listingDetailModal
    const modal = document.getElementById('listingDetailModal');
    const body = document.querySelector('#listingDetailModal .modal-body');

    if (!modal || !body) {
        console.error("매물 상세 모달 요소(listingDetailModal)를 찾을 수 없습니다.");
        return;
    }

    // listRenderer.js의 brokerInfo 구조를 따름 (brokerInfo 객체 안에 brokNm 등이 있음)
    const broker = {
      brokNm: data.brokerInfo?.brokNm || '-',
      reprNm: data.brokerInfo?.reprNm || '-',
      reprTelNo: data.brokerInfo?.reprTelNo || '-'
    };

    const nameCardImageUrl = data.brokerInfo?.businessCardUrl || null;



    body.innerHTML = `
    ${renderImageGallery(data.listingFiles)} <div class="detail-modal" style="padding: 20px;">
        <div class="header" style="border-bottom: 1px solid #eee; padding-bottom: 15px; margin-bottom: 20px;">
            <h2 class="listing-title" style="font-size: 1.8rem; margin-bottom: 5px; color: #333;">${data.lstgNm || '-'}</h2>
            <p class="listing-address" style="font-size: 1rem; color: #666;">${data.lstgAdd || ''} ${data.lstgAdd2 || ''}</p>
        </div>

        <div class="deal-section" style="margin-bottom: 20px; padding: 15px; border: 1px solid #ddd; border-radius: 8px; background-color: #f9f9f9;">
            <h4 style="font-size: 1.4rem; color: #007bff; margin-bottom: 15px;">가격 정보</h4>
            <ul style="list-style: none; padding: 0;">
                <li style="margin-bottom: 8px; font-size: 1.1rem;"><strong>거래유형:</strong> ${getDealType(data.lstgTypeSale)}</li>
                <li style="margin-bottom: 8px; font-size: 1.1rem;"><strong>금액:</strong> ${getDepositText(data)}</li>
                <li style="margin-bottom: 8px; font-size: 1.1rem;"><strong>관리비:</strong> ${data.lstgMgmtPrice ? `${data.lstgMgmtPrice.toLocaleString()}원` : '없음'}</li>
                <li style="margin-bottom: 8px; font-size: 1.1rem;">
                  <strong>면적:</strong>
                  <span id="area-display" data-unit="m2" data-gr-area="${data.lstgGrArea}">
                    ${data.lstgGrArea || 0}㎡
                  </span>
                  <button id="toggle-unit-btn" class="btn btn-sm btn-outline-secondary ml-2">㎡ → 평</button>
                </li>
                <li style="margin-bottom: 8px; font-size: 1.1rem;"><strong>방 개수:</strong> ${data.lstgRoomCnt || '-'}개</li>
                <li style="margin-bottom: 8px; font-size: 1.1rem;"><strong>층수:</strong> ${data.lstgFloor || 'N/A'}</li>
                <li style="margin-bottom: 8px; font-size: 1.1rem;"><strong>주차:</strong> ${data.lstgParkYn === 'Y' ? '가능' : '불가능'}</li>
            </ul>
        </div>

        <div class="option-section" style="margin-bottom: 20px; padding: 15px; border: 1px solid #ddd; border-radius: 8px; background-color: #f9f9f9;">
            <h4 style="font-size: 1.4rem; color: #007bff; margin-bottom: 15px;">시설 옵션</h4>
           ${renderFacilityOptions(data.facOptions)}
        </div>

        <div class="broker-section" style="margin-bottom: 20px; padding: 15px; border: 1px solid #ddd; border-radius: 8px; background-color: #f9f9f9;">
            <h4 style="font-size: 1.4rem; color: #007bff; margin-bottom: 15px;">중개사 정보</h4>
            <ul style="list-style: none; padding: 0;">
                <li style="margin-bottom: 8px; font-size: 1.1rem;"><strong>중개사명:</strong> ${broker.brokNm || '-'}</li>
                <li style="margin-bottom: 8px; font-size: 1.1rem;"><strong>대표자명:</strong> ${broker.reprNm || '-'}</li>
                <li style="margin-bottom: 8px; font-size: 1.1rem;"><strong>연락처:</strong> ${formatPhoneNumber(broker.reprTelNo) || '-'}</li>
            </ul>
            ${nameCardImageUrl ? `<div class="namecard-wrapper" style="margin-top: 10px; text-align: center;"><img src="${nameCardImageUrl}" alt="명함 이미지" style="max-width: 100%; height: auto; border-radius: 4px;" /></div>` : ''}
        </div>

        <div class="detail-actions" style="display: flex; justify-content: flex-end; align-items: center; padding-top: 20px; border-top: 1px solid #eee;">
            </div>
    </div>
    `;

    // 면적 단위 토글 버튼 이벤트 리스너 (listRenderer.js에서 가져옴)
    $(body).off('click', '#toggle-unit-btn').on('click', '#toggle-unit-btn', function() {
        const areaDisplay = $(body).find('#area-display');
        const currentUnit = areaDisplay.data('unit');
        const grArea = parseFloat(areaDisplay.data('gr-area'));

        if (isNaN(grArea)) return;

        if (currentUnit === 'm2') {
            const pyeong = (grArea * 0.3025).toFixed(2);
            areaDisplay.text(`${pyeong}평`);
            areaDisplay.data('unit', 'pyeong');
            $(this).text('평 → ㎡');
        } else {
            areaDisplay.text(`${grArea}㎡`);
            areaDisplay.data('unit', 'm2');
            $(this).text('㎡ → 평');
        }
    });


    // 이미지 클릭 이벤트 재바인딩 (body.innerHTML로 인해 기존 이벤트가 제거되었을 수 있음)
    // .image-gallery .main-image img와 .thumbnail-grid .image-item img에 대한 클릭 이벤트는
    // renderImageGallery 함수에 의해 생성된 HTML 요소에 대해 동작해야 합니다.
    $(body).off('click', '.image-gallery .main-image img').on('click', '.image-gallery .main-image img', function() {
        openGalleryModal(0);
    });

    $(body).off('click', '.thumbnail-grid .image-item img').on('click', '.thumbnail-grid .image-item img', function() {
        const index = parseInt($(this).data('index'));
        openGalleryModal(index);
    });

    // 매물 상세 모달 표시
    $('#listingDetailModal').modal('show');

    console.log('받은 상세 데이터:', data);
    console.log('📦 brokerInfo:', data.brokerInfo);
};

// openDetailModal 함수는 userList.js에서 axios를 통해 데이터를 가져온 후
// window.showDetailModal을 호출하는 역할을 합니다. (기존 userList.js 함수 유지)
window.openDetailModal = function(lstgId) {
    console.log("매물 상세 확인용 모달 열기 요청:", lstgId);
    const mbrCd = window.loggedInUserId || '';
    fetch('/map/api/viewLog', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({
            lstgId: lstgId,
            mbrCd: mbrCd
        })
    }).catch(console.warn);

    const url = `${contextPath}/map/api/detail?lstgId=${lstgId}&mbrCd=${encodeURIComponent(mbrCd)}`;
    axios.get(url)
        .then(response => {
            const data = response.data;
            if (!data || Object.keys(data).length === 0) {
                throw new Error("매물 상세 데이터가 비어있습니다.");
            }
            // 이제 이 showDetailModal은 listRenderer.js의 로직을 따르는 함수입니다.
            window.showDetailModal(data);
        })
        .catch(error => {
            console.error("매물 상세 정보 로드 실패:", error);
            if (typeof Swal !== 'undefined') {
                Swal.fire({
                    icon: 'error',
                    title: '매물 정보 로드 실패',
                    text: error.message || '매물 정보를 불러오는 중 오류가 발생했습니다.',
                    confirmButtonText: '확인'
                });
            } else {
                alert('매물 정보를 불러오는 중 오류가 발생했습니다: ' + (error.message || '알 수 없는 오류'));
            }
        });
};


// =====================================================================================================
// 신고 관리 페이지 로직 시작
// =====================================================================================================

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

        axios.get(`${contextPath}/axios/admin/report/detail/${reportId}`)
            .then(response => {
                const data = response.data;
                console.log("신고 상세 정보:", data);

                $('#modalReportId').text(data.rptId || 'null');
                $('#modalBrdTitlNm').text(data.brdTitlNm  ? data.brdTitlNm : '제목 없음');
                $('#modalBrdCont').html(data.brdCont ? data.brdCont.replace(/\n/g, '<br>') : '내용 없음');

                const isListingReport = (data.rptCode === 'LSTG');
                const $modalTargetIdLabel = $('#modalTargetIdLabel');
                const $modalRptTargetId = $('#modalRptTargetId');

                const $memberSpecificInfo = $('#memberSpecificInfo');
                const $modalNewMbrStatus = $('#modalNewMbrStatus');
                const $listingSpecificInfo = $('#listingSpecificInfo');
                const $modalNewLtsgDel = $('#modalNewLtsgDel');

                $memberSpecificInfo.hide();
                $listingSpecificInfo.hide();

                if (isListingReport) {
                    $modalTargetIdLabel.text('피신고매물 ID : ');
                    const lstgId = data.rptTargetId;

                    const listingLinkHtml = `<a href="#" class="listing-detail-link" data-lstg-id="${lstgId}">${lstgId || 'N/A'}</a>`;
                    $modalRptTargetId.html(listingLinkHtml);
                    $modalRptTargetId.data('lstg-id', lstgId);

                    $modalRptTargetId.off('click', '.listing-detail-link').on('click', '.listing-detail-link', function(event) {
                        event.preventDefault();
                        event.stopPropagation();
                        const clickedLstgId = $(this).data('lstg-id');
                        console.log("매물 상세 모달 열기 요청 (신고모달에서):", clickedLstgId);

                        if (typeof window.openDetailModal === 'function') {
                            window.openDetailModal(clickedLstgId);
                        } else {
                            console.error("window.openDetailModal 함수를 찾을 수 없습니다.");
                            Swal.fire({
                                icon: 'error',
                                title: '오류',
                                text: '매물 상세 페이지를 불러올 수 없습니다.',
                                confirmButtonText: '확인'
                            });
                        }
                    });

                    $listingSpecificInfo.show();
                    $('#modalNewLtsgDel').val(data.lstgDel);

                    $('#btnProcessAllChanges').data('original-lstg-del', data.lstgDel);
                    $('#btnProcessAllChanges').data('lstg-id', data.rptTargetId);
                    $('#btnProcessAllChanges').removeData('original-mbr-status');
                    $('#btnProcessAllChanges').removeData('mbr-cd');

                } else {
                    $modalTargetIdLabel.text('피신고자 ID : ');
                    $modalRptTargetId.html(data.rptTargetId || 'N/A');
                    $modalRptTargetId.removeData('lstg-id');
                    $modalRptTargetId.off('click', '.listing-detail-link');

                    $memberSpecificInfo.show();
                    $('#modalNewMbrStatus').val(data.rptTargetMbrStatus);

                    $('#btnProcessAllChanges').data('original-mbr-status', data.rptTargetMbrStatus);
                    $('#btnProcessAllChanges').data('mbr-cd', data.rptTargetMbrCd);
                    $('#btnProcessAllChanges').removeData('original-lstg-del');
                    $('#btnProcessAllChanges').removeData('lstg-id');
                }

                $('#modalRptStatusCode').val(data.rptStatusCode);
                $('#btnProcessAllChanges').data('report-id', data.rptId);
                $('#btnProcessAllChanges').data('original-rpt-status', data.rptStatusCode);

                // --- 첨부파일 관련 로직 시작 ---
                const fileDataHolder = document.querySelector("#reportDetailModal #fileDataHolder");
                const fileListJson = JSON.stringify(data.attachFiles || []);
				fileDataHolder.setAttribute("data-filelist", fileListJson);

                currentFileList = JSON.parse(fileListJson);
                currentIndex = 0;

                renderFileTable();
                updatePageIndicator();

                if (currentFileList.length > 0) {
                    fetchPdfOrImage(currentFileList[currentIndex]);
                } else {
                    const canvas = document.querySelector("#reportDetailModal #pdfCanvas");
                    const ctx = canvas.getContext("2d");
                    ctx.clearRect(0, 0, canvas.width, canvas.height);
                    canvas.width = 1;
                    canvas.height = 1;
                }

                $('#reportDetailModal #prevBtn').off('click').on('click', prevFile);
                $('#reportDetailModal #nextBtn').off('click').on('click', nextFile);

                const toggleBtn = document.querySelector("#reportDetailModal #toggleFileListBtn");
                const fileTable = document.querySelector("#reportDetailModal #fileTable");
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
                // --- 첨부파일 관련 로직 끝 ---

                $('#reportDetailModal').modal('show');
            })
            .catch(error => {
                console.error('신고 상세 정보 로드 실패:', error);
                Swal.fire({
                    icon: 'error',
                    title: '오류',
                    text: '신고 상세 정보를 불러오는 데 실패했습니다.',
                    confirmButtonText: '확인'
                });
            });
    });

    $('#closeReportDetailModalBtnX').on('click', function() {
        $('#reportDetailModal').modal('hide');
    });
    $('#closeListingDetailModalBtnX').on('click', function() {
        $('#listingDetailModal').modal('hide');
    });
    $('#closeReportDetailModalBtn').on('click', function() {
        $('#reportDetailModal').modal('hide');
    });
    $('#closeListingDetailModalBtn').on('click', function() {
        $('#listingDetailModal').modal('hide');
    });
    $('#CloseImageGalleryModalBtnX').on('click', function() {
        $('#imageGalleryModal').modal('hide');
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
            Swal.fire({
                icon: 'info',
                title: '알림',
                text: '변경할 내용이 없습니다.',
                confirmButtonText: '확인'
            });
            return;
        }

        Swal.fire({
            title: '모든 변경 사항을 저장하시겠습니까?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33'
        }).then((result) => {
            if (result.isConfirmed) {
                Promise.all(promises)
                    .then(() => {
                        let finalMessage = "";
                        if (successMessages.length > 0) {
                            finalMessage += successMessages.join('\n');
                        }
                        if (errorMessages.length > 0) {
                            if (finalMessage !== "") finalMessage += "\n\n";
                            finalMessage += errorMessages.join('\n');
                        }

                        Swal.fire({
                            title: '처리 완료',
                            text: finalMessage || "모든 변경 사항이 성공적으로 처리되었습니다.",
                            icon: errorMessages.length > 0 ? 'warning' : 'success',
                            confirmButtonText: '확인'
                        }).then(() => {
                            $('#reportDetailModal').modal('hide');
                            window.location.reload();
                        });
                    })
                    .catch(allErrors => {
                        console.error('모든 Promise 처리 중 오류 발생:', allErrors);
                        Swal.fire({
                            icon: 'error',
                            title: '오류',
                            text: '일부 변경 사항 처리 중 오류가 발생했습니다.',
                            confirmButtonText: '확인'
                        }).then(() => {
                            $('#reportDetailModal').modal('hide');
                            window.location.reload();
                        });
                    });
            }
        });
    });

    // 모달이 닫힐 때 파일 미리보기 상태 초기화
    $('#reportDetailModal').on('hidden.bs.modal', function () {
        currentFileList = [];
        currentIndex = 0;
        isRendering = false;
        const canvas = document.querySelector("#reportDetailModal #pdfCanvas");
        const ctx = canvas.getContext("2d");
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        canvas.width = 1;
        canvas.height = 1;
        renderFileTable();
        updatePageIndicator();
    });

    // 매물 상세 모달이 닫힐 때 갤러리 상태 초기화 (userList.js 기존 함수 유지)
    $('#listingDetailModal').on('hidden.bs.modal', function () {
        listingGalleryImages = [];
        currentGalleryIndex = 0;
        // listingDetailModal의 modal-body를 비워줍니다.
        const modalBody = document.querySelector('#listingDetailModal .modal-body');
        if (modalBody) {
            modalBody.innerHTML = '';
        }
    });
});