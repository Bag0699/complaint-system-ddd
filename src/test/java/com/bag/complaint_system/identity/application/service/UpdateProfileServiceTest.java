package com.bag.complaint_system.identity.application.service;

import com.bag.complaint_system.identity.application.dto.request.UpdateProfileRequest;
import com.bag.complaint_system.identity.application.dto.response.AuthResponse;
import com.bag.complaint_system.identity.application.mapper.UserMapper;
import com.bag.complaint_system.identity.application.ports.output.UserPersistencePort;
import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.identity.domain.model.valueobject.Phone;
import com.bag.complaint_system.identity.infrastructure.security.CustomUserDetail;
import com.bag.complaint_system.shared.exception.UserNotFoundException;
import com.bag.complaint_system.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateProfileServiceTest {

  @Mock private UserPersistencePort userPersistencePort;
  @Mock private UserMapper userMapper;

  @InjectMocks private UpdateProfileService updateProfileService;

  @AfterEach
  void clearContext() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void givenAuthenticatedUserUpdatingOwnProfile_whenExecute_thenReturnAuthResponse() {

    CustomUserDetail userDetail = TestUtils.buildCustomVictimUserDetailMock();
    var auth =
        new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(auth);

    UpdateProfileRequest request = new UpdateProfileRequest("Pedro", "Garcia", "+99999999");

    User user = mock(User.class);
    User updatedUser = mock(User.class);
    AuthResponse authResponse = TestUtils.buildVictimAuthResponseMock();

    when(userPersistencePort.findById(1L)).thenReturn(Optional.of(user));
    when(userPersistencePort.save(user)).thenReturn(updatedUser);
    when(userMapper.toAuthResponse(updatedUser, null)).thenReturn(authResponse);

    AuthResponse response = updateProfileService.execute(1L, request);

    assertNotNull(response);
    assertEquals("p.garcia@example.com", response.getEmail());

    verify(userPersistencePort, times(1)).findById(1L);
    verify(user).updateProfile("Pedro", "Garcia", new Phone("+99999999"));
    verify(userPersistencePort, times(1)).save(user);
    verify(userMapper, times(1)).toAuthResponse(updatedUser, null);
  }

  @Test
  void givenAdminUserUpdatingAnotherProfile_whenExecute_thenReturnAuthResponse() {

    CustomUserDetail userDetail = TestUtils.buildCustomAdminUserDetailMock();
    var auth =
        new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(auth);
    UpdateProfileRequest request =
        new UpdateProfileRequest("Maria-Update", "Gonzalez", "+99999992");

    User user = mock(User.class);
    User updatedUser = mock(User.class);
    AuthResponse authResponse =
        new AuthResponse(
            "token", "Bearer", 2L, "m.gonsalez@example.com", "Maria-Update", "Gonzalez", "VICTIM");

    when(userPersistencePort.findById(2L)).thenReturn(Optional.of(user));
    when(userPersistencePort.save(user)).thenReturn(updatedUser);
    when(userMapper.toAuthResponse(updatedUser, null)).thenReturn(authResponse);

    AuthResponse response = updateProfileService.execute(2L, request);

    assertNotNull(response);
    assertEquals("Maria-Update", response.getFirstName());

    verify(userPersistencePort, times(1)).findById(2L);
    verify(user).updateProfile("Maria-Update", "Gonzalez", new Phone("+99999992"));
    verify(userPersistencePort, times(1)).save(user);
    verify(userMapper, times(1)).toAuthResponse(updatedUser, null);
  }

  @Test
  void givenVictimUserUpdatingAnotherProfile_whenExecute_thenThrowSecurityException() {

    CustomUserDetail userDetail = TestUtils.buildCustomVictimUserDetailMock();
    var auth =
        new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(auth);

    UpdateProfileRequest request = mock(UpdateProfileRequest.class);
    assertThrows(SecurityException.class, () -> updateProfileService.execute(2L, request));

    verify(userPersistencePort, never()).findById(anyLong());
    verify(userPersistencePort, never()).save(any(User.class));
    verify(userMapper, never()).toAuthResponse(any(User.class), isNull());
  }

  @Test
  void givenUserNotAuthenticated_whenExecute_thenThrowSecurityException() {

    SecurityContextHolder.clearContext();

    UpdateProfileRequest request = new UpdateProfileRequest("Pedro", "Garcia", "+99999");

    assertThrows(SecurityException.class, () -> updateProfileService.execute(1L, request));

    verify(userPersistencePort, never()).findById(anyLong());
    verify(userPersistencePort, never()).save(any(User.class));
    verify(userMapper, never()).toAuthResponse(any(User.class), isNull());
  }

  @Test
  void givenUserNotFound_whenExecute_thenThrowUserNotFoundException() {

    CustomUserDetail userDetail = TestUtils.buildCustomAdminUserDetailMock();
    var auth =
        new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(auth);

    UpdateProfileRequest request = new UpdateProfileRequest("Pedro", "Garcia", "+99999");

    when(userPersistencePort.findById(2L)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> updateProfileService.execute(2L, request));

    verify(userPersistencePort, times(1)).findById(2L);
    verify(userPersistencePort, never()).save(any(User.class));
    verify(userMapper, never()).toAuthResponse(any(User.class), isNull());
  }
}
