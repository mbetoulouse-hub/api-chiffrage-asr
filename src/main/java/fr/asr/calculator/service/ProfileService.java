package fr.asr.calculator.service;

import fr.asr.calculator.data.dto.model.ProfileDTO;
import fr.asr.calculator.data.dto.request.CreateProfileDTO;
import fr.asr.calculator.data.dto.request.UpdateProfileDTO;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ProfileService {

    Page<ProfileDTO> getAllProfiles(String input, String role, int page);

    List<ProfileDTO> getAllManagerProfiles();

    ProfileDTO getProfile(String email);

    void createProfile(CreateProfileDTO createProfileDTO);

    void updateProfile(UpdateProfileDTO updateProfileDTO);

    void deleteProfile(String email);
}
