document.addEventListener('DOMContentLoaded', function() {
    const resetButton = document.getElementById('resetButton');
    const saveButton = document.getElementById('saveButton');
    const memberTypeSelect = document.getElementById('memberTypeSelect');
    const memberNameInput = document.getElementById('memberNameInput');
    const mbrFrstRegDtFrom = document.getElementById('mbrFrstRegDtFrom');
    const mbrFrstRegDtTo = document.getElementById('mbrFrstRegDtTo');
    const memberStatusSelect = document.getElementById('memberStatusSelect');
    const memberEmailInput = document.getElementById('memberEmailInput');

    const changedMembers = new Set(); // 중복 방지를 위해 Set 사용

    if (resetButton) {
        resetButton.addEventListener('click', function(event) {
            memberNameInput.value = '';
            mbrFrstRegDtFrom.value = '';
            mbrFrstRegDtTo.value = '';
            memberEmailInput.value = '';

            for (let i = 0; i < memberTypeSelect.options.length; i++) {
                memberTypeSelect.options[i].selected = false;
            }
            memberStatusSelect.value = ''; 
            
            changedMembers.clear(); 
            document.querySelectorAll('.member-status-select').forEach(selectElement => {
                selectElement.value = selectElement.dataset.originalStatus;
            });
        });
    }

    document.querySelectorAll('.member-status-select').forEach(selectElement => {
        selectElement.addEventListener('change', function() {
            const row = this.closest('tr');
            const mbrCd = row.dataset.mbrCd;
            const newStatusCode = this.value;
            
            changedMembers.add(mbrCd); 
            
            console.log(`Member ${mbrCd} status changed to ${newStatusCode}`); 
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
                    // CSRF 토큰이 필요하다면 여기에 추가 (Spring Security 사용 시)
                    // 'X-CSRF-TOKEN': 'YOUR_CSRF_TOKEN_HERE' 
                },
                body: JSON.stringify(membersToUpdate)
            })
            .then(response => {
                if (response.ok) {
                    return response.text(); 
                }
                throw new Error('네트워크 응답이 올바르지 않습니다.');
            })
            .then(data => {
                alert('회원 상태가 성공적으로 업데이트되었습니다.');
                console.log('Server response:', data);
                
                changedMembers.forEach(mbrCd => {
                    const row = document.querySelector(`tr[data-mbr-cd="${mbrCd}"]`);
                    if (row) {
                        const selectElement = row.querySelector('.member-status-select');
                        selectElement.dataset.originalStatus = selectElement.value;
                    }
                });
                changedMembers.clear(); 
            })
            .catch(error => {
                console.error('업데이트 중 오류 발생:', error);
                alert('회원 상태 업데이트 중 오류가 발생했습니다.');
            });
        });
    }
});