package com.em.taskmanager.controllers;

import com.em.taskmanager.dto.PerformerDTO;
import com.em.taskmanager.dto.StatusDTO;
import com.em.taskmanager.dto.TaskDTO;
import com.em.taskmanager.entities.Task;
import com.em.taskmanager.exceptions.NotTaskOwner;
import com.em.taskmanager.exceptions.RecordNotFound;
import com.em.taskmanager.exceptions.UserNotFound;
import com.em.taskmanager.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "Tasks", description = "Tasks APIs")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/task")
public class TaskController {

    private final TaskService taskService;

    @Operation(
            summary = "Get a task.",
            description = "Returns task by task ID with related comments. Bearer tag authentication requires.",
            tags = { "Tasks", "GET" })
    @GetMapping("/getTaskById")
    public ResponseEntity<Task> getTaskById(@RequestParam Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @Operation(
            summary = "Get a task by creator.",
            description = "Returns task by creator (user) ID with related comments. With pagination. Bearer tag authentication requires.",
            tags = { "Tasks", "GET" })
    @GetMapping("/getTasksByCreatorId")
    public ResponseEntity<Page<Task>> getTasksByCreatorId(int pageNum,
                                                          int pageSize,
                                                          Long userId) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return ResponseEntity.ok(taskService.getTasksByCreatorId(pageable, userId));
    }

    @Operation(
            summary = "Get a task by performer.",
            description = "Returns task by performer (user) ID wint related comments. Bearer tag authentication requires.",
            tags = { "Tasks", "GET" })
    @GetMapping("/getTasksByPerformerId")
    public ResponseEntity<Page<Task>> getTasksByPerformerId(int pageNum,
                                                            int pageSize,
                                                            Long userId) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return ResponseEntity.ok(taskService.getTasksByPerformerId(pageable, userId));
    }

    @Operation(
            summary = "Get tasks list.",
            description = "Returns list of all tasks with related comments. With pagination. Bearer tag authentication requires.",
            tags = { "Tasks", "GET" })
    @GetMapping("/getAllTasks")
    public ResponseEntity<Page<Task>> getAllTasks(int pageNum,
                                                  int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return ResponseEntity.ok(taskService.getAllTasks(pageable));
    }

    @Operation(
            summary = "Create a task.",
            description = "Task creation with required task header and task body (task description). Bearer tag authentication requires.",
            tags = { "Tasks", "POST" })
    @PostMapping("/createTask")
    public ResponseEntity<Task> createTask(@RequestBody TaskDTO taskDTO, Principal principal) {
        return ResponseEntity.ok(taskService.createTask(taskDTO, principal));
    }

    @Operation(
            summary = "Update a task.",
            description = "Task update include all main fields. Available for task creator only. Bearer tag authentication requires.",
            tags = { "Tasks", "POST" })
    @PostMapping("/updateTask")
    public ResponseEntity<?> updateTask(@RequestBody TaskDTO task, Principal principal) {
        try {
            return ResponseEntity.ok(taskService.updateTask(task, principal));
        } catch (Exception e) {
            return exceptionAdapter(e);
        }
    }

    @Operation(
            summary = "Update a task status.",
            description = "Update s task status field. Available for task creator and task performer. Bearer tag authentication requires.",
            tags = { "Tasks", "POST" })
    @PostMapping("/updateTaskStatus")
    public ResponseEntity<?> updateTaskStatus(@RequestBody StatusDTO statusDTO, Principal principal) {
        try {
            return ResponseEntity.ok(taskService.updateTaskStatus(statusDTO, principal));
        } catch (Exception e) {
            return exceptionAdapter(e);
        }
    }

    @Operation(
            summary = "Update a task performer.",
            description = "Update s task performer (performer user ID). Available for task creator only. Bearer tag authentication requires.",
            tags = { "Tasks", "POST" })
    @PostMapping("/updateTaskPerformer")
    public ResponseEntity<?> updateTaskPerformer(@RequestBody PerformerDTO performerDTO, Principal principal) {
        try {
            return ResponseEntity.ok(taskService.updateTaskPerformer(performerDTO, principal));
        } catch (Exception e) {
            return exceptionAdapter(e);
        }
    }

    @Operation(
            summary = "Delete task.",
            description = "Task deletion. With task deletes all comments related on this task. Available for task creator only. Bearer tag authentication requires.",
            tags = { "Tasks", "DELETE" })
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
