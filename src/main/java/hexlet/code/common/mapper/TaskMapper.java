package hexlet.code.common.mapper;

import hexlet.code.dto.CreateTaskDto;
import hexlet.code.dto.GetTaskDto;
import hexlet.code.dto.UpdateTaskDto;
import hexlet.code.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TaskStatusMapper.class})
public interface TaskMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "taskStatus", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "executor", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Task map(CreateTaskDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "taskStatus", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "executor", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Task map(UpdateTaskDto dto);

    GetTaskDto map(Task task);
}
