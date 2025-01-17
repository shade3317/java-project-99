package hexlet.code.controller.api;
import hexlet.code.dto.taskstatus.TaskStatusCreateDto;
import hexlet.code.dto.taskstatus.TaskStatusDto;
import hexlet.code.dto.taskstatus.TaskStatusUpdateDto;
import hexlet.code.service.TaskStatusService;

import java.util.List;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/task_statuses")
@AllArgsConstructor
public class TaskStatusController {
    private final TaskStatusService statusService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<List<TaskStatusDto>> getAll() {
        var tasksStatuses = statusService.getAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(tasksStatuses.size()))
                .body(tasksStatuses);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<TaskStatusDto> getById(@PathVariable Long id) {
        var taskStatus = statusService.findById(id);
        return ResponseEntity.ok()
                .body(taskStatus);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatusDto create(@Valid @RequestBody TaskStatusCreateDto dto) {
        return statusService.create(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskStatusDto update(@Valid @RequestBody TaskStatusUpdateDto dto, @PathVariable Long id) {
        return statusService.update(dto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        statusService.delete(id);
    }
}
