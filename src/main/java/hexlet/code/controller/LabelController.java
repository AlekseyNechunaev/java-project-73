package hexlet.code.controller;

import hexlet.code.dto.CreateLabelDto;
import hexlet.code.dto.GetLabelDto;
import hexlet.code.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "${base-api-url}" + LabelController.LABEL_PATH)
public class LabelController {
    public static final String LABEL_PATH = "/labels";
    private static final String ID = "/{id}";

    private final LabelService labelService;

    @Autowired
    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @GetMapping
    public List<GetLabelDto> findAll() {
        return labelService.findAll();
    }

    @GetMapping(path = ID)
    public GetLabelDto findById(@PathVariable Long id) {
        return labelService.findById(id);
    }

    @PostMapping
    public GetLabelDto create(@Valid @RequestBody CreateLabelDto dto) {
        return labelService.create(dto);
    }

    @PutMapping(path = ID)
    public GetLabelDto update(@PathVariable Long id, @Valid @RequestBody CreateLabelDto dto) {
        return labelService.update(id, dto);
    }

    @DeleteMapping(path = ID)
    public void delete(@PathVariable Long id) {
        labelService.delete(id);
    }
}
