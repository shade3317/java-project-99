package hexlet.code.dto.taskstatus;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TaskStatusCreateDto {
    @NotBlank
    private String name;

    @NotBlank
    private String slug;
}
