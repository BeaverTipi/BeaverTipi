// userList.js

document.addEventListener('DOMContentLoaded', function() {
    const searchForm = document.getElementById('searchForm');
    const resetButton = document.getElementById('resetBtn');
    const currentPageNoInput = document.getElementById('currentPageNoInput');
    const searchRptCodeInput = document.getElementById('searchRptCodeInput');

    const labelReportedTargetId = document.getElementById('labelReportedTargetId');
    const searchReportedTargetIdInput = document.getElementById('searchReportedTargetId');
    const reportedTargetHeader = document.getElementById('reportedTarget');

    // --- 유틸리티 함수 ---
    function updateReportedTargetLabels(rptCode) {
        let labelText = '피신고 ID';
        let placeholderText = '피신고 ID';
        let thText = '신고된 대상';

        if (rptCode === 'MEMB') {
            labelText = '피신고자ID';
            placeholderText = '피신고자ID';
            thText = '피신고자 ID';
        } else if (rptCode === 'LSTG') {
            labelText = '피신고매물 ID';
            placeholderText = '피신고매물 ID';
            thText = '피신고매물 ID';
        }

        if (labelReportedTargetId) {
            labelReportedTargetId.textContent = labelText;
        }
        if (searchReportedTargetIdInput) {
            searchReportedTargetIdInput.placeholder = placeholderText;
        }
        if (reportedTargetHeader) {
            reportedTargetHeader.textContent = thText;
        }
    }

    // --- 이벤트 리스너 설정 ---

    window.fn_paging = function(pageNo) {
        if (currentPageNoInput) {
            currentPageNoInput.value = pageNo;
        }
        searchForm.submit();
    };

    if (searchForm) {
        searchForm.addEventListener('submit', function(event) {
            // ...
        });
    }

    if (resetButton) {
        resetButton.addEventListener('click', function() {
            document.getElementById('searchTitle').value = '';
            document.getElementById('searchWriter').value = '';
            document.getElementById('searchReportedTargetId').value = '';
            document.getElementById('brdPblsDtmFrom').value = '';
            document.getElementById('brdPblsDtmTo').value = '';
            document.getElementById('searchRptStatusCode').value = '';

            if (currentPageNoInput) {
                currentPageNoInput.value = 1;
            }

            const activeTab = document.querySelector('#reportTabs .nav-link.active');
            const currentRptCode = activeTab ? activeTab.dataset.rptCode : 'MEMB';
            updateReportedTargetLabels(currentRptCode);

            searchForm.submit();
        });
    }

    $('#reportTabs .nav-link').on('shown.bs.tab', function(e) {
        const rptCode = $(this).data('rpt-code');
        searchRptCodeInput.value = rptCode;
        currentPageNoInput.value = 1;

        updateReportedTargetLabels(rptCode);

        searchForm.submit();
    });

    const initialActiveTab = document.querySelector('#reportTabs .nav-link.active');
    const initialRptCode = initialActiveTab ? initialActiveTab.dataset.rptCode : 'MEMB';
    updateReportedTargetLabels(initialRptCode);


    $(document).on('click', '.report-row', function(e) {
        if ($(e.target).closest('button, select').length) {
            return;
        }

        e.preventDefault();
        const reportId = $(this).data('report-id');

        axios.get(`/admin/report/detail/${reportId}`)
            .then(response => {
                const data = response.data; // data는 이제 ReportVO 객체입니다.

                $('#modalReportId').text(data.rptId || 'null');
                $('#modalBrdTitlNm').text(data.brdTitlNm  ? data.brdTitlNm : '제목 없음'); // boardVO를 통해 접근
                $('#modalBrdCont').html(data.brdCont ? data.brdCont.replace(/\n/g, '<br>') : '내용 없음');

                const isListingReport = (data.rptCode === 'LSTG');
                $('#modalTargetIdLabel').text(isListingReport ? '피신고매물 ID : ' : '피신고자 ID : ');
                $('#modalRptTargetId').text(data.rptTargetId || 'N/A');

                const $memberSpecificInfo = $('#memberSpecificInfo');
                const $modalNewMbrStatus = $('#modalNewMbrStatus');
                const $listingSpecificInfo = $('#listingSpecificInfo');
                const $modalNewLtsgDel = $('#modalNewLtsgDel');

                $memberSpecificInfo.hide();
                $listingSpecificInfo.hide();

                if (isListingReport) {
                    $listingSpecificInfo.show();
                    $modalNewLtsgDel.val(data.lstgDel);

                    $('#btnProcessAllChanges').data('original-lstg-del', data.lstgDel);
                    $('#btnProcessAllChanges').data('lstg-id', data.rptTargetId);
                    $('#btnProcessAllChanges').removeData('original-mbr-status');
                    $('#btnProcessAllChanges').removeData('mbr-cd');

                } else {
                    $memberSpecificInfo.show();
                    $modalNewMbrStatus.val(data.rptTargetMbrStatus);

                    $('#btnProcessAllChanges').data('original-mbr-status', data.rptTargetMbrStatus);
                    $('#btnProcessAllChanges').data('mbr-cd', data.rptTargetMbrCd);
                    $('#btnProcessAllChanges').removeData('original-lstg-del');
                    $('#btnProcessAllChanges').removeData('lstg-id');
                }

                $('#modalRptStatusCode').val(data.rptStatusCode);
                $('#btnProcessAllChanges').data('report-id', data.rptId);
                $('#btnProcessAllChanges').data('original-rpt-status', data.rptStatusCode);
                $('#reportDetailModal').modal('show');
            })
            .catch(error => {
                console.error('신고 상세 정보 로드 실패:', error);
                alert('신고 상세 정보를 불러오는 데 실패했습니다.');
            });
    });

    $('#closeReportDetailModalBtn').on('click', function() {
        $('#reportDetailModal').modal('hide');
    });

    $('#btnProcessAllChanges').on('click', function() {
        const $thisBtn = $(this);
        const reportId = $thisBtn.data('report-id');
        const originalRptStatus = $thisBtn.data('original-rpt-status');
        const newRptStatusCode = $('#modalRptStatusCode').val();

        const originalMbrStatus = $thisBtn.data('original-mbr-status');
        const newMbrStatus = $('#modalNewMbrStatus').val();
        const mbrCd = $thisBtn.data('mbr-cd');

        const originalLstgDel = $thisBtn.data('original-lstg-del');
        const newLstgDel = $('#modalNewLtsgDel').val();
        const lstgId = $thisBtn.data('lstg-id');

        let changesMade = false;
        let successMessages = [];
        let errorMessages = [];
        const promises = [];

        if (newRptStatusCode !== originalRptStatus) {
            changesMade = true;
            promises.push(
                axios.post('/admin/report/updateStatuses', [{ rptId: reportId, rptStatusCode: newRptStatusCode }])
                    .then(response => {
                        if (response.data.status === 'success') {
                            successMessages.push('신고 처리 상태가 성공적으로 변경되었습니다.');
                        } else {
                            errorMessages.push('신고 처리 상태 변경 실패: ' + response.data.message);
                        }
                    })
                    .catch(error => {
                        console.error('신고 상태 변경 AJAX 오류:', error);
                        errorMessages.push('신고 상태 변경 중 오류가 발생했습니다.');
                    })
            );
        }

        if (mbrCd && newMbrStatus !== originalMbrStatus) {
            changesMade = true;
            promises.push(
                axios.post('/admin/report/updateMemberStatus', null, {
                    params: { mbrCd: mbrCd, mbrStatus: newMbrStatus }
                })
                .then(response => {
                    if (response.data === 'SUCCESS') {
                        successMessages.push(`회원 (${mbrCd}) 상태가 성공적으로 변경되었습니다.`);
                    } else {
                        errorMessages.push(`회원 (${mbrCd}) 상태 변경 실패.`);
                    }
                })
                .catch(error => {
                    console.error('회원 상태 변경 AJAX 오류:', error);
                    errorMessages.push(`회원 (${mbrCd}) 상태 변경 중 오류가 발생했습니다.`);
                })
            );
        }

        if (lstgId && newLstgDel !== originalLstgDel) {
            changesMade = true;
            promises.push(
                axios.post('/admin/report/updateListingDeleteStatus', null, {
                    params: { lstgId: lstgId, lstgDel: newLstgDel }
                })
                .then(response => {
                    if (response.data === 'SUCCESS') {
                        successMessages.push(`매물 (${lstgId}) 삭제 상태가 성공적으로 변경되었습니다.`);
                    } else {
                        errorMessages.push(`매물 (${lstgId}) 삭제 상태 변경 실패.`);
                    }
                })
                .catch(error => {
                        console.error('매물 삭제 상태 변경 AJAX 오류:', error);
                        errorMessages.push(`매물 (${lstgId}) 삭제 상태 변경 중 오류가 발생했습니다.`);
                })
            );
        }

        if (!changesMade) {
            alert('변경할 내용이 없습니다.');
            return;
        }

        if (confirm('모든 변경 사항을 저장하시겠습니까?')) {
            Promise.all(promises)
                .then(() => {
                    let finalMessage = "";
                    if (successMessages.length > 0) {
                        finalMessage += "✅ 성공:\n" + successMessages.join('\n');
                    }
                    if (errorMessages.length > 0) {
                        if (finalMessage !== "") finalMessage += "\n\n";
                        finalMessage += "❌ 실패:\n" + errorMessages.join('\n');
                    }
                    alert(finalMessage || "처리 완료 (메시지 없음)");
                    $('#reportDetailModal').modal('hide');
                    window.location.reload();
                })
                .catch(allErrors => {
                    console.error('모든 Promise 처리 중 오류 발생:', allErrors);
                    alert('일부 변경 사항 처리 중 오류가 발생했습니다.');
                    $('#reportDetailModal').modal('hide');
                    window.location.reload();
                });
        }
    });
});