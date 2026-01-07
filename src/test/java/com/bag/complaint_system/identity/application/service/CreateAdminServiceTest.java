package com.bag.complaint_system.identity.application.service;

import com.bag.complaint_system.identity.application.dto.request.RegisterUserRequest;
import com.bag.complaint_system.identity.application.dto.response.AuthResponse;
import com.bag.complaint_system.identity.application.mapper.UserMapper;
import com.bag.complaint_system.identity.application.ports.output.PasswordEncryptionPort;
import com.bag.complaint_system.identity.application.ports.output.UserPersistencePort;
import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.identity.domain.model.valueobject.Email;
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
class CreateAdminServiceTest {

  @Mock private UserPersistencePort userPersistencePort;
  @Mock private PasswordEncryptionPort passwordService;
  @Mock private UserMapper userMapper;

  @InjectMocks private CreateAdminService createAdminService;

  private RegisterUserRequest request;
  private AuthResponse expectedResponse;
  private Email testEmail;

  @BeforeEach
  void setUp() {
    request =
        new RegisterUserRequest(
            "Pedro", "Garcia", "p.garcia@example.com", "Password123!", "+99999992");

    testEmail = new Email(request.getEmail());

    expectedResponse =
        new AuthResponse(
            "token", "Bearer", 1L, "p.garcia@example.com", "Pedro", "Garcia", "ADMIN");
  }

  @Test
  void givenNewAdminWithUniqueEmail_whenSave_thenPersistAndReturnAuthResponse() {
    when(userPersistencePort.existsByEmail(any(Email.class))).thenReturn(false);
    when(passwordService.encryptPassword(anyString())).thenReturn("Hashed-password123!");
    when(userPersistencePort.save(any(User.class))).thenReturn(TestUtils.buildUserAdminMock());
    when(userMapper.toAuthResponse(any(User.class), isNull())).thenReturn(expectedResponse);

    AuthResponse response = createAdminService.execute(request);

    assertNotNull(response);
    assertEquals("token", response.getToken());
    assertEquals(1L, response.getId());
    assertEquals("p.garcia@example.com", response.getEmail());
    assertEquals("ADMIN", response.getRole());

    verify(userPersistencePort, times(1)).existsByEmail(testEmail);
    verify(passwordService, times(1)).encryptPassword(request.getPassword());
    verify(userPersistencePort, times(1)).save(any(User.class));
    verify(userMapper, times(1)).toAuthResponse(any(User.class), isNull());
  }

  @Test
  void givenAdminWithExistingEmail_whenSave_thenThrowUserAlreadyExistsException() {

    when(userPersistencePort.existsByEmail(any(Email.class))).thenReturn(true);
    assertThrows(UserAlreadyExistsException.class, () -> createAdminService.execute(request));

    verify(userPersistencePort, times(1)).existsByEmail(testEmail);
    verify(passwordService, never()).encryptPassword(anyString());
    verify(userPersistencePort, never()).save(any(User.class));
    verify(userMapper, never()).toAuthResponse(any(User.class), isNull());
  }
}
