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
function renderComplaintPosts(posts, loginMbrCd, isLandlord, currentPageNo,pageSize) {
  const tableBody = document.querySelector("#boardTableBody");
  tableBody.innerHTML = "";

  if (!posts?.length) {
    tableBody.innerHTML = `
      <tr><td colspan="6" class="no-data-center">검색 결과가 없습니다.</td></tr>
    `;
    return;
  }
  const startIdx = (currentPageNo - 1) * pageSize + 1;
  
  posts.forEach((post,idx) => {
    const isOwner = post.mbrCd === loginMbrCd;
    const isPublic = post.openYn === "Y";
    const isVisible = isPublic || isOwner || isLandlord;

    const titleHtml = isVisible
    ? `<a href="/resident/complaint/view?rsdBrdId=${post.rsdBrdId}&bldgIdParam=${post.bldgId}" 
           class="notice-title" title="${post.rsdBrdTitl}">
         ${post.rsdBrdTitl}
       </a>`
    : `<a href="javascript:void(0)" onclick="showPrivateAlert()" class="notice-title text-muted">
         🔒 비공개 글입니다.
       </a>`;
    const 공개여부 = post.openYn === "Y"
      ? `<span class="badge badge-blue">공개</span>`
      : `<span class="badge badge-dark">비공개</span>`;

    const 처리상태 = {
      "001": `<span class="badge badge-orange">처리중</span>`,
      "002": `<span class="badge badge-green">처리완료</span>`
    }[post.reqStatus] || "";

    const rowHtml = `
      <tr>
        <td>${startIdx + idx}</td>
        <td>${post.mbrNnm}</td>
        <td>${titleHtml}</td>
        <td>${공개여부}</td>
        <td>${처리상태}</td>
        <td>${formatDate(post.rsdBrdPblsDate)}</td>
      </tr>
    `;

    tableBody.insertAdjacentHTML("beforeend", rowHtml);
  });
}

// 🔁 전역 변수
let currentBuildingId = localStorage.getItem("selectedBuildingId") || "";

// 📌 민원 게시글 로드 (최초 입주 건물 포함)
function loadComplaints(bldgId = currentBuildingId, page = 1, pageSize = 10) {
  page = parseInt(page) || 1;

  if (window.location.pathname.includes("/resident/complaint/form")) return;

  // selectedBuildingId가 없는 경우, 최초 입주 건물 불러오기
  if (!bldgId) {
    axios.get("/ajax/resident/api/complaints/initial")
      .then(res => {
        const { bldgId: initBldgId } = res.data;
        if (!initBldgId) {
          document.querySelector("#boardTableBody").innerHTML = `
            <tr><td colspan="6" class="no-data-center">민원 데이터가 없습니다.</td></tr>
          `;
          return;
        }
        localStorage.setItem("selectedBuildingId", initBldgId);
        currentBuildingId = initBldgId;

        // 초기 건물로 다시 로딩
        loadComplaints(initBldgId, page,pageSize);
      })
      .catch(err => console.error("최초 건물 게시글 로딩 실패:", err));
    return;
  }

  currentBuildingId = bldgId;
  localStorage.setItem("selectedBuildingId", bldgId);

  const form = document.getElementById("searchForm");
  const params = new URLSearchParams({
    bldgIdParam: bldgId,
    page,
    pageSize,
    searchType: form.searchType?.value || "",
    searchWord: form.searchWord?.value || "",
    openYn: form.openYn?.value || "",
    reqStatus: form.reqStatus?.value || "",
    searchStartDate: form.searchStartDate?.value || "",
    searchEndDate: form.searchEndDate?.value || "",
    myPostsOnly: form.myPostsOnly?.checked ? "Y" : "N"
  });

  axios.get(`/ajax/resident/api/complaints?${params.toString()}`)
    .then(res => {
      const { postList, pagination, loginMbrCd, isLandlord } = res.data;
      renderComplaintPosts(postList, loginMbrCd, isLandlord,page,pageSize);
      renderPagination(pagination, currentBuildingId, page, pageSize);
    })
    .catch(err => console.error("민원 목록 불러오기 실패:", err));
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

// 📌 폼 초기화 함수
function clearForm(e) {
  e.preventDefault();
  const form = document.getElementById("searchForm");
  if (!form) return;

  form.reset();
  form.page.value = 1;

  loadComplaints(currentBuildingId, 1);
}

// 📌 페이징 처리
function renderPagination(pagination, bldgId, currentPageNo, pageSize) {
  const wrapper = document.querySelector('.pagination-wrapper');
  wrapper.innerHTML = '';

  const ul = document.createElement('ul');
  ul.className = 'pagination';

  // ◀ 이전 버튼
  if (currentPageNo > 1) {
    const prevLi = document.createElement('li');
    prevLi.innerHTML = `<a href="javascript:void(0)">«</a>`;
    prevLi.addEventListener('click', () =>
      loadComplaints(bldgId, currentPageNo - 1, pageSize)
    );
    ul.appendChild(prevLi);
  }

  // 🔢 숫자 버튼
  for (let i = pagination.firstPageNoOnPageList; i <= pagination.lastPageNoOnPageList; i++) {
    const li = document.createElement('li');
    li.className = (i === currentPageNo) ? 'active' : '';
    li.innerHTML = `<a href="javascript:void(0)">${i}</a>`;
    li.addEventListener('click', () =>
      loadComplaints(bldgId, i, pageSize)
    );
    ul.appendChild(li);
  }

  // ▶ 다음 버튼
  if (currentPageNo < pagination.totalPageCount) {
    const nextLi = document.createElement('li');
    nextLi.innerHTML = `<a href="javascript:void(0)">»</a>`;
    nextLi.addEventListener('click', () =>
      loadComplaints(bldgId, currentPageNo + 1, pageSize)
    );
    ul.appendChild(nextLi);
  }

  wrapper.appendChild(ul);
}

// 📌 초기화 및 이벤트 바인딩
document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("searchForm");
  form?.addEventListener("submit", (e) => {
    e.preventDefault();
    loadComplaints(currentBuildingId, 1);
  });

  const checkbox = document.querySelector("#myPostsOnly");
  checkbox?.addEventListener("change", () => loadComplaints(currentBuildingId, 1));

  loadComplaints(currentBuildingId); // 최초 로딩
});
