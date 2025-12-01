package com.tecsup.example.hexagonal.domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User {

    private Long id;
    private String name;
    private String lastName;
    private String apellidoMaterno;
    private Integer edad;
    private String dni;
    private String telefono;
    private String email;
    private String password;
    private boolean enabled;

    private Role role;

    // Constructor explícito para asegurar el orden correcto
    public User(Long id, String name, String lastName, String apellidoMaterno, Integer edad, String dni, String telefono, String email, String password, boolean enabled, Role role) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.apellidoMaterno = apellidoMaterno;
        this.edad = edad;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.role = role;
    }

    // Business logic methods - PURE domain logic!
    public boolean hasValidEmail() {
        return email != null &&
                email.contains("@") &&
                email.contains(".") &&
                email.length() > 5;
    }

    public boolean hasValidName() {
        return name != null &&
                !name.trim().isEmpty() &&
                name.length() >= 2;
    }

    public boolean hasValidLastName() {
        return lastName != null &&
                !lastName.trim().isEmpty() &&
                lastName.length() >= 2;
    }

    public boolean hasValidApellidoMaterno() {
        return apellidoMaterno != null &&
                !apellidoMaterno.trim().isEmpty() &&
                apellidoMaterno.length() >= 2;
    }

    public boolean hasValidEdad() {
        return edad != null && edad > 0 && edad <= 120;
    }

    public boolean hasValidDni() {
        return dni != null &&
                !dni.trim().isEmpty() &&
                dni.length() == 8 &&
                dni.matches("[0-9]{8}");
    }

    public boolean hasValidTelefono() {
        return telefono != null &&
                !telefono.trim().isEmpty() &&
                telefono.length() == 9 &&
                telefono.matches("[0-9]{9}");
    }

    public boolean isMinor() {
        return edad != null && edad < 18;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', lastName='" + lastName + "', email='" + email + "'}";
    }

}