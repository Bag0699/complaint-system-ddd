package com.bag.complaint_system.analytics.infrastructure.adapters.output.mapper;

import java.util.List;
import java.util.Map;

public interface AnalyticsMapper {

  Map<String, Long> converToMap(List<Object[]> results);
}
