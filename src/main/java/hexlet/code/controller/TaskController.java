package hexlet.code.controller;

import hexlet.code.dto.CreateTaskDto;
import hexlet.code.dto.GetTaskDto;
import hexlet.code.dto.UpdateTaskDto;
import hexlet.code.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("${base-api-url}" + TaskController.TASK_PATH)
public class TaskController {
    public static final String TASK_PATH = "/tasks";
    private static final String ID = "/{id}";

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<GetTaskDto> findAll() {
        return taskService.findAll();
    }

    @GetMapping(path = ID)
    public GetTaskDto findById(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @PostMapping
    public GetTaskDto registration(@Valid @RequestBody CreateTaskDto dto) {
        return taskService.create(dto);
    }

    @PutMapping(path = ID)
    public GetTaskDto update(@PathVariable Long id, @Valid @RequestBody UpdateTaskDto dto) {
        return taskService.update(id, dto);
    }

    @PreAuthorize("@authorizationHelper.canAccessDeleteTask(#id)")
    @DeleteMapping(path = ID)
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }
}
