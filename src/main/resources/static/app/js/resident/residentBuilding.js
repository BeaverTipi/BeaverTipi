// /app/js/resident/residentBuilding.js

function loadPosts(bldgId) {
  if (!bldgId) return;

  const postListContainer = document.querySelector('.post-list');
  if (!postListContainer) {
    console.error('게시글 목록을 표시할 요소가 없습니다.');
    return;
  }


  axios.get(`/ajax/resident/api/board?bldgIdParam=${bldgId}`)
    .then(response => {
      displayPosts(response.data);
    })
    .catch(error => {
      console.error("게시글 로드 중 오류 발생:", error);
      postListContainer.innerHTML = '<p>게시글을 불러오는 데 실패했습니다.</p>';
    });
}

function displayPosts(posts) {
  const tableBody = document.querySelector('#boardTableBody');
  if (!tableBody) return;

  tableBody.innerHTML = '';

  if (!posts || posts.length === 0) {
    tableBody.innerHTML = `
      <tr>
        <td colspan="6" class="no-data-center">게시글이 없습니다.</td>
      </tr>
    `;
  } else {
    posts.forEach((post, index) => {
      const row = document.createElement('tr');
      row.innerHTML = `
        <td>${index + 1}</td>
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
}

// 외부에서 사용 가능하게 export
window.loadPosts = loadPosts;
