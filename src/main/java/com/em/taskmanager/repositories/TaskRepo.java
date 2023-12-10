package com.em.taskmanager.repositories;

import com.em.taskmanager.entities.Task;
import com.em.taskmanager.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepo extends CrudRepository<Task, Long> {

    Page<Task> findByCreatorId(Long creatorId, Pageable pageable);

    Page<Task> findByPerformerId(Long userId, Pageable pageable);

    Page<Task> findAll(Pageable pageable);

}
