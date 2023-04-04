package hexlet.code.service;

import hexlet.code.dto.AuthDto;

public interface AuthService {

    String authenticate(AuthDto dto);
}
