package com.bag.complaint_system.identity.infrastructure.security;

import com.bag.complaint_system.identity.infrastructure.adapters.output.persitence.entity.UserEntity;
import com.bag.complaint_system.identity.infrastructure.adapters.output.persitence.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

  private final JpaUserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    UserEntity user =
        userRepository
            .findByEmail(email)
            .orElseThrow(
                () -> new UsernameNotFoundException("User not found with email: " + email));
    return CustomUserDetail.builder()
        .userId(user.getId())
        .email(user.getEmail())
        .password(user.getPassword())
        .authorities(
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())))
        .isActive(user.getIsActive())
        .build();
  }

  @Transactional(readOnly = true)
  public UserDetails loadUserById(Long id) {
    UserEntity user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

    return CustomUserDetail.builder()
        .userId(user.getId())
        .email(user.getEmail())
        .password(user.getPassword())
        .authorities(
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())))
        .isActive(user.getIsActive())
        .build();
  }
}
