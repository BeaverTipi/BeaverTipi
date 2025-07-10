/**
 * 
 */
function execDaumPostcode() {
	new daum.Postcode({
		oncomplete: function(data) {
			document.querySelector("#postcode").value = data.zonecode;
			document.querySelector("#address").value = data.address;
			document.querySelector("#detailAddress").focus();
		}
	}).open();
}
