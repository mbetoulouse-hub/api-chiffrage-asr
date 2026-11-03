package fr.asr.calculator.mapper;

import fr.asr.calculator.data.dto.model.ProfileDTO;
import fr.asr.calculator.data.dto.request.CreateProfileDTO;
import fr.asr.calculator.data.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);

    @Mapping(target = "active", source = "enabled")
    ProfileDTO of(Profile profile);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "email", source = "createProfileDTO.email")
    @Mapping(target = "password", source = "createProfileDTO.password")
    @Mapping(target = "company", source = "createProfileDTO.company")
    @Mapping(target = "role", source = "createProfileDTO.role")
    @Mapping(target = "manager", source = "manager")
    Profile of(CreateProfileDTO createProfileDTO, Profile manager);
}
