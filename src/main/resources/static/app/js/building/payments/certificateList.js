/**
 * 
 */
document.addEventListener("DOMContentLoaded",()=>{
	document.querySelectorAll('.btn-red').forEach(btn => {
    btn.addEventListener('click', function () {
      const confirmDelete = confirm("이 사업자등록증을 삭제하시겠습니까?");
      if (confirmDelete) {
        const row = this.closest('tr');
        row.remove();
      }
    });
  });
})