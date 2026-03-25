package com.tvlicensing.tvlicensing.controller;

import com.tvlicensing.tvlicensing.model.CardPaymentForm;
import com.tvlicensing.tvlicensing.model.Fine;
import com.tvlicensing.tvlicensing.model.FineLookupForm;
import com.tvlicensing.tvlicensing.service.FineService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@Controller
@RequestMapping("/fine")
public class FineController {

    private final FineService fineService;

    public FineController(FineService fineService) {
        this.fineService = fineService;
    }

    // LOOKUP

    @GetMapping("/lookup")
    public String showLookupForm(Model model) {
        model.addAttribute("fineLookupForm", new FineLookupForm());
        return "home";
    }

    @PostMapping("/lookup")
    public String lookup(@Valid @ModelAttribute("fineLookupForm") FineLookupForm form,
                         BindingResult bindingResult,
                         Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("fineLookupForm", form);
            return "home";
        }

        Optional<Fine> result =
                fineService.lookupFine(form.getFineReference(), form.getPostcode());

        if (result.isEmpty()) {
            return "not-found";
        }

        model.addAttribute("fine", result.get());
        return "fine-details";
    }

    // CARD DETAILS PAGE

    @GetMapping("/pay")
    public String showPaymentForm(@RequestParam Long fineId, Model model) {
        Fine fine = fineService.lookupFine(fineId);
        model.addAttribute("fineId", fineId);
        model.addAttribute("fineAmount", fine.getAmount());
        model.addAttribute("cardPaymentForm", new CardPaymentForm());
        return "card-details";
    }

    // PROCESS PAYMENT (including amount deduction)

    @PostMapping("/pay")
    public String payFine(@Valid @ModelAttribute("cardPaymentForm") CardPaymentForm form,
                          BindingResult bindingResult,
                          @RequestParam("fineId") Long fineId,
                          Model model) {

        Fine fine = fineService.lookupFine(fineId);

        // card validation already handled by annotations; keep your test card check
        if ("4000000000000002".equals(form.getCardNumber())) {
            model.addAttribute("fineId", fineId);
            model.addAttribute("fineReference", fine.getFineReference());
            return "payment-failed";
        }

        // extra validation for amount vs fine amount
        if (form.getAmount() == null || form.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            bindingResult.rejectValue("amount", "amount.invalid",
                    "Enter an amount greater than 0");
        } else if (form.getAmount().compareTo(fine.getAmount()) > 0) {
            bindingResult.rejectValue("amount", "amount.toolarge",
                    "Enter an amount that is not more than the fine amount");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("fineId", fineId);
            model.addAttribute("fineAmount", fine.getAmount());
            return "card-details";
        }

        // deduct amount and save
        BigDecimal amountPaid = form.getAmount();
        BigDecimal remaining = fine.getAmount().subtract(amountPaid);
        fine.setAmount(remaining);

        if (remaining.compareTo(BigDecimal.ZERO) <= 0) {
            fine.setStatus(Fine.FineStatus.PAID);
        }

// save the updated fine
        fineService.saveFine(fine);


// pass data to the view
        model.addAttribute("fine", fine);
        model.addAttribute("amountPaid", amountPaid);
        return "payment-success";
    }
}
