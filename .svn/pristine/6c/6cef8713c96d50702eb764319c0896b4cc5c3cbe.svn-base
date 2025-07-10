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

            // ⭐ memberTypeSelect (단일 선택) 옵션 초기화 ⭐
            // 단일 선택 드롭다운은 value를 '' (첫 번째 "--선택--" 옵션의 값)으로 설정하면 됩니다.
            // jQuery를 사용하고 있다면 $('#memberTypeSelect').val('');
            memberTypeSelect.value = ''; // 또는 기본값이 있다면 그 값으로 설정 (예: 'USER')

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
    // 주의: 이 로직은 "검색" 버튼을 눌렀을 때만 페이지 번호를 1로 초기화합니다.
    // 리셋 버튼은 위에서 따로 처리하고 있으므로, 겹치지 않도록 주의합니다.
    // 현재 `resetButton` 클릭 시에도 `searchForm.submit()`을 호출하고 있으므로,
    // 이 `searchForm`의 `submit` 이벤트 리스너도 동작할 것입니다.
    // 따라서 `resetButton` 로직에서 페이지를 1로 설정하는 것이 중복될 수 있습니다.
    // 어느 한 곳에서만 페이지 초기화를 하거나, 명확히 구분해야 합니다.
    // 현재는 `resetButton`에서 1로 설정하고, `searchForm` submit시에도 1로 설정하므로
    // "검색" 버튼을 누르거나 (페이지 이동은 아님), 리셋 버튼을 누르면 무조건 1페이지로 갑니다.
    if (searchForm) {
        searchForm.addEventListener('submit', function(event) {
            // 폼이 제출되기 전에 페이지 번호를 1로 설정
            // (이미 resetButton에서 1로 설정된 경우 중복이지만, 안전하게 유지)
            // 만약 '검색' 버튼만 눌렀을 때도 1페이지로 가길 원한다면 이 로직이 필요합니다.
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