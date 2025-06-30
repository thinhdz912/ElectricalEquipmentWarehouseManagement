package com.eewms.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class TestController {
    @GetMapping("/test")
    public String testPage() {
        return "test";
    }

}
