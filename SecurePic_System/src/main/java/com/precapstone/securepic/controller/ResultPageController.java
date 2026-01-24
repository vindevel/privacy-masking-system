package com.precapstone.securepic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResultPageController {

    @GetMapping("/result")
    public String resultPage() {
        return "result/index"; // templates/result/index.html 반환
    }
}
