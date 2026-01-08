package com.bag.complaint_system.shared.rest;

import com.bag.complaint_system.shared.exception.*;
import com.bag.complaint_system.shared.util.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.bag.complaint_system.shared.util.ErrorCatalog.*;

@RestControllerAdvice
public class GlobalControllerAdvice {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(UserNotFoundException.class)
  public ErrorResponse handleUserNotFoundException() {
    return ErrorResponse.builder()
        .code(USER_NOT_FOUND.getCode())
        .status(HttpStatus.NOT_FOUND)
        .message(USER_NOT_FOUND.getMessage())
        .timestamp(LocalDateTime.now())
        .build();
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(ComplaintNotFoundException.class)
  public ErrorResponse handleComplaintNotFoundException() {
    return ErrorResponse.builder()
        .code(COMPLAINT_NOT_FOUND.getCode())
        .status(HttpStatus.NOT_FOUND)
        .message(COMPLAINT_NOT_FOUND.getMessage())
        .timestamp(LocalDateTime.now())
        .build();
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(VictimNotFoundException.class)
  public ErrorResponse handleVictimNotFoundException() {
    return ErrorResponse.builder()
        .code(VICTIM_NOT_FOUND.getCode())
        .status(HttpStatus.NOT_FOUND)
        .message(VICTIM_NOT_FOUND.getMessage())
        .timestamp(LocalDateTime.now())
        .build();
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(SupportCenterNotFoundException.class)
  public ErrorResponse handleSupportCenterNotFoundException() {
    return ErrorResponse.builder()
        .code(SUPPORT_CENTER_NOT_FOUND.getCode())
        .status(HttpStatus.NOT_FOUND)
        .message(SUPPORT_CENTER_NOT_FOUND.getMessage())
        .timestamp(LocalDateTime.now())
        .build();
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401 Unauthorized
  @ExceptionHandler(SecurityException.class)
  public ErrorResponse handleSecurityException(SecurityException exception) {

    String detailMessage = exception.getMessage();

    return ErrorResponse.builder()
        .code(UNAUTHENTICATED.getCode())
        .status(HttpStatus.UNAUTHORIZED)
        .message(UNAUTHENTICATED.getMessage())
        .detailMessage(Collections.singletonList(detailMessage))
        .timestamp(LocalDateTime.now())
        .build();
  }

  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler(RoleAccessDeniedException.class)
  public ErrorResponse handleRoleAccessDeniedException(RoleAccessDeniedException exception) {
    String message = exception.getMessage();

    return ErrorResponse.builder()
        .code(INSUFFICIENT_ADMIN_ROLE.getCode())
        .status(HttpStatus.FORBIDDEN)
        .message(message)
        .detailMessage(Collections.emptyList())
        .timestamp(LocalDateTime.now())
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(InvalidValueException.class)
  public ErrorResponse handleInvalidValueException() {

    return ErrorResponse.builder()
        .code(INVALID_COMPLAINT_STATUS.getCode())
        .status(HttpStatus.BAD_REQUEST)
        .message(INVALID_COMPLAINT_STATUS.getMessage())
        .timestamp(LocalDateTime.now())
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(InvalidDistrictException.class)
  public ErrorResponse handleInvalidDistrictException() {

    return ErrorResponse.builder()
        .code(INVALID_SUPPORT_CENTER_DISTRICT.getCode())
        .status(HttpStatus.BAD_REQUEST)
        .message(INVALID_SUPPORT_CENTER_DISTRICT.getMessage())
        .timestamp(LocalDateTime.now())
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(UserAlreadyExistsException.class)
  public ErrorResponse handleUserAlreadyExistsException() {

    return ErrorResponse.builder()
        .code(USER_EMAIL_ALREADY_EXISTS.getCode())
        .status(HttpStatus.BAD_REQUEST)
        .message(USER_EMAIL_ALREADY_EXISTS.getMessage())
        .timestamp(LocalDateTime.now())
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ErrorResponse handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {

    BindingResult result = exception.getBindingResult();

    List<String> detailMessages =
        result.getFieldErrors().stream()
            .map(
                error -> {
                  String field = error.getField();
                  String defaultMessage = error.getDefaultMessage();
                  return String.format(
                      "%s: %s",
                      field, defaultMessage != null ? defaultMessage : "Validation failed");
                })
            .collect(Collectors.toList());

    if (detailMessages.isEmpty() && result.hasGlobalErrors()) {
      result
          .getGlobalErrors()
          .forEach(
              error ->
                  detailMessages.add(
                      error.getDefaultMessage() != null
                          ? error.getDefaultMessage()
                          : "Global validation error"));
    }

    return ErrorResponse.builder()
        .code(INVALID_ARGUMENT.getCode())
        .status(HttpStatus.BAD_REQUEST)
        .message(INVALID_ARGUMENT.getMessage())
        .detailMessage(detailMessages)
        .timestamp(LocalDateTime.now())
        .build();
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public ErrorResponse handleInternalServerError(Exception exception) {
    return ErrorResponse.builder()
        .code(GENERIC_ERROR.getCode())
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .message(GENERIC_ERROR.getMessage())
        .detailMessage(Collections.singletonList(exception.getMessage()))
        .timestamp(LocalDateTime.now())
        .build();
  }
}
