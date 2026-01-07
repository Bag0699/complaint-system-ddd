package com.bag.complaint_system.identity.application.service;

import com.bag.complaint_system.identity.application.dto.request.LoginRequest;
import com.bag.complaint_system.identity.application.dto.response.AuthResponse;
import com.bag.complaint_system.identity.application.mapper.UserMapper;
import com.bag.complaint_system.identity.application.ports.input.LoginUseCase;
import com.bag.complaint_system.identity.application.ports.output.UserPersistencePort;
import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.identity.domain.model.valueobject.Email;
import com.bag.complaint_system.identity.infrastructure.security.PasswordEncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUserService implements LoginUseCase {

  private final UserPersistencePort userPersistencePort;
  private final PasswordEncryptionService passwordEncryptionService;
  private final GenerateTokenService generateTokenService;
  private final UserMapper userMapper;

  @Override
  public AuthResponse execute(LoginRequest request) {
    Email email = new Email(request.getEmail());

    User user =
        userPersistencePort
            .findByEmail(email)
            .orElseThrow(() -> new BadCredentialsException("Invalid email"));

    boolean passwordMatches =
        passwordEncryptionService.checkPassword(request.getPassword(), user.getPassword());

    if (!passwordMatches) {
      throw new BadCredentialsException("Invalid password");
    }

    String token = generateTokenService.execute(user);
    return userMapper.toAuthResponse(user, token);
  }
}
