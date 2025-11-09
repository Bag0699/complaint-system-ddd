package com.bag.complaint_system.identity.infrastructure.adapters.input.rest;

import com.bag.complaint_system.identity.application.dto.request.RegisterUserRequest;
import com.bag.complaint_system.identity.application.dto.response.AuthResponse;
import com.bag.complaint_system.identity.application.ports.input.CreateAdminUseCase;
import com.bag.complaint_system.identity.application.ports.input.GetProfileUseCase;
import com.bag.complaint_system.identity.application.ports.input.UpdateProfileUseCase;

import com.bag.complaint_system.identity.infrastructure.security.JwtAuthenticationFilter;
import com.bag.complaint_system.shared.config.SecurityConfig;
import com.bag.complaint_system.utils.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
@ContextConfiguration(classes = {UserController.class})
class UserControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private CreateAdminUseCase createAdminUseCase;
  @MockitoBean private UpdateProfileUseCase updateProfileUseCase;
  @MockitoBean private GetProfileUseCase getProfileUseCase;

  @MockitoBean private JwtAuthenticationFilter jwtAuthFilter;

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() throws Exception {
    objectMapper = new ObjectMapper();

    doAnswer(
            invocation -> {
              FilterChain filterChain = invocation.getArgument(2, FilterChain.class);
              filterChain.doFilter(invocation.getArgument(0), invocation.getArgument(1));

              return null;
            })
        .when(jwtAuthFilter)
        .doFilter(
            any(HttpServletRequest.class), any(HttpServletResponse.class), any(FilterChain.class));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void givenValidRegisterUserRequest_whenCreateAdmin_thenReturnCreateAdminUser() throws Exception {
    RegisterUserRequest request = TestUtils.buildRegisterUserRequestMock();
    AuthResponse response = TestUtils.buildVictimAuthResponseMock();

    when(createAdminUseCase.execute(any(RegisterUserRequest.class))).thenReturn(response);

    mockMvc
        .perform(
            post("/api/v1/users/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf()))
        .andExpect(status().isCreated())
        .andExpect(content().json(objectMapper.writeValueAsString(response)))
        .andDo(print());
    verify(createAdminUseCase, Mockito.times(1)).execute(any(RegisterUserRequest.class));
  }

  @Test
  @WithMockUser(roles = "VICTIM")
  void givenUserNotAdmin_whenCreateAdmin_thenReturnForbidden() throws Exception {
    RegisterUserRequest request = TestUtils.buildRegisterUserRequestMock();

    mockMvc
        .perform(
            post("/api/v1/users/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isForbidden())
        .andDo(print());

    verify(createAdminUseCase, Mockito.times(0)).execute(any(RegisterUserRequest.class));
  }
}
