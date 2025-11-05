package com.careerpath.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class DummyController {
    @GetMapping("/api/test")
    public String testEndpoint() {
        return "OK";
    }
}