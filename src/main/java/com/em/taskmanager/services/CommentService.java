package com.em.taskmanager.services;

import com.em.taskmanager.dto.CommentDTO;
import com.em.taskmanager.entities.Comment;
import com.em.taskmanager.exceptions.RecordNotFound;
import com.em.taskmanager.repositories.CommentRepo;
import com.em.taskmanager.repositories.TaskRepo;
import com.em.taskmanager.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class CommentService {

    private final CommentRepo commentRepo;
    private final UserRepo userRepo;
    private final TaskRepo taskRepo;

    @Autowired
    public CommentService(CommentRepo commentRepo, UserRepo userRepo, TaskRepo taskRepo) {
        this.commentRepo = commentRepo;
        this.userRepo = userRepo;
        this.taskRepo = taskRepo;
    }

    public Page<Comment> getCommentsByTaskId(Long taskId, Pageable pageable) {
        return commentRepo.findAllByTaskId(taskId, pageable);
    }

    public Comment createComment(CommentDTO commentDTO, Principal principal) throws RecordNotFound {
        if (taskRepo.existsById(commentDTO.getTaskId())) {
            Comment comment = new Comment();
            comment.setCommentBody(commentDTO.getCommentBody());
            comment.setTaskId(commentDTO.getTaskId());
            comment.setCommenter(userRepo.findByEmail(principal.getName()).get());
            return commentRepo.save(comment);
        } else throw new RecordNotFound("Task not found.");
    }

}
