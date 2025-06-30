package com.eewms.repository;

import com.eewms.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name); // dùng để lấy role ROLE_ADMIN,...
}
