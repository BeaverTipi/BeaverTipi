document.addEventListener('DOMContentLoaded', function() {
    const resetButton = document.getElementById('resetButton');
    const saveButton = document.getElementById('saveButton');
    const searchForm = document.getElementById('searchForm'); // searchForm 참조 추가
    const memberTypeSelect = document.getElementById('memberTypeSelect');
    const memberNameInput = document.getElementById('memberNameInput');
    const mbrFrstRegDtFrom = document.getElementById('mbrFrstRegDtFrom');
    const mbrFrstRegDtTo = document.getElementById('mbrFrstRegDtTo');
    const memberStatusSelect = document.getElementById('memberStatusSelect');
    const memberEmailInput = document.getElementById('memberEmailInput');
    const currentPageNoInput = document.getElementById('currentPageNoInput'); // hidden input ID 변경

    const changedMembers = new Set(); // 중복 방지를 위해 Set 사용

    // --- 페이징 처리 함수 ---
    // PaginationRenderer에서 호출할 페이지 이동 함수
    // 이 함수는 전역 스코프에 있어야 HTML에서 직접 호출할 수 있습니다.
    window.fnPaging = function(pageNo) {
        if (currentPageNoInput) {
            currentPageNoInput.value = pageNo; // hidden input의 값 변경
        }
        searchForm.submit(); // 검색 폼 제출 (현재 검색 조건 유지하면서 페이지 이동)
    };

    if (resetButton) {
        resetButton.addEventListener('click', function(event) {
            // 1. 모든 검색 필드 값 초기화
            memberNameInput.value = '';
            mbrFrstRegDtFrom.value = '';
            mbrFrstRegDtTo.value = '';
            memberEmailInput.value = '';

            // memberTypeSelect (다중 선택) 옵션 초기화
            // jQuery를 사용하고 있다면 $('#memberTypeSelect').val(''); 또는 .val(null);
            for (let i = 0; i < memberTypeSelect.options.length; i++) {
                memberTypeSelect.options[i].selected = false;
            }
            // memberStatusSelect (단일 선택) 초기화 (첫 번째 빈 옵션 선택)
            memberStatusSelect.value = '';

            // 2. 일괄 저장을 위한 변경 내역 Set 초기화
            changedMembers.clear();

            // 3. 페이지 번호도 1로 초기화 (hidden input의 값 변경)
            if (currentPageNoInput) {
                currentPageNoInput.value = 1;
            }

            // 4. 검색 폼 제출
            searchForm.submit();
        });
    }

	// 검색 폼 자체에 submit 이벤트 리스너를 추가하여 페이지 번호를 초기화합니다.
    if (searchForm) {
        searchForm.addEventListener('submit', function(event) {
            // 폼이 제출되기 전에 페이지 번호를 1로 설정
            if (currentPageNoInput) {
                currentPageNoInput.value = 1;
            }
            // 폼의 기본 제출 동작은 유지 (return true; 와 동일)
        });
    }

    document.querySelectorAll('.member-status-select').forEach(selectElement => {
        selectElement.addEventListener('change', function() {
            const row = this.closest('tr');
            const mbrCd = row.dataset.mbrCd;
            
            // 변경된 회원 목록에 추가 (중복 방지)
            changedMembers.add(mbrCd); 
            
            console.log(`회원 ${mbrCd}의 상태 변경이 감지되었습니다.`); 
        });
    });

    if (saveButton) {
        saveButton.addEventListener('click', function() {
            if (changedMembers.size === 0) {
                alert('변경할 내용이 없습니다.');
                return;
            }

            const membersToUpdate = [];
            changedMembers.forEach(mbrCd => {
                const row = document.querySelector(`tr[data-mbr-cd="${mbrCd}"]`);
                if (row) {
                    const selectElement = row.querySelector('.member-status-select');
                    membersToUpdate.push({
                        mbrCd: mbrCd,
                        mbrStatusCode: selectElement.value 
                    });
                }
            });

            fetch('/admin/member/updateStatus', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    // Spring Security를 사용한다면 CSRF 토큰을 여기에 포함해야 합니다.
                    // JSP에 <meta name="_csrf" content="${_csrf.token}"/> 와 같이 추가하고 가져올 수 있습니다.
                    // 'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').getAttribute('content')
                },
                body: JSON.stringify(membersToUpdate)
            })
            .then(response => {
                if (response.ok) { // HTTP 상태 코드가 200-299 범위인 경우
                    return response.text(); 
                }
                // 오류 응답의 경우, 본문 내용을 읽어 오류 메시지로 던집니다.
                return response.text().then(text => { throw new Error(text) });
            })
            .then(data => {
                alert('회원 상태가 성공적으로 업데이트되었습니다.');
                console.log('서버 응답:', data);
                
                // 성공적으로 업데이트된 후, 각 select 박스의 원래 상태(data-original-status)를 현재 값으로 갱신
                // 이렇게 하면 다시 변경사항이 없음을 정확히 판단할 수 있습니다.
                changedMembers.forEach(mbrCd => {
                    const row = document.querySelector(`tr[data-mbr-cd="${mbrCd}"]`);
                    if (row) {
                        const selectElement = row.querySelector('.member-status-select');
                        selectElement.dataset.originalStatus = selectElement.value;
                    }
                });
                changedMembers.clear(); // 변경 내역 초기화
            })
            .catch(error => {
                console.error('업데이트 중 오류 발생:', error);
                alert('회원 상태 업데이트 중 오류가 발생했습니다. 상세: ' + error.message);
            });
        });
    }
});