document.addEventListener("DOMContentLoaded", function () {
    // SecurePic 로고 클릭 시 메인 페이지로 이동
    const securePicLogo = document.querySelector(".securepic");
    if (securePicLogo) {
        securePicLogo.addEventListener("click", function () {
            window.location.href = "/"; // Main 페이지로 이동
        });
    }

    fetch("/api/blurred_image", {
        method: "GET",
    })
        .then((response) => {
            if (!response.ok) {
                throw new Error("블러 처리된 이미지를 불러오지 못했습니다.");
            }
            return response.blob(); // 이미지 데이터를 Blob 형태로 변환
        })
        .then((blob) => {
            const imageUrl = URL.createObjectURL(blob);
            sessionStorage.setItem("blurredImageUrl", imageUrl);
            window.location.href = "/result";
        })
        .catch((error) => {
            console.error("에러 발생:", error);
            alert("이미지 불러오기 오류 발생");
        });
    
    
});
