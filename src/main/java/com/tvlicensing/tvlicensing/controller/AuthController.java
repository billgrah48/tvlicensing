// This file belongs to the controller package
package com.tvlicensing.tvlicensing.controller;

import com.tvlicensing.tvlicensing.model.Customer;
import com.tvlicensing.tvlicensing.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// @Controller handles web page requests
@Controller
public class AuthController {

    // The service that handles our business logic
    private final CustomerService customerService;

    // Spring injects the CustomerService automatically
    public AuthController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // GET /register - loads the registration page
    @GetMapping("/register")
    public String register(Model model) {
        // We pass an empty Customer object to the form so
        // Thymeleaf can bind the form fields to it
        model.addAttribute("customer", new Customer());
        return "register";
    }

    // POST /register - handles the form submission
    // @ModelAttribute binds the submitted form data directly
    // into a Customer object automatically
    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute Customer customer,
                                 @RequestParam String confirmPassword,
                                 Model model) {

        // Check passwords match before doing anything else
        if (!customer.getPassword().equals(confirmPassword)) {
            // Add an error message to send back to the page
            model.addAttribute("error", "Passwords do not match");
            model.addAttribute("customer", customer);
            return "register";
        }

        try {
            // Pass the customer to the service to handle
            // encryption and saving to the database
            customerService.registerCustomer(customer);

            // Registration successful - redirect to login page
            // The "registered=true" flag lets us show a success message
            return "redirect:/login?registered=true";

        } catch (RuntimeException e) {
            // If the service threw an error (e.g. email already exists)
            // send the error message back to the registration page
            model.addAttribute("error", e.getMessage());
            model.addAttribute("customer", customer);
            return "register";
        }
    }

    // GET /login - loads the login page
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,
                        @RequestParam(required = false) String registered,
                        Model model) {

        // If login failed, show an error message
        if (error != null) {
            model.addAttribute("error", "Invalid email or password");
        }

        // If they just registered, show a success message
        if (registered != null) {
            model.addAttribute("success", "Account created successfully! Please sign in.");
        }

        return "login";
    }
}
