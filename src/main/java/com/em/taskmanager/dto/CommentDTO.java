package com.em.taskmanager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {

    private String commentBody;
    private Long taskId;

}
