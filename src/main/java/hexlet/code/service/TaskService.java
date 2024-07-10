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


//
//import hexlet.code.model.Label;
//import hexlet.code.model.TaskStatus;
//import hexlet.code.model.User;
//import hexlet.code.repository.TaskStatusRepository;

//import hexlet.code.repository.UserRepository;
//import hexlet.code.repository.LabelRepository;

//import java.util.Set;
//


@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository       taskRepository;
    private final TaskMapper           taskMapper;
    private final TaskSpecification    taskSpecification;
    //private final TaskStatusRepository taskStatusRepository;
    //private final UserRepository       userRepository;
    //private final LabelRepository      labelRepository;


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

        //
//        User assignee = null;
//        if (dto.getAssignee_id() != 0L) {
//            assignee = userRepository.findById(dto.getAssignee_id()).orElse(null);
//        }
//        task.setAssignee(assignee);

//        TaskStatus taskStatus = null;
//        if (dto.getStatus() != null) {
//            taskStatus = taskStatusRepository.findBySlug(dto.getStatus()).orElse(null);
//        }
//        task.setTaskStatus(taskStatus);

//        Set<Label> labelSet = null;
//        if (dto.getTaskLabelIds() != null) {
//            labelSet = labelRepository.findByIdIn((dto.getTaskLabelIds())).orElse(null);
//        }
//        task.setLabels(labelSet);
        //

        taskRepository.save(task);
        return taskMapper.map(task);
    }

    public TaskDto update(TaskUpdateDto data, Long taskId) {
        var task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Задача с id: " + taskId + " не найдена"));

        taskMapper.update(data, task);

        //
//        var assigneeId = data.getAssignee_id(); //!!!
//        if (assigneeId != null) {
//            var assignee = assigneeId.get() == null ? null
//                    : userRepository.findById(assigneeId.get()).orElseThrow();
//            task.setAssignee(assignee);
//        }

//        TaskStatus taskStatus = null;
//        if (data.getStatus() != null) {
//            taskStatus = taskStatusRepository.findBySlug(data.getStatus().get()).orElse(null);
//            task.setTaskStatus(taskStatus);
//        }

//        Set<Label> labelSet = null;
//        if (data.getTaskLabelIds() != null) {
//            labelSet = labelRepository.findByIdIn((data.getTaskLabelIds()).get()).orElse(null);
//            task.setLabels(labelSet);
//        }
        //

        taskRepository.save(task);

        return taskMapper.map(task);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
