package fr.asr.calculator.service.impl;

import static fr.asr.calculator.constant.ApplicationConstant.PAGE_SIZE;
import static fr.asr.calculator.data.dto.model.ProfileDTO.RoleDTO.USER;
import static fr.asr.calculator.exception.ExceptionType.*;

import fr.asr.calculator.data.dao.profile.ProfileDao;
import fr.asr.calculator.data.dto.model.ProfileDTO;
import fr.asr.calculator.data.dto.request.CreateProfileDTO;
import fr.asr.calculator.data.dto.request.UpdateProfileDTO;
import fr.asr.calculator.data.entity.Profile;
import fr.asr.calculator.exception.functionnal.ProfileException;
import fr.asr.calculator.mapper.ProfileMapper;
import fr.asr.calculator.service.ProfileService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileDao profileDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<ProfileDTO> getAllProfiles(String input, String role, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("role").ascending());
        return profileDao.findAll(input, role, pageable).map(ProfileMapper.INSTANCE::of);
    }

    @Override
    public List<ProfileDTO> getAllManagerProfiles() {
        return profileDao.finAllManager().stream()
                .map(ProfileMapper.INSTANCE::of)
                .collect(Collectors.toList());
    }

    @Override
    public ProfileDTO getProfile(String email) {
        return ProfileMapper.INSTANCE.of(profileDao.findByEmail(email));
    }

    @Override
    public void createProfile(CreateProfileDTO createProfileDTO) {
        log.info("Creating profile for email {}..", createProfileDTO.getEmail());
        if (profileDao.existsByEmail(createProfileDTO.getEmail())) {
            throw new ProfileException(PROFILE_ALREADY_EXISTS, "email", createProfileDTO.getEmail());
        }

        createProfileDTO.setPassword(passwordEncoder.encode(createProfileDTO.getPassword()));

        Profile manager = null;
        if (USER.equals(createProfileDTO.getRole()) && createProfileDTO.getManagerId() != null) {
            log.info(
                    "Setting manager for user profile with email {} to manager ID {}",
                    createProfileDTO.getEmail(),
                    createProfileDTO.getManagerId());

            manager = profileDao
                    .findByIdOptional(createProfileDTO.getManagerId())
                    .orElseThrow(() -> new ProfileException(
                            PROFILE_MANAGER_DOES_NOT_EXISTS, "managerId", createProfileDTO.getManagerId()));
        }

        profileDao.save(ProfileMapper.INSTANCE.of(createProfileDTO, manager));
        log.info("Profile created for email {}", createProfileDTO.getEmail());
    }

    @Override
    public void updateProfile(UpdateProfileDTO updateProfileDTO) {
        log.info("Updating profile for email {}", updateProfileDTO.getEmail());
        Optional<Profile> optionalProfile = profileDao.findByEmailOptional(updateProfileDTO.getEmail());

        if (optionalProfile.isEmpty()) {
            throw new ProfileException(PROFILE_NOT_EXISTS, "email", updateProfileDTO.getEmail());
        }

        Profile profile = optionalProfile.get();

        if (updateProfileDTO.getPassword() != null) {
            log.info("Updating password..");
            profile.setPassword(passwordEncoder.encode(updateProfileDTO.getPassword()));
        }

        if (updateProfileDTO.getCompany() != null) {
            log.info("Updating company..");
            profile.setCompany(updateProfileDTO.getCompany());
        }

        if (updateProfileDTO.getRole() != null) {
            log.info("Updating role..");
            profile.setRole(Profile.Role.valueOf(updateProfileDTO.getRole().name()));
        }

        if (USER.equals(updateProfileDTO.getRole()) && updateProfileDTO.getManagerId() != null) {
            log.info("Updating manager to manager ID {}..", updateProfileDTO.getManagerId());

            Profile manager = profileDao
                    .findByIdOptional(updateProfileDTO.getManagerId())
                    .orElseThrow(() -> new ProfileException(
                            PROFILE_MANAGER_DOES_NOT_EXISTS, "managerId", updateProfileDTO.getManagerId()));
            profile.setManager(manager);
        } else {
            profile.setManager(null);
        }

        profileDao.save(profile);
        log.info("Profile updated for email {}", updateProfileDTO.getEmail());
    }

    @Override
    public void deleteProfile(String email) {
        log.info("Deleting profile for email {}", email);
        Optional<Profile> optionalProfile = profileDao.findByEmailOptional(email);
        if (optionalProfile.isEmpty()) {
            throw new ProfileException(PROFILE_NOT_EXISTS, "email", email);
        }

        profileDao.delete(optionalProfile.get());
        log.info("Profile deleted for email {}", email);
    }
}
