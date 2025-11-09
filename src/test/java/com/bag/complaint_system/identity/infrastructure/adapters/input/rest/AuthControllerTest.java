package com.bag.complaint_system.identity.infrastructure.adapters.input.rest;

import com.bag.complaint_system.identity.application.dto.request.LoginRequest;
import com.bag.complaint_system.identity.application.dto.request.RegisterUserRequest;
import com.bag.complaint_system.identity.application.dto.response.AuthResponse;
import com.bag.complaint_system.identity.application.ports.input.LoginUseCase;
import com.bag.complaint_system.identity.application.ports.input.RegisterUserUseCase;
import com.bag.complaint_system.utils.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@ContextConfiguration(classes = {AuthController.class})
@WebMvcTest(
    controllers = AuthController.class,
    excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class AuthControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private RegisterUserUseCase registerUserUseCase;
  @MockitoBean private LoginUseCase loginUserUseCase;

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
  }

  @Test
  void givenValidRegisterUserRequest_whenRegisterUser_thenReturnCreateUser() throws Exception {
    RegisterUserRequest request = TestUtils.buildRegisterUserRequestMock();
    AuthResponse response = TestUtils.buildVictimAuthResponseMock();

    when(registerUserUseCase.execute(any(RegisterUserRequest.class))).thenReturn(response);

    mockMvc
        .perform(
            post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(content().json(objectMapper.writeValueAsString(response)))
        .andDo(print());

    verify(registerUserUseCase, Mockito.times(1)).execute(any(RegisterUserRequest.class));
  }

  @Test
  void givenValidLoginUserRequest_whenLoginUser_thenReturnBadRequest() throws Exception {
    RegisterUserRequest invalidRequest =
        new RegisterUserRequest("Pedro", "Garcia", "p.garcia@example", null, null);

    mockMvc
        .perform(
            post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(registerUserUseCase, Mockito.times(0)).execute(any(RegisterUserRequest.class));
  }

  @Test
  void givenValidLoginUserRequest_whenLoginUser_thenReturnAuthResponse() throws Exception {
    LoginRequest request = new LoginRequest("p.garcia@example", "Password123!");
    AuthResponse response = TestUtils.buildVictimAuthResponseMock();
    when(loginUserUseCase.execute(any(LoginRequest.class))).thenReturn(response);

    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(response)))
        .andDo(print());

    verify(loginUserUseCase, Mockito.times(1)).execute(any(LoginRequest.class));
  }
}
