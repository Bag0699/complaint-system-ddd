package com.bag.complaint_system.complaint.application.ports.input;

import com.bag.complaint_system.complaint.application.dto.response.ComplaintResponse;

import java.util.List;

public interface GetComplaintsByVictimUseCase {

  List<ComplaintResponse> execute(Long victimId);
}
