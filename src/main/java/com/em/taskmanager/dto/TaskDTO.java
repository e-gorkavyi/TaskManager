package com.em.taskmanager.dto;

import com.em.taskmanager.enums.Priority;
import com.em.taskmanager.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDTO {
    private Long id = 0L;
    private String header;
    private String description;
    private Status status = Status.WAIT;
    private Priority priority = Priority.MIDDLE;
    private Long performerId;
}
