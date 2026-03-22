package com.tvlicensing.tvlicensing.controller;

import com.tvlicensing.tvlicensing.model.Customer;
import com.tvlicensing.tvlicensing.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final CustomerService customerService;

    public AuthController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // GET /register - loads the registration page
    @GetMapping("/register")
    public String register(Model model) {

        model.addAttribute("customer", new Customer());
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@Valid @ModelAttribute("customer") Customer customer,
                                 BindingResult bindingResult,
                                 @RequestParam String confirmPassword,
                                 Model model) {

        // Check the two password fields match if they don't, manually add an error into BindingResult
        // so it gets displayed on the form like any other validation error
        if (!customer.getPassword().equals(confirmPassword)) {
            bindingResult.rejectValue("password", "error.customer", "Passwords do not match");
        }

        // If any validation rules failed (e.g. @Size, @NotBlank, @Email)
        // OR the passwords didn't match above, return to the register page
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {

            customerService.registerCustomer(customer);

            // Registration successful - redirect to login page
            return "redirect:/login?registered=true";

        } catch (RuntimeException e) {
            // If the service threw an error send the error message back to the registration page
            model.addAttribute("error", e.getMessage());
            model.addAttribute("customer", customer);
            return "register";
        }
    }



    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,
                        @RequestParam(required = false) String registered,
                        Model model) {

        // If login failed show an error message
        if (error != null) {
            model.addAttribute("error", "Invalid email or password");
        }

        // If they just registered show a success message
        if (registered != null) {
            model.addAttribute("success", "Account created successfully! Please sign in.");
        }

        return "login";
    }
}
