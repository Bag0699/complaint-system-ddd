package com.bag.complaint_system.identity.application.service;

import com.bag.complaint_system.identity.application.dto.request.RegisterUserRequest;
import com.bag.complaint_system.identity.application.dto.response.AuthResponse;
import com.bag.complaint_system.identity.application.mapper.UserMapper;
import com.bag.complaint_system.identity.application.ports.output.UserPersistencePort;
import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.identity.domain.model.valueobject.Email;
import com.bag.complaint_system.identity.infrastructure.security.PasswordEncryptionService;
import com.bag.complaint_system.shared.exception.UserAlreadyExistsException;
import com.bag.complaint_system.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserServiceTest {
  @Mock private UserPersistencePort userPersistencePort;
  @Mock private PasswordEncryptionService passwordService;
  @Mock private GenerateTokenService generateTokenService;
  @Mock private UserMapper userMapper;

  @InjectMocks private RegisterUserService registerUserService;

  private RegisterUserRequest request;
  private Email testEmail;
  private AuthResponse expectedResponse;
  private User userSaved;

  @BeforeEach
  void setUp() {
    request = TestUtils.buildRegisterUserRequestMock();
    testEmail = new Email(request.getEmail());
    expectedResponse = TestUtils.buildVictimAuthResponseMock();
    userSaved = TestUtils.buildUserVictimMock();
  }

  @Test
  void givenNewUserVictimWithUniqueEmail_whenSave_thenPersistAndReturnAuthResponse() {
    when(userPersistencePort.existsByEmail(any(Email.class))).thenReturn(false);
    when(passwordService.encryptPassword("Password123!")).thenReturn("Hashed-password123!");
    when(userPersistencePort.save(any(User.class))).thenReturn(userSaved);
    when(generateTokenService.execute(any(User.class))).thenReturn("token");
    when(userMapper.toAuthResponse(any(User.class), eq("token"))).thenReturn(expectedResponse);

    AuthResponse response = registerUserService.execute(request);

    assertNotNull(response);
    assertEquals("token", response.getToken());
    assertEquals(1L, response.getId());
    assertEquals("p.garcia@example.com", response.getEmail());
    assertEquals("VICTIM", response.getRole());

    verify(userPersistencePort, times(1)).existsByEmail(testEmail);
    verify(passwordService, times(1)).encryptPassword(request.getPassword());
    verify(userPersistencePort, times(1)).save(any(User.class));
    verify(userMapper, times(1)).toAuthResponse(any(User.class), eq("token"));
  }

  @Test
  void givenUserVictimWithExistingEmail_whenSave_thenThrowUserAlreadyExistsException() {
    when(userPersistencePort.existsByEmail(testEmail)).thenReturn(true);
    assertThrows(UserAlreadyExistsException.class, () -> registerUserService.execute(request));

    verify(userPersistencePort, times(1)).existsByEmail(testEmail);
    verify(passwordService, never()).encryptPassword(anyString());
    verify(generateTokenService, never()).execute(any(User.class));
    verify(userPersistencePort, never()).save(any(User.class));
    verify(userMapper, never()).toAuthResponse(any(User.class), isNull());
  }
}
