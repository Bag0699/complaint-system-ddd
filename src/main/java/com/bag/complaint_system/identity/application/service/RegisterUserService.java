package com.bag.complaint_system.identity.application.service;

import com.bag.complaint_system.identity.application.dto.request.RegisterUserRequest;
import com.bag.complaint_system.identity.application.dto.response.AuthResponse;
import com.bag.complaint_system.identity.application.mapper.UserMapper;
import com.bag.complaint_system.identity.application.ports.input.RegisterUserUseCase;
import com.bag.complaint_system.identity.application.ports.output.UserPersistencePort;
import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.identity.domain.model.valueobject.Email;
import com.bag.complaint_system.identity.domain.model.valueobject.Phone;
import com.bag.complaint_system.identity.infrastructure.security.PasswordEncryptionService;
import com.bag.complaint_system.shared.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUserService implements RegisterUserUseCase {

  private final UserPersistencePort userPersistencePort;
  private final PasswordEncryptionService passwordEncryptionService;
  private final GenerateTokenService generateTokenService;
  private final UserMapper userMapper;

  @Override
  public AuthResponse execute(RegisterUserRequest request) {
    Email email = new Email(request.getEmail());
    if (userPersistencePort.existsByEmail(email)) {
      throw new UserAlreadyExistsException();
    }

    String hashedPassword = passwordEncryptionService.encryptPassword(request.getPassword());
    Phone phone = new Phone(request.getPhone());
    User user =
        User.createVictim(
            request.getFirstName(), request.getLastName(), email, hashedPassword, phone);

    User userSaved = userPersistencePort.save(user);
    String token = generateTokenService.execute(userSaved);
    return userMapper.toAuthResponse(userSaved, token);
  }
}
