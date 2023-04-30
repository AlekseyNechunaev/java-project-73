package hexlet.code.controller;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.CreateTaskDto;
import hexlet.code.dto.GetTaskDto;
import hexlet.code.dto.UpdateTaskDto;
import hexlet.code.entity.Task;
import hexlet.code.service.TaskService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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


    @ApiResponse(responseCode = "200", description = "success get task list", content =
    @Content(mediaType = "application/json", schema = @Schema(implementation = GetTaskDto.class)))
    @GetMapping
    public List<GetTaskDto> findAll(@QuerydslPredicate(root = Task.class)
                                    @Parameter(description = "search for a task by specific criteria")
                                    Predicate predicate) {
        return taskService.findAll(predicate);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success get task by id", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = GetTaskDto.class))),
            @ApiResponse(responseCode = "404", description = "task by id not found")
    })
    @GetMapping(path = ID)
    public GetTaskDto findById(@PathVariable @Parameter(description = "id searched task", required = true) Long id) {
        return taskService.findById(id);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "success create new task", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = GetTaskDto.class))),
            @ApiResponse(responseCode = "422", description = "validation error"),
            @ApiResponse(responseCode = "400", description = "invalid request data")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public GetTaskDto registration(@Valid @RequestBody @Parameter(description = "data for create new task",
            required = true) CreateTaskDto dto) {
        return taskService.create(dto);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success update task", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = GetTaskDto.class))),
            @ApiResponse(responseCode = "422", description = "validation error"),
            @ApiResponse(responseCode = "400", description = "invalid request data"),
            @ApiResponse(responseCode = "404", description = "task by id not found")
    })
    @PutMapping(path = ID)
    public GetTaskDto update(@PathVariable @Parameter(description = "id of the task to be updated", required = true)
                             Long id,
                             @Valid @RequestBody @Parameter(description = "data for update task", required = true)
                             UpdateTaskDto dto) {
        return taskService.update(id, dto);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success delete task"),
            @ApiResponse(responseCode = "404", description = "task by id not found")
    })
    @PreAuthorize("@authorizationHelper.canAccessDeleteTask(#id)")
    @DeleteMapping(path = ID)
    public void delete(@PathVariable @Parameter(description = "id of the task to be deleted", required = true)
                       Long id) {
        taskService.delete(id);
    }
}
