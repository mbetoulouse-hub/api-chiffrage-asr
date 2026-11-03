package fr.asr.calculator.mapper;

import fr.asr.calculator.configuration.properties.GlobalConfigurationProperties;
import fr.asr.calculator.data.dto.HourlyRateDto;
import fr.asr.calculator.data.dto.enumeration.FrequencyDTO;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface HourlyRateMapper {

    HourlyRateMapper INSTANCE = Mappers.getMapper(HourlyRateMapper.class);

    @Mapping(target = "ranges", expression = "java(mapRates(rates))")
    HourlyRateDto of(Map<String, GlobalConfigurationProperties.Rate> rates);

    HourlyRateDto.Range toRange(GlobalConfigurationProperties.Rate rate);

    default Map<FrequencyDTO, HourlyRateDto.Range> mapRates(Map<String, GlobalConfigurationProperties.Rate> rates) {
        return rates.entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(
                        entry -> FrequencyDTO.valueOf(entry.getKey()), entry -> toRange(entry.getValue())));
    }
}
