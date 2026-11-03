package fr.asr.calculator.service.auth;

import fr.asr.calculator.data.dto.internal.AuthenticateDTO;

public interface AuthService {

    AuthenticateDTO authenticate(String email, String password);

    AuthenticateDTO refreshToken(String token);

    void logout(String token);
}
