package com.precapstone.securepic.controller;

import java.util.Map;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api") // API 기본 경로 설정
public class BlurController {

    private static final String FLASK_SERVER_URL = "https://aaac-34-45-225-141.ngrok-free.app/blurred_image";


    @PostMapping("/process-ids")
    public ResponseEntity<?> processIdsInFlask(@RequestBody Map<String, Object> requestBody) {
        try {
            // Flask 서버의 /process-ids 엔드포인트에 POST 요청 보내기
            RestTemplate restTemplate = new RestTemplate();
            String flaskEndpoint = FLASK_SERVER_URL + "/process-ids";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(flaskEndpoint, HttpMethod.POST, entity, String.class);

            // Flask 서버 응답을 그대로 반환
            return ResponseEntity
                    .status(response.getStatusCode())
                    .body(response.getBody());

        } catch (Exception e) {
            // 오류 발생 시 500 반환
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing IDs in Flask: " + e.getMessage());
        }
    }


    @GetMapping("/blurred_image")
    public ResponseEntity<?> getBlurredImageFromFlask() {
        try {
            // Flask 서버의 blurred_image 엔드포인트에 GET 요청 보내기
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<byte[]> response = restTemplate.getForEntity(FLASK_SERVER_URL, byte[].class);

            // Flask 서버 응답을 그대로 반환 (이미지 타입 유지)
            return ResponseEntity
                    .status(response.getStatusCode())
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(response.getBody());

        } catch (Exception e) {
            // 오류 발생 시 500 반환
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching blurred image: " + e.getMessage());
        }
    }
}
