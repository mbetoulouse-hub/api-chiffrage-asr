package fr.asr.calculator.controller;

import fr.asr.calculator.data.dto.response.TechnicalCadenceDTO;
import fr.asr.calculator.data.dto.response.TechnicalCadenceRateDTO;
import fr.asr.calculator.service.TechnicalCadenceService;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/technical-cadence")
@RequiredArgsConstructor
public class TechnicalCadenceController {

    private final TechnicalCadenceService technicalCadenceService;

    @GetMapping("/all")
    public ResponseEntity<List<TechnicalCadenceDTO>> all() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES).cachePublic())
                .body(technicalCadenceService.getAllCadences());
    }

    @GetMapping("/rates")
    public ResponseEntity<List<TechnicalCadenceRateDTO>> rates() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES).cachePublic())
                .body(technicalCadenceService.getAllRates());
    }
}
