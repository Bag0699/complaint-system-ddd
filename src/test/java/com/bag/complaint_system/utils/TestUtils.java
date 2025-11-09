package com.bag.complaint_system.utils;

import com.bag.complaint_system.identity.application.dto.request.RegisterUserRequest;
import com.bag.complaint_system.identity.application.dto.response.AuthResponse;
import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.identity.domain.model.valueobject.Email;
import com.bag.complaint_system.identity.domain.model.valueobject.Phone;
import com.bag.complaint_system.identity.domain.model.valueobject.UserRole;
import com.bag.complaint_system.identity.infrastructure.adapters.output.persistence.entity.UserEntity;
import com.bag.complaint_system.identity.infrastructure.security.CustomUserDetail;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.List;

public class TestUtils {

  public static User buildUserAdminMock() {
    Email testEmail = new Email("p.garcia@example.com");
    Phone testPhone = new Phone("+999999992");
    return new User(
        1L,
        "Pedro",
        "Garcia",
        testEmail,
        "Hashed-password123!",
        testPhone,
        UserRole.ADMIN,
        true,
        LocalDateTime.now(),
        LocalDateTime.now());
  }

  public static User buildUserVictimMock() {
    Email testEmail = new Email("p.garcia@example.com");
    Phone testPhone = new Phone("+999999992");
    return new User(
        1L,
        "Pedro",
        "Garcia",
        testEmail,
        "Hashed-password123!",
        testPhone,
        UserRole.VICTIM,
        true,
        LocalDateTime.now(),
        LocalDateTime.now());
  }

  public static RegisterUserRequest buildRegisterUserRequestMock() {
    return new RegisterUserRequest(
        "Pedro", "Garcia", "p.garcia@example.com", "Password123!", "+999999992");
  }

  public static AuthResponse buildVictimAuthResponseMock() {
    return new AuthResponse(
        "token", "Bearer", 1L, "p.garcia@example.com", "Pedro", "Garcia", "VICTIM");
  }

  public static AuthResponse buildAdminAuthResponseMock() {
    return new AuthResponse(
        "token", "Bearer", 1L, "p.garcia@example.com", "Pedro", "Garcia", "ADMIN");
  }

  public static CustomUserDetail buildCustomVictimUserDetailMock() {
    return CustomUserDetail.builder()
        .userId(1L)
        .email("p.garcia@example.com")
        .password("Password123!")
        .authorities(List.of(new SimpleGrantedAuthority("ROLE_VICTIM")))
        .isActive(true)
        .build();
  }

  public static CustomUserDetail buildCustomAdminUserDetailMock() {
    return CustomUserDetail.builder()
        .userId(1L)
        .email("p.garcia@example.com")
        .password("Password123!")
        .authorities(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
        .isActive(true)
        .build();
  }

  public static UserEntity buildUserEntityVictimMock() {
    return new UserEntity(
        1L,
        "Pedro",
        "Garcia",
        "p.garcia@example.com",
        "Hashed-password123!",
        "+999999992",
        UserEntity.RoleEntity.VICTIM,
        true,
        LocalDateTime.now(),
        LocalDateTime.now());
  }
}
