package com.bag.complaint_system.analytics.infrastructure.adapters.output.mapper;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AnalyticsMapperImpl implements AnalyticsMapper {

  @Override
  public Map<String, Long> converToMap(List<Object[]> results) {
    Map<String, Long> map = new HashMap<>();
    for (Object[] result : results) {
      String key = result[0].toString();
      Long count = (Long) result[1];
      map.put(key, count);
    }
    return map;
  }
}
