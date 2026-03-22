package com.tvlicensing.tvlicensing.controller;

import com.tvlicensing.tvlicensing.model.Customer;
import com.tvlicensing.tvlicensing.model.TvLicence;
import com.tvlicensing.tvlicensing.repository.CustomerRepository;
import com.tvlicensing.tvlicensing.service.LicenceService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/licence")
public class LicenceController {

    private final LicenceService licenceService;
    private final CustomerRepository customerRepository;

    public LicenceController(LicenceService licenceService,
                             CustomerRepository customerRepository) {
        this.licenceService = licenceService;
        this.customerRepository = customerRepository;
    }

    //Loads the purchase form Pre-fills address from the customer's registered details
    @GetMapping("/buy")
    public String buyLicencePage(@AuthenticationPrincipal UserDetails userDetails,
                                 Model model) {

        Customer customer = customerRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Pre-fill a TvLicence object with the customer's address
        TvLicence licence = new TvLicence();
        licence.setAddressLine1(customer.getAddressLine1());
        licence.setAddressLine2(customer.getAddressLine2());
        licence.setCity(customer.getCity());
        licence.setPostcode(customer.getPostcode());

        model.addAttribute("licence", licence);
        return "buy-licence";
    }

    //handles the purchase form submission
    @PostMapping("/buy")
    public String buyLicenceSubmit(@AuthenticationPrincipal UserDetails userDetails,
                                   @RequestParam String addressLine1,
                                   @RequestParam String addressLine2,
                                   @RequestParam String city,
                                   @RequestParam String postcode,
                                   @RequestParam TvLicence.PaymentType paymentType,
                                   Model model) {

        Customer customer = customerRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        try {
            licenceService.purchaseLicence(customer, addressLine1, addressLine2,
                    city, postcode, paymentType);
            return "redirect:/dashboard?licencePurchased=true";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            // Re-populate the form fields on error
            TvLicence licence = new TvLicence();
            licence.setAddressLine1(addressLine1);
            licence.setAddressLine2(addressLine2);
            licence.setCity(city);
            licence.setPostcode(postcode);
            model.addAttribute("licence", licence);
            return "buy-licence";
        }
    }

    //cancels a licence by ID
    @PostMapping("/cancel")
    public String cancelLicence(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam Long licenceId,
                                Model model) {

        Customer customer = customerRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        try {
            licenceService.cancelLicence(licenceId, customer);
            return "redirect:/dashboard?licenceCancelled=true";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/dashboard?error=true";
        }
    }
}
