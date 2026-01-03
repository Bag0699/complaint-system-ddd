package com.bag.complaint_system.analytics.application.dto.response;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintByStatusResponse {
  private Map<String, Long> data;
  private Long totalComplaints;
}
