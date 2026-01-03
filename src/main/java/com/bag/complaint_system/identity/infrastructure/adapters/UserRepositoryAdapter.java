package com.bag.complaint_system.identity.infrastructure.adapters;

import com.bag.complaint_system.identity.application.ports.output.UserPersistencePort;
import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.identity.domain.model.valueobject.Email;
import com.bag.complaint_system.identity.infrastructure.adapters.output.persistence.entity.UserEntity;
import com.bag.complaint_system.identity.infrastructure.adapters.output.mapper.UserPersistenceMapper;
import com.bag.complaint_system.identity.infrastructure.adapters.output.persistence.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserPersistencePort {

  private final JpaUserRepository jpaUserRepository;
  private final UserPersistenceMapper mapper;

  @Override
  public User save(User user) {
    UserEntity userEntity = mapper.toUserEntity(user);
    return mapper.toUser(jpaUserRepository.save(userEntity));
  }

  @Override
  public Optional<User> findById(Long id) {
    return jpaUserRepository.findById(id).map(mapper::toUser);
  }

  @Override
  public Optional<User> findByEmail(Email email) {
    return jpaUserRepository.findByEmail(email.getValue()).map(mapper::toUser);
  }

  @Override
  public boolean existsByEmail(Email email) {
    return jpaUserRepository.existsByEmail(email.getValue());
  }

  @Override
  public List<User> findAllActive() {
    return mapper.toUserList(jpaUserRepository.findAllByIsActive(true));
  }

  @Override
  public List<User> findAllByRole(String role) {
    UserEntity.RoleEntity roleEntity = UserEntity.RoleEntity.valueOf(role);
    return mapper.toUserList(jpaUserRepository.findAllByRole(roleEntity));
  }

  @Override
  public List<User> findAllById(List<Long> ids) {
    return mapper.toUserList(jpaUserRepository.findAllById(ids));
  }

  @Override
  public void deleteById(Long id) {
    jpaUserRepository.deleteById(id);
  }
}
