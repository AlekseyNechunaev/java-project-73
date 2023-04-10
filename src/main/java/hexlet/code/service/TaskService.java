package hexlet.code.service;

import hexlet.code.dto.CreateTaskDto;
import hexlet.code.dto.GetTaskDto;
import hexlet.code.dto.UpdateTaskDto;

import java.util.List;

public interface TaskService {

    List<GetTaskDto> findAll();

    GetTaskDto findById();

    GetTaskDto create(CreateTaskDto dto);

    GetTaskDto update(Long id, UpdateTaskDto dto);

    void delete(Long id);
}
