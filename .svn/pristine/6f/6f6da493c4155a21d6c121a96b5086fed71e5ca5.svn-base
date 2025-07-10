document.addEventListener("DOMContentLoaded", function() {
	const fileInput = document.querySelector("#uploadFile");
	const crtfInput = document.querySelector("#crtfNo");
	const pdfContainer = document.getElementById("pdfViewer");
	const emptyMessage = document.getElementById("emptyMessage");

	fileInput.addEventListener("change", function(event) {
		const file = event.target.files[0];
		pdfContainer.innerHTML = "";
		emptyMessage.style.display = "none";

		if (!file) {
			emptyMessage.style.display = "block";
			return;
		}

		const fileType = file.type;

		// 1. 미리보기
		if (fileType === "application/pdf") {
			const reader = new FileReader();
			reader.onload = function() {
				const typedarray = new Uint8Array(this.result);
				pdfjsLib.getDocument(typedarray).promise.then(function(pdf) {
					pdf.getPage(1).then(function(page) {
						const scale = 0.8;
						const viewport = page.getViewport({ scale });

						const canvas = document.createElement("canvas");
						const context = canvas.getContext("2d");
						canvas.height = viewport.height;
						canvas.width = viewport.width;

						pdfContainer.appendChild(canvas);

						const renderContext = {
							canvasContext: context,
							viewport: viewport,
						};
						page.render(renderContext);
					});
				});
			};
			reader.readAsArrayBuffer(file);
		} else if (fileType.startsWith("image/")) {
			const img = document.createElement("img");
			img.src = URL.createObjectURL(file);
			img.style.maxWidth = "100%";
			img.style.maxHeight = "750px";
			img.style.borderRadius = "8px";
			pdfContainer.appendChild(img);
		} else {
			pdfContainer.innerHTML =
				'<div class="text-danger">지원하지 않는 파일 형식입니다.</div>';
			return;
		}

		// 2. 자격증 번호 OCR 처리
		const formData = new FormData();
		formData.append("file", file);

		axios
			.post("/ajax/pdf/extract-crtfno", formData, {
				headers: {
					"Content-Type": "multipart/form-data",
				},
			})
			.then((response) => {
				const data = response.data;

				if (data.success) {
					if (data.crtfNo && data.crtfNo !== "Not found") {
						crtfInput.value = data.crtfNo;
					} else {
						crtfInput.value = ""; // 혹시 이전 값 있을 수 있으므로 초기화
						Swal.fire({
							icon: "warning",
							title: "자격증 번호 없음",
							text: "자격증 번호를 찾을 수 없습니다.",
						});
					}
				} else {
					Swal.fire({
						icon: "error",
						title: "오류",
						text: "파일 분석 중 오류가 발생했습니다.",
					});
				}
			})

			.catch((error) => {
				console.error("자격증 번호 추출 오류", error);
				Swal.fire({
					icon: "error",
					title: "오류",
					text: "파일 분석 중 오류가 발생했습니다.",
				});
			});
	});
});
