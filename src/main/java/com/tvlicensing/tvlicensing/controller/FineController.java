package com.tvlicensing.tvlicensing.controller;

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
    public String lookup(@RequestParam String fineReference,
                         @RequestParam String postcode,
                         Model model) {

        Optional<Fine> result = fineService.lookupFine(fineReference, postcode);

        if (result.isEmpty()) {
            return "not-found";
        }

        model.addAttribute("fine", result.get());
        return "fine-details";
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
