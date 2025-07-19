// /app/js/resident/residentComplaint.js

function loadComplaintPosts(bldgId) {
  if (!bldgId) return;

  const postListContainer = document.querySelector('.post-list');
  if (!postListContainer) {
    console.error('게시글 목록을 표시할 요소가 없습니다.');
    return;
  }

  postListContainer.innerHTML = '<div class="loading">로딩 중...</div>';

  axios.get(`/ajax/resident/api/complaints?bldgIdParam=${bldgId}`)
    .then(response => {
      displayComplaintPosts(response.data);
    })
    .catch(error => {
      console.error("민원 게시글 로드 중 오류 발생:", error);
      postListContainer.innerHTML = '<p>게시글을 불러오는 데 실패했습니다.</p>';
    });
}

function displayComplaintPosts(posts) {
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
    posts.forEach((post) => {
      const isPrivate = post.openYn === 'N';
      const isAuthor = post.isAuthor === true; // 서버에서 이 정보 포함시켜야 함

      const titleHtml = isPrivate && !isAuthor
        ? '<span class="text-muted">비공개 글입니다.</span>'
        : post.rsdBrdTitl;

      const viewButtonHtml = isPrivate && !isAuthor
        ? `<button type="button" class="btn-view" onclick="showPrivateAlert()">보기</button>`
        : `
          <form method="get" action="/resident/complaint/view" style="display:inline;">
            <input type="hidden" name="rsdBrdId" value="${post.rsdBrdId}" />
            <input type="hidden" name="bldgIdParam" value="${post.bldgId}" />
            <button type="submit" class="btn-view">보기</button>
          </form>
        `;

      const openYnBadge = post.openYn === 'Y'
        ? `<span class="badge badge-blue">공개</span>`
        : `<span class="badge badge-dark">비공개</span>`;

      const reqStatusBadge = (() => {
        if (post.reqStatus === '001') {
          return `<span class="badge badge-orange">접수중</span>`;
        } else if (post.reqStatus === '002') {
          return `<span class="badge badge-green">처리완료</span>`;
        }
        return '';
      })();

      const row = document.createElement('tr');
      row.innerHTML = `
        <td>${post.mbrNnm}</td>
        <td>${titleHtml}</td>
        <td>${openYnBadge}</td>
        <td>${reqStatusBadge}</td>
        <td>${new Date(post.rsdBrdPblsDtm).toLocaleDateString()}</td>
        <td>${viewButtonHtml}</td>
      `;
      tableBody.appendChild(row);
    });
  }
}

// 비공개 알림 팝업
function showPrivateAlert() {
  Swal.fire({
    icon: 'warning',
    title: '비공개 글입니다',
    text: '작성자만 확인할 수 있습니다.',
    confirmButtonColor: '#E17100'
  });
}

window.loadComplaintPosts = loadComplaintPosts;
