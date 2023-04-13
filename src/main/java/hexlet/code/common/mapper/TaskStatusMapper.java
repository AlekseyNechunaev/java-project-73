package hexlet.code.common.mapper;

import hexlet.code.dto.CreateStatusDto;
import hexlet.code.dto.GetStatusDto;
import hexlet.code.entity.TaskStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskStatusMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    TaskStatus map(CreateStatusDto dto);

    GetStatusDto map(TaskStatus taskStatus);
}
