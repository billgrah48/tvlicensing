package com.tvlicensing.tvlicensing.service;

import com.tvlicensing.tvlicensing.model.Customer;
import com.tvlicensing.tvlicensing.model.TvLicence;
import com.tvlicensing.tvlicensing.repository.TvLicenceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LicenceService {

    private final TvLicenceRepository tvLicenceRepository;

    public LicenceService(TvLicenceRepository tvLicenceRepository) {
        this.tvLicenceRepository = tvLicenceRepository;
    }

    // PURCHASE A LICENCE
    // Called when the customer submits the buy licence form
    // Uses the TvLicence constructor to set dates, status and
    // monthly amount automatically based on payment type
    public TvLicence purchaseLicence(Customer customer, String addressLine1,
                                     String addressLine2, String city,
                                     String postcode, TvLicence.PaymentType paymentType) {

        // Check if this customer already has an ACTIVE licence
        // at this postcode - prevents duplicate licences at same address
        List<TvLicence> existing = tvLicenceRepository.findByLicenceHolder(customer);
        for (TvLicence licence : existing) {
            if (licence.getPostcode().equalsIgnoreCase(postcode)
                    && licence.getStatus() == TvLicence.LicenceStatus.ACTIVE) {
                throw new RuntimeException(
                        "You already have an active licence at this address");
            }
        }

        // Create and save the new licence
        TvLicence licence = new TvLicence(customer, addressLine1, addressLine2,
                city, postcode.toUpperCase().trim(), paymentType);
        return tvLicenceRepository.save(licence);
    }

    // CANCEL A LICENCE
    // Sets the status to CANCELLED - we never delete licences
    // for audit/history purposes
    public void cancelLicence(Long licenceId, Customer customer) {

        TvLicence licence = tvLicenceRepository.findById(licenceId)
                .orElseThrow(() -> new RuntimeException("Licence not found"));

        // Security check - make sure the licence belongs to this customer
        // Prevents one customer cancelling another's licence via URL manipulation
        if (!licence.getLicenceHolder().getId().equals(customer.getId())) {
            throw new RuntimeException("You are not authorised to cancel this licence");
        }

        licence.setStatus(TvLicence.LicenceStatus.CANCELLED);
        tvLicenceRepository.save(licence);
    }

    // GET ALL LICENCES FOR A CUSTOMER
    // Used by the dashboard to display licence history and active licences
    public List<TvLicence> getLicencesForCustomer(Customer customer) {
        return tvLicenceRepository.findByLicenceHolder(customer);
    }
}
