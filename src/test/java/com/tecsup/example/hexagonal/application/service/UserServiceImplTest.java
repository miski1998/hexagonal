package com.tecsup.example.hexagonal.application.service;

import com.tecsup.example.hexagonal.application.port.output.UserRepository;
import com.tecsup.example.hexagonal.domain.exception.UserNotFoundException;
import com.tecsup.example.hexagonal.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser1;
    private User testUser2;
    private User testUser3;

    @BeforeEach
    void setUp() {
        testUser1 = new User(1L, "Juan", "Pérez", "González", 25, "12345678", "987654321", "juan.perez@email.com", "password123", true, null);
        testUser2 = new User(2L, "María", "García", "López", 30, "87654321", "123456789", "maria.garcia@email.com", "password456", true, null);
        testUser3 = new User(3L, "Carlos", "Pérez", "Martín", 17, "11223344", "555666777", "carlos.perez@email.com", "password789", true, null);
    }

    @Test
    void findUsersByLastName_ShouldReturnUsersWithMatchingLastName() {
        // Arrange
        String lastName = "Pérez";
        List<User> expectedUsers = Arrays.asList(testUser1, testUser3);

        when(userRepository.findByLastName(lastName)).thenReturn(expectedUsers);

        // Act
        List<User> result = userService.findUsersByLastName(lastName);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(testUser1));
        assertTrue(result.contains(testUser3));
        assertFalse(result.contains(testUser2));

        verify(userRepository, times(1)).findByLastName(lastName);
    }

    @Test
    void findUsersByLastName_ShouldReturnEmptyListWhenNoMatches() {
        // Arrange
        String lastName = "Rodríguez";
        List<User> expectedUsers = Collections.emptyList();

        when(userRepository.findByLastName(lastName)).thenReturn(expectedUsers);

        // Act
        List<User> result = userService.findUsersByLastName(lastName);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userRepository, times(1)).findByLastName(lastName);
    }

    @Test
    void findUsersByLastName_ShouldThrowExceptionWhenLastNameIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.findUsersByLastName(null)
        );

        assertEquals("Last name cannot be null or empty", exception.getMessage());
        verify(userRepository, never()).findByLastName(anyString());
    }

    @Test
    void findUsersByLastName_ShouldThrowExceptionWhenLastNameIsEmpty() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.findUsersByLastName("")
        );

        assertEquals("Last name cannot be null or empty", exception.getMessage());
        verify(userRepository, never()).findByLastName(anyString());
    }

    @Test
    void findUsersByLastName_ShouldThrowExceptionWhenLastNameIsBlank() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.findUsersByLastName("   ")
        );

        assertEquals("Last name cannot be null or empty", exception.getMessage());
        verify(userRepository, never()).findByLastName(anyString());
    }

    @Test
    void findUsersByLastName_ShouldHandleCaseSensitiveSearch() {
        // Arrange
        String lastName = "PÉREZ"; // Uppercase
        List<User> expectedUsers = Collections.emptyList(); // No matches for uppercase

        when(userRepository.findByLastName(lastName)).thenReturn(expectedUsers);

        // Act
        List<User> result = userService.findUsersByLastName(lastName);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userRepository, times(1)).findByLastName(lastName);
    }


    @Test
    void findUserByDni_ShouldReturnUserWithMatchingDni() {
        // Arrange
        String dni = "12345678";

        when(userRepository.findByDni(dni)).thenReturn(java.util.Optional.of(testUser1));

        // Act
        User result = userService.findUserByDni(dni);

        // Assert
        assertNotNull(result);
        assertEquals(testUser1, result);

        verify(userRepository, times(1)).findByDni(dni);
    }

    @Test
    void findUserByDni_ShouldThrowExceptionWhenDniIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.findUserByDni(null)
        );

        assertEquals("DNI cannot be null or empty", exception.getMessage());
        verify(userRepository, never()).findByDni(anyString());
    }

    @Test
    void findUsersByAgeLessThan_ShouldReturnUsersWithAgeLessThanMaxAge() {
        // Arrange
        Integer maxAge = 20;
        List<User> expectedUsers = Arrays.asList(testUser3); // Carlos is 17 years old

        when(userRepository.findByEdadLessThan(maxAge)).thenReturn(expectedUsers);

        // Act
        List<User> result = userService.findUsersByAgeLessThan(maxAge);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(testUser3));

        verify(userRepository, times(1)).findByEdadLessThan(maxAge);
    }

    @Test
    void findUsersByAgeLessThan_ShouldThrowExceptionWhenMaxAgeIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.findUsersByAgeLessThan(null)
        );

        assertEquals("Max age must be a positive number", exception.getMessage());
        verify(userRepository, never()).findByEdadLessThan(anyInt());
    }

    @Test
    void findUsersByAgeLessThan_ShouldThrowExceptionWhenMaxAgeIsZero() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.findUsersByAgeLessThan(0)
        );

        assertEquals("Max age must be a positive number", exception.getMessage());
        verify(userRepository, never()).findByEdadLessThan(anyInt());
    }
}