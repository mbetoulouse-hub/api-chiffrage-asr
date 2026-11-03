package fr.asr.calculator.controller;

import fr.asr.calculator.data.dto.request.SimulateDTO;
import fr.asr.calculator.data.dto.request.SubmitDTO;
import fr.asr.calculator.data.dto.response.CalculateDTO;
import fr.asr.calculator.service.CalculateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/calculate")
@RequiredArgsConstructor
public class CalculateController {

    private final CalculateService calculateService;

    @PostMapping("simulate")
    public CalculateDTO simulate(@Valid @RequestBody SimulateDTO simulateDTO) {
        return calculateService.simulate(simulateDTO);
    }

    @PostMapping("submit")
    public CalculateDTO simulate(Authentication authentication, @Valid @RequestBody SubmitDTO submitDTO) {
        return calculateService.submit(authentication.getName(), submitDTO);
    }

    @PostMapping("export")
    public ResponseEntity<byte[]> export(Authentication authentication, @Valid @RequestBody SubmitDTO submitDTO) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=export.xlsx")
                .contentType(
                        MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(calculateService.export(authentication.getName(), submitDTO));
    }
}
