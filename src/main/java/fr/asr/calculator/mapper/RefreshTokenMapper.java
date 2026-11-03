package fr.asr.calculator.mapper;

import fr.asr.calculator.data.entity.Profile;
import fr.asr.calculator.data.entity.RefreshToken;
import java.time.Instant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper {

    RefreshTokenMapper INSTANCE = Mappers.getMapper(RefreshTokenMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "revoked", constant = "false")
    RefreshToken toEntity(String token, Instant expiryDate, Profile profile);
}
