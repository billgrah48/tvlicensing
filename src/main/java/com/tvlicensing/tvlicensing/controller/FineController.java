package com.tvlicensing.tvlicensing.controller;

import com.tvlicensing.tvlicensing.model.FineLookupForm;
import com.tvlicensing.tvlicensing.model.CardPaymentForm;
import com.tvlicensing.tvlicensing.model.Fine;
import com.tvlicensing.tvlicensing.service.FineService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
@RequestMapping("/fine")
public class FineController {

    private final FineService fineService;

    public FineController(FineService fineService) {
        this.fineService = fineService;
    }

    @PostMapping("/lookup")
    public String lookup(@Valid @ModelAttribute("fineLookupForm") FineLookupForm form,
                         BindingResult bindingResult,
                         Model model) {

        // If validation fails, stay on the same page and show errors
        if (bindingResult.hasErrors()) {
            model.addAttribute("fineLookupForm", form);
            return "home";   // your lookup page
        }

        Optional<Fine> result =
                fineService.lookupFine(form.getFineReference(), form.getPostcode());

        if (result.isEmpty()) {
            return "not-found";
        }

        model.addAttribute("fine", result.get());
        return "fine-details";
    }
    @GetMapping("/lookup")
    public String showLookupForm(Model model) {
        model.addAttribute("fineLookupForm", new FineLookupForm());
        return "home";
    }

    @GetMapping("/pay")
    public String showPaymentForm(@RequestParam Long fineId, Model model) {
        model.addAttribute("fineId", fineId);
        model.addAttribute("cardPaymentForm", new CardPaymentForm());
        return "card-details";
    }

    @PostMapping("/pay")
    public String processPayment(@RequestParam Long fineId,
                                 @Valid @ModelAttribute CardPaymentForm cardPaymentForm,
                                 BindingResult bindingResult,
                                 Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("fineId", fineId);
            return "card-details";
        }

        if (cardPaymentForm.getCardNumber().equals("4000000000000002")) {
            Fine fine = fineService.lookupFine(fineId);
            model.addAttribute("fineId", fineId);
            model.addAttribute("fineReference", fine.getFineReference());
            return "payment-failed";
        }

        Fine fine = fineService.payFine(fineId);
        model.addAttribute("fine", fine);
        return "payment-success";
    }
}
