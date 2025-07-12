let fileList = [];
let fileIds = [];
let currentIndex = 0;
let isRendering = false;

document.addEventListener("DOMContentLoaded", () => {
  const dataHolder = document.querySelector("#fileDataHolder");
  if (!dataHolder) {
    alert("파일 데이터 요소를 찾을 수 없습니다.");
    return;
  }

  const fileListJson = dataHolder.getAttribute("data-filelist");
  if (!fileListJson) {
    alert("파일 정보가 존재하지 않습니다.");
    return;
  }

  try {
    fileList = JSON.parse(fileListJson);
  } catch (e) {
    console.error("파일 리스트 JSON 파싱 오류:", e);
    alert("파일 정보 파싱에 실패했습니다.");
    return;
  }

  fileIds = fileList.map(f => f.fileId);

  renderFileTable();
  updatePageIndicator();

  if (fileIds.length > 0) {
    fetchPdfOrImage(fileList[currentIndex]);
  }

  document.querySelector("#prevBtn").addEventListener("click", prev);
  document.querySelector("#nextBtn").addEventListener("click", next);

  const toggleBtn = document.querySelector("#toggleFileListBtn");
  const fileTable = document.querySelector("#fileTable");
  fileTable.style.display = "none";

  toggleBtn.addEventListener("click", () => {
    if (fileTable.style.display === "none" || fileTable.style.display === "") {
      fileTable.style.display = "table";
      toggleBtn.innerText = "첨부파일 목록 숨기기";
    } else {
      fileTable.style.display = "none";
      toggleBtn.innerText = "첨부파일 목록 보기";
    }
  });
});

function renderFileTable() {
  const tbody = document.querySelector("#fileTable tbody");
  tbody.innerHTML = "";
  fileList.forEach(file => {
    const row = document.createElement("tr");
    row.innerHTML = `<td>${file.fileOriginalname || '파일명 없음'}</td><td>${file.fileSize || 0} bytes</td>`;
    tbody.appendChild(row);
  });
}

function fetchPdfOrImage(file) {
  if (!file || !file.fileMime) {
    alert("유효하지 않은 파일 정보입니다.");
    return;
  }
  if (isRendering) return;
  isRendering = true;

  if (file.fileMime === 'application/pdf') {
    fetchPdf(file.fileId);
  } else if (file.fileMime.startsWith('image/')) {
    renderImage(file.fileId);
  } else {
    alert("미리보기 지원하지 않는 파일 형식입니다.");
    isRendering = false;
  }
}

function fetchPdf(fileId) {
  axios({
    method: 'get',
    url: `/admin/business/file/preview/${fileId}`,
    responseType: 'blob'
  })
  .then(response => {
    const contentType = response.headers['content-type'];
    if (!contentType || !contentType.includes('pdf')) {
      throw new Error('PDF 파일이 아닙니다.');
    }
    const blob = response.data;
    const url = URL.createObjectURL(blob);
    return pdfjsLib.getDocument(url).promise;
  })
  .then(pdf => pdf.getPage(1))
  .then(page => {
    const scale = 1.5;
    const viewport = page.getViewport({ scale });
    const canvas = document.querySelector("#pdfCanvas");
    const context = canvas.getContext("2d");
    canvas.width = viewport.width;
    canvas.height = viewport.height;
    return page.render({ canvasContext: context, viewport }).promise;
  })
  .catch(err => {
    console.error("PDF 로딩 오류:", err);
    alert("PDF 미리보기 중 오류가 발생했습니다.");
  })
  .finally(() => {
    updatePageIndicator();
    isRendering = false;
  });
}

function renderImage(fileId) {
  const canvas = document.querySelector("#pdfCanvas");
  const ctx = canvas.getContext("2d");
  ctx.clearRect(0, 0, canvas.width, canvas.height);

  const img = new Image();
  img.onload = () => {
    // 최대 크기 지정
    const maxWidth = 800;
    const maxHeight = 600;

    let width = img.width;
    let height = img.height;

    // 비율 유지하며 크기 조절
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
    isRendering = false;
  };
  img.src = `/admin/business/file/preview/${fileId}`;
}

function next() {
  if (currentIndex < fileIds.length - 1) {
    currentIndex++;
    fetchPdfOrImage(fileList[currentIndex]);
  }
}

function prev() {
  if (currentIndex > 0) {
    currentIndex--;
    fetchPdfOrImage(fileList[currentIndex]);
  }
}

function updatePageIndicator() {
  const indicator = document.querySelector("#fileIndex");
  const total = document.querySelector("#totalCount");
  if (indicator && total) {
    indicator.innerText = fileIds.length > 0 ? currentIndex + 1 : 0;
    total.innerText = fileIds.length;
  }
}
