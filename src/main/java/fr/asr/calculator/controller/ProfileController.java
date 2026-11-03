package fr.asr.calculator.controller;

import fr.asr.calculator.data.dto.model.ProfileDTO;
import fr.asr.calculator.data.dto.request.CreateProfileDTO;
import fr.asr.calculator.data.dto.request.UpdateProfileDTO;
import fr.asr.calculator.data.dto.response.PageDTO;
import fr.asr.calculator.service.ProfileService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @Secured("ROLE_ADMIN")
    @PostMapping("/create")
    public void create(@NotNull @Valid @RequestBody CreateProfileDTO createProfileDTO) {
        profileService.createProfile(createProfileDTO);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/update")
    public void update(@NotNull @Valid @RequestBody UpdateProfileDTO updateProfileDTO) {
        profileService.updateProfile(updateProfileDTO);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/delete")
    public void delete(@NotBlank @RequestParam String email) {
        profileService.deleteProfile(email);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/all")
    public PageDTO<ProfileDTO> all(
            @Min(0) @RequestParam Integer page,
            @RequestParam(required = false) String input,
            @RequestParam(required = false) String role) {
        Page<ProfileDTO> profiles = profileService.getAllProfiles(input, role, page);

        PageDTO<ProfileDTO> pageDTO = new PageDTO<>();
        pageDTO.setContent(profiles.getContent());
        pageDTO.setTotalPages(profiles.getTotalPages());
        pageDTO.setTotalElements(profiles.getTotalElements());
        pageDTO.setNumberOfElements(profiles.getNumberOfElements());
        pageDTO.setSize(profiles.getSize());
        pageDTO.setNumber(profiles.getNumber());
        pageDTO.setEmpty(profiles.isEmpty());
        pageDTO.setFirst(profiles.isFirst());
        pageDTO.setLast(profiles.isLast());
        return pageDTO;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/all-managers")
    public List<ProfileDTO> allManagers() {
        return profileService.getAllManagerProfiles();
    }
}
