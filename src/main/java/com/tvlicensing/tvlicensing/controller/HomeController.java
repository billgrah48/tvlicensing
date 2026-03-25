package com.tvlicensing.tvlicensing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.tvlicensing.tvlicensing.model.FineLookupForm;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("fineLookupForm", new FineLookupForm());
        return "home";
    }
}
