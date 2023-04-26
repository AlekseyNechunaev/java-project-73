package hexlet.code.controller;

import hexlet.code.dto.CreateLabelDto;
import hexlet.code.dto.GetLabelDto;
import hexlet.code.service.LabelService;
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
@RequestMapping(path = "${base-api-url}" + LabelController.LABEL_PATH)
public class LabelController {
    public static final String LABEL_PATH = "/labels";
    private static final String ID = "/{id}";

    private final LabelService labelService;

    @Autowired
    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @ApiResponse(responseCode = "200", description = "success get a list of all labels", content =
    @Content(mediaType = "application/json", schema = @Schema(implementation = GetLabelDto.class)))
    @GetMapping
    public List<GetLabelDto> findAll() {
        return labelService.findAll();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success get label by id", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = GetLabelDto.class))),
            @ApiResponse(responseCode = "404", description = "label by id not found")
    })
    @GetMapping(path = ID)
    public GetLabelDto findById(@PathVariable @Parameter(description = "id searched label", required = true) Long id) {
        return labelService.findById(id);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success create new label", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = GetLabelDto.class))),
            @ApiResponse(responseCode = "400", description = "label exist by this name"),
            @ApiResponse(responseCode = "422", description = "validation error")
    })
    @PostMapping
    public GetLabelDto create(@Valid @RequestBody
                              @Parameter(description = "data for creating a new label", required = true)
                              CreateLabelDto dto) {
        return labelService.create(dto);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success update label", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = GetLabelDto.class))),
            @ApiResponse(responseCode = "400", description = "label exist by this name"),
            @ApiResponse(responseCode = "404", description = "label by id not found"),
            @ApiResponse(responseCode = "422", description = "validation error")
    })
    @PutMapping(path = ID)
    public GetLabelDto update(@PathVariable @Parameter(description = "id of the label to be updated", required = true)
                              Long id, @Valid @RequestBody @Parameter(description = "data for update a label",
            required = true) CreateLabelDto dto) {
        return labelService.update(id, dto);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success delete label"),
            @ApiResponse(responseCode = "400", description = "illegal delete label"),
            @ApiResponse(responseCode = "404", description = "label by id not found")
    })
    @DeleteMapping(path = ID)
    public void delete(@PathVariable @Parameter(description = "id of the label to be deleted", required = true)
                       Long id) {
        labelService.delete(id);
    }
}
