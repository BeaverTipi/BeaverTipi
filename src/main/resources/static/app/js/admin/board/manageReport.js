$(document).ready(function() {

            if (window.opener) { // 현재 창이 window.open()으로 열린 팝업 창일 경우에만 실행
                // 콘텐츠의 실제 크기 측정
                // .container-fluid 요소의 실제 스크롤 가능한 너비/높이를 측정
                const contentContainer = document.querySelector('.container-fluid.content-wrapper');
                let newWidth = 0;
                let newHeight = 0;

                if (contentContainer) {
                    newWidth = contentContainer.scrollWidth;
                    newHeight = contentContainer.scrollHeight;
                } else {
                    // 안전 장치: .content-wrapper가 없다면 body 전체를 측정 (덜 정확할 수 있음)
                    newWidth = document.body.scrollWidth;
                    newHeight = document.body.scrollHeight;
                }

                // 브라우저 UI 요소 (스크롤바, 창 테두리, 타이틀 바 등)를 위한 여백 추가
                // 이 값은 브라우저와 OS에 따라 다를 수 있으므로, 테스트 후 적절히 조절해야 합니다.
                // 특히 타이틀 바, 주소 표시줄, 스크롤바 등 브라우저 자체 UI가 차지하는 공간을 고려해야 합니다.
                const paddingWidth = 50;  // 좌우 여백 및 스크롤바 공간
                const paddingHeight = 120; // 상하 여백 및 타이틀 바, 하단 버튼 등을 위한 공간 (더 여유 있게)

                newWidth += paddingWidth;
                newHeight += paddingHeight;

                // 최소/최대 크기 제한 (선택 사항, 팝업이 너무 작거나 커지는 것을 방지)
                const minWidth = 600;
                const minHeight = 400;
                const maxWidth = window.screen.width * 0.6; // 화면 너비의 %
                const maxHeight = window.screen.height * 0.7; // 화면 높이의 %

                newWidth = Math.max(minWidth, Math.min(newWidth, maxWidth));
                newHeight = Math.max(minHeight, Math.min(newHeight, maxHeight));

                // 팝업 창 크기 조절
                window.resizeTo(newWidth, newHeight);

                // 팝업 창 중앙 정렬 (선택 사항, resizeTo 후 다시 이동)
                const screenLeft = window.screenLeft || window.screenX;
                const screenTop = window.screenTop || window.screenY;

                const left = screenLeft + (window.outerWidth - newWidth) / 2;
                const top = screenTop + (window.outerHeight - newHeight) / 2;

                window.moveTo(left, top);
            }
            
            $('#popupChangeMemberStatusBtn').on('click', function() {
                const mbrCd = $(this).data('mbr-cd');
                const currentStatus = $(this).data('current-status');
                $('#selectedMbrCd').text(mbrCd);
                $('#currentMbrStatus').text(currentStatus);
                $('#newMbrStatus').val(currentStatus);
                $('#statusChangeModal').modal('show');
            });

            $('#popupChangeListingStatusBtn').on('click', function() {
                const lstgId = $(this).data('lstg-id');
                const currentDel = $(this).data('current-del');
                $('#selectedLstgId').text(lstgId);
                $('#currentLstgDel').text(currentDel == 'Y' ? '삭제됨' : '활성');
                $('#newLtsgDel').val(currentDel);
                $('#listingStatusChangeModal').modal('show');
            });

            $('#btnUpdateMemberStatus').on('click', function() {
                const mbrCd = $('#selectedMbrCd').text();
                const newStatus = $('#newMbrStatus').val();

                if (!mbrCd || !newStatus) {
                    alert('회원 ID와 새로운 상태를 선택해주세요.');
                    return;
                }

                if (confirm(`${mbrCd} 회원의 상태를 "${newStatus}"(으)로 변경하시겠습니까?`)) {
                    axios.post('/ajax/admin/report/updateMemberStatus', null, {
                        params: {
                            mbrCd: mbrCd,
                            mbrStatus: newStatus
                        }
                    })
                    .then(response => {
                        if (response.data == 'SUCCESS') {
                            alert('회원 상태가 성공적으로 변경되었습니다.');
                            $('#statusChangeModal').modal('hide');
                            window.opener.location.reload();
                            window.close();
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

                if (confirm(`${lstgId} 매물의 삭제 상태를 "${newDel == 'Y' ? '삭제' : '미삭제'}"(으)로 변경하시겠습니까?`)) {
                    axios.post('/admin/report/updateListingDeleteStatus', null, {
                        params: {
                            lstgId: lstgId,
                            lstgDel: newDel
                        }
                    })
                    .then(response => {
                        if (response.data == 'SUCCESS') {
                            alert('매물 삭제 상태가 성공적으로 변경되었습니다.');
                            $('#listingStatusChangeModal').modal('hide');
                            window.opener.location.reload();
                            window.close();
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