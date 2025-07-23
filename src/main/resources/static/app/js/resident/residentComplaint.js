// 📌 날짜 포맷 함수
function formatDate(dateStr) {
  if (!dateStr) return "";
  const date = new Date(dateStr);
  const yyyy = date.getFullYear();
  const mm = String(date.getMonth() + 1).padStart(2, "0");
  const dd = String(date.getDate()).padStart(2, "0");
  return `${yyyy}-${mm}-${dd}`;
}

// 📌 게시글 목록 렌더링
function renderComplaintPosts(posts, loginMbrCd,isLandlord) {
  const tableBody = document.querySelector("#boardTableBody");
  tableBody.innerHTML = "";

  if (!posts || posts.length === 0) {
    tableBody.innerHTML = `
      <tr>
        <td colspan="6" class="no-data-center">검색 결과가 없습니다.</td>
      </tr>
    `;
    return;
  }

  posts.forEach((post) => {
    const isOwner = post.mbrCd === loginMbrCd;
    const isPublic = post.openYn === "Y";
    const isVisible = isPublic || isOwner || isLandlord;

    const titleHtml = isVisible
      ? post.rsdBrdTitl
      : `<span class="text-muted">비공개 글입니다.</span>`;

    const viewButtonHtml = isVisible
      ? `
        <form method="get" action="/resident/complaint/view">
          <input type="hidden" name="rsdBrdId" value="${post.rsdBrdId}" />
          <input type="hidden" name="bldgIdParam" value="${post.bldgId}" />
          <button type="submit" class="btn-view">보기</button>
        </form>
      `
      : `<button type="button" class="btn-view" onclick="showPrivateAlert()">보기</button>`;

    const 공개여부 = post.openYn === "Y"
      ? `<span class="badge badge-blue">공개</span>`
      : `<span class="badge badge-dark">비공개</span>`;

    const 처리상태 = post.reqStatus === "001"
      ? `<span class="badge badge-orange">처리중</span>`
      : post.reqStatus === "002"
      ? `<span class="badge badge-green">처리완료</span>`
      : "";

    const formattedDate = formatDate(post.rsdBrdPblsDate);

    const rowHtml = `
      <tr>
        <td>${post.mbrNnm}</td>
        <td>${titleHtml}</td>
        <td>${공개여부}</td>
        <td>${처리상태}</td>
        <td>${formattedDate}</td>
        <td>${viewButtonHtml}</td>
      </tr>
    `;

    tableBody.insertAdjacentHTML("beforeend", rowHtml);
  });
}

// 📌 페이징 HTML 렌더링
function renderPagination(pagingHtml) {
  const wrapper = document.querySelector(".pagination-wrapper");
  wrapper.innerHTML = pagingHtml;

  wrapper.querySelectorAll("a").forEach(link => {
    link.addEventListener("click", function (e) {
      e.preventDefault();
      const pageNo = this.getAttribute("data-page");
      if (pageNo) {
        loadComplaints(currentBuildingId, pageNo);
      }
    });
  });
}

// 🔁 전역 변수
let currentBuildingId = localStorage.getItem("selectedBuildingId") || "";

// 📌 민원 게시글 로드
function loadComplaints(bldgId = currentBuildingId, page = 1) {
  if (!bldgId) return;

  currentBuildingId = bldgId;
  localStorage.setItem("selectedBuildingId", bldgId);

  const form = document.getElementById("searchForm");
  const params = new URLSearchParams({
    bldgIdParam: bldgId,
    page,
    searchType: form.searchType?.value || "",
    searchWord: form.searchWord?.value || "",
    openYn: form.openYn?.value || "",
    reqStatus: form.reqStatus?.value || "",
    searchStartDate: form.searchStartDate?.value || "",
    searchEndDate: form.searchEndDate?.value || ""
  });

  axios.get(`/ajax/resident/api/complaints?${params.toString()}`)
    .then(res => {
      const { postList, pagination, loginMbrCd,isLandlord } = res.data;
      renderComplaintPosts(postList, loginMbrCd, isLandlord); // ✅ 사용자 ID도 전달
      renderPagination(pagination);
    })
    .catch(err => {
      console.error("민원 목록 불러오기 실패:", err);
    });
}

// 📌 SweetAlert 비공개 알림
function showPrivateAlert() {
  Swal.fire({
    icon: 'warning',
    title: '비공개 글입니다',
    text: '작성자만 확인할 수 있습니다.',
    confirmButtonColor: '#E17100'
  });
}

// 📌 초기화 및 검색 이벤트 바인딩
document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("searchForm");
  if (form) {
    form.addEventListener("submit", function (e) {
      e.preventDefault();
      loadComplaints(currentBuildingId, 1);
    });
  }

  loadComplaints(currentBuildingId, 1);
});
