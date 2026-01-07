package com.bag.complaint_system.identity.application.ports.output;

import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.identity.domain.model.valueobject.Email;

import java.util.List;
import java.util.Optional;

public interface UserPersistencePort {

  User save(User user);

  Optional<User> findById(Long id);

  Optional<User> findByEmail(Email email);

  boolean existsByEmail(Email email);

  List<User> findAllActive();

  List<User> findAllByRole(String role);

  List<User> findAllById(List<Long> ids);

  void deleteById(Long id);
}
