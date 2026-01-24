package com.precapstone.securepic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPageController {

    @GetMapping("/")  // 메인 페이지
    public String mainPage() {
        return "main/index"; // templates/main/index.html 반환
    }

    @GetMapping("/feedback") // 피드백 페이지
    public String feedbackPage() {
        return "feedback/index"; // templates/feedback/index.html 반환
    }
}