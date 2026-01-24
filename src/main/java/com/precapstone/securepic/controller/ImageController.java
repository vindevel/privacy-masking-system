package com.precapstone.securepic.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    // Flask 서버의 이미지 및 GPS 처리 엔드포인트
    private final String FLASK_URL = "https://aaac-34-45-225-141.ngrok-free.app/detect";
    private static final String FLASK_SERVER_URL = "https://aaac-34-45-225-141.ngrok-free.app/processed_image";

    /**
     * 클라이언트가 이미지를 업로드하는 엔드포인트.
     * Flask 서버에 이미지를 전송하고 JSON 응답을 반환합니다.
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("image") MultipartFile file) {
        // 1. 업로드된 파일이 비어 있는지 확인
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "No file uploaded"));
        }

        // 2. 고유 업로드 ID 생성
        String uploadId = UUID.randomUUID().toString();

        try {
            // 3. Flask 서버로 이미지 전송
            Map<String, Object> flaskResponse = sendToFlask(file, uploadId);

            if (flaskResponse == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Failed to process image with Flask server"));
            }

            // 4. Flask 서버의 응답을 그대로 반환
            return ResponseEntity.ok(flaskResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }

    /**
     * Flask 서버로 이미지를 전송하고 JSON 응답을 반환받는 메서드.
     *
     * @param file     업로드된 이미지 파일
     * @param uploadId 고유 업로드 ID
     * @return Flask 서버에서 반환된 JSON 데이터
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> sendToFlask(MultipartFile file, String uploadId) {
        RestTemplate restTemplate = new RestTemplate();

        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // HTTP 요청 바디 설정
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", file.getResource()); // 이미지 파일 추가
        body.add("uploadId", uploadId);

        // HTTP 요청 생성
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            // Flask 서버로 POST 요청 전송
            ResponseEntity<Map> response = restTemplate.exchange(
                    FLASK_URL,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class);

            return response.getBody(); // JSON 응답 반환

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadImageFromFlask() {
        try {
            // 1. Flask 서버에서 이미지 가져오기
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<byte[]> response = restTemplate.getForEntity(FLASK_SERVER_URL, byte[].class);

            // 2. Flask 서버 응답 확인
            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to fetch image from Flask server".getBytes());
            }

            // 3. 이미지 데이터를 직접 반환 (다운로드 파일로 처리)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("blurred_image.jpg").build());

            return new ResponseEntity<>(response.getBody(), headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error downloading image: " + e.getMessage()).getBytes());
        }
    }

}
