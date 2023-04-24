package hexlet.code.service;

import com.querydsl.core.types.Predicate;
import hexlet.code.common.mapper.TaskMapper;
import hexlet.code.common.utils.Utils;
import hexlet.code.dto.CreateTaskDto;
import hexlet.code.dto.GetTaskDto;
import hexlet.code.dto.UpdateTaskDto;
import hexlet.code.entity.Label;
import hexlet.code.entity.Task;
import hexlet.code.entity.TaskStatus;
import hexlet.code.entity.User;
import hexlet.code.exception.ExceptionMessage;
import hexlet.code.exception.ResourceExistException;
import hexlet.code.exception.ResourceNotExistException;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.security.AuthorizationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final LabelRepository labelRepository;
    private final AuthorizationHelper authorizationHelper;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           TaskMapper taskMapper,
                           UserRepository userRepository,
                           TaskStatusRepository taskStatusRepository,
                           LabelRepository labelRepository,
                           AuthorizationHelper authorizationHelper
    ) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.userRepository = userRepository;
        this.taskStatusRepository = taskStatusRepository;
        this.labelRepository = labelRepository;
        this.authorizationHelper = authorizationHelper;
    }

    @Override
    public List<GetTaskDto> findAll(Predicate predicate) {
        List<Task> tasks = (List<Task>) taskRepository.findAll(predicate);
        return tasks.stream()
                .map(taskMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public GetTaskDto findById(Long id) {
        return taskRepository.findById(id)
                .map(taskMapper::map)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessage.RESOURCE_NOT_FOUND));
    }

    @Override
    public GetTaskDto create(CreateTaskDto dto) {
        String trimmedName = Utils.trimmedText(dto.getName());
        dto.setName(trimmedName);
        if (taskRepository.existsByName(dto.getName())) {
            throw new ResourceExistException(ExceptionMessage.TASK_EXIST_BY_NAME);
        }
        User author = authorizationHelper.getUserByAuthenticationName();
        User executor = dto.getExecutorId() == null ? null : userRepository.findById(dto.getExecutorId())
                .orElseThrow(() -> new ResourceNotExistException(ExceptionMessage.EXECUTOR_NOT_EXIST));
        TaskStatus status = taskStatusRepository.findById(dto.getTaskStatusId())
                .orElseThrow(() -> new ResourceNotExistException(ExceptionMessage.STATUS_NOT_EXIST));
        Task task = taskMapper.map(dto);
        task.setAuthor(author);
        task.setExecutor(executor);
        task.setTaskStatus(status);
        List<Label> labels = takeAllLabelsIsExist(dto.getLabelIds());
        task.setLabels(labels);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    @Override
    public GetTaskDto update(Long id, UpdateTaskDto dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessage.RESOURCE_NOT_FOUND));
        String trimmedName = Utils.trimmedText(dto.getName());
        dto.setName(trimmedName);
        if (taskRepository.existsByNameAndIdNot(dto.getName(), id)) {
            throw new ResourceExistException(ExceptionMessage.TASK_EXIST_BY_NAME);
        }
        User executor = dto.getExecutorId() == null ? null : userRepository.findById(dto.getExecutorId())
                .orElseThrow(() -> new ResourceNotExistException(ExceptionMessage.EXECUTOR_NOT_EXIST));
        TaskStatus status = taskStatusRepository.findById(dto.getTaskStatusId())
                .orElseThrow(() -> new ResourceNotExistException(ExceptionMessage.STATUS_NOT_EXIST));
        task.setName(dto.getName());
        task.setExecutor(executor);
        task.setDescription(dto.getDescription());
        task.setTaskStatus(status);
        List<Label> labels = takeAllLabelsIsExist(dto.getLabelIds());
        task.setLabels(labels);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    @Override
    public void delete(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessage.RESOURCE_NOT_FOUND));
        taskRepository.delete(task);
    }

    private List<Label> takeAllLabelsIsExist(Collection<Long> requestLabelIds) {
        if (CollectionUtils.isEmpty(requestLabelIds)) {
            return Collections.emptyList();
        }
        List<Label> foundLabels = labelRepository.findAllById(requestLabelIds);
        Set<Long> foundLabelIds = foundLabels.stream()
                .map(Label::getId)
                .collect(Collectors.toSet());
        if (foundLabelIds.size() != requestLabelIds.size()) {
            Set<Long> diffIds = requestLabelIds.stream()
                    .filter(id -> !foundLabelIds.contains(id))
                    .collect(Collectors.toSet());
            throw new ResourceNotExistException(ExceptionMessage.LABELS_EXIST_BY_ID + ": " + diffIds);
        }
        return foundLabels;
    }
}
