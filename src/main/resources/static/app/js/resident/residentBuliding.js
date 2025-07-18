document.addEventListener('DOMContentLoaded', () => {
  const urlParams = new URLSearchParams(window.location.search);
  let selectedBldgId = urlParams.get('bldgIdParam');  // URL에서 건물 ID를 가져옵니다.

  // 1. 건물 선택 셀렉터 처리
  const selector = document.querySelector('select[name="bldgIdParam"]');

  // URL 쿼리 파라미터가 있으면 해당 건물 ID로 설정
  if (selectedBldgId) {
    localStorage.setItem('selectedBuildingId', selectedBldgId);  // 로컬 스토리지에 건물 ID 저장
  } else {
    // URL에 건물 ID가 없으면 로컬 스토리지에서 값을 가져옵니다.
    selectedBldgId = localStorage.getItem('selectedBuildingId');
  }

  // URL에서 건물 ID가 있으면 셀렉터의 값 설정
  if (selector && selectedBldgId) {
    selector.value = selectedBldgId;
  }

  // 건물 선택 시 URL에 건물 정보 추가
  if (selector) {
    selector.addEventListener('change', () => {
      const selectedBuildingId = selector.value;
      // URL에 쿼리 파라미터로 건물 ID를 추가하여 페이지 리로드
      localStorage.setItem('selectedBuildingId', selectedBuildingId);  // 선택한 건물 ID를 로컬 스토리지에 저장
      window.location.href = `${window.location.pathname}?bldgIdParam=${selectedBuildingId}`;
    });
  }

  // 2. 건물 정보에 맞는 게시글 로드
  if (selectedBldgId) {
    console.log("선택된 건물 ID: ", selectedBldgId);

    // 해당 건물의 게시글을 자동으로 로드하는 AJAX 요청
    fetch(`/resident/board?bldgIdParam=${selectedBldgId}`)
      .then(response => response.json())
      .then(data => {
        console.log("게시글 로드 데이터:", data);

        // 게시글 데이터를 화면에 표시하는 로직을 추가합니다.
        console.log("게시글 로드 데이터:", data); // 데이터 확인
        displayPosts(data);
      })
      .catch(error => {
        console.error("게시글 로드 중 오류 발생:", error);
        Swal.fire({
          icon: 'error',
          title: '게시글 로드 실패',
          text: '게시글을 불러오는 데 실패했습니다. 다시 시도해주세요.',
          confirmButtonText: '확인'
        });
      });
  }

  // 게시글을 화면에 표시하는 함수
  function displayPosts(posts) {
    const postListContainer = document.querySelector('.post-list');
    if (postListContainer) {
      // 게시글이 있을 경우 화면에 추가
      posts.forEach(post => {
        const postElement = document.createElement('div');
        postElement.classList.add('post');
        postElement.innerHTML = `
          <h3>${post.title}</h3>
          <p>${post.content}</p>
          <span>${post.date}</span>
        `;
        postListContainer.appendChild(postElement);
      });
    } else {
      console.log('게시글 목록이 없습니다.');
    }
  }
});
