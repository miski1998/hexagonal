package com.tecsup.example.hexagonal.infrastructure.adapter.input.rest.controller;


import com.tecsup.example.hexagonal.application.port.input.UserService;
import com.tecsup.example.hexagonal.domain.exception.InvalidUserDataException;
import com.tecsup.example.hexagonal.domain.exception.UserNotFoundException;
import com.tecsup.example.hexagonal.domain.model.User;
import com.tecsup.example.hexagonal.infrastructure.adapter.input.rest.dto.UserRequest;
import com.tecsup.example.hexagonal.infrastructure.adapter.input.rest.dto.UserResponse;
import com.tecsup.example.hexagonal.infrastructure.adapter.output.persistance.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody(required = false) UserRequest request) {
        try {

            if (request == null) {
                log.warn("Received null UserRequest");
                return ResponseEntity.badRequest().build();
            }

            log.info("Creating request with name: {} and email: {}", request.getName(), request.getEmail());
            User newUser = this.userMapper.toDomain(request);

            log.info("Mapped User entity: {}", newUser);
            User createUser = this.userService.createUser(newUser);

            UserResponse response = this.userMapper.toResponse(createUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (InvalidUserDataException e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().build();

        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        try {

            User user = this.userService.findUser(id);
            UserResponse response = this.userMapper.toResponse(user);
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            log.warn("User not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching user with ID: {}", id, e);
            return ResponseEntity.badRequest().build();

        }

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search/lastname")
    public ResponseEntity<List<UserResponse>> searchUsersByLastName(@RequestParam String lastName) {
        try {
            log.info("Searching users with last name: {}", lastName);

            List<User> users = this.userService.findUsersByLastName(lastName);
            List<UserResponse> responses = users.stream()
                    .map(this.userMapper::toResponse)
                    .toList();

            log.info("Found {} users with last name: {}", responses.size(), lastName);
            return ResponseEntity.ok(responses);

        } catch (IllegalArgumentException e) {
            log.warn("Invalid search parameter: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error searching users by last name: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search/dni")
    public ResponseEntity<UserResponse> searchUserByDni(@RequestParam String dni) {
        try {
            log.info("Searching user with DNI: {}", dni);

            User user = this.userService.findUserByDni(dni);
            UserResponse response = this.userMapper.toResponse(user);

            log.info("Found user with DNI: {}", dni);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.warn("Invalid search parameter: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (UserNotFoundException e) {
            log.warn("User not found with DNI: {}", dni);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error searching user by DNI: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasRole('MONITOR')")
    @GetMapping("/search/age")
    public ResponseEntity<List<UserResponse>> searchUsersByAge(@RequestParam Integer maxAge) {
        try {
            log.info("Searching users with age less than: {}", maxAge);

            List<User> users = this.userService.findUsersByAgeLessThan(maxAge);
            List<UserResponse> responses = users.stream()
                    .map(this.userMapper::toResponse)
                    .toList();

            log.info("Found {} users with age less than: {}", responses.size(), maxAge);
            return ResponseEntity.ok(responses);

        } catch (IllegalArgumentException e) {
            log.warn("Invalid search parameter: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error searching users by age: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}