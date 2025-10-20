package org.example.identityservice.repository;

import java.util.Optional;

import org.example.identityservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String id, String name);

    Optional<Role> findByName(String name);
}
