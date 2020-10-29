package com.uber.uberapi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/1")
public class ConcurrencyDemo {
    @GetMapping("")
    public String hello() {
        S3Manager s3Manager = S3Manager.getInstance();
        return s3Manager.toString();
    }
}
