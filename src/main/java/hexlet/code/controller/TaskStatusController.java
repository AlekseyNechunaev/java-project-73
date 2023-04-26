package hexlet.code.controller;

import hexlet.code.dto.CreateStatusDto;
import hexlet.code.dto.GetStatusDto;
import hexlet.code.service.TaskStatusService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("${base-api-url}" + TaskStatusController.TASK_STATUS_PATH)
public class TaskStatusController {
    public static final String TASK_STATUS_PATH = "/statuses";
    public static final String ID = "/{id}";

    private final TaskStatusService taskStatusService;

    @Autowired
    public TaskStatusController(TaskStatusService taskStatusService) {
        this.taskStatusService = taskStatusService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success get status list", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = GetStatusDto.class))),
    })
    @GetMapping
    public List<GetStatusDto> findAll() {
        return taskStatusService.findAll();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success get status by id", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = GetStatusDto.class))),
            @ApiResponse(responseCode = "404", description = "status by id not found"),
    })
    @GetMapping(ID)
    public GetStatusDto findById(@PathVariable @Parameter(description = "id searched status") Long id) {
        return taskStatusService.findById(id);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success create new status", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = GetStatusDto.class))),
            @ApiResponse(responseCode = "400", description = "status exist by this name"),
            @ApiResponse(responseCode = "422", description = "validation error")
    })
    @PostMapping
    public GetStatusDto create(@RequestBody @Valid @Parameter(description = "data for create new task", required = true)
                               CreateStatusDto createStatusDto) {
        return taskStatusService.create(createStatusDto);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success create new status", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = GetStatusDto.class))),
            @ApiResponse(responseCode = "400", description = "status exist by this name"),
            @ApiResponse(responseCode = "422", description = "validation error")
    })
    @PutMapping(ID)
    public GetStatusDto update(@PathVariable @Parameter(description = "id of the status to be updated", required = true)
                               Long id, @RequestBody @Valid @Parameter(description = "data for update task",
            required = true) CreateStatusDto createStatusDto) {
        return taskStatusService.update(id, createStatusDto);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success delete status"),
            @ApiResponse(responseCode = "400", description = "illegal delete status"),
            @ApiResponse(responseCode = "422", description = "status by id not found")
    })
    @DeleteMapping(ID)
    public void delete(@PathVariable @Parameter(description = "id of the status to be deleted", required = true)
                       Long id) {
        taskStatusService.delete(id);
    }
}
