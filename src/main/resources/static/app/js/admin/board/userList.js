document.addEventListener('DOMContentLoaded', function() {
    const searchForm = document.getElementById('searchForm');
    const resetButton = document.getElementById('resetButton');
    const currentPageNoInput = document.getElementById('currentPageNoInput');
    const saveButton = document.getElementById('saveButton');

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
            // 페이지 번호가 1이 아니면 1로 리셋 (새로운 검색 시)
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

    // --- 저장하기 버튼 이벤트 리스너 (신고 처리 상태 일괄 저장) ---
    if (saveButton) {
        saveButton.addEventListener('click', function() {
            const updates = [];
            document.querySelectorAll('#reportedUserTable tbody tr').forEach(function(row) {
                const selectElement = row.querySelector('.report-status-select');
                const hiddenReportId = row.querySelector('input[type="hidden"]');

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
                        // 성공적으로 업데이트되면 원본 상태를 최신 상태로 갱신
                        document.querySelectorAll('#reportedUserTable tbody tr').forEach(function(row) {
                            const selectElement = row.querySelector('.report-status-select');
                            if (selectElement) {
                                selectElement.dataset.originalStatus = selectElement.value;
                            }
                        });
                        // 페이지 새로고침 (선택 사항, 상태가 즉시 반영되도록 할 경우 유용)
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

    // ⭐ 신고 상세 모달 열기 및 데이터 로드 ⭐
    // `$(document).on('click', ...)` 형태로 변경하여 동적으로 생성된 요소에도 이벤트가 작동하도록 합니다.
    $(document).on('click', '.report-title', function(e) {
        e.preventDefault(); // 링크의 기본 동작 방지 (페이지 이동)
        const reportId = $(this).data('report-id'); // data-report-id 속성에서 reportId 가져오기
        console.log("신고 상세 조회 요청:", reportId);

        // AJAX 호출로 신고 상세 정보 가져오기
        axios.get(`/admin/report/detail/${reportId}`) // Axios 사용
            .then(response => {
                const data = response.data;
                console.log("신고 상세 데이터 수신:", data);

                // 모달 내용 채우기
                $('#modalReportId').text(data.reportId);
                $('#modalBrdTitlNm').text(data.brdTitlNm);
                // 줄바꿈 문자를 <br> 태그로 변환하여 HTML로 삽입
                $('#modalBrdCont').html(data.brdCont ? data.brdCont.replace(/\n/g, '<br>') : '내용 없음');
                $('#modalRptTargetId').text(data.rptTargetId);

                // ⭐ rptTargetMbrStatus 표시 및 회원 상태 변경 버튼 클릭 이벤트 ⭐
                const $modalRptTargetMbrStatus = $('#modalRptTargetMbrStatus');
                $modalRptTargetMbrStatus.html(''); // 기존 내용 비우기

                if (data.rptTargetMbrStatus) {
                    const statusText = $('<span>').text(data.rptTargetMbrStatus);
                    $modalRptTargetMbrStatus.append(statusText);

                    // 회원 상태 변경 버튼 추가 (신고 대상 ID가 있으면)
                    if (data.rptTargetId) {
                        const changeStatusBtn = $('<button>')
                            .addClass('btn btn-sm btn-info ml-2') // Bootstrap 버튼 스타일
                            .text('상태 변경')
                            .data('mbr-cd', data.rptTargetId) // 회원 ID를 데이터 속성으로 저장
                            .data('current-status', data.rptTargetMbrStatus) // 현재 상태를 데이터 속성으로 저장
                            .on('click', function() {
                                // 상태 변경 모달 열기
                                const mbrCd = $(this).data('mbr-cd');
                                const currentStatus = $(this).data('current-status');
                                $('#selectedMbrCd').text(mbrCd);
                                $('#currentMbrStatus').text(currentStatus);
                                $('#newMbrStatus').val(currentStatus); // 현재 상태를 기본 선택으로 설정
                                $('#statusChangeModal').modal('show');
                            });
                        $modalRptTargetMbrStatus.append(changeStatusBtn);
                    }
                } else {
                    $modalRptTargetMbrStatus.text('정보 없음');
                }


                // 첨부 파일 목록 초기화
                const $modalAttachFiles = $('#modalAttachFiles');
                $modalAttachFiles.empty(); // 기존 파일 목록 비우기

                // 첨부 파일이 있을 경우
                if (data.attachFiles && data.attachFiles.length > 0) {
                    $('#attachFilesSection').show(); // 첨부 파일 섹션 보이기
                    $.each(data.attachFiles, function(index, file) {
                        console.log("처리 중인 파일:", file); // 각 파일 데이터 확인

                        // 파일 MIME 타입이 이미지인지 확인 (image/로 시작하는지)
                        if (file.fileMime && file.fileMime.startsWith('image/')) {
                            // 이미지일 경우 <img> 태그로 표시
                            const imgElement = $('<img>').attr('src', file.filePathUrl) // ⭐ S3 URL 직접 사용 ⭐
                                                        .attr('alt', file.fileOriginalname)
                                                        .addClass('img-fluid'); // Bootstrap 반응형 이미지 클래스
                            $modalAttachFiles.append(imgElement);
                        } else {
                            // 이미지가 아닐 경우 다운로드 링크로 표시
                            const fileLink = $('<a>')
                                .attr('href', file.filePathUrl) // ⭐ S3 URL 직접 사용 ⭐
                                .attr('target', '_blank') // 새 탭에서 열기
                                .addClass('file-link')
                                .text(file.fileOriginalname);
                            $modalAttachFiles.append(fileLink);
                        }
                    });
                } else {
                    $('#attachFilesSection').hide(); // 첨부 파일이 없으면 섹션 숨기기
                }

                // 신고 상세 모달 열기
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
            axios.post('/admin/report/updateMemberStatus', null, { // POST 요청, 바디 없이 파라미터로 전송
                params: {
                    mbrCd: mbrCd,
                    mbrStatus: newStatus
                }
            })
            .then(response => {
                if (response.data === 'SUCCESS') { // 컨트롤러에서 "SUCCESS" 문자열 반환
                    alert('회원 상태가 성공적으로 변경되었습니다.');
                    $('#statusChangeModal').modal('hide'); // 모달 닫기
                    window.location.reload(); // 페이지 새로고침하여 목록 업데이트
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

    // select change 이벤트 리스너(일괄 저장으로 바꾸면서 필요 없어짐...)
    // document.querySelectorAll('.report-status-select').forEach(function(selectElement) {
    //     selectElement.addEventListener('change', function() {
    //     });
    // });
});