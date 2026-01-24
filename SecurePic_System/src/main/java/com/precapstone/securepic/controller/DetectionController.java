package com.precapstone.securepic.controller;

import org.springframework.http.ResponseEntity; // 추가
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/detection")
public class DetectionController {

    @GetMapping("/results/{uploadId}")
    public ResponseEntity<Map<String, Object>> getDetectionResults(@PathVariable String uploadId) {
        Map<String, Object> response = Map.of(
            "detectionComplete", true,
            "detectedImageUrl", "http://localhost:8080/uploads/detected_" + uploadId + ".jpg",
            "sensitiveData", List.of(
                Map.of("id", 1, "type", "Address", "feedback", "Detected address"),
                Map.of("id", 2, "type", "Phone", "feedback", "Detected phone number")
            )
        );
        return ResponseEntity.ok(response);
    }
}