package hexlet.code.mapper;
import hexlet.code.dto.task.TaskCreateDto;
import hexlet.code.dto.task.TaskDto;
import hexlet.code.dto.task.TaskUpdateDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(
        uses = {JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {
    @Autowired
    private TaskStatusRepository statusRepository;
    @Autowired
    private LabelRepository      labelRepository;
    @Autowired
    private UserRepository       userRepository;


    @Mapping(source = "title", target = "name")
    @Mapping(source = "status", target = "taskStatus")
    @Mapping(source = "content", target = "description")
    @Mapping(source = "taskLabelIds", target = "labels", qualifiedByName = "toEntity")
    @Mapping(source = "assignee_id", target = "assignee")
    public abstract Task map(TaskCreateDto dto);

    @Mapping(source = "name", target = "title")
    @Mapping(source = "description", target = "content")
    @Mapping(source = "taskStatus.slug", target = "status")
    @Mapping(source = "assignee.id", target = "assignee_id")
    @Mapping(source = "labels", target = "taskLabelIds")
    public abstract TaskDto map(Task model);

    @Mapping(source = "title", target = "name")
    @Mapping(source = "status", target = "taskStatus")
    @Mapping(source = "assignee_id", target = "assignee")
    @Mapping(source = "taskLabelIds", target = "labels", qualifiedByName = "toEntity")
    public abstract void update(TaskUpdateDto dto, @MappingTarget Task model);

    public TaskStatus toTaskStatus(String statusSlug) {
        return statusRepository.findBySlug(statusSlug)
                .orElseThrow(() -> new NoSuchElementException("TaskStatus с slug " + statusSlug + " не найден"));
    }

    public Set<Long> toDto(Set<Label> labels) {
        return labels == null ? null : labels.stream()
                .map(Label::getId)
                .collect(Collectors.toSet());
    }

    @Named("toEntity")
    public Set<Label> toEntity(Set<Long> labelIds) {
        if (labelIds == null) {
            return null;
        }
        return labelIds.stream()
                .map(labelId -> labelRepository.findById(labelId)
                        .orElseThrow())
                .collect(Collectors.toSet());
    }

    public User toAssigneeId(Long assigneeId) {
        if (assigneeId == 0L || assigneeId == null) {
            return null;
        }

        return userRepository.findById(assigneeId).orElse(null);
    }
}
