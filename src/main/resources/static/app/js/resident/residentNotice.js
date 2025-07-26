// 📌 날짜 포맷 함수
function formatDate(dateStr) {
  if (!dateStr) return "";
  const date = new Date(dateStr);
  const yyyy = date.getFullYear();
  const mm = String(date.getMonth() + 1).padStart(2, "0");
  const dd = String(date.getDate()).padStart(2, "0");
  return `${yyyy}-${mm}-${dd}`;
}

// 📌 null 또는 "null" 방지용 안전 빌딩 ID 가져오기
function getSafeBldgId() {
  const raw = localStorage.getItem("selectedBuildingId");
  return !raw || raw === "null" ? "" : raw;
}

// 📌 공지사항 목록 렌더링
function renderNoticePosts(posts) {
  const tableBody = document.querySelector("#noticeTableBody");
  tableBody.innerHTML = "";

  if (!posts || posts.length === 0) {
    tableBody.innerHTML = `
      <tr>
        <td colspan="6" class="no-data-center">등록된 공지사항이 없습니다.</td>
      </tr>
    `;
    return;
  }

  posts.forEach((post, idx) => {
    const titleShort = post.brdTitlNm.length > 30 ? `${post.brdTitlNm.slice(0, 30)}...` : post.brdTitlNm;
    const formattedDate = formatDate(post.brdPblsDtm);

    const rowHtml = `
      <tr>
        <td>${idx + 1}</td>
        <td>${post.noticeTypeCode?.codeName || ""}</td>
        <td title="${post.brdTitlNm}">
          <a href="/resident/notice/detail?noticeNo=${post.noticeNo}&bldgIdParam=${getSafeBldgId()}"
             class="notice-title" title="${post.brdTitlNm}">
            ${titleShort}
          </a>
        </td>
        <td>${post.member?.mbrNnm || ""}</td>
        <td>${formattedDate}</td>
        <td>${post.brdVwCnt}</td>
      </tr>
    `;

    tableBody.insertAdjacentHTML("beforeend", rowHtml);
  });
}

// 📌 페이징 HTML 렌더링
function renderPagination(pagination) {
  const wrapper = document.querySelector(".pagination-wrapper");
  wrapper.innerHTML = "";

  if (!pagination || pagination.totalPageCount === 0) return;

  const { currentPageNo, firstPageNoOnPageList, lastPageNoOnPageList } = pagination;

  let html = "";
  for (let i = firstPageNoOnPageList; i <= lastPageNoOnPageList; i++) {
    html += `<button class="page-btn ${i === currentPageNo ? 'active' : ''}" data-page="${i}">${i}</button>`;
  }

  wrapper.innerHTML = html;

  // 버튼 이벤트 바인딩
  document.querySelectorAll(".page-btn").forEach(btn => {
    btn.addEventListener("click", (e) => {
      const page = e.target.getAttribute("data-page");
      loadNotices(page);
    });
  });
}

// 📌 공지사항 목록 불러오기
function loadNotices(page = 1) {
  const form = document.getElementById("noticeSearchForm");
  const params = new URLSearchParams({
    bldgIdParam: getSafeBldgId(),
    page,
    searchType: form.searchType?.value || "",
    searchWord: form.searchWord?.value || "",
    noticeType: form.noticeType?.value || "",
    searchStartDate: form.searchStartDate?.value || "",
    searchEndDate: form.searchEndDate?.value || ""
  });

  axios.get(`/ajax/resident/api/notice?${params.toString()}`)
    .then(res => {
      const { noticeList, pagination } = res.data;
      renderNoticePosts(noticeList);
      renderPagination(pagination);
    })
    .catch(err => {
      console.error("공지사항 불러오기 실패:", err);
    });
}

// 📌 초기화 및 검색 이벤트 바인딩
document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("noticeSearchForm");
  if (form) {
    form.addEventListener("submit", function (e) {
      e.preventDefault();
      loadNotices(1);
    });

    const resetBtn = document.querySelector(".btn-reset");
    if (resetBtn) {
      resetBtn.addEventListener("click", () => {
        const baseUrl = `${location.pathname}?bldgIdParam=${getSafeBldgId()}&page=1`;
        location.href = baseUrl;
      });
    }
  }

  loadNotices(1);
});
