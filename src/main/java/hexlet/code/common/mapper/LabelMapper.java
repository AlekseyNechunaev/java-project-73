package hexlet.code.common.mapper;

import hexlet.code.dto.CreateLabelDto;
import hexlet.code.dto.GetLabelDto;
import hexlet.code.entity.Label;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LabelMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    Label map(CreateLabelDto dto);

    GetLabelDto map(Label label);
}
