package hexlet.code.mapper;
import hexlet.code.dto.taskstatus.TaskStatusCreateDto;
import hexlet.code.dto.taskstatus.TaskStatusDto;
import hexlet.code.dto.taskstatus.TaskStatusUpdateDto;
import hexlet.code.model.TaskStatus;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(
        uses = {JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskStatusMapper {
    public abstract TaskStatus    map(TaskStatusCreateDto dto);
    public abstract TaskStatusDto map(TaskStatus model);
    public abstract void          update(TaskStatusUpdateDto dto, @MappingTarget TaskStatus model);
}
