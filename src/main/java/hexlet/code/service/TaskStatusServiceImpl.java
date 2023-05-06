package hexlet.code.service;

import hexlet.code.common.mapper.TaskStatusMapper;
import hexlet.code.common.utils.Utils;
import hexlet.code.dto.CreateStatusDto;
import hexlet.code.dto.GetStatusDto;
import hexlet.code.entity.TaskStatus;
import hexlet.code.exception.ExceptionMessage;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskStatusServiceImpl implements TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;
    private final TaskStatusMapper mapper;

    @Autowired
    public TaskStatusServiceImpl(TaskStatusRepository taskStatusRepository,
                                 TaskStatusMapper mapper) {
        this.taskStatusRepository = taskStatusRepository;
        this.mapper = mapper;
    }

    @Override
    public List<GetStatusDto> findAll() {
        return taskStatusRepository.findAll().stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public GetStatusDto findById(Long id) {
        return taskStatusRepository.findById(id)
                .map(mapper::map)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessage.RESOURCE_NOT_FOUND));
    }

    @Override
    public GetStatusDto create(CreateStatusDto dto) {
        String trimmedName = Utils.trimmedText(dto.getName());
        dto.setName(trimmedName);
        TaskStatus status = mapper.map(dto);
        taskStatusRepository.save(status);
        return mapper.map(status);
    }

    @Override
    public GetStatusDto update(Long id, CreateStatusDto dto) {
        String trimmedName = Utils.trimmedText(dto.getName());
        dto.setName(trimmedName);
        TaskStatus status = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessage.RESOURCE_NOT_FOUND));
        status.setName(dto.getName());
        taskStatusRepository.save(status);
        return mapper.map(status);
    }

    @Override
    public void delete(Long id) {
        TaskStatus status = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessage.RESOURCE_NOT_FOUND));
        taskStatusRepository.delete(status);
    }

}
