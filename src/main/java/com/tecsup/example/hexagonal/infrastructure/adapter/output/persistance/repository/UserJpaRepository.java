package com.tecsup.example.hexagonal.infrastructure.adapter.output.persistance.repository;

import com.tecsup.example.hexagonal.infrastructure.adapter.output.persistance.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findByLastName(String lastName);

    Optional<UserEntity> findByDni(String dni);

    List<UserEntity> findByEdadLessThan(Integer maxAge);
}