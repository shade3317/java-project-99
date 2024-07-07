package hexlet.code.dto;

import java.util.Set;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TaskDto {
    private Long      id;
    private String    title;
    private Integer   index;
    private String    content;
    private String    status;
    private Long      assignee_id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createdAt;

    private Set<Long> taskLabelIds;
}
