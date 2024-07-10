package hexlet.code.dto.task;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TaskCreateDto {
    private int       index;
    @JsonProperty("assignee_id")
    private long      assignee_id;

    @NotNull
    @Size(min = 1)
    private String    title;

    private String    content;

    @NotNull
    private String    status;

    private Set<Long> taskLabelIds;
}

