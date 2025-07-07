// memberList.js 파일 내용

// Renderer가 호출하는 JavaScript 함수
function fn_paging(pageNo, queryString) {
    // hidden input으로 현재 페이지 번호 설정
    let currentPageInput = document.createElement('input');
    currentPageInput.type = 'hidden';
    currentPageInput.name = 'page';
    currentPageInput.value = pageNo;
    document.getElementById('searchForm').appendChild(currentPageInput);

    // 쿼리 스트링 파싱 및 hidden input으로 추가
    if (queryString) {
        let params = new URLSearchParams(queryString.substring(1));
        params.set('page', pageNo);

        let form = document.getElementById('searchForm');
        let originalAction = form.action.split('?')[0];
        form.action = originalAction + "?" + params.toString();
    } else {
        let form = document.getElementById('searchForm');
        let originalAction = form.action.split('?')[0];
        form.action = originalAction + "?page=" + pageNo;
    }

    document.getElementById('searchForm').submit();
}

document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('resetButton').addEventListener('click', function() {
        // detailSearch 필드 초기화
        document.getElementById('searchTitle').value = '';
        document.getElementById('searchWriter').value = '';
        document.getElementById('brdPblsDtmFrom').value = '';
        document.getElementById('brdPblsDtmTo').value = '';
        document.getElementById('searchRptStatusCode').value = '';

        document.getElementById('searchForm').submit();
    });

    document.querySelectorAll('.report-status-select').forEach(function(selectElement) {
        selectElement.addEventListener('change', function() {
            const reportId = this.dataset.reportId;
            const newStatus = this.value;
            const originalStatus = this.dataset.originalStatus;

            if (confirm(`신고 번호 ${reportId}의 상태를 '${newStatus}'(으)로 변경하시겠습니까?`)) {
                axios.post('/admin/report/updateStatus', {
                    reportId: reportId,
                    rptStatusCode: newStatus
                })
                .then(response => {
                    if (response.data.status === 'success') {
                        alert(response.data.message);
                        this.dataset.originalStatus = newStatus;
                    } else {
                        alert('상태 변경 실패: ' + response.data.message);
                        this.value = originalStatus;
                    }
                })
                .catch(error => {
                    console.error('AJAX 오류:', error);
                    alert('상태 변경 중 오류가 발생했습니다.');
                    this.value = originalStatus;
                });
            } else {
                this.value = originalStatus;
            }
        });
    });
});