package com.tecsup.example.hexagonal.application.port.input;

import com.tecsup.example.hexagonal.domain.model.User;

import java.util.List;

public interface UserService {

    User createUser(User newUser);

    User findUser(Long id);

    List<User> findUsersByLastName(String lastName);

    User findUserByDni(String dni);

    List<User> findUsersByAgeLessThan(Integer maxAge);
}