package com.bag.complaint_system.shared.config;

import com.bag.complaint_system.identity.infrastructure.security.CustomUserDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextHelper {

  public Long getAuthenticatedUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new SecurityException("User not authenticated");
    }

    var principal = authentication.getPrincipal();
    if (!(principal instanceof CustomUserDetail userDetails)) {
      throw new SecurityException("Invalid principal type");
    }

    return userDetails.getUserId();
  }

  public String getAuthenticatedUserRole() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new SecurityException("User not authenticated");
    }

    Object principal = authentication.getPrincipal();
    if (!(principal instanceof CustomUserDetail userDetails)) {
      throw new SecurityException("Invalid authentication principal");
    }

    return userDetails.getAuthorities().iterator().next().getAuthority();
  }
}
