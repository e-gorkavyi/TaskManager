package com.em.taskmanager.dto;

import com.em.taskmanager.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusDTO {

    private Long taskId;
    private Status status;

}
