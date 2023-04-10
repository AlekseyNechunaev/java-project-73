package hexlet.code.service;

import hexlet.code.common.mapper.TaskMapper;
import hexlet.code.common.mapper.TaskStatusMapper;
import hexlet.code.common.mapper.UserMapper;
import hexlet.code.dto.CreateTaskDto;
import hexlet.code.dto.GetTaskDto;
import hexlet.code.dto.UpdateTaskDto;
import hexlet.code.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserMapper userMapper;
    private final TaskStatusMapper taskStatusMapper;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           TaskMapper taskMapper,
                           UserMapper userMapper,
                           TaskStatusMapper taskStatusMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.userMapper = userMapper;
        this.taskStatusMapper = taskStatusMapper;
    }

    @Override
    public List<GetTaskDto> findAll() {
        return null;
    }

    @Override
    public GetTaskDto findById() {
        return null;
    }

    @Override
    public GetTaskDto create(CreateTaskDto dto) {
        return null;
    }

    @Override
    public GetTaskDto update(Long id, UpdateTaskDto dto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
