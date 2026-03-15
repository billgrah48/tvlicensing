package com.tvlicensing.tvlicensing.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tv_licence")
public class TvLicence {

    // Primary key - auto generated
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The named licence holder - many licences can belong to one customer
    // (e.g. second home) but each licence has only one holder
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer licenceHolder;

    // The licensed ADDRESS - separate from the customer's account address
    // Pre-filled from customer details but can be changed at purchase
    @Column(nullable = false)
    private String addressLine1;

    @Column
    private String addressLine2;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String postcode;

    // ACTIVE = currently valid
    // CANCELLED = cancelled by customer
    // EXPIRED = past end date
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LicenceStatus status;

    // FULL = one payment of £174.50/year
    // DIRECT_DEBIT = £14.54/month
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType paymentType;

    // Licence always runs for exactly one year
    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    // Only populated for DIRECT_DEBIT licences
    @Column
    private LocalDate nextPaymentDate;

    @Column
    private BigDecimal monthlyAmount;

    // =============================================
    // ENUMS
    // Defined inside the class to keep them together
    // =============================================

    public enum LicenceStatus {
        ACTIVE, CANCELLED, EXPIRED
    }

    public enum PaymentType {
        FULL, DIRECT_DEBIT
    }

    // =============================================
    // CONSTRUCTORS
    // =============================================

    public TvLicence() {}

    public TvLicence(Customer licenceHolder, String addressLine1, String addressLine2,
                     String city, String postcode, PaymentType paymentType) {
        this.licenceHolder = licenceHolder;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.postcode = postcode;
        this.paymentType = paymentType;
        this.status = LicenceStatus.ACTIVE;
        this.startDate = LocalDate.now();
        this.endDate = LocalDate.now().plusYears(1);

        // If direct debit, set first payment date to one month from now
        // and monthly amount to £14.54
        if (paymentType == PaymentType.DIRECT_DEBIT) {
            this.nextPaymentDate = LocalDate.now().plusMonths(1);
            this.monthlyAmount = new BigDecimal("14.54");
        }

    }

    // =============================================
    // GETTERS AND SETTERS
    // =============================================

    public Long getId() { return id; }

    public Customer getLicenceHolder() { return licenceHolder; }
    public void setLicenceHolder(Customer licenceHolder) { this.licenceHolder = licenceHolder; }

    public String getAddressLine1() { return addressLine1; }
    public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }

    public String getAddressLine2() { return addressLine2; }
    public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPostcode() { return postcode; }
    public void setPostcode(String postcode) { this.postcode = postcode; }

    public LicenceStatus getStatus() { return status; }
    public void setStatus(LicenceStatus status) { this.status = status; }

    public PaymentType getPaymentType() { return paymentType; }
    public void setPaymentType(PaymentType paymentType) { this.paymentType = paymentType; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public LocalDate getNextPaymentDate() { return nextPaymentDate; }
    public void setNextPaymentDate(LocalDate nextPaymentDate) { this.nextPaymentDate = nextPaymentDate; }

    public BigDecimal getMonthlyAmount() { return monthlyAmount; }
    public void setMonthlyAmount(BigDecimal monthlyAmount) { this.monthlyAmount = monthlyAmount; }
}
