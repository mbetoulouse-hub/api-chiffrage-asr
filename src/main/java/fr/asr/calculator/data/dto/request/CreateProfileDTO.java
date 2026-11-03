package fr.asr.calculator.data.dto.request;

import fr.asr.calculator.data.dto.model.ProfileDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProfileDTO {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String company;

    @NotNull
    private ProfileDTO.RoleDTO role;

    @Nullable
    private Integer managerId;
}
