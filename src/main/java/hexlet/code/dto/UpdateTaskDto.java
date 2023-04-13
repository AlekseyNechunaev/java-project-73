package hexlet.code.dto;

import javax.validation.constraints.NotNull;

public class UpdateTaskDto {
    @NotNull
    private String name;
    private String description;
    private Long executorId;

    @NotNull
    private Long taskStatusId;

    public UpdateTaskDto(String name, String description, Long executorId, Long taskStatusId) {
        this.name = name;
        this.description = description;
        this.executorId = executorId;
        this.taskStatusId = taskStatusId;
    }

    public UpdateTaskDto() {

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
}
