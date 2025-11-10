package com.bag.complaint_system.complaint.application.ports.input;

import com.bag.complaint_system.complaint.application.dto.response.ComplaintResponse;

import java.util.List;

public interface GetAllComplaintUseCase {

    List<ComplaintResponse> execute(Long authId);
}
