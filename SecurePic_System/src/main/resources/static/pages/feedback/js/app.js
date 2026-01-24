document.addEventListener("DOMContentLoaded", function () {
	// sessionStorage에서 Flask 서버 응답 데이터 가져오기
	const flaskResponse = JSON.parse(sessionStorage.getItem("flaskResponse"));

	if (!flaskResponse) {
		console.error("Flask 응답 데이터가 없습니다.");
		alert("데이터를 불러오는 중 오류가 발생했습니다.");
		return;
	}

	// 1. 처리된 이미지 표시
	const processedImageUrl = flaskResponse.processedImageUrl;
	const imageElement = document.getElementById("processedImage");
	imageElement.src = processedImageUrl;

	// 2. 바운딩 박스 데이터 준비
	const boundingBoxes = flaskResponse.boundingBoxes || [];
	const structuredData = boundingBoxes.map((box, index) => ({
		id: box.id,
		feedback: box.feedback,
		coordinates: box.bbox,
		index: index + 1, // 순서 부여
	}));

	// 3. 피드백 요소 및 토글 버튼 참조
	const feedbackElements = [
		document.getElementById("feedback1"),
		document.getElementById("feedback2"),
		document.getElementById("feedback3"),
	];
	const toggleElements = [
		document.getElementById("toggle1"),
		document.getElementById("toggle2"),
		document.getElementById("toggle3"),
	];

	// 4. 바운딩 박스 처리
	for (let i = 0; i < feedbackElements.length; i++) {
		const data = structuredData[i];

		if (data) {
			// ID가 "O"일 경우: 토글 숨기고 피드백만 표시
			if (data.id === "O") {
				feedbackElements[i].textContent = data.feedback || "";
				toggleElements[i].style.display = "none";
				feedbackElements[i].style.display = "block";
			} else {
				// ID가 숫자일 경우: 토글과 피드백 모두 표시
				feedbackElements[i].textContent = data.feedback || "";
				toggleElements[i].style.display = "block";
				feedbackElements[i].style.display = "block";
				toggleElements[i].dataset.id = data.id; // data-id 속성 추가
				toggleElements[i].dataset.coordinates = JSON.stringify(data.coordinates); // data-coordinates 추가
			}
		} else {
			// null일 경우: 피드백과 토글 모두 숨기기
			feedbackElements[i].textContent = "";
			toggleElements[i].style.display = "none";
			feedbackElements[i].style.display = "none";
		}
	}

	// 5. 토글 버튼 동작 추가
	toggleElements.forEach((toggle) => {
		toggle.addEventListener("click", function () {
			toggle.classList.toggle("on");
		});
	});

	// 6. 완료 버튼 클릭 시 선택된 데이터 추출
	const completeButton = document.querySelector(".complete_box2");
	completeButton.addEventListener("click", function () {
		const toggles = document.querySelectorAll(".toggle");
		const selectedData = [];

		toggles.forEach((toggle) => {
			if (toggle.classList.contains("on")) {
				const id = toggle.dataset.id;
				const coordinates = toggle.dataset.coordinates;

				if (id && coordinates) {
					selectedData.push({
						id: parseInt(id),
						coordinates: JSON.parse(coordinates),
					});
				}
			}
		});

		// 선택된 데이터 전송
		console.log("Final Selected Data:", selectedData);
		fetch("/api/selected-ids", {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
			},
			body: JSON.stringify({ selectedDatas: selectedData }),
		})
			.then((response) => response.json())
			.then((data) => {
				window.location.href = "/loading";
			})
			.catch((error) => {
				console.error("Error sending selected data:", error);
			});
	});

	// 7. 로고 클릭 시 메인 페이지로 이동
	const logoText = document.querySelector(".logo_txt");
	if (logoText) {
		logoText.addEventListener("click", function () {
			window.location.href = "/";
		});
	}
});
