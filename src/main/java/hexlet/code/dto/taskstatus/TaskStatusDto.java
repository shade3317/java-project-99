package hexlet.code.dto.taskstatus;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonFormat;


@Getter
@Setter
public class TaskStatusDto {
    private Long      id;
    private String    name;
    private String    slug;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
}
