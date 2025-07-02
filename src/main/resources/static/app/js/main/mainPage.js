/**
 * 
 */
document.addEventListener("DOMContentLoaded", function() {
	const loginForm = document.querySelector("#loginForm");
	const username = document.querySelector("#username");
	const password = document.querySelector("#password");
	const logout = document.querySelector("#logout");
	const baseURL = "http://localhost";


	loginForm.addEventListener("submit", (e) => {
		e.preventDefault();
		if (!username.value.trim()) {
			Swal.fire({
				icon: 'warning',
				title: '알림',
				text: '아이디가 입력되지 않았습니다.',
				confirmButtonText: '확인'
			})
			return;
		}
		if (!password.value.trim()) {
			Swal.fire({
				icon: 'warning',
				title: '알림',
				text: '비밀번호가 입력되지 않았습니다.',
				confirmButtonText: '확인'
			})
			return;
		}
		const json = axios.formToJSON(e.target);
		customAxios.post("/account/login", json)
			.then(resp => {
				const params = new URLSearchParams(location.search);
      			const redirect = params.get("redirect");
				if(redirect){
					location.href = redirect;
				}else{
					history.back();
				}
			})
			.catch(err => {
				if (err.response && err.response.status === 401) {
					Swal.fire({
						icon: "error",
						title: "로그인 실패",
						text: "아이디 또는 비밀번호가 올바르지 않습니다.",
						confirmButtonText: '확인'
					});
				} else {
					Swal.fire({
						icon: "error",
						title: "오류",
						text: "예기치 않은 오류가 발생했습니다.",
						confirmButtonText: '확인'
					});
				}
			});

	})
	logout.addEventListener("click", (e) => {
		e.preventDefault();
		e.stopPropagation();
		e.stopImmediatePropagation();
		customAxios.post("/account/logout", {})
			.then(resp => {
				Swal.fire({
					icon: 'success',
					title: '알림',
					text: '로그아웃 되었습니다.',
					confirmButtonText: '확인'
				})
				.then(()=>{
				location.href = baseURL;
				})
			})
			.catch(err => {
				Swal.fire({
					icon: "error",
					title: "오류",
					text: "예기치 않은 오류가 발생했습니다.",
					confirmButtonText: '확인'
				});

			})

	});
})