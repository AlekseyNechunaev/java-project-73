package hexlet.code.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class CreateTaskDto {

    @NotBlank
    private String name;
    private String description;
    private Long executorId;

    @NotNull
    private Long taskStatusId;

    private Set<Long> labelIds;

    public CreateTaskDto(String name, String description, Long executorId, Long taskStatusId) {
        this.name = name;
        this.description = description;
        this.executorId = executorId;
        this.taskStatusId = taskStatusId;
    }

    public CreateTaskDto() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getExecutorId() {
        return executorId;
    }

    public void setExecutorId(Long executorId) {
        this.executorId = executorId;
    }

    public Long getTaskStatusId() {
        return taskStatusId;
    }

    public void setTaskStatusId(Long taskStatusId) {
        this.taskStatusId = taskStatusId;
    }

    public Set<Long> getLabelIds() {
        return labelIds;
    }

    public void setLabelIds(Set<Long> labelIds) {
        this.labelIds = labelIds;
    }
}
