package hexlet.code.controller;

import hexlet.code.dto.CreateStatusDto;
import hexlet.code.dto.GetStatusDto;
import hexlet.code.service.TaskStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("${base-api-url}" + TaskStatusController.TASK_STATUS_PATH)
public class TaskStatusController {
    public static final String TASK_STATUS_PATH = "/statuses";
    public static final String ID = "/{id}";

    private final TaskStatusService taskStatusService;

    @Autowired
    public TaskStatusController(TaskStatusService taskStatusService) {
        this.taskStatusService = taskStatusService;
    }

    @GetMapping
    public List<GetStatusDto> findAll() {
        return taskStatusService.findAll();
    }

    @GetMapping(ID)
    public GetStatusDto findById(@PathVariable Long id) {
        return taskStatusService.findById(id);
    }

    @PostMapping
    public GetStatusDto create(@RequestBody @Valid CreateStatusDto createStatusDto) {
        return taskStatusService.create(createStatusDto);
    }

    @PutMapping (ID)
    public GetStatusDto update(@PathVariable Long id, @RequestBody @Valid CreateStatusDto createStatusDto) {
       return taskStatusService.update(id, createStatusDto);
    }

    @DeleteMapping(ID)
    public void delete(@PathVariable Long id) {
        taskStatusService.delete(id);
    }
}
