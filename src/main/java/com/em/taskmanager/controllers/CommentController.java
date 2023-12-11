package com.em.taskmanager.controllers;

import com.em.taskmanager.dto.CommentDTO;
import com.em.taskmanager.entities.Comment;
import com.em.taskmanager.exceptions.RecordNotFound;
import com.em.taskmanager.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/getByTaskId")
    public ResponseEntity<Page<Comment>> getCommentsByTaskId(int pageNum,
                                                             int pageSize,
                                                             Long taskId) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("created").ascending());
        return ResponseEntity.ok(commentService.getCommentsByTaskId(taskId, pageable));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createComment(@RequestBody CommentDTO commentDTO, Principal principal) {
        try {
            return ResponseEntity.ok(commentService.createComment(commentDTO, principal));
        } catch (RecordNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
