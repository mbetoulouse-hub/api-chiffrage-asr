package fr.asr.calculator.data.dto.internal;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenDTO {

    private String token;

    private long expiresAt;
}
