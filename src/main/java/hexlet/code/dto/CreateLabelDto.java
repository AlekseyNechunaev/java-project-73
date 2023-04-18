package hexlet.code.dto;

import javax.validation.constraints.NotBlank;

public class CreateLabelDto {
    @NotBlank
    private String name;


    public CreateLabelDto(String name) {
        this.name = name;
    }

    public CreateLabelDto() {

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
