package fr.asr.calculator.service;

import fr.asr.calculator.data.dto.HotelZoneDataDTO;
import fr.asr.calculator.data.dto.response.CalculateDTO;

public interface ZoneHotelCalculateService {

    CalculateDTO.HotelCalculationResult calculateHotelZone(HotelZoneDataDTO hotelZoneDataDTO);
}
