package com.em.taskmanager.controllers;

import com.em.taskmanager.dto.PerformerDTO;
import com.em.taskmanager.dto.StatusDTO;
import com.em.taskmanager.dto.TaskDTO;
import com.em.taskmanager.entities.Task;
import com.em.taskmanager.exceptions.NotTaskOwner;
import com.em.taskmanager.exceptions.RecordNotFound;
import com.em.taskmanager.exceptions.UserNotFound;
import com.em.taskmanager.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/task")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/getTaskById")
    public ResponseEntity<Task> getTaskById(@RequestParam Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/getTasksByCreatorId")
    public ResponseEntity<Page<Task>> getTasksByCreatorId(int pageNum,
                                                          int pageSize,
                                                          Long userId) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return ResponseEntity.ok(taskService.getTasksByCreatorId(pageable, userId));
    }

    @GetMapping("/getTasksByPerformerId")
    public ResponseEntity<Page<Task>> getTasksByPerformerId(int pageNum,
                                                            int pageSize,
                                                            Long userId) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return ResponseEntity.ok(taskService.getTasksByPerformerId(pageable, userId));
    }

    @GetMapping("/getAllTasks")
    public ResponseEntity<Page<Task>> getAllTasks(int pageNum,
                                                  int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return ResponseEntity.ok(taskService.getAllTasks(pageable));
    }

    @PostMapping("/createTask")
    public ResponseEntity<Task> createTask(@RequestBody TaskDTO taskDTO, Principal principal) {
        return ResponseEntity.ok(taskService.createTask(taskDTO, principal));
    }

    @PostMapping("/updateTask")
    public ResponseEntity<?> updateTask(@RequestBody TaskDTO task, Principal principal) {
        try {
            return ResponseEntity.ok(taskService.updateTask(task, principal));
        } catch (Exception e) {
            return exceptionAdapter(e);
        }
    }

    @PostMapping("/updateTaskStatus")
    public ResponseEntity<?> updateTaskStatus(@RequestBody StatusDTO statusDTO, Principal principal) {
        try {
            return ResponseEntity.ok(taskService.updateTaskStatus(statusDTO, principal));
        } catch (Exception e) {
            return exceptionAdapter(e);
        }
    }

    @PostMapping("/updateTaskPerformer")
    public ResponseEntity<?> updateTaskPerformer(@RequestBody PerformerDTO performerDTO, Principal principal) {
        try {
            return ResponseEntity.ok(taskService.updateTaskPerformer(performerDTO, principal));
        } catch (Exception e) {
            return exceptionAdapter(e);
        }
    }

    @DeleteMapping("/deleteById")
    public ResponseEntity<?> deleteById(Long taskId, Principal principal) {
        try {
            return ResponseEntity.ok(taskService.deleteTaskById(taskId, principal));
        } catch (NotTaskOwner | RecordNotFound e) {
            return exceptionAdapter(e);
        }
    }

    public ResponseEntity<?> exceptionAdapter(Exception exception) {
        if (exception.getClass() == NotTaskOwner.class)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
        if (exception.getClass() == RecordNotFound.class)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        if (exception.getClass() == UserNotFound.class)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }

}
