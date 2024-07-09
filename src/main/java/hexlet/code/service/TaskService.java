package hexlet.code.service;
import hexlet.code.dto.task.TaskCreateDto;
import hexlet.code.dto.task.TaskDto;
import hexlet.code.dto.task.TaskParamsDto;
import hexlet.code.dto.task.TaskUpdateDto;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.TaskRepository;
import hexlet.code.specification.TaskSpecification;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository       taskRepository;
    private final TaskMapper           taskMapper;
    private final TaskSpecification    taskSpecification;

    public List<TaskDto> getAll(TaskParamsDto params) {
        var specification = taskSpecification.build(params);
        var tasks = taskRepository.findAll(specification);
        var result = tasks.stream()
                .map(t -> taskMapper.map(t))
                .toList();
        return result;
    }

    public TaskDto findById(Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Задача с id " + id + " не найдена"));

        var result = taskMapper.map(task);
        return result;
    }

    public TaskDto create(TaskCreateDto dto) {
        var task = taskMapper.map(dto);

        taskRepository.save(task);
        return taskMapper.map(task);
    }

    public TaskDto update(TaskUpdateDto data, Long taskId) {
        var task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Задача с id: " + taskId + " не найдена"));

        taskMapper.update(data, task);
        taskRepository.save(task);

        return taskMapper.map(task);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
