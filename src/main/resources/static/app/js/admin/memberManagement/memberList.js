document.addEventListener('DOMContentLoaded', function() {
    const resetButton = document.getElementById('resetBtn');
    const searchBtn = document.getElementById('searchBtn');
    const searchForm = document.getElementById('searchForm');

    // 검색 필드 요소 참조
    const userRoleIdSelect = document.querySelector('[name="userRoleId"]');
    const mbrIdInput = document.querySelector('[name="mbrId"]');
    const mbrFrstRegDtFrom = document.querySelector('[name="mbrFrstRegDtFrom"]');
    const mbrFrstRegDtTo = document.querySelector('[name="mbrFrstRegDtTo"]');
    const mbrStatusCodeSelect = document.querySelector('[name="mbrStatusCode"]');
    const mbrEmlAddrInput = document.querySelector('[name="mbrEmlAddr"]');
    const memberNameSearchInput = document.getElementById('memberNameSearchInput');
    const memberNicknameSearchInput = document.getElementById('memberNicknameSearchInput');

    const currentPageNoInput = document.getElementById('currentPageNoInput');

    const closeButtons = document.querySelectorAll('[data-dismiss="modal"]');
    closeButtons.forEach(button => {
        button.addEventListener('click', function(event) {
            console.log('닫기 버튼 클릭됨!', this);
            console.log('클릭된 버튼의 data-dismiss 값:', this.getAttribute('data-dismiss'));
        });
    });

    // 페이징 처리 함수
    window.fnPaging = function(pageNo) {
        if (currentPageNoInput) {
            currentPageNoInput.value = pageNo;
        }
        searchForm.submit();
    };

    // 초기화 버튼 클릭 이벤트
    if (resetButton) {
        resetButton.addEventListener('click', function(event) {
            // 모든 검색 필드 값 초기화
            if (mbrIdInput) mbrIdInput.value = '';
            if (mbrFrstRegDtFrom) mbrFrstRegDtFrom.value = '';
            if (mbrFrstRegDtTo) mbrFrstRegDtTo.value = '';
            if (mbrEmlAddrInput) mbrEmlAddrInput.value = '';
            if (memberNameSearchInput) memberNameSearchInput.value = '';
            if (memberNicknameSearchInput) memberNicknameSearchInput.value = '';

            // 드롭다운 초기화
            if (userRoleIdSelect) userRoleIdSelect.value = '';
            if (mbrStatusCodeSelect) mbrStatusCodeSelect.value = '';

            // 페이지 번호도 1로 초기화
            if (currentPageNoInput) {
                currentPageNoInput.value = 1;
            }

            searchForm.submit();
        });
    }

    // --- 검색 폼 제출 이벤트 ---
    if (searchForm) {
        searchForm.addEventListener('submit', function(event) {
            // "검색" 버튼 클릭 시 페이지를 1로 초기화 (일반적인 검색 동작)
            if (currentPageNoInput) {
                currentPageNoInput.value = 1;
            }
        });
    }

    // --- ⭐ 회원 목록 행 클릭 이벤트 (상세 모달 띄우기) ⭐ ---
    // document.querySelectorAll('.member-row-clickable') 대신 jQuery 사용 (JSP에 jQuery 포함되어 있으므로)
    $(document).on('click', '.member-row-clickable', function() {
        const mbrCd = $(this).data('mbr-cd'); // data-mbr-cd 속성 값 가져오기
        console.log("회원 상세 조회 요청:", mbrCd);

        // AJAX 요청으로 회원 상세 정보 가져오기
        axios.get(`/ajax/admin/member/detail/${mbrCd}`) // 예시 URL, 실제 백엔드 URL에 맞춰 수정 필요
            .then(response => {
                const member = response.data;
                console.log("회원 상세 데이터 수신:", member);

                // 모달에 데이터 채우기
                $('#modalMbrCd').val(member.mbrCd); // Hidden input에 회원 코드 저장
                $('#modalMbrNm').text(member.mbrNm || 'null');
                $('#modalMbrId').text(member.mbrId || 'null');
                $('#modalMbrNnm').text(member.mbrNnm || 'null');

                // 회원 구분 (여러 개일 수 있으므로 쉼표로 연결)
                let userRoleDisplay = 'null';
                if (member.memRoleList && member.memRoleList.length > 0) {
                    userRoleDisplay = member.memRoleList.map(role => {
                        switch(role.userRoleId) {
                            case 'USER': return '일반회원';
                            case 'TENANCY': return '임대인';
                            case 'BROKER': return '중개인';
                            case 'ADMIN': return '관리자';
                            default: return role.userRoleId;
                        }
                    }).join(', ');
                }
                $('#modalUserRoleIds').text(userRoleDisplay);

                $('#modalMbrFrstRegDt').text(member.mbrFrstRegDt || 'null');
                $('#modalMbrEmlAddr').text(member.mbrEmlAddr || 'null');

                // 회원 상태 드롭다운 선택
                $('#modalMbrStatusCode').val(member.mbrStatusCode);

                // 모달 띄우기
                $('#memberDetailModal').modal('show');
            })
            .catch(error => {
                console.error('회원 상세 정보 로드 실패:', error);
                Swal.fire({
                    title: "오류",
                    text: "회원 상세 정보를 불러오는 데 실패했습니다.",
                    icon: "error",
                    confirmButtonText: "확인"
                });
            });
    });

    // --- ⭐ 모달 내 '상태 변경' 버튼 클릭 이벤트 ⭐ ---
    $('#updateMemberStatusBtn').on('click', function() {
        const mbrCd = $('#modalMbrCd').val(); // hidden input에서 회원 코드 가져오기
        const newMbrStatusCode = $('#modalMbrStatusCode').val();

        if (!mbrCd || !newMbrStatusCode) {
            Swal.fire({
                title: "경고",
                text: "회원 정보가 불완전합니다.",
                icon: "warning",
                confirmButtonText: "확인"
            });
            return;
        }

        const statusDisplayNames = {
            'ACTIVE': '정상',
            'INACTIVE': '비활성',
            'SUSPENDED': '정지',
            'WITHDRAWN': '탈퇴'
        };

        const displayStatus = statusDisplayNames[newMbrStatusCode] || newMbrStatusCode; // 매핑된 이름이 없으면 원본 코드 사용

        Swal.fire({
            title: `회원 ${mbrCd}의 상태를 '${displayStatus}'(으)로 변경하시겠습니까?`,
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33'
        }).then((result) => {
            if (result.isConfirmed) {
                axios.post('/admin/member/updateStatusFromDetail', null, {
                    params: {
                        mbrCd: mbrCd,
                        mbrStatusCode: newMbrStatusCode
                    }
                })
                .then(response => {
                    if (response.data === 'SUCCESS') {
                        Swal.fire({
                            title: "성공",
                            text: "회원 상태가 성공적으로 변경되었습니다.",
                            icon: "success",
                            confirmButtonText: "확인"
                        }).then(() => {
                            $('#memberDetailModal').modal('hide'); // 모달 닫기
                            window.location.reload(); // 페이지 새로고침하여 변경된 상태 반영
                        });
                    } else {
                        Swal.fire({
                            title: "실패",
                            text: "회원 상태 변경에 실패했습니다.",
                            icon: "error",
                            confirmButtonText: "확인"
                        });
                    }
                })
                .catch(error => {
                    console.error('회원 상태 변경 AJAX 오류:', error);
                    Swal.fire({
                        title: "오류",
                        text: "회원 상태 변경 중 오류가 발생했습니다.",
                        icon: "error",
                        confirmButtonText: "확인"
                    });
                });
            }
        });
    });
});