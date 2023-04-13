package hexlet.code.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateTaskDto {

    @NotBlank
    private String name;
    private String description;

    @NotNull
    private Long authorId;
    private Long executorId;

    @NotNull
    private Long taskStatusId;

    public CreateTaskDto(String name, String description, Long authorId, Long executorId, Long taskStatusId) {
        this.name = name;
        this.description = description;
        this.authorId = authorId;
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

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
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
