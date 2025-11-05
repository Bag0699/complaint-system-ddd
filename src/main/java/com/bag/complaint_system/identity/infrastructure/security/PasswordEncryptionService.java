package com.bag.complaint_system.identity.infrastructure.security;

import com.bag.complaint_system.identity.application.ports.output.PasswordEncryptionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordEncryptionService implements PasswordEncryptionPort {

  private final PasswordEncoder passwordEncoder;

  @Override
  public String encryptPassword(String password) {
    return passwordEncoder.encode(password);
  }

  @Override
  public boolean checkPassword(String password, String encryptedPassword) {
    return passwordEncoder.matches(password, encryptedPassword);
  }
}
