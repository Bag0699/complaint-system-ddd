package com.bag.complaint_system.identity.application.service;

import com.bag.complaint_system.identity.application.dto.request.LoginRequest;
import com.bag.complaint_system.identity.application.dto.response.AuthResponse;
import com.bag.complaint_system.identity.application.mapper.UserMapper;
import com.bag.complaint_system.identity.application.ports.output.UserPersistencePort;
import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.identity.domain.model.valueobject.Email;
import com.bag.complaint_system.identity.infrastructure.security.PasswordEncryptionService;
import com.bag.complaint_system.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUserServiceTest {

  @Mock private UserPersistencePort userPersistencePort;
  @Mock private PasswordEncryptionService passwordService;
  @Mock private GenerateTokenService generateTokenService;
  @Mock private UserMapper userMapper;

  @InjectMocks private LoginUserService loginUserService;

  private User user;
  private LoginRequest request;

  @BeforeEach
  void setUp() {
    user = TestUtils.buildUserVictimMock();
    request = new LoginRequest("p.garcia@example.com", "Password123!");
  }

  @Test
  void givenValidCredentials_whenExecute_thenReturnAuthResponse() {

    when(userPersistencePort.findByEmail(any(Email.class))).thenReturn(Optional.of(user));
    when(passwordService.checkPassword("Password123!", "Hashed-password123!")).thenReturn(true);
    when(generateTokenService.execute(user)).thenReturn("token");
    when(userMapper.toAuthResponse(user, "token"))
        .thenReturn(TestUtils.buildVictimAuthResponseMock());

    AuthResponse response = loginUserService.execute(request);

    assertNotNull(response);
    assertEquals("token", response.getToken());
    assertEquals("p.garcia@example.com", response.getEmail());

    verify(userPersistencePort, times(1)).findByEmail(any(Email.class));
    verify(passwordService, times(1)).checkPassword("Password123!", "Hashed-password123!");
    verify(generateTokenService, times(1)).execute(user);
    verify(userMapper, times(1)).toAuthResponse(user, "token");
  }
}
