package com.em.taskmanager.services;

import com.em.taskmanager.dto.StatusDTO;
import com.em.taskmanager.dto.TaskDTO;
import com.em.taskmanager.entities.Task;
import com.em.taskmanager.entities.User;
import com.em.taskmanager.exceptions.NotTaskOwner;
import com.em.taskmanager.exceptions.RecordNotFound;
import com.em.taskmanager.exceptions.UserNotFound;
import com.em.taskmanager.repositories.TaskRepo;
import com.em.taskmanager.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepo taskRepo;
    private final UserRepo userRepo;

    @Autowired
    public TaskService(TaskRepo taskRepo, UserRepo userRepo) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepo.findById(id);
    }

    public Page<Task> getTasksByCreatorId(Pageable pageable, Long userId) {
        return taskRepo.findByCreatorId(userId, pageable);
    }

    public Page<Task> getAllTasks(Pageable pageable) {
        return taskRepo.findAll(pageable);
    }

    public Page<Task> getTasksByPerformerId(Pageable pageable, Long userId) {
        return taskRepo.findByPerformerId(userId, pageable);
    }

    public Task createTask(TaskDTO taskDTO, Principal principal) {
        User user = userRepo.findByEmail(principal.getName()).get();
        Task task = new Task();
        task.setHeader(taskDTO.getHeader());
        task.setDescription(taskDTO.getDescription());
        task.setCreator(user);
        return taskRepo.save(task);
    }


    public Task updateTask(TaskDTO taskDTO, Principal principal) throws NotTaskOwner, RecordNotFound, UserNotFound {

        User user = userRepo.findByEmail(principal.getName()).get();

        Optional<Task> task = taskRepo.findById(taskDTO.getId());
        if (task.isEmpty())
            throw new RecordNotFound("Record not found.");

        if (taskDTO.getPerformerId() != null)
            if (userRepo.findById(taskDTO.getPerformerId()).isPresent()) {
                task.get().setPerformer(userRepo.findById(taskDTO.getPerformerId()).get());
            } else throw new UserNotFound("User for 'Performer' field not found.");

        if (taskDTO.getHeader() != null)
            task.get().setHeader(taskDTO.getHeader());
        if (taskDTO.getDescription() != null)
            task.get().setDescription(taskDTO.getDescription());
        if (taskDTO.getStatus() != null)
            task.get().setStatus(taskDTO.getStatus());
        if (taskDTO.getPriority() != null)
            task.get().setPriority(taskDTO.getPriority());

        if (!task.get().getCreator().getId().equals(user.getId())) {
            throw new NotTaskOwner("Update record denied because the user is not record creator.");
        } else return taskRepo.save(task.get());
    }

    public Task updateTaskStatus(StatusDTO statusDTO, Principal principal) throws NotTaskOwner, RecordNotFound {
        User user = userRepo.findByEmail(principal.getName()).get();

        Optional<Task> task = taskRepo.findById(statusDTO.getTaskId());

        if (task.isPresent()) {
            if (task.get().getCreator().getId().equals(user.getId()) || task.get().getPerformer().getId().equals(user.getId())) {
                task.get().setStatus(statusDTO.getStatus());
                return taskRepo.save(task.get());
            } else throw new NotTaskOwner("Update record denied because the user does not have rights to do this action");
        } else throw new RecordNotFound("Record not found.");
    }
}
