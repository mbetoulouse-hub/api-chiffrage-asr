package fr.asr.calculator.configuration.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
@Validated
public class JWTSecretsProperties {

    @NotBlank
    private String privateKey;

    @NotBlank
    private String publicKey;
}
