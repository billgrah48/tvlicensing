package com.tvlicensing.tvlicensing.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.math.BigDecimal;

public class CardPaymentForm {

    @NotBlank(message = "Name on card is required")
    private String nameOnCard;

    @CreditCardNumber(message = "Please enter a valid card number")
    private String cardNumber;

    @Pattern(regexp = "(0[1-9]|1[0-2])/\\d{2}", message = "Expiry date is required and must be in MM/YY format")
    private String expiryDate;

    @Pattern(regexp = "\\d{3}", message = "CVV is required and must be 3 digits")
    private String cvv;

    @DecimalMin(value = "0.01", message = "Enter an amount greater than 0")
    private BigDecimal amount;

    public CardPaymentForm() {}

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
