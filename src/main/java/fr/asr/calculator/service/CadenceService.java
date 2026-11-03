package fr.asr.calculator.service;

import fr.asr.calculator.data.dto.BuildingDto;
import fr.asr.calculator.data.dto.DetermineCadenceDto;
import fr.asr.calculator.data.dto.RoomDto;
import fr.asr.calculator.data.dto.enumeration.SectorDTO;

public interface CadenceService {

    DetermineCadenceDto determineCadence(SectorDTO sector, RoomDto roomDto, BuildingDto building);
}
