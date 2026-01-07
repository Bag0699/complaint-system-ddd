package com.bag.complaint_system.support.application.service;

import com.bag.complaint_system.identity.application.ports.output.UserPersistencePort;
import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.identity.domain.model.valueobject.Email;
import com.bag.complaint_system.identity.domain.model.valueobject.Phone;
import com.bag.complaint_system.shared.exception.InvalidDistrictException;
import com.bag.complaint_system.shared.exception.RoleAccessDeniedException;
import com.bag.complaint_system.shared.exception.UserNotFoundException;
import com.bag.complaint_system.support.application.dto.request.CreateSupportCenterRequest;
import com.bag.complaint_system.support.application.dto.response.SupportCenterResponse;
import com.bag.complaint_system.support.application.mapper.SupportCenterMapper;
import com.bag.complaint_system.support.application.ports.input.CreateSupportCenterUseCase;
import com.bag.complaint_system.support.application.ports.output.SupportPersistencePort;
import com.bag.complaint_system.support.domain.aggregate.SupportCenter;
import com.bag.complaint_system.support.domain.valueobject.Address;
import com.bag.complaint_system.support.domain.valueobject.District;
import com.bag.complaint_system.support.domain.valueobject.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateSupportCenterService implements CreateSupportCenterUseCase {

  private final SupportPersistencePort supportPersistencePort;
  private final UserPersistencePort userPersistencePort;
  private final SupportCenterMapper mapper;

  @Override
  public SupportCenterResponse execute(Long authId, CreateSupportCenterRequest request) {
    User authenticatedUser =
        userPersistencePort.findById(authId).orElseThrow(UserNotFoundException::new);

    if (!authenticatedUser.isAdmin()) {
      throw new RoleAccessDeniedException("Only admins can create support centers");
    }

    District district;

    try {
      district = District.valueOf(request.getDistrict().toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new InvalidDistrictException();
    }

    Address address = new Address(request.getStreet(), district);
    Phone phone = new Phone(request.getPhone());
    Email email = new Email(request.getEmail());
    Schedule schedule = new Schedule(request.getSchedule());

    SupportCenter center = SupportCenter.create(request.getName(), address, phone, email, schedule);

    return mapper.toResponse(supportPersistencePort.save(center));
  }
}
