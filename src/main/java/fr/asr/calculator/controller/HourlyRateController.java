package fr.asr.calculator.controller;

import fr.asr.calculator.configuration.properties.GlobalConfigurationProperties;
import fr.asr.calculator.data.dto.HourlyRateDto;
import fr.asr.calculator.mapper.HourlyRateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/rates")
@RequiredArgsConstructor
public class HourlyRateController {

    private final GlobalConfigurationProperties globalConfigurationProperties;

    @GetMapping
    public HourlyRateDto all() {
        return HourlyRateMapper.INSTANCE.of(globalConfigurationProperties.getRates());
    }
}
