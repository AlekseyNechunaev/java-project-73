package hexlet.code.service;

import hexlet.code.dto.CreateStatusDto;
import hexlet.code.dto.GetStatusDto;

import java.util.List;

public interface TaskStatusService {

    List<GetStatusDto> findAll();

    GetStatusDto findById(Long id);

    GetStatusDto create(CreateStatusDto dto);

    GetStatusDto update(Long id, CreateStatusDto dto);

    void delete(Long id);
}
