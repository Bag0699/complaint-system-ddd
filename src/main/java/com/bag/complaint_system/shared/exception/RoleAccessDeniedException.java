package com.bag.complaint_system.shared.exception;

public class RoleAccessDeniedException extends RuntimeException {
  public RoleAccessDeniedException(String message) {
    super(message);
  }
}
