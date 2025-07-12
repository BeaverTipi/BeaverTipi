document.addEventListener('DOMContentLoaded', function () {
  const addButtons = document.querySelectorAll('.add-btn');
  addButtons.forEach(button => {
    button.addEventListener('click', () => {
      const roomName = button.dataset.room || '해당 호실';
      openModal(roomName);
    });
  });
});

function openModal(room) {
  const modal = document.getElementById('idSearchModal');
  modal.style.display = 'block';
  console.log(`${room}에 입주자 추가`);
}

function closeModal() {
  const modal = document.getElementById('idSearchModal');
  modal.style.display = 'none';
}
