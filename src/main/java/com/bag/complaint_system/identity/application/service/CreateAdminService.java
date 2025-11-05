package com.bag.complaint_system.identity.application.service;

import com.bag.complaint_system.identity.application.dto.request.RegisterUserRequest;
import com.bag.complaint_system.identity.application.dto.response.AuthResponse;
import com.bag.complaint_system.identity.application.mapper.UserMapper;
import com.bag.complaint_system.identity.application.ports.input.CreateAdminUseCase;
import com.bag.complaint_system.identity.application.ports.output.PasswordEncryptionPort;
import com.bag.complaint_system.identity.application.ports.output.UserPersistencePort;
import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.identity.domain.model.valueobject.Email;
import com.bag.complaint_system.identity.domain.model.valueobject.Phone;
import com.bag.complaint_system.shared.exception.UserAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateAdminService implements CreateAdminUseCase {

  private final UserPersistencePort userPersistencePort;
  private final PasswordEncryptionPort passwordService;
  private final UserMapper userMapper;

  @Override
  public AuthResponse execute(RegisterUserRequest request) {
    Email email = new Email(request.getEmail());
    if (userPersistencePort.existsByEmail(email)) {
      throw new UserAlreadyExistsException();
    }

    String hashedPassword = passwordService.encryptPassword(request.getPassword());

    Phone phone = new Phone(request.getPhone());

    User admin =
        User.createAdmin(
            request.getFirstName(), request.getLastName(), email, hashedPassword, phone);
    User adminSaved = userPersistencePort.save(admin);
    return userMapper.toAuthResponse(adminSaved, null);
  }
}
