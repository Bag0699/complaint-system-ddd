package com.bag.complaint_system.identity.infrastructure.adapters.output.persistence.repository;

import com.bag.complaint_system.identity.infrastructure.adapters.output.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByEmail(String email);

  boolean existsByEmail(String email);

  List<UserEntity> findAllByIsActive(Boolean isActive);

  List<UserEntity> findAllByRole(UserEntity.RoleEntity role);
}
