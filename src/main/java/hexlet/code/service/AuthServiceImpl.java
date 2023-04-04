package hexlet.code.service;

import hexlet.code.dto.AuthDto;
import hexlet.code.entity.User;
import hexlet.code.exception.AuthenticationException;
import hexlet.code.exception.ExceptionMessage;
import hexlet.code.repository.UserRepository;
import hexlet.code.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenUtil jwtTokenUtil,
                           AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public String authenticate(AuthDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .filter(findUser -> passwordEncoder.matches(dto.getPassword(), findUser.getPassword()))
                .orElseThrow(() -> new AuthenticationException(ExceptionMessage.WRONG_EMAIL_OR_PASSWORD));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        return jwtTokenUtil.generateToken(user);
    }
}
