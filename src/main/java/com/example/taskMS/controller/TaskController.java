package com.example.taskMS.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

    @GetMapping("/taskms")
    public String sayHello() {
        return "Hello World from taskMS!";
    }
}