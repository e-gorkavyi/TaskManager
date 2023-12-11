package com.em.taskmanager.repositories;

import com.em.taskmanager.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepo extends CrudRepository<Comment, Long> {

    Page<Comment> findAllByTaskId(Long taskId, Pageable pageable);
}
