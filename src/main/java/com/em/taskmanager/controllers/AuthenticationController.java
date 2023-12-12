package com.em.taskmanager.controllers;

import com.em.taskmanager.auth.*;
import com.em.taskmanager.exceptions.UserExists;
import com.em.taskmanager.util.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@Tag(name = "Authentication", description = "Authentication management controller")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @Operation(
            summary = "Register a new user",
            description = """
                    Put user basic user information.
                    A password is considered valid if all the following constraints are satisfied:
                    It contains at least 8 characters.
                    It contains at least one digit.
                    It contains at least one upper case alphabet.
                    It contains at least one lower case alphabet.
                    It contains at least one special character which includes @#$%^&+=.
                    It doesn’t contain any white space.""",
            tags = { "Authentication", "POST" })
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {

        if (!EmailValidator.getInstance().isValid(request.getEmail()))
            return new ResponseEntity<>("Invalid Email format.", HttpStatus.BAD_REQUEST);

        if (!PassCheck.isValidPassword(request.getPassword()))
            return new ResponseEntity<>("""
                    Invalid password format.
                    A password is considered valid if all the following constraints are satisfied:
                    It contains at least 8 characters.
                    It contains at least one digit.
                    It contains at least one upper case alphabet.
                    It contains at least one lower case alphabet.
                    It contains at least one special character which includes @#$%^&+=.
                    It doesn’t contain any white space.""",
                    HttpStatus.BAD_REQUEST);

        try {
            return ResponseEntity.ok(service.register(request));
        } catch (UserExists e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @Operation(
            summary = "Authenticate registered user.",
            description = "The Bearer tag is returned in the response.",
            tags = { "Authentication", "POST" })
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
