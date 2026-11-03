package fr.asr.calculator.controller;

import fr.asr.calculator.data.dto.response.SectorTypeDTO;
import fr.asr.calculator.service.SectorService;
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
@RequestMapping("/api/v1/sector")
@RequiredArgsConstructor
public class SectorController {

    private final SectorService sectorService;

    @GetMapping("/types")
    public ResponseEntity<List<SectorTypeDTO>> types() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES).cachePublic())
                .body(sectorService.getAllSectorTypes());
    }
}
