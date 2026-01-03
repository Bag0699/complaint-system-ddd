package com.bag.complaint_system.support.infrastructure.adapters.output.persistence.mapper;

import com.bag.complaint_system.identity.domain.model.valueobject.Email;
import com.bag.complaint_system.identity.domain.model.valueobject.Phone;
import com.bag.complaint_system.support.domain.aggregate.SupportCenter;
import com.bag.complaint_system.support.domain.valueobject.Address;
import com.bag.complaint_system.support.domain.valueobject.District;
import com.bag.complaint_system.support.domain.valueobject.Schedule;
import com.bag.complaint_system.support.infrastructure.adapters.output.persistence.entity.SupportCenterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SupportCenterPersistenceMapper {

  @Mapping(target = "email", source = "supportCenter", qualifiedByName = "getEmailValue")
  @Mapping(target = "phone", source = "supportCenter", qualifiedByName = "getPhoneValue")
  @Mapping(target = "schedule", source = "supportCenter", qualifiedByName = "getScheduleValue")
  @Mapping(target = "street", source = "supportCenter", qualifiedByName = "getStreetValue")
  SupportCenterEntity toSupportCenterEntity(SupportCenter supportCenter);

  @Mapping(
      target = "email",
      source = "supportCenterEntity",
      qualifiedByName = "getEmailValueEntity")
  @Mapping(
      target = "phone",
      source = "supportCenterEntity",
      qualifiedByName = "getPhoneValueEntity")
  @Mapping(
      target = "schedule",
      source = "supportCenterEntity",
      qualifiedByName = "getScheduleValueEntity")
  @Mapping(
          target = "address",
          source = "supportCenterEntity",
          qualifiedByName = "getAddressValueEntity")
  SupportCenter toSupportCenter(SupportCenterEntity supportCenterEntity);

  List<SupportCenter> toSupportCenterList(List<SupportCenterEntity> supportCenterEntities);

  // Métodos de mapeo a entidad

  @Named("getEmailValue")
  default String getEmailValue(SupportCenter supportCenter) {
    return supportCenter.getEmail().getValue();
  }

  @Named("getPhoneValue")
  default String getPhoneValue(SupportCenter supportCenter) {
    return supportCenter.getPhone().getValue();
  }

  @Named("getScheduleValue")
  default String getScheduleValue(SupportCenter supportCenter) {
    return supportCenter.getSchedule().getValue();
  }

  @Named("getStreetValue")
  default String getStreetValue(SupportCenter supportCenter) {
    return supportCenter.getAddress().getStreet();
  }

  // Métodos de mapeo a dominio

  @Named("getEmailValueEntity")
  default Email getEmailValueEntity(SupportCenterEntity supportCenterEntity) {
    return new Email(supportCenterEntity.getEmail());
  }

  @Named("getPhoneValueEntity")
  default Phone getPhoneValueEntity(SupportCenterEntity supportCenterEntity) {
    return new Phone(supportCenterEntity.getPhone());
  }

  @Named("getScheduleValueEntity")
  default Schedule getScheduleValueEntity(SupportCenterEntity supportCenterEntity) {
    return new Schedule(supportCenterEntity.getSchedule());
  }

  @Named("getAddressValueEntity")
  default Address getAddressValueEntity(SupportCenterEntity supportCenterEntity) {
    District district = District.valueOf(supportCenterEntity.getDistrict().name());
    return new Address(supportCenterEntity.getStreet(), district);
  }
}
