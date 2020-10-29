package com.uber.uberapi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/2")
public class ConcurrencyDemo2 {
    @GetMapping("")
    public String hello2() {
        return null;
//        S3Manager s3Manager = S3Manager.getInstance();
//        return s3Manager.toString();
    }
}
