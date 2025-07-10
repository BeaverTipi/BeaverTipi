document.addEventListener('DOMContentLoaded', function() {
    const searchForm = document.getElementById('searchForm');
    const resetButton = document.getElementById('resetButton');
    const currentPageNoInput = document.getElementById('currentPageNoInput');
    const searchRptCodeInput = document.getElementById('searchRptCodeInput');
    const saveButton = document.getElementById('saveButton');

    window.fn_paging = function(pageNo) {
        if (currentPageNoInput) {
            currentPageNoInput.value = pageNo;
        }
        searchForm.submit();
    };

    if (searchForm) {
        searchForm.addEventListener('submit', function(event) {
            // ⭐ 탭 전환 시에는 페이지 번호를 초기화하는 대신, 검색 시에만 1로 초기화 ⭐
            // 이 로직은 컨트롤러에서 page=1로 다시 설정해주기 때문에 필요 없을 수 있습니다.
            // 하지만 명시적으로 초기화하려면 검색 버튼 클릭 시에만 적용되도록 해야 합니다.
            // 현재 코드에는 검색 버튼에 대한 명시적인 이벤트 리스너가 없으므로,
            // 검색 버튼이 눌리면 폼이 제출되고 컨트롤러에서 페이지를 처리할 것입니다.
            // 만약 검색 버튼 클릭 시 항상 1페이지로 가고 싶다면, 검색 버튼에 이벤트 리스너를 추가하여
            // currentPageNoInput.value = 1; 을 설정한 후 searchForm.submit(); 을 호출해야 합니다.
            // 현재 상태에서는 탭 클릭 시에만 currentPageNoInput.value = 1; 이 적용됩니다.
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
                        // 원본 상태를 업데이트하여 중복 저장 방지 (필요 시)
                        // document.querySelectorAll('#reportedUserTable tbody tr').forEach(function(row) {
                        //     const selectElement = row.querySelector('.report-status-select');
                        //     if (selectElement) {
                        //         selectElement.dataset.originalStatus = selectElement.value;
                        //     }
                        // });
                        window.location.reload(); // 성공 시 페이지 새로고침
                    } else {
                        alert('상태 저장 실패: ' + response.data.message);
                        window.location.reload(); // 실패 시에도 새로고침하여 최신 상태 반영
                    }
                })
                .catch(error => {
                    console.error('AJAX 오류:', error);
                    alert('상태 저장 중 오류가 발생했습니다.');
                    window.location.reload(); // 오류 시에도 새로고침
                });
            }
        });
    }

    // ⭐ 탭 클릭 이벤트 처리 ⭐
    $('#reportTabs .nav-link').on('click', function(e) {
        e.preventDefault();
        const rptCode = $(this).data('rpt-code');
        searchRptCodeInput.value = rptCode;
        currentPageNoInput.value = 1;
        searchForm.submit();
    });

    // ⭐ 신고 상세 모달 열기 및 데이터 로드 (LSTG_DEL 반영) ⭐
    $(document).on('click', '.report-title', function(e) {
        e.preventDefault();
        const reportId = $(this).data('report-id');
        console.log("신고 상세 조회 요청:", reportId);

        axios.get(`/admin/report/detail/${reportId}`)
            .then(response => {
                const data = response.data;
                console.log("신고 상세 데이터 수신:", data);

                $('#modalReportId').text(data.reportId || 'N/A');
                $('#modalBrdTitlNm').text(data.brdTitlNm || 'N/A');
                $('#modalBrdCont').html(data.brdCont ? data.brdCont.replace(/\n/g, '<br>') : '내용 없음');

                // ⭐ RPT_CODE에 따른 문구 및 UI 변경 ⭐
                const isListingReport = (data.rptCode === 'LSTG'); // data.rptCode 사용
                $('#modalTargetIdLabel').text(isListingReport ? '신고 대상 매물 ID:' : '신고 대상 회원 ID:');
                $('#modalRptTargetId').text(data.rptTargetId || 'N/A');

                if (isListingReport) {
                    $('#memberSpecificInfo').hide();
                    $('#listingSpecificInfo').show();

                    // ⭐ LSTG_DEL 표시 및 변경 버튼 추가 ⭐
                    const $modalLstgDel = $('#modalLstgDel'); // ID 변경
                    $modalLstgDel.empty();

                    if (data.lstgDel) { // ⭐ data.lstgDel 사용 ⭐
                        const statusText = $('<span>').text(data.lstgDel === 'Y' ? '삭제됨' : '활성');
                        $modalLstgDel.append(statusText);

                        const changeStatusBtn = $('<button>')
                            .addClass('btn btn-sm btn-info ml-2')
                            .text('상태 변경')
                            .data('lstg-id', data.rptTargetId) // RPT_TARGET_ID가 LSTG_ID임
                            .data('current-del', data.lstgDel) // ⭐ data-current-del 사용 ⭐
                            .on('click', function() {
                                const lstgId = $(this).data('lstg-id');
                                const currentDel = $(this).data('current-del'); // ⭐ currentDel 사용 ⭐
                                $('#selectedLstgId').text(lstgId);
                                $('#currentLstgDel').text(currentDel === 'Y' ? '삭제됨' : '활성'); // ⭐ ID 변경 ⭐
                                $('#newLtsgDel').val(currentDel); // ⭐ ID 변경 ⭐
                                $('#listingStatusChangeModal').modal('show'); // 매물 모달 띄우기
                            });
                        $modalLstgDel.append(changeStatusBtn);
                    } else {
                        $modalLstgDel.text('정보 없음');
                    }
                } else { // 회원 신고 (MEMB)
                    $('#memberSpecificInfo').show();
                    $('#listingSpecificInfo').hide();
                    const $modalRptTargetMbrStatus = $('#modalRptTargetMbrStatus');
                    $modalRptTargetMbrStatus.empty();

                    if (data.rptTargetMbrStatus) {
                        const statusText = $('<span>').text(data.rptTargetMbrStatus);
                        $modalRptTargetMbrStatus.append(statusText);

                        if (data.rptTargetMbrCd) {
                            const changeStatusBtn = $('<button>')
                                .addClass('btn btn-sm btn-info ml-2')
                                .text('상태 변경')
                                .data('mbr-cd', data.rptTargetMbrCd)
                                .data('current-status', data.rptTargetMbrStatus)
                                .on('click', function() {
                                    const mbrCd = $(this).data('mbr-cd');
                                    const currentStatus = $(this).data('current-status');
                                    $('#selectedMbrCd').text(mbrCd);
                                    $('#currentMbrStatus').text(currentStatus);
                                    $('#newMbrStatus').val(currentStatus);
                                    $('#statusChangeModal').modal('show'); // 회원 모달 띄우기
                                });
                            $modalRptTargetMbrStatus.append(changeStatusBtn);
                        }
                    } else {
                        $modalRptTargetMbrStatus.text('정보 없음');
                    }
                }

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

    // ⭐ 회원 상태 변경 모달의 "변경" 버튼 클릭 이벤트 ⭐
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

    // ⭐ 추가: 매물 삭제 상태 변경 모달의 "변경" 버튼 클릭 이벤트 (LSTG_DEL 반영) ⭐
    $('#btnUpdateListingDeleteStatus').on('click', function() {
        const lstgId = $('#selectedLstgId').text();
        const newDel = $('#newLtsgDel').val(); // ⭐ newLtsgDel 사용 ⭐

        if (!lstgId || !newDel) {
            alert('매물 ID와 새로운 삭제 상태를 선택해주세요.');
            return;
        }

        if (confirm(`${lstgId} 매물의 삭제 상태를 "${newDel === 'Y' ? '삭제' : '미삭제'}"(으)로 변경하시겠습니까?`)) {
            axios.post('/admin/report/updateListingDeleteStatus', null, {
                params: {
                    lstgId: lstgId,
                    lstgDel: newDel // ⭐ lstgDel 사용 ⭐
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