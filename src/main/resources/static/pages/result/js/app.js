document.addEventListener("DOMContentLoaded", function () {
    const mainBox = document.querySelector(".main_box");

    // Flask 서버의 이미지 URL
    const imageUrl = "/api/blurred_image";

    // 이미지 태그 생성 및 삽입
    const imgElement = document.createElement("img");
    imgElement.src = imageUrl; // 서버에서 반환하는 이미지 URL
    imgElement.alt = "Blurred Image";
    imgElement.classList.add("centered-image"); // CSS 스타일 적용을 위해 클래스 추가

    mainBox.appendChild(imgElement);

    // SecurePic 로고 클릭 시 메인 페이지로 이동
    const securePicLogo = document.querySelector(".securepic");
    if (securePicLogo) {
        securePicLogo.addEventListener("click", function () {
            window.location.href = "/"; // 메인 페이지로 이동
        });
    }

    // "블러 위치 재선택" 버튼 클릭 시 feedback 페이지로 이동
    const blurButton = document.querySelector(".button.blur_button");
    if (blurButton) {
        blurButton.addEventListener("click", function () {
            window.location.href = "/feedback"; // feedback 페이지로 이동
        });
    }

    const saveButton = document.querySelector(".button.save_button");

    if (saveButton) {
        saveButton.addEventListener("click", function () {
            // 이미지 다운로드 요청
            const link = document.createElement("a");
            link.href = "/api/blurred_image"; // Java 서버 엔드포인트
            link.download = "blurred_image.jpg"; // 저장될 파일명
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        });
    }

    // SecurePic 로고 클릭 시 메인 페이지로 이동
    const reStart = document.querySelector(".button.upload_button");
    if (reStart) {
        reStart.addEventListener("click", function () {
            window.location.href = "/"; // 메인 페이지로 이동
        });
    }
    
});