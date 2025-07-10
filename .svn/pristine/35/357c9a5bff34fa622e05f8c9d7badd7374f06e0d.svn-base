document.addEventListener('DOMContentLoaded', function() {
    const searchForm = document.getElementById('searchForm');
    const resetButton = document.getElementById('resetButton');
    const currentPageNoInput = document.getElementById('currentPageNoInput');
    const saveButton = document.getElementById('saveButton'); // ⭐ 추가 ⭐

    // --- 페이징 처리 함수 (Global Function) ---
    window.fn_paging = function(pageNo) {
        if (currentPageNoInput) {
            currentPageNoInput.value = pageNo;
        }
        searchForm.submit();
    };

    // --- 검색 폼 submit 이벤트 리스너 ---
    if (searchForm) {
        searchForm.addEventListener('submit', function(event) {
            if (currentPageNoInput.value !== '1') {
                 currentPageNoInput.value = 1;
            }
        });
    }

    // --- 초기화 버튼 이벤트 리스너 ---
    if (resetButton) {
        resetButton.addEventListener('click', function() {
            document.getElementById('searchTitle').value = '';
            document.getElementById('searchWriter').value = '';
            document.getElementById('brdPblsDtmFrom').value = '';
            document.getElementById('brdPblsDtmTo').value = '';
            document.getElementById('searchRptStatusCode').value = '';

            if (currentPageNoInput) {
                currentPageNoInput.value = 1;
            }
            searchForm.submit();
        });
    }

    // ⭐ 저장하기 버튼 이벤트 리스너 추가 ⭐
    if (saveButton) {
        saveButton.addEventListener('click', function() {
            const updates = [];
            document.querySelectorAll('#reportedUserTable tbody tr').forEach(function(row) {
                const selectElement = row.querySelector('.report-status-select');
                const hiddenReportId = row.querySelector('input[type="hidden"]'); // 추가된 hidden input
                
                if (selectElement && hiddenReportId) {
                    const currentStatus = selectElement.value;
                    const originalStatus = selectElement.dataset.originalStatus; // data-original-status 사용
                    const reportId = hiddenReportId.value; // hidden input에서 reportId 가져오기

                    // 원래 상태와 다른 경우에만 업데이트 목록에 추가
                    if (currentStatus !== originalStatus) {
                        updates.push({
                            reportId: reportId,
                            rptStatusCode: currentStatus
                        });
                    }
                }
            });

            if (updates.length === 0) {
                alert('변경할 신고 상태가 없습니다.');
                return;
            }

            if (confirm(`총 ${updates.length}건의 신고 상태를 저장하시겠습니까?`)) {
                axios.post('/admin/report/updateStatuses', updates) // ⭐ 새로운 URL ⭐
                .then(response => {
                    if (response.data.status === 'success') {
                        alert(response.data.message);
                        // 성공적으로 업데이트되면 원본 상태를 최신 상태로 갱신
                        document.querySelectorAll('#reportedUserTable tbody tr').forEach(function(row) {
                            const selectElement = row.querySelector('.report-status-select');
                            if (selectElement) {
                                selectElement.dataset.originalStatus = selectElement.value;
                            }
                        });
                        // 페이지 새로고침 (선택 사항)
                        // window.location.reload(); 
                    } else {
                        alert('상태 저장 실패: ' + response.data.message);
                        // 실패 시 사용자에게 알리고 원래 상태로 롤백 (이 경우 페이지 새로고침이 더 간단할 수 있음)
                        window.location.reload(); 
                    }
                })
                .catch(error => {
                    console.error('AJAX 오류:', error);
                    alert('상태 저장 중 오류가 발생했습니다.');
                    window.location.reload();
                });
            }
        });
    }

    // ⭐ 기존의 개별 select change 이벤트 리스너 제거 ⭐
    // document.querySelectorAll('.report-status-select').forEach(function(selectElement) {
    //     selectElement.addEventListener('change', function() {
    //         // ... 이 부분의 코드는 더 이상 필요 없습니다.
    //     });
    // });
});