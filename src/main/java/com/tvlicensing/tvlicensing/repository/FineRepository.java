package com.tvlicensing.tvlicensing.repository;

import com.tvlicensing.tvlicensing.model.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FineRepository extends JpaRepository<Fine, Long> {
    Optional<Fine> findByFineReferenceIgnoreCaseAndPostcodeIgnoreCase(String fineReference, String postcode);

    Object findByFineReferenceAndPostcode(String s, String s1);
}
