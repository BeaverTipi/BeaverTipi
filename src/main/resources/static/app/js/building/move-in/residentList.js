/**
 * 
 */    
document.addEventListener("DOMContentLoaded",()=>{
	
	document.querySelector('#selectAll').addEventListener('change', function() {
      const checkboxes = document.querySelectorAll('.rowCheckbox');
      checkboxes.forEach(cb => cb.checked = this.checked);
    });
})