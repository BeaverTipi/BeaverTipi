// ✅ 게시글 목록 로드
function loadPosts(bldgId, page = 1, pageSize = 10) {
  if (!bldgId) return;

  const tableBody = document.querySelector('#boardTableBody');
  const loadingOverlay = document.querySelector('#tableLoading');

  // 👉 오버레이 표시
  loadingOverlay.style.display = 'flex';

  const searchParams = collectSearchParams(bldgId, page, pageSize);

  axios.get(`/ajax/resident/api/board?${searchParams}`)
    .then(response => {
      const { postList, pagination } = response.data;
      displayPosts(postList, pagination);
      renderPagination(pagination);
    })
    .catch(error => {
      console.error("게시글 로드 실패:", error);
      tableBody.innerHTML = `
        <tr><td colspan="5" class="error-row">게시글을 불러오는 데 실패했습니다.</td></tr>`;
    })
    .finally(() => {
      loadingOverlay.style.display = 'none'; // ✅ 항상 숨김
    });
}

// ✅ 검색 파라미터 수집
function collectSearchParams(bldgId, page, pageSize) {
  const searchType = document.querySelector('select[name="searchType"]').value;
  const searchWord = document.querySelector('input[name="searchWord"]').value;
  const searchStartDate = document.querySelector('input[name="searchStartDate"]').value;
  const searchEndDate = document.querySelector('input[name="searchEndDate"]').value;
  const myPostsOnlyCheckbox = document.querySelector('input[name="myPostsOnly"]');
  const myPostsOnly = myPostsOnlyCheckbox && myPostsOnlyCheckbox.checked ? 'Y' : '';

  return new URLSearchParams({
    bldgIdParam: bldgId,
    page,
    pageSize,
    searchType,
    searchWord,
    searchStartDate,
    searchEndDate,
    myPostsOnly
  }).toString();
}

// ✅ 게시글 테이블 렌더링
function displayPosts(posts, pagination) {
  const tableBody = document.querySelector('#boardTableBody');

  // 1️⃣ 기존 행 fade-out
  tableBody.classList.add('fade-out');

  setTimeout(() => {
    tableBody.innerHTML = '';

    if (!posts || posts.length === 0) {
      tableBody.innerHTML = `
        <tr>
          <td colspan="5" class="no-data-center">게시글이 없습니다.</td>
        </tr>`;
    } else {
      posts.forEach((post, index) => {
        const row = document.createElement('tr');
        row.innerHTML = `
          <td>${pagination.firstRecordIndex + index}</td>
          <td>
            <a href="/resident/board/detail?rsdBrdId=${post.rsdBrdId}&bldgIdParam=${post.bldgId}" 
               class="post-title-link">${post.rsdBrdTitl}</a>
          </td>
          <td>${post.mbrNnm}</td>
          <td>${new Date(post.rsdBrdPblsDtm).toLocaleDateString()}</td>
          <td>${post.rsdBrdCnt || 0}</td>
        `;
        tableBody.appendChild(row);
      });
    }

    // 2️⃣ fade-in 애니메이션
    tableBody.classList.remove('fade-out');
    tableBody.classList.add('fade-in');

    // 3️⃣ 애니메이션 클래스 제거 (다음 호출 대비)
    setTimeout(() => {
      tableBody.classList.remove('fade-in');
    }, 300);

  }, 100); // 부드럽게 바뀌도록 약간의 delay
}

// ✅ 페이지네이션 렌더링
function renderPagination(pagination) {
  const wrapper = document.querySelector('.pagination-wrapper');
  wrapper.innerHTML = '';

  const ul = document.createElement('ul');
  ul.className = 'pagination';

  // ◀ 이전 버튼
  if (pagination.currentPageNo > 1) {
    const prevLi = document.createElement('li');
    prevLi.innerHTML = `<a href="#">«</a>`;
    prevLi.addEventListener('click', () =>
      loadPosts(pagination.simpleSearch.bldgId, pagination.currentPageNo - 1)
    );
    ul.appendChild(prevLi);
  }

  // 🔢 숫자 버튼
  for (let i = pagination.firstPageNoOnPageList; i <= pagination.lastPageNoOnPageList; i++) {
    const li = document.createElement('li');
    li.className = (i === pagination.currentPageNo) ? 'active' : '';
    li.innerHTML = `<a href="#">${i}</a>`;
    li.addEventListener('click', () =>
      loadPosts(pagination.simpleSearch.bldgId, i)
    );
    ul.appendChild(li);
  }

  // ▶ 다음 버튼
  if (pagination.currentPageNo < pagination.totalPageCount) {
    const nextLi = document.createElement('li');
    nextLi.innerHTML = `<a href="#">»</a>`;
    nextLi.addEventListener('click', () =>
      loadPosts(pagination.simpleSearch.bldgId, pagination.currentPageNo + 1)
    );
    ul.appendChild(nextLi);
  }

  wrapper.appendChild(ul);
}
