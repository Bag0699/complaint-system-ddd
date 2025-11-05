package com.bag.complaint_system.identity.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

  private String token;
  private String type = "Bearer";
  private Long id;
  private String email;
  private String firstName;
  private String lastName;
  private String role;
}
