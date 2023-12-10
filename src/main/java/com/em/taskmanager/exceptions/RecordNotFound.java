package com.em.taskmanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT)
public class RecordNotFound extends Exception {

    public RecordNotFound(String message) {
        super(message);
    }

}
