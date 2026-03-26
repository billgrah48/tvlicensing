package com.tvlicensing.tvlicensing.model;// package com.tvlicensing.tvlicensing.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class FineLookupForm {

    @Pattern(
            regexp = "(?i)^REF-[A-Za-z0-9]{6}$",
            message = "Enter a valid fine reference, for example REF-12AB34.\nYou can find it on the letter about your fine."
    )
    private String fineReference = "REF-";

    @Pattern(
            // simple UK postcode pattern, good enough for your project
            regexp = "(?i)^[A-Z]{1,2}[0-9][0-9A-Z]? ?[0-9][A-Z]{2}$",
            message = "Enter a valid UK postcode, for example BT1 1AA"
    )
    private String postcode;

    public String getFineReference() {
        return fineReference;
    }

    public void setFineReference(String fineReference) {
        this.fineReference = fineReference != null ? fineReference.toUpperCase() : null;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode != null ? postcode.toUpperCase() : null;
    }
}