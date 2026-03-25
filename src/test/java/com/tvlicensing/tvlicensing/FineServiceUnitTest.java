package com.tvlicensing.tvlicensing;

import com.tvlicensing.tvlicensing.model.Fine;
import com.tvlicensing.tvlicensing.repository.FineRepository;
import com.tvlicensing.tvlicensing.service.FineService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FineServiceUnitTest {

    @Mock
    private FineRepository fineRepository;

    @InjectMocks
    private FineService fineService;

    @Test
    void lookupFine_returnsEmpty_whenNotFound() {
        when(fineRepository.findByFineReferenceIgnoreCaseAndPostcodeIgnoreCase(any(), any()))
                .thenReturn(Optional.empty());

        Optional<Fine> result = fineService.lookupFine("REF-999999", "BT9 9ZZ");

        assertTrue(result.isEmpty());
    }

    @Test
    void lookupFine_returnsFine_whenFound() {
        Fine fine = new Fine();
        fine.setFineReference("REF-123456");

        when(fineRepository.findByFineReferenceIgnoreCaseAndPostcodeIgnoreCase("REF-123456", "BT1 1AA"))
                .thenReturn(Optional.of(fine));

        Optional<Fine> result = fineService.lookupFine("REF-123456", "BT1 1AA");

        assertTrue(result.isPresent());
    }
}
