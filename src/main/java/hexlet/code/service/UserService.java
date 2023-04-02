package hexlet.code.service;

import hexlet.code.dto.CreateUserDto;
import hexlet.code.dto.GetUserDto;

import java.util.List;

public interface UserService {

    List<GetUserDto> findAll();

    GetUserDto findById(Long id);

    GetUserDto create(CreateUserDto dto);

    GetUserDto update(Long id, CreateUserDto dto);

    void delete(Long id);
}
