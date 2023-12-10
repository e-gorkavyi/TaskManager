package com.em.taskmanager.controllers;

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
        } catch (NotTaskOwner | RecordNotFound | UserNotFound e) {
            if (e.getClass() == NotTaskOwner.class)
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            if (e.getClass() == RecordNotFound.class)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            if (e.getClass() == UserNotFound.class)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/updateTaskStatus")
    public ResponseEntity<?> updateTaskStatus(@RequestBody StatusDTO statusDTO, Principal principal) {
        try {
            return ResponseEntity.ok((taskService.updateTaskStatus(statusDTO, principal)));
        } catch (NotTaskOwner | RecordNotFound e) {
            if (e.getClass() == NotTaskOwner.class)
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            if (e.getClass() == RecordNotFound.class)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
