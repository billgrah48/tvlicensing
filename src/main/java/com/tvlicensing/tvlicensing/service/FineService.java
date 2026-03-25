package com.tvlicensing.tvlicensing.service;

import com.tvlicensing.tvlicensing.model.Fine;
import com.tvlicensing.tvlicensing.repository.FineRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class FineService {

    private final FineRepository fineRepository;

    public FineService(FineRepository fineRepository) {
        this.fineRepository = fineRepository;
    }

    public Optional<Fine> lookupFine(String fineReference, String postcode) {
        return fineRepository.findByFineReferenceIgnoreCaseAndPostcodeIgnoreCase(fineReference, postcode);
    }

    public Fine lookupFine(Long fineId) {
        return fineRepository.findById(fineId)
                .orElseThrow(() -> new RuntimeException("Fine not found"));
    }

    public Fine payFine(Long fineId) {
        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new RuntimeException("Fine not found"));
        fine.setStatus(Fine.FineStatus.PAID);
        return fineRepository.save(fine);
    }
    public Fine saveFine(Fine fine) {
        return fineRepository.save(fine);
    }

}
