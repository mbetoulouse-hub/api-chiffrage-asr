package fr.asr.calculator.data.dto.request;

import fr.asr.calculator.data.dto.model.ProfileDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileDTO {

    @NotBlank
    private String email;

    @Nullable
    private String password;

    @Nullable
    private String company;

    @Nullable
    private ProfileDTO.RoleDTO role;

    @Nullable
    private Integer managerId;
}
