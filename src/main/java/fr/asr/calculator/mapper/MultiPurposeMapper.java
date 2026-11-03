package fr.asr.calculator.mapper;

import fr.asr.calculator.data.dto.EstimateDTO;
import fr.asr.calculator.data.entity.Estimate;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MultiPurposeMapper {

    MultiPurposeMapper INSTANCE = Mappers.getMapper(MultiPurposeMapper.class);

    default Estimate.MultiPurposeData of(EstimateDTO.MultiPurposeData multiPurpose) {
        if (multiPurpose == null) {
            return new Estimate.MultiPurposeData(List.of(), List.of());
        }

        List<Estimate.MultiPurpose> multiPurposes = multiPurpose.getMultiPurposes() == null
                ? List.of()
                : multiPurpose.getMultiPurposes().stream()
                        .map(this::ofMultiPurposeDtoToModel)
                        .collect(Collectors.toList());

        List<Estimate.MultiPurposeRate> multiPurposeRates = multiPurpose.getMultiPurposeRates() == null
                ? List.of()
                : multiPurpose.getMultiPurposeRates().stream()
                        .map(this::ofMultiPurposeRateDtoToModel)
                        .collect(Collectors.toList());

        return new Estimate.MultiPurposeData(multiPurposes, multiPurposeRates);
    }

    default EstimateDTO.MultiPurposeData of(Estimate.MultiPurposeData multiPurpose) {
        if (multiPurpose == null) {
            return new EstimateDTO.MultiPurposeData(List.of(), List.of());
        }

        List<EstimateDTO.MultiPurpose> multiPurposes = multiPurpose.getMultiPurposes() == null
                ? List.of()
                : multiPurpose.getMultiPurposes().stream()
                        .map(this::ofMultiPurposeModelToDto)
                        .collect(Collectors.toList());

        List<EstimateDTO.MultiPurposeRate> multiPurposeRates = multiPurpose.getMultiPurposeRates() == null
                ? List.of()
                : multiPurpose.getMultiPurposeRates().stream()
                        .map(this::ofMultiPurposeRateModelToDto)
                        .collect(Collectors.toList());

        return new EstimateDTO.MultiPurposeData(multiPurposes, multiPurposeRates);
    }

    @Named("ofMultiPurposeDtoToModel")
    Estimate.MultiPurpose ofMultiPurposeDtoToModel(EstimateDTO.MultiPurpose multiPurpose);

    @Named("ofMultiPurposeModelToDto")
    EstimateDTO.MultiPurpose ofMultiPurposeModelToDto(Estimate.MultiPurpose multiPurpose);

    @Named("ofMultiPurposeDtoToModel")
    Estimate.MultiPurposeRate ofMultiPurposeRateDtoToModel(EstimateDTO.MultiPurposeRate multiPurpose);

    @Named("ofMultiPurposeModelToDto")
    EstimateDTO.MultiPurposeRate ofMultiPurposeRateModelToDto(Estimate.MultiPurposeRate multiPurpose);
}
