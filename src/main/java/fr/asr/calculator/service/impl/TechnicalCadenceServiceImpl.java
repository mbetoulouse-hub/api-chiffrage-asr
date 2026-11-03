package fr.asr.calculator.service.impl;

import fr.asr.calculator.configuration.properties.TechnicalCadenceConfigurationProperties;
import fr.asr.calculator.data.dto.enumeration.MultiPurposeType;
import fr.asr.calculator.data.dto.response.TechnicalCadenceDTO;
import fr.asr.calculator.data.dto.response.TechnicalCadenceRateDTO;
import fr.asr.calculator.service.TechnicalCadenceService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;

@Slf4j
@RequiredArgsConstructor
public class TechnicalCadenceServiceImpl implements TechnicalCadenceService {

    private final TechnicalCadenceConfigurationProperties cadenceConfigurationProperties;

    @Cacheable(value = "technicalCadences", key = "'allCadences'")
    @Override
    public List<TechnicalCadenceDTO> getAllCadences() {
        return cadenceConfigurationProperties.getCadences().entrySet().stream()
                .map(entry -> {
                    String key = entry.getKey();
                    TechnicalCadenceConfigurationProperties.TechnicalCadence cadence = entry.getValue();
                    List<TechnicalCadenceDTO.TechnicalCadenceServiceDTO> services =
                            cadence.getServices().entrySet().stream()
                                    .map(serviceEntry -> {
                                        String serviceKey = serviceEntry.getKey();
                                        TechnicalCadenceConfigurationProperties.TechnicalCadenceService service =
                                                serviceEntry.getValue();
                                        return new TechnicalCadenceDTO.TechnicalCadenceServiceDTO(
                                                serviceKey,
                                                service.getTitle(),
                                                service.getCadence(),
                                                service.getType(),
                                                service.getRange());
                                    })
                                    .toList();
                    return new TechnicalCadenceDTO(key, cadence.getTitle(), services);
                })
                .toList();
    }

    @Cacheable(value = "technicalCadences", key = "'allRates'")
    @Override
    public List<TechnicalCadenceRateDTO> getAllRates() {
        return cadenceConfigurationProperties.getRates().entrySet().stream()
                .map(entry -> {
                    MultiPurposeType type = entry.getKey();
                    TechnicalCadenceConfigurationProperties.Range range = entry.getValue();
                    return new TechnicalCadenceRateDTO(type, range);
                })
                .toList();
    }
}
