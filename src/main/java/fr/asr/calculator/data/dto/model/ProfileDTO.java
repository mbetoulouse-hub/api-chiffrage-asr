package fr.asr.calculator.data.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {

    private Integer id;

    private String email;

    private String company;

    private RoleDTO role;

    @JsonProperty("isActive")
    private Boolean active;

    private ProfileDTO manager;

    private Instant createdAt;

    public enum RoleDTO {
        ADMIN,
        MANAGER,
        USER;
    }
}
