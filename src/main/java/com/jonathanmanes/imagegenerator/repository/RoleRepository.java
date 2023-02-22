package com.jonathanmanes.imagegenerator.repository;

import com.jonathanmanes.imagegenerator.model.EnumRole;
import com.jonathanmanes.imagegenerator.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(EnumRole name);
}
