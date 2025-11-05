package com.bag.complaint_system.identity.application.service;

import com.bag.complaint_system.identity.application.dto.request.UpdateProfileRequest;
import com.bag.complaint_system.identity.application.dto.response.AuthResponse;
import com.bag.complaint_system.identity.application.mapper.UserMapper;
import com.bag.complaint_system.identity.application.ports.input.UpdateProfileUseCase;
import com.bag.complaint_system.identity.application.ports.output.UserPersistencePort;
import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.identity.domain.model.valueobject.Phone;
import com.bag.complaint_system.identity.infrastructure.security.CustomUserDetail;
import com.bag.complaint_system.shared.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateProfileService implements UpdateProfileUseCase {

  private final UserPersistencePort userPersistencePort;
  private final UserMapper userMapper;

  @Override
  public AuthResponse execute(Long id, UpdateProfileRequest request) {

    var authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new SecurityException("User is not authenticated");
    }

    var principal = authentication.getPrincipal();
    CustomUserDetail userDetail = (CustomUserDetail) principal;

    Long authenticatedUserId = userDetail.getUserId();
    String role = userDetail.getAuthorities().iterator().next().getAuthority();

    if (!role.equals("ROLE_ADMIN") && !id.equals(authenticatedUserId)) {
      throw new SecurityException("You are not allowed to update another user's profile");
    }

    User user = userPersistencePort.findById(id).orElseThrow(UserNotFoundException::new);

    Phone phone = new Phone(request.getPhone());

    user.updateProfile(request.getFirstName(), request.getLastName(), phone);
    User updatedUser = userPersistencePort.save(user);

    return userMapper.toAuthResponse(updatedUser, null);
  }
}
