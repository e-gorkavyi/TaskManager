package com.em.taskmanager.controllers;

import com.em.taskmanager.dto.CommentDTO;
import com.em.taskmanager.entities.Comment;
import com.em.taskmanager.exceptions.RecordNotFound;
import com.em.taskmanager.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "Comments", description = "Task's comments APIs")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @Operation(
            summary = "Get comments related to a task.",
            description = "Returns comments list where comments related to a task. In request task ID needed. Bearer tag authentication requires.",
            tags = { "Comments", "GET" })
    @GetMapping("/getByTaskId")
    public ResponseEntity<Page<Comment>> getCommentsByTaskId(int pageNum,
                                                             int pageSize,
                                                             Long taskId) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("created").ascending());
        return ResponseEntity.ok(commentService.getCommentsByTaskId(taskId, pageable));
    }

    @Operation(
            summary = "Create a comment for a task.",
            description = "Create new comment related to task by task ID. Bearer tag authentication requires.",
            tags = { "Comments", "POST" })
    @PostMapping("/create")
    public ResponseEntity<?> createComment(@RequestBody CommentDTO commentDTO, Principal principal) {
        try {
            return ResponseEntity.ok(commentService.createComment(commentDTO, principal));
        } catch (RecordNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
