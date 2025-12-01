package com.tecsup.example.hexagonal.infrastructure.adapter.output.persistance.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "lastname", nullable = false, length = 100)
    private String lastName;

    @Column(name = "apellido_materno", length = 100)
    private String apellidoMaterno;

    @Column(name = "edad")
    private Integer edad;

    @Column(name = "dni", length = 8, unique = true)
    private String dni;

    @Column(name = "telefono", length = 9)
    private String telefono;

    @Column(nullable = false, length = 150)
    private String email;

    private String password;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;

}