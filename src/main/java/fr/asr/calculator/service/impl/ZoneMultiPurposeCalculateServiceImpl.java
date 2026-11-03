package fr.asr.calculator.service.impl;

import static fr.asr.calculator.data.dto.enumeration.MultiPurposeFrequencyDTO.UNIQUE;
import static fr.asr.calculator.exception.ExceptionType.*;
import static fr.asr.calculator.util.NumberUtil.toTwoDecimalPlaces;

import fr.asr.calculator.configuration.properties.TechnicalCadenceConfigurationProperties;
import fr.asr.calculator.data.dto.EstimateDTO;
import fr.asr.calculator.data.dto.response.CalculateDTO;
import fr.asr.calculator.exception.functionnal.CadenceException;
import fr.asr.calculator.exception.functionnal.CalculateException;
import fr.asr.calculator.service.ZoneMultiPurposeCalculateService;
import fr.asr.calculator.util.HoursUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ZoneMultiPurposeCalculateServiceImpl implements ZoneMultiPurposeCalculateService {

    private final TechnicalCadenceConfigurationProperties cadenceConfigurationProperties;

    @Override
    public CalculateDTO.MultiPurposeCalculationResult calculate(EstimateDTO.MultiPurposeData multiPurposeData) {
        CalculateDTO.MultiPurposeCalculationResult result = new CalculateDTO.MultiPurposeCalculationResult();

        List<CalculateDTO.MultiPurposeCalculationDetail> details = multiPurposeData.getMultiPurposes().stream()
                .map(multiPurpose -> calculationDetail(multiPurposeData, multiPurpose))
                .toList();

        result.setDetails(details);
        result.setTotalSurface(details.stream()
                .mapToDouble(CalculateDTO.MultiPurposeCalculationDetail::getSurface)
                .sum());
        result.setTotalTime(details.stream()
                .mapToDouble(CalculateDTO.MultiPurposeCalculationDetail::getTimeInHours)
                .sum());
        result.setTotalPrice(details.stream()
                .mapToDouble(CalculateDTO.MultiPurposeCalculationDetail::getPrice)
                .sum());

        return result;
    }

    private CalculateDTO.MultiPurposeCalculationDetail calculationDetail(
            EstimateDTO.MultiPurposeData multiPurposeData, EstimateDTO.MultiPurpose multiPurpose) {
        CalculateDTO.MultiPurposeCalculationDetail detail = new CalculateDTO.MultiPurposeCalculationDetail();
        detail.setName(multiPurpose.getName());
        detail.setType(multiPurpose.getType());
        detail.setSurface(multiPurpose.getSurface());
        detail.setRemarks(multiPurpose.getRemarks());
        detail.setServiceType(multiPurpose.getServiceType());
        detail.setFrequency(multiPurpose.getFrequency());
        detail.setUsedTravelCosts(multiPurpose.getTravelCosts() != null && multiPurpose.getTravelCosts() > 0);
        detail.setTravelCosts(multiPurpose.getTravelCosts());
        detail.setHourlyRateUsed(determineHourlyRate(multiPurposeData, multiPurpose));

        int cadence = determineCadence(multiPurpose);
        detail.setCadence(cadence);

        if (UNIQUE.equals(detail.getFrequency())) {
            detail.setTimeInHours(HoursUtil.roundUpToHalfDay(multiPurpose.getSurface() / cadence));
            detail.setPriceByService(toTwoDecimalPlaces(
                    ((detail.getTimeInHours() * detail.getHourlyRateUsed()) + determineTravelCosts(detail))));
            detail.setPrice(detail.getPriceByService());
        } else {
            detail.setTimeInHours(HoursUtil.roundUpToHour(multiPurpose.getSurface() / cadence));
            // price / month
            detail.setPrice(toTwoDecimalPlaces(
                    (((detail.getTimeInHours() * detail.getHourlyRateUsed()) + determineTravelCosts(detail))
                                    * detail.getFrequency().getValue())
                            / 12));
            detail.setPriceByService(toTwoDecimalPlaces(
                    (((detail.getTimeInHours() * detail.getHourlyRateUsed()) + determineTravelCosts(detail))
                            * detail.getFrequency().getValue())));
        }

        detail.setPrice(toTwoDecimalPlaces(detail.getPrice()));

        return detail;
    }

    private double determineHourlyRate(
            EstimateDTO.MultiPurposeData multiPurposeData, EstimateDTO.MultiPurpose multiPurpose) {
        TechnicalCadenceConfigurationProperties.TechnicalCadenceService service = getServiceCadence(multiPurpose);

        EstimateDTO.MultiPurposeRate rateSend = multiPurposeData.getMultiPurposeRates().stream()
                .filter(rate -> rate.getType().equals(service.getType()))
                .findFirst()
                .orElseThrow(() -> new CadenceException(MULTI_PURPOSE_INVALID_RATE_SEND, "service", service.getType()));

        return rateSend.getHourlyRate();
    }

    private int determineCadence(EstimateDTO.MultiPurpose multiPurpose) {
        TechnicalCadenceConfigurationProperties.TechnicalCadenceService service = getServiceCadence(multiPurpose);

        if (service.getRange() != null) {
            return multiPurpose.getCustomCadence() != null ? multiPurpose.getCustomCadence() : service.getCadence();
        }

        if (service.getCadence() == -1) {
            if (multiPurpose.getCustomCadence() == null) {
                log.warn(
                        "Multi-purpose {} with type {} does not have a defined custom cadence",
                        multiPurpose.getName(),
                        multiPurpose.getType());
                throw new CalculateException(
                        MULTI_PURPOSE_INVALID_CUSTOM_CADENCE, "multiPurpose", multiPurpose.getName());
            }

            return multiPurpose.getCustomCadence();
        }

        return service.getCadence();
    }

    private double determineTravelCosts(CalculateDTO.MultiPurposeCalculationDetail detail) {
        return detail.isUsedTravelCosts() ? detail.getTravelCosts() : 0;
    }

    private TechnicalCadenceConfigurationProperties.TechnicalCadenceService getServiceCadence(
            EstimateDTO.MultiPurpose multiPurpose) {
        TechnicalCadenceConfigurationProperties.TechnicalCadence technicalCadence =
                cadenceConfigurationProperties.getCadences().get(multiPurpose.getType());

        if (technicalCadence == null) {
            log.warn(
                    "Multi-purpose {} with type {} does not have a defined technical cadence",
                    multiPurpose.getName(),
                    multiPurpose.getType());
            throw new CalculateException(MULTI_PURPOSE_INVALID_TECHNICAL_CADENCE, "type", multiPurpose.getType());
        }

        TechnicalCadenceConfigurationProperties.TechnicalCadenceService service =
                technicalCadence.getServices().get(multiPurpose.getServiceType());

        if (service == null) {
            log.warn(
                    "Service type {} for multi-purpose {} is not defined in technical cadence {}",
                    multiPurpose.getServiceType(),
                    multiPurpose.getName(),
                    technicalCadence.getTitle());
            throw new CalculateException(
                    MULTI_PURPOSE_INVALID_CADENCE_SERVICE,
                    "serviceType",
                    multiPurpose.getServiceType(),
                    "multiPurpose",
                    multiPurpose.getName());
        }

        return service;
    }
}
