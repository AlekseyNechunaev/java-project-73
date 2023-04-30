package hexlet.code.service;

import hexlet.code.common.mapper.LabelMapper;
import hexlet.code.common.utils.Utils;
import hexlet.code.dto.CreateLabelDto;
import hexlet.code.dto.GetLabelDto;
import hexlet.code.entity.Label;
import hexlet.code.exception.ExceptionMessage;
import hexlet.code.exception.IllegalOperationException;
import hexlet.code.exception.ResourceExistException;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;
    private final LabelMapper mapper;

    @Autowired
    public LabelServiceImpl(LabelRepository labelRepository, LabelMapper mapper) {
        this.labelRepository = labelRepository;
        this.mapper = mapper;
    }

    @Override
    public List<GetLabelDto> findAll() {
        return labelRepository.findAll().stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public GetLabelDto findById(Long id) {
        return labelRepository.findById(id)
                .map(mapper::map)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessage.RESOURCE_NOT_FOUND));
    }

    @Override
    public GetLabelDto create(CreateLabelDto dto) {
        String trimmedName = Utils.trimmedText(dto.getName());
        dto.setName(trimmedName);
        if (labelRepository.existsByName(dto.getName())) {
            throw new ResourceExistException(ExceptionMessage.LABEL_EXIST_BY_NAME);
        }
        Label label = mapper.map(dto);
        labelRepository.save(label);
        return mapper.map(label);
    }

    @Override
    public GetLabelDto update(Long id, CreateLabelDto dto) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessage.RESOURCE_NOT_FOUND));
        String trimmedName = Utils.trimmedText(dto.getName());
        dto.setName(trimmedName);
        if (labelRepository.existsByNameAndIdIsNot(dto.getName(), id)) {
            throw new ResourceExistException(ExceptionMessage.LABEL_EXIST_BY_NAME);
        }
        label.setName(dto.getName());
        labelRepository.save(label);
        return mapper.map(label);
    }

    @Override
    public void delete(Long id) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessage.RESOURCE_NOT_FOUND));
        if (!label.getTasks().isEmpty()) {
            throw new IllegalOperationException(ExceptionMessage.ILLEGAL_DELETE_TASK);
        }
        labelRepository.delete(label);
    }
}
