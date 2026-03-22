package com.tvlicensing.tvlicensing.controller;

import com.tvlicensing.tvlicensing.model.Customer;
import com.tvlicensing.tvlicensing.repository.CustomerRepository;
import com.tvlicensing.tvlicensing.service.LicenceService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DashboardController {

    private final CustomerRepository customerRepository;
    private final LicenceService licenceService;

    public DashboardController(CustomerRepository customerRepository,
                               LicenceService licenceService) {
        this.customerRepository = customerRepository;
        this.licenceService = licenceService;
    }

    //Loads the dashboard for the logged-in customer
    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(required = false) String licencePurchased,
                            @RequestParam(required = false) String licenceCancelled,
                            Model model) {

        // Get the logged-in customer's email from the security context
        // then look them up in the database to get their full details
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Pass the customer object to the template
        model.addAttribute("customer", customer);

        // Load all licences held by this customer and pass them
        // to the template so the dashboard can display them
        model.addAttribute("licences", licenceService.getLicencesForCustomer(customer));

        // If redirected from a licence purchase or cancellation,
        // pass the flag through so the success/cancel message displays
        if (licencePurchased != null) model.addAttribute("licencePurchased", true);
        if (licenceCancelled != null) model.addAttribute("licenceCancelled", true);

        return "dashboard";
    }
}
