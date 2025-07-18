document.addEventListener('DOMContentLoaded', () => {
  // URL에서 건물 ID 가져오기
  const urlParams = new URLSearchParams(window.location.search);
  let selectedBldgId = urlParams.get('search.bldgId');  

  console.log('URL에서 받은 건물 ID:', selectedBldgId);

  // 1. 건물 선택 셀렉터 처리
  const selector = document.querySelector('select[name="search.bldgId"]');

  // URL에 건물 ID가 있으면 로컬 스토리지에 저장
  if (selectedBldgId) {
    localStorage.setItem('selectedBuildingId', selectedBldgId);
  }

  // URL에 건물 ID가 없으면 로컬 스토리지에서 가져오기
  selectedBldgId = selectedBldgId || localStorage.getItem('selectedBuildingId');

  // 건물 ID가 있으면 셀렉터의 값 설정
  if (selector && selectedBldgId) {
    selector.value = selectedBldgId;
  }

  // 건물 선택 시 로컬 스토리지에 저장하고 게시글 로드
  if (selector) {
    selector.addEventListener('change', (e) => {
      const building = e.target.value;
      console.log('건물 선택됨:', building);
      localStorage.setItem('selectedBuildingId', building);  // 선택된 건물 ID 로컬 스토리지에 저장
      history.pushState(null, '', `?search.bldgId=${building}`);
      loadPosts(building);  // 게시글 로드
    });
  }

  // 2. 선택된 건물 ID가 있으면 해당 건물의 게시글을 로드
  if (selectedBldgId) {
    console.log("선택된 건물 ID:", selectedBldgId);
    loadPosts(selectedBldgId);
  }
});

// 게시글을 서버에서 가져오는 함수
function loadPosts(bldgId) {
  if (!bldgId) return;

  const postListContainer = document.querySelector('.post-list');
  if (!postListContainer) {
    console.error('게시글 목록을 표시할 요소가 없습니다.');
    return;
  }

  // 로딩 상태 표시
  postListContainer.innerHTML = '<div class="loading">로딩 중...</div>';

  console.log('게시글 로드 요청:', bldgId);  // 서버 요청을 디버깅하기 위해 로그 출력

  // AJAX 요청을 통해 게시글 로드
  axios.get(`/ajax/resident/api/board?bldgIdParam=${bldgId}`)
    .then(response => {
      console.log("게시글 로드 데이터:", response.data);
      displayPosts(response.data);  // 게시글을 화면에 표시
    })
    .catch(error => {
      console.error("게시글 로드 중 오류 발생:", error);
      postListContainer.innerHTML = '<p>게시글을 불러오는 데 실패했습니다.</p>';
    });
}

// 게시글을 화면에 표시하는 함수
function displayPosts(posts) {
  const postListContainer = document.querySelector('.post-list');
  if (postListContainer) {
    postListContainer.innerHTML = '';  // 이전 게시글을 지우고 새로 로드
    if (posts.length === 0) {
      postListContainer.innerHTML = '<p>게시글이 없습니다. 건물을 선택하거나 다시 시도해주세요.</p>';
    } else {
      posts.forEach(post => {
        const postElement = document.createElement('div');
        postElement.classList.add('post');
        postElement.innerHTML = `
          <h3>${post.rsdBrdTitl}</h3>
          <p>${post.rsdBrdCont}</p>
          <span>작성자: ${post.mbrNnm}</span>
          <span>${post.rsdBrdPblsDtm}</span>
        `;
        postListContainer.appendChild(postElement);
      });
    }
  } else {
    console.log('게시글 목록이 없습니다.');
  }
}
