package com.precapstone.securepic.controller;

import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Controller
public class FeedbackPageController {

    private final String FLASK_IDS_URL = "https://aaac-34-45-225-141.ngrok-free.app/process-ids"; // Flask ID 처리 모듈 URL

    // 로딩 페이지 반환
    @GetMapping("/loading")
    public String loadingPage() {
        return "loading/index"; // templates/loading/index.html 반환
    }

    @PostMapping("/api/selected-ids")
    public ResponseEntity<?> sendSelectedDatasToFlask(@RequestBody Map<String, List<Map<String, Object>>> payload) {
        try {
            // 선택된 데이터 리스트 확인
            List<Map<String, Object>> selectedDatas = payload.get("selectedDatas");

            System.out.println("Received Datas(spring): " + selectedDatas);

            // Flask 서버로 데이터 전송
            RestTemplate restTemplate = new RestTemplate();

            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 요청 데이터와 헤더를 합침
            HttpEntity<Map<String, List<Map<String, Object>>>> request = new HttpEntity<>(
                    Map.of("selectedDatas", selectedDatas), headers);

            System.out.println("Sending POST request to Flask at: " + FLASK_IDS_URL);

            // Flask 서버로 POST 요청
            ResponseEntity<String> flaskResponse = restTemplate.postForEntity(FLASK_IDS_URL, request, String.class);

            // 성공 응답 반환
            return ResponseEntity
                    .ok(Map.of("message", "Data sent to Flask successfully", "response", flaskResponse.getBody()));

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("error", "Flask server error", "details", e.getResponseBodyAsString()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Unexpected error occurred", "details", e.getMessage()));
        }
    }

}
