package com.bag.complaint_system.identity.application.service;

import com.bag.complaint_system.identity.domain.model.aggregate.User;

import com.bag.complaint_system.identity.infrastructure.security.JwtProvider;
import com.bag.complaint_system.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenerateTokenServiceTest {
  @Mock private JwtProvider jwtProvider;

  @InjectMocks private GenerateTokenService generateTokenService;

  private User userTest;
  private String email;
  private String role;

  @BeforeEach
  void setUp() {
    userTest = TestUtils.buildUserVictimMock();
    email = userTest.getEmail().getValue();
    role = userTest.getRole().toString();
  }

  @Test
  void givenAValidUser_whenExecute_thenJwtProviderIsCalledWithCorrectData() {

    when(jwtProvider.generateToken(userTest.getId(), email, role)).thenReturn("token");

    String token = generateTokenService.execute(userTest);

    assertEquals("token", token);

    verify(jwtProvider, times(1)).generateToken(userTest.getId(), email, role);
  }
}
