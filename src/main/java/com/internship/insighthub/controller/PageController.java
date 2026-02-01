package com.internship.insighthub.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class PageController {


    @GetMapping("/week11")
    public String week11() {
        return "week11";
    }
}