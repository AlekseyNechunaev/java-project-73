package hexlet.code.dto;

import javax.validation.constraints.NotBlank;

public class CreateStatusDto {

    @NotBlank
    private String name;

    public CreateStatusDto(String name) {
        this.name = name;
    }

    public CreateStatusDto() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
