package hexlet.code.service;

import hexlet.code.common.mapper.UserMapper;
import hexlet.code.dto.CreateUserDto;
import hexlet.code.dto.GetUserDto;
import hexlet.code.entity.User;
import hexlet.code.exception.ExceptionMessage;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.exception.UserExistException;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper mapper, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.encoder = encoder;
    }

    @Override
    public List<GetUserDto> findAll() {
        return userRepository.findAll().stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public GetUserDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessage.USER_NOT_FOUND));
        return mapper.map(user);
    }

    @Override
    public GetUserDto create(CreateUserDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new UserExistException(ExceptionMessage.USER_EXIST_BY_EMAIL);
        }
        String encodePassword = encoder.encode(dto.getPassword());
        User user = mapper.map(dto);
        user.setPassword(encodePassword);
        userRepository.save(user);
        return mapper.map(user);
    }

    @Override
    public GetUserDto update(Long id, CreateUserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessage.USER_NOT_FOUND));
        if (userRepository.existsByEmailAndIdIsNot(dto.getEmail(), id)) {
            throw new UserExistException(ExceptionMessage.USER_EXIST_BY_EMAIL);
        }
        User updatedUser = mapper.map(dto);
        String encodePassword = encoder.encode(dto.getPassword());
        updatedUser.setId(user.getId());
        updatedUser.setPassword(encodePassword);
        return mapper.map(updatedUser);
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessage.USER_NOT_FOUND));
        userRepository.delete(user);
    }
}
