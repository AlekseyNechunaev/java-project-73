package hexlet.code.dto;

import java.util.Date;
import java.util.List;

public class GetTaskDto {
    private long id;
    private GetUserDto author;
    private GetUserDto executor;
    private GetStatusDto taskStatus;
    private List<GetLabelDto> labels;
    private String name;
    private String description;
    private Date createdAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GetUserDto getAuthor() {
        return author;
    }

    public void setAuthor(GetUserDto author) {
        this.author = author;
    }

    public GetUserDto getExecutor() {
        return executor;
    }

    public void setExecutor(GetUserDto executor) {
        this.executor = executor;
    }

    public GetStatusDto getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(GetStatusDto taskStatus) {
        this.taskStatus = taskStatus;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<GetLabelDto> getLabels() {
        return labels;
    }

    public void setLabels(List<GetLabelDto> labels) {
        this.labels = labels;
    }
}
