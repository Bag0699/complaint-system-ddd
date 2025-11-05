package com.bag.complaint_system.identity.application.service;

import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.identity.infrastructure.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenerateTokenService {

  private final JwtProvider jwtProvider;

  public String execute(User user) {
    return jwtProvider.generateToken(
        user.getId(), user.getEmail().getValue(), user.getRole().name());
  }
}
