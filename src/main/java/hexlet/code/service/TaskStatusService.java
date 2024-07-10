package hexlet.code.service;
import hexlet.code.dto.taskstatus.TaskStatusCreateDto;
import hexlet.code.dto.taskstatus.TaskStatusDto;
import hexlet.code.dto.taskstatus.TaskStatusUpdateDto;
//import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.repository.TaskStatusRepository;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class TaskStatusService {
    private final TaskStatusRepository taskStatusRepository;
    private final TaskStatusMapper     taskStatusMapper;

    public List<TaskStatusDto> getAll() {
        var statuses = taskStatusRepository.findAll();
        var result = statuses.stream()
                .map(t -> taskStatusMapper.map(t))
                .toList();
        return result;
    }

    public TaskStatusDto findById(Long id) {
        var status = taskStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Статус с id " + id + " не найден"));

        var result = taskStatusMapper.map(status);
        return result;
    }

    public TaskStatusDto create(TaskStatusCreateDto dto) {
        var status = taskStatusMapper.map(dto);
        taskStatusRepository.save(status);
        var result = taskStatusMapper.map(status);
        return result;
    }

    public TaskStatusDto update(TaskStatusUpdateDto dto, Long id) {
        var status = taskStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Статус с id " + id + " не найден"));

        taskStatusMapper.update(dto, status);
        taskStatusRepository.save(status);
        var result = taskStatusMapper.map(status);
        return result;
    }

    public void delete(Long id) {
        taskStatusRepository.deleteById(id);
    }
}
