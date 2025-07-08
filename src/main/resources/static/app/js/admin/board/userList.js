document.addEventListener('DOMContentLoaded', function() {
    const searchForm = document.getElementById('searchForm');
    const resetButton = document.getElementById('resetButton');
    const currentPageNoInput = document.getElementById('currentPageNoInput'); // JSP에 추가된 hidden input

    // --- 페이징 처리 함수 (Global Function) ---
    // DefaultPaginationRenderer가 호출하는 함수
    // 이 함수는 렌더러가 생성한 HTML에서 페이지 번호를 클릭할 때 호출됩니다.
    window.fn_paging = function(pageNo) {
        if (currentPageNoInput) {
            currentPageNoInput.value = pageNo;
        }
        searchForm.submit(); // 페이지 번호만 변경하고 폼 제출
    };

    // --- 검색 폼 submit 이벤트 리스너 ---
    // 검색 버튼 클릭 시 (submit 발생 시) 현재 페이지를 1로 초기화
    if (searchForm) {
        searchForm.addEventListener('submit', function(event) {
            // event.submitter는 폼을 제출한 버튼을 참조합니다. (브라우저 호환성 주의: IE는 지원 안함)
            // '초기화' 버튼이 아닌 '검색' 버튼으로 폼이 제출되었을 때만 currentPageNo를 1로 설정
            // 또는, 그냥 submit 될 때마다 1로 초기화해도 무방합니다. (검색은 항상 1페이지부터 시작하는 것이 일반적)
            
            // 모든 검색 시 페이지 번호를 1로 초기화 (일반적인 검색 동작)
            // 단, fn_paging을 통해 제출될 때는 이미 pageNo가 설정되었으므로 무시
            if (currentPageNoInput.value !== '1') { // 이미 1이 아닌 경우에만 1로 설정 (효율성을 위해)
                 currentPageNoInput.value = 1;
            }
            // 폼의 기본 제출 동작은 여기서 막지 않고 그대로 진행
        });
    }

    // --- 초기화 버튼 이벤트 리스너 ---
    if (resetButton) {
        resetButton.addEventListener('click', function() {
            // detailSearch 필드 초기화
            document.getElementById('searchTitle').value = '';
            document.getElementById('searchWriter').value = '';
            document.getElementById('brdPblsDtmFrom').value = '';
            document.getElementById('brdPblsDtmTo').value = '';
            document.getElementById('searchRptStatusCode').value = '';

            // simpleSearch 필드가 있다면 이곳에서 초기화 (현재 userList.jsp에는 없음)
            // 예: document.getElementById('searchWord').value = '';
            // 예: document.getElementById('searchType').value = '';

            // 초기화 시 페이지 번호도 1로 설정
            if (currentPageNoInput) {
                currentPageNoInput.value = 1;
            }

            // 폼 제출하여 초기화된 검색 조건으로 1페이지 재조회
            searchForm.submit();
        });
    }

    // --- 신고 상태 변경 이벤트 리스너 ---
    document.querySelectorAll('.report-status-select').forEach(function(selectElement) {
        selectElement.addEventListener('change', function() {
            const reportId = this.dataset.reportId;
            const newStatus = this.value;
            const originalStatus = this.dataset.originalStatus; // 기존 상태를 저장

            if (confirm(`신고 번호 ${reportId}의 상태를 '${newStatus}'(으)로 변경하시겠습니까?`)) {
                // Axios를 사용하여 POST 요청
                axios.post('/admin/updateStatus', { // 컨트롤러의 매핑 경로 확인: /admin/updateStatus
                    reportId: reportId,
                    rptStatusCode: newStatus
                })
                .then(response => {
                    if (response.data.status === 'success') {
                        alert(response.data.message);
                        // 성공 시 originalStatus를 새로운 상태로 업데이트
                        this.dataset.originalStatus = newStatus;
                    } else {
                        alert('상태 변경 실패: ' + response.data.message);
                        // 실패 시 원래 상태로 롤백
                        this.value = originalStatus;
                    }
                })
                .catch(error => {
                    console.error('AJAX 오류:', error);
                    alert('상태 변경 중 오류가 발생했습니다.');
                    // 오류 발생 시 원래 상태로 롤백
                    this.value = originalStatus;
                });
            } else {
                // 사용자가 취소했을 경우, 원래 상태로 롤백
                this.value = originalStatus;
            }
        });
    });
});