package com.tvlicensing.tvlicensing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    //http://localhost:8080
    @GetMapping("/")
    public String home() {
        //Thymeleaf will look for home.html inside resources/templates
        return "home";

    }

}
