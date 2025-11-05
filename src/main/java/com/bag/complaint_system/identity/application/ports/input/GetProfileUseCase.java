package com.bag.complaint_system.identity.application.ports.input;

import com.bag.complaint_system.identity.application.dto.response.UserProfileResponse;

public interface GetProfileUseCase {

    UserProfileResponse execute();
}
