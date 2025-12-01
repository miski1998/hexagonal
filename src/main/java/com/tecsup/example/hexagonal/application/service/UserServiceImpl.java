package com.tecsup.example.hexagonal.application.service;

import com.tecsup.example.hexagonal.application.port.input.UserService;
import com.tecsup.example.hexagonal.application.port.output.UserRepository;
import com.tecsup.example.hexagonal.domain.exception.InvalidUserDataException;
import com.tecsup.example.hexagonal.domain.exception.UserNotFoundException;
import com.tecsup.example.hexagonal.domain.model.Role;
import com.tecsup.example.hexagonal.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(User newUser) {

        // Validation logic can be added here
        validateUserInput(newUser);

        // Set default values
        if (newUser.getRole() == null)
            newUser.setRole(Role.USER);

        // Set enabled by default
        newUser.setEnabled(true);

        // Encrypt password
        if (newUser.getPassword() != null) {
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        }

        // Save the user using the repository
        User user = this.userRepository.save(newUser);

        //user.setName("Margot"); // Garbage line for testing purposes

        return user;

    }

    @Override
    public User findUser(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        User user = this.userRepository.findById(id)
                .orElseThrow( ()-> new UserNotFoundException(id) );

        return user;
    }

    @Override
    public List<User> findUsersByLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }

        return this.userRepository.findByLastName(lastName);
    }

    @Override
    public User findUserByDni(String dni) {
        if (dni == null || dni.trim().isEmpty()) {
            throw new IllegalArgumentException("DNI cannot be null or empty");
        }

        return this.userRepository.findByDni(dni)
                .orElseThrow(() -> new UserNotFoundException("User not found with DNI: " + dni));
    }

    @Override
    public List<User> findUsersByAgeLessThan(Integer maxAge) {
        if (maxAge == null || maxAge <= 0) {
            throw new IllegalArgumentException("Max age must be a positive number");
        }

        return this.userRepository.findByEdadLessThan(maxAge);
    }

    private void validateUserInput(User newUser) {

        if (newUser.getName() == null || newUser.getName().trim().isEmpty())
            throw new InvalidUserDataException("Invalid name");

        if (newUser.getLastName() == null || newUser.getLastName().trim().isEmpty())
            throw new InvalidUserDataException("Invalid last name");

        if (newUser.getEmail() == null || newUser.getEmail().trim().isEmpty() || !newUser.getEmail().contains("@"))
            throw new InvalidUserDataException("Invalid email");

        if (newUser.getPassword() == null || newUser.getPassword().trim().isEmpty())
            throw new InvalidUserDataException("Password is required");

    }
}