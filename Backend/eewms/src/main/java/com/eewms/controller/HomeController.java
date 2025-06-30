package com.eewms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
        @GetMapping("/")
        public String redirectToHomePage() {
            System.out.println(">> HomeController activated!");
            return "homepage/homepage";
        }
    }

