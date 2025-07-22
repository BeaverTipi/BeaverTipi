// /app/js/resident/residentBuilding.js

function loadPosts(bldgId, page = 1, pageSize = 10) {
  if (!bldgId) return;

  const postListContainer = document.querySelector('.post-list');
  const tableBody = document.querySelector('#boardTableBody');
  const paginationWrapper = document.querySelector('.pagination-wrapper');

//  postListContainer.innerHTML = '<div class="loading">로딩 중...</div>';
  tableBody.innerHTML = '';
  paginationWrapper.innerHTML = '';

  // 검색 조건 가져오기
  const searchType = document.querySelector('select[name="searchType"]').value;
  const searchWord = document.querySelector('input[name="searchWord"]').value;
  const searchStartDate = document.querySelector('input[name="searchStartDate"]').value;
  const searchEndDate = document.querySelector('input[name="searchEndDate"]').value;

  // 쿼리 구성
  const params = new URLSearchParams({
    bldgIdParam: bldgId,
    page,
    pageSize,
    searchType,
    searchWord,
    searchStartDate,
    searchEndDate
  });

  axios.get(`/ajax/resident/api/board?${params.toString()}`)
    .then(response => {
      const { postList, pagination } = response.data;
      displayPosts(postList, pagination);
      renderPagination(pagination);
    })
    .catch(error => {
      console.error("게시글 로드 실패:", error);
      tableBody.innerHTML = `<tr><td colspan="6">불러오는 데 실패했습니다.</td></tr>`;
    });
}

function displayPosts(posts, pagination) {
  const tableBody = document.querySelector('#boardTableBody');
  tableBody.innerHTML = '';

  if (!posts || posts.length === 0) {
    tableBody.innerHTML = `
      <tr>
        <td colspan="6" class="no-data-center">게시글이 없습니다.</td>
      </tr>`;
    return;
  }

  posts.forEach((post, index) => {
    const row = document.createElement('tr');
    row.innerHTML = `
      <td>${pagination.firstRecordIndex + index}</td>
      <td>${post.rsdBrdTitl}</td>
      <td>${post.mbrNnm}</td>
      <td>${new Date(post.rsdBrdPblsDtm).toLocaleDateString()}</td>
      <td>${post.rsdBrdCnt || 0}</td>
      <td>
        <form method="get" action="/resident/board/detail" style="display:inline;">
          <input type="hidden" name="rsdBrdId" value="${post.rsdBrdId}" />
          <input type="hidden" name="bldgIdParam" value="${post.bldgId}" />
          <button type="submit" class="btn-view">보기</button>
        </form>
      </td>
    `;
    tableBody.appendChild(row);
  });
}

function renderPagination(pagination) {
  const wrapper = document.querySelector('.pagination-wrapper');
  wrapper.innerHTML = '';

  const ul = document.createElement('ul');
  ul.className = 'pagination';

  // 이전 페이지
  if (pagination.currentPageNo > 1) {
    const prevLi = document.createElement('li');
    prevLi.innerHTML = `<a href="#">«</a>`;
    prevLi.addEventListener('click', () => loadPosts(pagination.simpleSearch.bldgId, pagination.currentPageNo - 1));
    ul.appendChild(prevLi);
  }

  for (let i = pagination.firstPageNoOnPageList; i <= pagination.lastPageNoOnPageList; i++) {
    const li = document.createElement('li');
    li.className = (i === pagination.currentPageNo) ? 'active' : '';
    li.innerHTML = `<a href="#">${i}</a>`;
    li.addEventListener('click', () => loadPosts(pagination.simpleSearch.bldgId, i));
    ul.appendChild(li);
  }

  // 다음 페이지
  if (pagination.currentPageNo < pagination.totalPageCount) {
    const nextLi = document.createElement('li');
    nextLi.innerHTML = `<a href="#">»</a>`;
    nextLi.addEventListener('click', () => loadPosts(pagination.simpleSearch.bldgId, pagination.currentPageNo + 1));
    ul.appendChild(nextLi);
  }

  wrapper.appendChild(ul);
}


