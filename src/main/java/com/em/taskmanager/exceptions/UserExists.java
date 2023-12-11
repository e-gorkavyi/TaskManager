package com.em.taskmanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class UserExists extends Exception {

    public UserExists(String message) {
        super(message);
    }

}