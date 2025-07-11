let fileList = [];
let fileIds = [];
let currentIndex = 0;
let isRendering = false;

document.addEventListener("DOMContentLoaded", () => {
  // JSP에서 data-filelist 속성으로 JSON 문자열 받아서 파싱
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
    fetchPdf(fileIds[currentIndex]);
  }

  document.querySelector("#prevBtn").addEventListener("click", prev);
  document.querySelector("#nextBtn").addEventListener("click", next);
});

function renderFileTable() {
  const tbody = document.querySelector("#fileTable tbody");
  tbody.innerHTML = "";
  fileList.forEach(file => {
    const row = document.createElement("tr");
    row.innerHTML = `<td>${file.fileOriginalname}</td><td>${file.fileSize} bytes</td>`;
    tbody.appendChild(row);
  });
}

function fetchPdf(fileId) {
  if (isRendering) return;
  isRendering = true;

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

function next() {
  if (currentIndex < fileIds.length - 1) {
    currentIndex++;
    fetchPdf(fileIds[currentIndex]);
  }
}

function prev() {
  if (currentIndex > 0) {
    currentIndex--;
    fetchPdf(fileIds[currentIndex]);
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
