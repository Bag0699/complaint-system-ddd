package com.bag.complaint_system.analytics.application.ports.input;

import com.bag.complaint_system.analytics.application.dto.response.ComplaintByDateRangeResponse;

import java.time.LocalDate;

public interface GetComplaintsByDateUseCase {

  ComplaintByDateRangeResponse execute(Long authId, LocalDate startDate, LocalDate endDate);
}
