document.addEventListener('DOMContentLoaded', function() {
    const searchForm = document.getElementById('searchForm');
    const resetButton = document.getElementById('resetButton');
    const currentPageNoInput = document.getElementById('currentPageNoInput');
    const searchRptCodeInput = document.getElementById('searchRptCodeInput');
    const saveButton = document.getElementById('saveButton');
    const reportedTargetHeader = document.getElementById('reportedTarget'); // ⭐ 변경된 ID 참조 ⭐

    // 페이지 로드 시 현재 활성화된 탭에 따라 헤더 설정
    // JSP에서 초기 detailSearch.searchRptCode가 설정되어 있을 수 있으므로 이를 활용
    // (JSP의 hidden input #searchRptCodeInput 값 활용)
    const initialRptCode = searchRptCodeInput ? searchRptCodeInput.value : '';
    if (reportedTargetHeader) {
        if (initialRptCode === 'MEMB' || initialRptCode === '') { // MEMB이거나 기본값(공백)일 경우 회원
            reportedTargetHeader.textContent = '피신고자ID';
        } else if (initialRptCode === 'LSTG') { // LSTG일 경우 매물
            reportedTargetHeader.textContent = '피신고매물ID';
        }
    }

    window.fn_paging = function(pageNo) {
        if (currentPageNoInput) {
            currentPageNoInput.value = pageNo;
        }
        searchForm.submit();
    };

    if (searchForm) {
        searchForm.addEventListener('submit', function(event) {
        });
    }

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
            
            // 초기화 시 탭을 '회원'으로 설정하고 헤더 텍스트도 변경
            if (searchRptCodeInput) {
                searchRptCodeInput.value = 'MEMB'; // 탭 코드를 '회원'으로 초기화
            }
            $('#memb-tab').tab('show'); // Bootstrap 탭 활성화 (CSS도 함께 변경)
            
            if (reportedTargetHeader) {
                reportedTargetHeader.textContent = '피신고자'; // ⭐ 헤더 텍스트도 초기화 ⭐
            }
            
            searchForm.submit();
        });
    }

    if (saveButton) {
        saveButton.addEventListener('click', function() {
            const updates = [];
            document.querySelectorAll('#reportedUserTable tbody tr').forEach(function(row) {
                const selectElement = row.querySelector('.report-status-select');
                const hiddenReportId = row.querySelector('input[type="hidden"][name^="rptStatusUpdates"]');

                if (selectElement && hiddenReportId) {
                    const currentStatus = selectElement.value;
                    const originalStatus = selectElement.dataset.originalStatus;
                    const reportId = hiddenReportId.value;

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
                axios.post('/admin/report/updateStatuses', updates)
                .then(response => {
                    if (response.data.status === 'success') {
                        alert(response.data.message);
                        window.location.reload();
                    } else {
                        alert('상태 저장 실패: ' + response.data.message);
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

    // ⭐ 탭 클릭 이벤트 처리 (이전 코드에서 수정 및 강화) ⭐
    $('#reportTabs .nav-link').on('click', function(e) {
        e.preventDefault();
        const rptCode = $(this).data('rpt-code');
        
        if (searchRptCodeInput) {
            searchRptCodeInput.value = rptCode; // 숨겨진 input 값 업데이트
        }
        if (currentPageNoInput) {
            currentPageNoInput.value = 1; // 탭 변경 시 페이지 1로 초기화
        }

        // 테이블 헤더 텍스트 변경
        if (reportedTargetHeader) { // reportedTargetHeader가 존재할 경우에만 처리
            if (rptCode === 'MEMB') {
                reportedTargetHeader.textContent = '피신고자ID';
            } else if (rptCode === 'LSTG') {
                reportedTargetHeader.textContent = '피신고매물ID';
            }
        }
        
        // 폼 제출하여 검색 결과 업데이트
        searchForm.submit();
    });

    $(document).on('click', '.report-title', function(e) {
        e.preventDefault();
        const reportId = $(this).data('report-id');
        console.log("신고 상세 팝업 조회 요청:", reportId);

        const popupWidth = 1000;
        const popupHeight = 600;

        const screenWidth = window.screen.width;
        const screenHeight = window.screen.height;

        const left = Math.round((screenWidth - popupWidth) / 2);
        const top = Math.round((screenHeight - popupHeight) / 2);

        const popupUrl = `/admin/report/detailPopup?reportId=${reportId}&popup=true`;
        const popupName = `reportDetailPopup_${reportId}`;
        const popupFeatures = `width=${popupWidth},height=${popupHeight},left=${left},top=${top},scrollbars=yes,resizable=yes`;

        window.open(popupUrl, popupName, popupFeatures);
    });

    // ⭐ 모달 관련 코드 (기존과 동일하게 유지) ⭐
    $('#cancelStatusChangeModalBtn').on('click', function() {
        console.log("회원 상태 변경 모달 취소 버튼 클릭됨.");
        $('#statusChangeModal').modal('hide');
    });

    $('#cancelListingStatusChangeModalBtn').on('click', function() {
        console.log("매물 삭제 상태 변경 모달 취소 버튼 클릭됨.");
        $('#listingStatusChangeModal').modal('hide');
    });

    $('#btnUpdateMemberStatus').on('click', function() {
        const mbrCd = $('#selectedMbrCd').text();
        const newStatus = $('#newMbrStatus').val();

        if (!mbrCd || !newStatus) {
            alert('회원 ID와 새로운 상태를 선택해주세요.');
            return;
        }

        if (confirm(`${mbrCd} 회원의 상태를 "${newStatus}"(으)로 변경하시겠습니까?`)) {
            axios.post('/admin/report/updateMemberStatus', null, {
                params: {
                    mbrCd: mbrCd,
                    mbrStatus: newStatus
                }
            })
            .then(response => {
                if (response.data === 'SUCCESS') {
                    alert('회원 상태가 성공적으로 변경되었습니다.');
                    $('#statusChangeModal').modal('hide');
                    window.location.reload();
                } else {
                    alert('회원 상태 변경에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('회원 상태 변경 AJAX 오류:', error);
                alert('회원 상태 변경 중 오류가 발생했습니다.');
            });
        }
    });

    $('#btnUpdateListingDeleteStatus').on('click', function() {
        const lstgId = $('#selectedLstgId').text();
        const newDel = $('#newLtsgDel').val();

        if (!lstgId || !newDel) {
            alert('매물 ID와 새로운 삭제 상태를 선택해주세요.');
            return;
        }

        if (confirm(`${lstgId} 매물의 삭제 상태를 "${newDel === 'Y' ? '삭제' : '미삭제'}"(으)로 변경하시겠습니까?`)) {
            axios.post('/admin/report/updateListingDeleteStatus', null, {
                params: {
                    lstgId: lstgId,
                    lstgDel: newDel
                }
            })
            .then(response => {
                if (response.data === 'SUCCESS') {
                    alert('매물 삭제 상태가 성공적으로 변경되었습니다.');
                    $('#listingStatusChangeModal').modal('hide');
                    window.location.reload();
                } else {
                    alert('매물 삭제 상태 변경에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('매물 삭제 상태 변경 AJAX 오류:', error);
                alert('매물 삭제 상태 변경 중 오류가 발생했습니다.');
            });
        }
    });
});