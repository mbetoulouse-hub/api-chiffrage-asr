package fr.asr.calculator.controller;

import fr.asr.calculator.data.dto.response.ZoneHotelPrefillRoomTypeDTO;
import fr.asr.calculator.service.SectorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/hotel")
@RequiredArgsConstructor
public class ZoneHotelController {

    private final SectorService sectorService;

    @GetMapping("/types")
    public ZoneHotelPrefillRoomTypeDTO types() {
        return sectorService.getZoneHotelPrefillRoomType();
    }
}
