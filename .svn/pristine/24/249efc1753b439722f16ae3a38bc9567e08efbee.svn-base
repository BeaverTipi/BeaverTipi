/**
 * 
 */
document.addEventListener("DOMContentLoaded", function() {
	const loginForm = document.querySelector("#loginForm");
	const username = document.querySelector("#username");
	const password = document.querySelector("#password");
	const logout = document.querySelector("#logout");
	const baseURL = location.origin;

	 const roleRadios = document.querySelectorAll('input[name="loginRole"]');

  	 const presetAccounts = {
        landlord: { id: "manual_user_08", pw: "qwer" },
        broker: { id: "openthatsodapop", pw: "qwer" },
        admin: { id: "manual_user_13", pw: "qwer" },
        resident: { id: "qwer", pw: "qwer" },
        member: { id: "", pw: "" }
      };

      roleRadios.forEach(radio => {
        radio.addEventListener("change", () => {
          const selected = presetAccounts[radio.value];
          if (selected) {
            username.value = selected.id;
            password.value = selected.pw;
          }
        });
      });
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
				Swal.fire({
					icon: 'success',
					title: '로그인 성공',
					text: '잠시 후 이동합니다.',
					showConfirmButton: false,
					timer: 1000
				})
			})
			.then(() => {
				const params = new URLSearchParams(location.search);
				const redirect = params.get("redirect");
				location.href = redirect || "/";
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
					.then(() => {
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