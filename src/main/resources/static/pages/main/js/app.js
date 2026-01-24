document.addEventListener("DOMContentLoaded", function () {
	// DOM 요소 가져오기
	const uploadButton = document.getElementById("uploadButton");
	const fileInput = document.getElementById("fileInput");
	const progressBar = document.getElementById("progressBar"); // 로딩바 추가

	// 버튼 클릭 시 파일 선택 창 열기
	uploadButton.addEventListener("click", function () {
		uploadButton.className = "ei72_3384_72_3369";
		fileInput.click();

		// 파일 선택 취소 시 버튼 상태 복구
		setTimeout(() => {
			if (!fileInput.files || fileInput.files.length === 0) {
				uploadButton.className = "ei72_3384_72_3367";
			}
		}, 500);
	});

	// 파일 선택 후 이벤트 처리
	fileInput.addEventListener("change", function () {
		const file = fileInput.files[0];

		if (file) {
			updateProgressBar(0);
			uploadButton.className = "ei72_3384_72_33610";
			uploadImageToServer(file);
		} else {
			uploadButton.className = "ei72_3384_72_3367";
		}
	});

	// 서버로 이미지 업로드 함수
	function uploadImageToServer(file) {
		const formData = new FormData();
		formData.append("image", file);

		const uploadUrl = "/api/image/upload"; // Spring Boot의 업로드 엔드포인트

		// 로딩바 초기화
		updateProgressBar(10);

		fetch(uploadUrl, {
			method: "POST",
			body: formData,
		})
			.then((response) => {
				if (!response.ok) {
					throw new Error("서버에 이미지 전송 실패");
				}
				return response.json();
			})
			.then((data) => {
				// 서버 응답 데이터 저장
				sessionStorage.setItem("flaskResponse", JSON.stringify(data));

				// 로딩바 50%로 업데이트
				updateProgressBar(50);

				// Flask 서버에서 처리된 이미지 다운로드 시작
				return downloadProcessedImage();
			})
			.then(() => {
				uploadButton.className = "ei72_3384_72_3367";

				// 페이지 이동
				window.location.href = "/feedback";
			})
			.catch((error) => {
				console.error("에러 발생:", error);
				alert("이미지 업로드 중 오류가 발생했습니다.");
				uploadButton.className = "ei72_3384_72_3367";
				updateProgressBar(0);
			});
	}

	// Flask 서버에서 처리된 이미지 다운로드 함수
	function downloadProcessedImage() {
		const downloadUrl = "/api/image/download"; // Spring Boot의 다운로드 엔드포인트

		// 로딩바 50%에서 100%까지 시뮬레이션
		return fetch(downloadUrl)
			.then((response) => {
				if (!response.ok) {
					throw new Error("Flask 서버에서 이미지 다운로드 실패");
				}

				// 다운로드 성공 시 로딩바 100%로 업데이트
				return simulateProgress(50, 100);
			});
	}

	// 로딩바 업데이트 함수
	function updateProgressBar(percentage) {
		if (progressBar) {
			progressBar.style.width = `${percentage}%`;
		}
	}

	// 로딩 진행 시뮬레이션 함수
	function simulateProgress(start, end) {
		return new Promise((resolve) => {
			let progress = start;
			const interval = setInterval(() => {
				progress += 5;
				updateProgressBar(progress);

				if (progress >= end) {
					clearInterval(interval);
					resolve();
				}
			}, 100);
		});
	}
});
