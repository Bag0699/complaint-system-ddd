package com.bag.complaint_system.identity.application.service;

import com.bag.complaint_system.identity.application.dto.response.UserProfileResponse;
import com.bag.complaint_system.identity.application.mapper.UserMapper;
import com.bag.complaint_system.identity.application.ports.output.UserPersistencePort;
import com.bag.complaint_system.identity.domain.model.aggregate.User;
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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetProfileServiceTest {

  @Mock private UserPersistencePort userPersistencePort;
  @Mock private UserMapper userMapper;

  @InjectMocks private GetProfileService getProfileService;

  @Mock private SecurityContext securityContext;

  @AfterEach
  void clearContext() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void givenUserAuthenticated_whenExecute_thenReturnUserProfileResponse() {

    CustomUserDetail userDetail = TestUtils.buildCustomVictimUserDetailMock();

    var auth =
        new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(auth);

    User user = mock(User.class);

    UserProfileResponse userProfileResponse =
        new UserProfileResponse("Pedro Garcia", "p.garcia@example.com", "+99999992", "VICTIM");

    when(userPersistencePort.findById(1L)).thenReturn(Optional.of(user));
    when(userMapper.toUserProfileResponse(user)).thenReturn(userProfileResponse);

    UserProfileResponse response = getProfileService.execute();

    assertNotNull(response);
    assertEquals("p.garcia@example.com", response.getEmail());

    verify(userPersistencePort, times(1)).findById(1L);
    verify(userMapper, times(1)).toUserProfileResponse(user);
  }

  @Test
  void givenUserNotAuthenticated_whenExecute_thenThrowSecurityException() {
    SecurityContextHolder.setContext(securityContext);

    assertThrows(SecurityException.class, () -> getProfileService.execute());

    verify(userPersistencePort, never()).findById(anyLong());
    verify(userMapper, never()).toUserProfileResponse(any(User.class));
  }

  @Test
  void givenAuthenticatedUserNotFound_whenExecute_thenThrowUserNotFoundException() {

    CustomUserDetail userDetail = TestUtils.buildCustomVictimUserDetailMock();

    var auth =
        new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(auth);

    when(userPersistencePort.findById(1L)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> getProfileService.execute());

    verify(userPersistencePort, times(1)).findById(1L);
    verify(userMapper, never()).toUserProfileResponse(any(User.class));
  }
}
