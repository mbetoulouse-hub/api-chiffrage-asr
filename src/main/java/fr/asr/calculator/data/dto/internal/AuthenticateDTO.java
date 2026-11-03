package fr.asr.calculator.data.dto.internal;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticateDTO {

    private TokenDTO accessToken;

    private TokenDTO refreshToken;
}
