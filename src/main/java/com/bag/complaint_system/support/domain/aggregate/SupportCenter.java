package com.bag.complaint_system.support.domain.aggregate;

import com.bag.complaint_system.identity.domain.model.valueobject.Email;
import com.bag.complaint_system.identity.domain.model.valueobject.Phone;
import com.bag.complaint_system.support.domain.valueobject.Address;
import com.bag.complaint_system.support.domain.valueobject.District;
import com.bag.complaint_system.support.domain.valueobject.Schedule;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SupportCenter {

  private Long id;
  private String name;
  private Address address;
  private Phone phone;
  private Email email;
  private Schedule schedule;
  private Boolean isActive;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static SupportCenter create(
      String name, Address address, Phone phone, Email email, Schedule schedule) {

    validateName(name);
    validateDependencies(address, phone, email, schedule);

    SupportCenter supportCenter = new SupportCenter();
    supportCenter.name = name.trim();
    supportCenter.address = address;
    supportCenter.phone = phone;
    supportCenter.email = email;
    supportCenter.schedule = schedule;
    supportCenter.isActive = true;
    supportCenter.createdAt = LocalDateTime.now();
    supportCenter.updatedAt = LocalDateTime.now();

    return supportCenter;
  }

  public void update(String name, Address address, Phone phone, Email email, Schedule schedule) {
    validateName(name);

    this.name = name.trim();
    this.address = address;
    this.phone = phone;
    this.email = email;
    this.schedule = schedule;
    this.updatedAt = LocalDateTime.now();
  }

  public void deactivate() {
    this.isActive = false;
    this.updatedAt = LocalDateTime.now();
  }

  public void activate() {
    this.isActive = true;
    this.updatedAt = LocalDateTime.now();
  }

  public boolean isIndDistrict(District district) {
    return this.address.getDistrict().equals(district);
  }

  public District getDistrict() {
    return this.address.getDistrict();
  }

  private static void validateName(String name) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Support center name cannot be empty");
    }
    if (name.trim().length() > 200) {
      throw new IllegalArgumentException("Support center name cannot exceed 200 characters");
    }
  }

  private static void validateDependencies(
      Address address, Phone phone, Email email, Schedule schedule) {
    if (address == null) {
      throw new IllegalArgumentException("Address is required.");
    }
    if (phone == null) {
      throw new IllegalArgumentException("Phone is required.");
    }
    if (email == null) {
      throw new IllegalArgumentException("Email is required.");
    }
    if (schedule == null) {
      throw new IllegalArgumentException("Schedule is required.");
    }
  }
}
