package com.sunire.rental_tomo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index.th.html";
    }

    @GetMapping("/index")
    public String index() {
        return "realindex.th.html";
    }

    @GetMapping("/login")
    public String login() {
        return "user/login.th.html";
    }

    @GetMapping("/join/1")
    public String join() {
        return "user/join_step1.th.html";
    }

    @GetMapping("/join/2")
    public String join2() {
        return "user/join_step2.th.html";
    }
}
