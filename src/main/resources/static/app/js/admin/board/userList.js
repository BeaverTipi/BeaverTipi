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
    // 피신고 대상 라벨, 플레이스홀더, 테이블 헤더 텍스트를 업데이트하는 함수
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

    // 페이지 이동 함수 (페이징 관련)
    window.fn_paging = function(pageNo) {
        if (currentPageNoInput) {
            currentPageNoInput.value = pageNo;
        }
        searchForm.submit();
    };

    // 검색 폼 제출 이벤트 (현재는 추가 동작 없음)
    if (searchForm) {
        searchForm.addEventListener('submit', function(event) {
            // 필요하다면 여기에 추가적인 제출 전 로직을 넣을 수 있습니다.
        });
    }

    // 초기화 버튼 클릭 이벤트
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

            // 초기화 시 현재 활성화된 탭의 rptCode를 가져와 라벨 초기화
            const activeTab = document.querySelector('#reportTabs .nav-link.active');
            const currentRptCode = activeTab ? activeTab.dataset.rptCode : 'MEMB'; // 기본값 MEMB
            updateReportedTargetLabels(currentRptCode);

            searchForm.submit();
        });
    }

    // 탭 클릭 이벤트 처리 (Bootstrap shown.bs.tab 이벤트 사용)
    $('#reportTabs .nav-link').on('shown.bs.tab', function(e) {
        const rptCode = $(this).data('rpt-code');
        searchRptCodeInput.value = rptCode;
        currentPageNoInput.value = 1; // 탭 변경 시 페이지 1로 리셋

        updateReportedTargetLabels(rptCode); // 라벨 및 플레이스홀더, 테이블 헤더 업데이트

        // 탭 변경 시 자동으로 검색 폼 제출
        searchForm.submit();
    });

    // 페이지 로드 시 초기 탭에 맞춰 라벨 설정
    // 현재 활성화된 탭의 data-rpt-code 값을 가져와 라벨을 업데이트합니다.
    const initialActiveTab = document.querySelector('#reportTabs .nav-link.active');
    const initialRptCode = initialActiveTab ? initialActiveTab.dataset.rptCode : 'MEMB'; // 기본값 MEMB
    updateReportedTargetLabels(initialRptCode);


    // 신고 상세 모달 열기 및 데이터 로드
    $(document).on('click', '.report-row', function(e) {
        // select 박스나 다른 버튼 클릭 시 행 클릭 이벤트 방지
        if ($(e.target).closest('button, select').length) {
            return;
        }

        e.preventDefault();
        const reportId = $(this).data('report-id');
        console.log("신고 상세 조회 요청:", reportId);

        axios.get(`/admin/report/detail/${reportId}`)
            .then(response => {
                const data = response.data;
                console.log("신고 상세 데이터 수신:", data);

                // 모달에 데이터 설정
                $('#modalReportId').text(data.reportId || 'null');
                $('#modalBrdTitlNm').text(data.brdTitlNm || 'null');
                $('#modalBrdCont').html(data.brdCont ? data.brdCont.replace(/\n/g, '<br>') : '내용 없음');

                // RPT_CODE에 따른 문구 및 UI 변경 (회원/매물)
                const isListingReport = (data.rptCode === 'LSTG');
                $('#modalTargetIdLabel').text(isListingReport ? '피신고매물 ID : ' : '피신고자 ID : ');
                $('#modalRptTargetId').text(data.rptTargetId || 'N/A');

                // 회원 상태 관리 섹션
                const $memberSpecificInfo = $('#memberSpecificInfo');
                const $modalNewMbrStatus = $('#modalNewMbrStatus');

                // 매물 상태 관리 섹션
                const $listingSpecificInfo = $('#listingSpecificInfo');
                const $modalNewLtsgDel = $('#modalNewLtsgDel');

                // 초기화: 둘 다 숨김
                $memberSpecificInfo.hide();
                $listingSpecificInfo.hide();

                // 신고 유형에 따른 회원/매물 UI 활성화 및 데이터 설정
                if (isListingReport) { // 매물 신고
                    $listingSpecificInfo.show();
                    $modalNewLtsgDel.val(data.lstgDel);

                    // data 속성 저장 (초기 값과 변경된 값 비교용)
                    $('#btnProcessAllChanges').data('original-lstg-del', data.lstgDel);
                    $('#btnProcessAllChanges').data('lstg-id', data.rptTargetId); // 매물 ID 저장
                    // 회원 관련 데이터는 초기화
                    $('#btnProcessAllChanges').removeData('original-mbr-status');
                    $('#btnProcessAllChanges').removeData('mbr-cd');

                } else { // 회원 신고 (MEMB)
                    $memberSpecificInfo.show();
                    $modalNewMbrStatus.val(data.rptTargetMbrStatus);

                    // data 속성 저장 (초기 값과 변경된 값 비교용)
                    $('#btnProcessAllChanges').data('original-mbr-status', data.rptTargetMbrStatus);
                    $('#btnProcessAllChanges').data('mbr-cd', data.rptTargetMbrCd); // 회원 코드 저장
                    // 매물 관련 데이터는 초기화
                    $('#btnProcessAllChanges').removeData('original-lstg-del');
                    $('#btnProcessAllChanges').removeData('lstg-id');
                }

                // 신고 처리 상태 설정
                $('#modalRptStatusCode').val(data.rptStatusCode);
                // data 속성 저장 (초기 값과 변경된 값 비교용)
                $('#btnProcessAllChanges').data('report-id', data.reportId);
                $('#btnProcessAllChanges').data('original-rpt-status', data.rptStatusCode);


                // 첨부 파일 설정
                const $modalAttachFiles = $('#modalAttachFiles');
                $modalAttachFiles.empty();
                if (data.attachFiles && data.attachFiles.length > 0) {
                    $('#attachFilesSection').show();
                    $.each(data.attachFiles, function(index, file) {
                        if (file.fileMime && file.fileMime.startsWith('image/')) {
                            const imgElement = $('<img>').attr('src', file.filePathUrl)
                                                        .attr('alt', file.fileOriginalname)
                                                        .addClass('img-fluid');
                            $modalAttachFiles.append(imgElement);
                        } else {
                            const fileLink = $('<a>')
                                .attr('href', file.filePathUrl)
                                .attr('target', '_blank')
                                .addClass('file-link')
                                .text(file.fileOriginalname);
                            $modalAttachFiles.append(fileLink);
                        }
                    });
                } else {
                    $('#attachFilesSection').hide();
                }

                $('#reportDetailModal').modal('show');
            })
            .catch(error => {
                console.error('신고 상세 정보 로드 실패:', error);
                alert('신고 상세 정보를 불러오는 데 실패했습니다.');
            });
    });

    // 신고 상세 모달 닫기 버튼 이벤트
    $('#closeReportDetailModalBtn').on('click', function() {
        console.log("신고 상세 모달 닫기 버튼 클릭됨.");
        $('#reportDetailModal').modal('hide');
    });

    // 모든 변경 사항 저장 버튼 클릭 이벤트 (통합 처리)
    $('#btnProcessAllChanges').on('click', function() {
        const $thisBtn = $(this);
        const reportId = $thisBtn.data('report-id');
        const originalRptStatus = $thisBtn.data('original-rpt-status');
        const newRptStatusCode = $('#modalRptStatusCode').val();

        // 회원 관련 데이터 (초기 값이 없으면 undefined)
        const originalMbrStatus = $thisBtn.data('original-mbr-status');
        const newMbrStatus = $('#modalNewMbrStatus').val();
        const mbrCd = $thisBtn.data('mbr-cd');

        // 매물 관련 데이터 (초기 값이 없으면 undefined)
        const originalLstgDel = $thisBtn.data('original-lstg-del');
        const newLstgDel = $('#modalNewLtsgDel').val();
        const lstgId = $thisBtn.data('lstg-id');

        let changesMade = false;
        let successMessages = [];
        let errorMessages = [];
        const promises = [];

        // 1. 신고 처리 상태 변경 (항상 확인)
        if (newRptStatusCode !== originalRptStatus) {
            changesMade = true;
            promises.push(
                axios.post('/admin/report/updateStatuses', [{ reportId: reportId, rptStatusCode: newRptStatusCode }])
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

        // 2. 회원 상태 변경 (회원 신고였고, 변경 사항이 있을 경우)
        // mbrCd가 존재하고, 현재 탭이 회원 탭(MEMB)이거나, 상세 정보를 불러왔을 때 RPT_CODE가 MEMB였을 경우에만 유효
        // 모달을 띄울 때 해당 값을 data에 저장했으므로 mbrCd 존재 여부로 판단
        if (mbrCd && newMbrStatus !== originalMbrStatus) {
            changesMade = true;
            promises.push(
                axios.post('/ajax/admin/report/updateMemberStatus', null, {
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

        // 3. 매물 삭제 상태 변경 (매물 신고였고, 변경 사항이 있을 경우)
        // lstgId가 존재하고, 현재 탭이 매물 탭(LSTG)이거나, 상세 정보를 불러왔을 때 RPT_CODE가 LSTG였을 경우에만 유효
        // 모달을 띄울 때 해당 값을 data에 저장했으므로 lstgId 존재 여부로 판단
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
                    window.location.reload(); // 성공 여부와 관계없이 새로고침하여 최신 상태 반영
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