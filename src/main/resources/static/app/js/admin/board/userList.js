// userList.js

document.addEventListener('DOMContentLoaded', function() {
    const searchForm = document.getElementById('searchForm');
    const resetButton = document.getElementById('resetBtn');
    const searchButton = document.getElementById('searchBtn');
    const currentPageNoInput = document.getElementById('currentPageNoInput');
    const searchRptCodeInput = document.getElementById('searchRptCodeInput');

    // 페이지 이동 함수 (페이징 관련)
    window.fn_paging = function(pageNo) {
        if (currentPageNoInput) {
            currentPageNoInput.value = pageNo;
        }
        searchForm.submit();
    };

    // 검색 폼 제출 이벤트 (현재는 아무 동작 없음)
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
            document.getElementById('brdPblsDtmFrom').value = '';
            document.getElementById('brdPblsDtmTo').value = '';
            document.getElementById('searchRptStatusCode').value = '';

            if (currentPageNoInput) {
                currentPageNoInput.value = 1;
            }
            searchForm.submit();
        });
    }

    // 탭 클릭 이벤트 처리
    $('#reportTabs .nav-link').on('click', function(e) {
        e.preventDefault();
        const rptCode = $(this).data('rpt-code');
        searchRptCodeInput.value = rptCode;
        currentPageNoInput.value = 1;

        // 테이블 헤더 텍스트 변경
        const reportedTargetHeader = $('#reportedTarget'); // <th>의 ID
        if (rptCode === 'MEMB') {
            reportedTargetHeader.text('피신고자 ID');
        } else if (rptCode === 'LSTG') {
            reportedTargetHeader.text('피신고매물 ID');
        }

        searchForm.submit();
    });

    // 페이지 로드 시 초기 탭에 맞춰 <th> 텍스트 설정
    const initialRptCode = $('#reportTabs .nav-link.active').data('rpt-code');
    if (initialRptCode) {
        const reportedTargetHeader = $('#reportedTarget');
        if (initialRptCode === 'MEMB') {
            reportedTargetHeader.text('피신고자 ID');
        } else if (initialRptCode === 'LSTG') {
            reportedTargetHeader.text('피신고매물 ID');
        }
    } else {
        // 기본값 (페이지 로드 시 처음 보여지는 탭이 회원 탭이거나 searchRptCode가 없는 경우)
        // 이 경우, 초기 'active' 탭이 MEMB이므로 기본값을 '피신고자 ID'로 설정
        $('#reportedTarget').text('피신고자 ID');
    }


    // 신고 상세 모달 열기 및 데이터 로드 (매물/회원/신고 상태 반영)
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
                // const $modalRptTargetMbrCurrentStatus = $('#modalRptTargetMbrCurrentStatus'); // ⭐ 제거됨
                const $modalNewMbrStatus = $('#modalNewMbrStatus');

                // 매물 상태 관리 섹션
                const $listingSpecificInfo = $('#listingSpecificInfo');
                // const $modalCurrentLstgDel = $('#modalCurrentLstgDel'); // ⭐ 제거됨
                const $modalNewLtsgDel = $('#modalNewLtsgDel');

                // 초기화: 둘 다 숨김
                $memberSpecificInfo.hide();
                $listingSpecificInfo.hide();

                // 신고 유형에 따른 회원/매물 UI 활성화 및 데이터 설정
                if (isListingReport) { // 매물 신고
                    $listingSpecificInfo.show();
                    // $('#listingSpecificInfo strong').first().text('피신고매물 삭제 상태 : '); // ⭐ 이 라인은 이제 필요 없습니다 (label에 직접 텍스트 있음)
                    // $modalCurrentLstgDel.text(data.lstgDel === 'Y' ? '삭제됨' : '활성'); // ⭐ 이 라인 제거
                    $modalNewLtsgDel.val(data.lstgDel);

                    // data 속성 저장 (초기 값과 변경된 값 비교용)
                    $('#btnProcessAllChanges').data('original-lstg-del', data.lstgDel);
                    $('#btnProcessAllChanges').data('lstg-id', data.rptTargetId); // 매물 ID 저장
                    // 회원 관련 데이터는 초기화
                    $('#btnProcessAllChanges').removeData('original-mbr-status');
                    $('#btnProcessAllChanges').removeData('mbr-cd');

                } else { // 회원 신고 (MEMB)
                    $memberSpecificInfo.show();
                    // $('#memberSpecificInfo strong').first().text('피신고자 상태 : '); // ⭐ 이 라인은 이제 필요 없습니다 (label에 직접 텍스트 있음)
                    // $modalRptTargetMbrCurrentStatus.text(data.rptTargetMbrStatus || '정보 없음'); // ⭐ 이 라인 제거
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