package hexlet.code.service;

import hexlet.code.dto.CreateLabelDto;
import hexlet.code.dto.GetLabelDto;

import java.util.List;

public interface LabelService {

    List<GetLabelDto> findAll();

    GetLabelDto findById(Long id);

    GetLabelDto create(CreateLabelDto dto);

    GetLabelDto update(Long id, CreateLabelDto dto);

    void delete(Long id);
}
