package hexlet.code.controller;

import hexlet.code.dto.CreateTaskDto;
import hexlet.code.dto.GetTaskDto;
import hexlet.code.dto.UpdateTaskDto;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("${base-api-url}" + TaskController.TASK_PATH)
public class TaskController {
    public static final String TASK_PATH = "/tasks";
    private static final String ID = "/{id}";

    @GetMapping
    public List<GetTaskDto> findAll() {
        // TODO
        return null;
    }

    @GetMapping(path = ID)
    public GetTaskDto findById(@PathVariable Long id) {
        // TODO
        return null;
    }

    @PostMapping
    public GetTaskDto registration(@Valid @RequestBody CreateTaskDto dto) {
        // TODO
        return null;
    }

    @PutMapping(path = ID)
    public GetTaskDto update(@PathVariable Long id, @Valid @RequestBody UpdateTaskDto dto) {
        // TODO
        return null;
    }

    @DeleteMapping(path = ID)
    public void delete(@PathVariable Long id) {
        // TODO
    }
}
