package hexlet.code.common.mapper;

import hexlet.code.dto.CreateUserDto;
import hexlet.code.dto.GetUserDto;
import hexlet.code.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "authorTasks", ignore = true)
    @Mapping(target = "executorTasks", ignore = true)
    User map(CreateUserDto dto);

    GetUserDto map(User user);
}
