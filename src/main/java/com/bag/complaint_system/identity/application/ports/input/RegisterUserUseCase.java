package com.bag.complaint_system.identity.application.ports.input;

import com.bag.complaint_system.identity.application.dto.request.RegisterUserRequest;
import com.bag.complaint_system.identity.application.dto.response.AuthResponse;

public interface RegisterUserUseCase {

    AuthResponse execute(RegisterUserRequest request);
}
