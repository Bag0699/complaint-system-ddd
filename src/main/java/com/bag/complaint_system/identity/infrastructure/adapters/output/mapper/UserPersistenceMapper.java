package com.bag.complaint_system.identity.infrastructure.adapters.output.mapper;

import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.identity.domain.model.valueobject.Email;
import com.bag.complaint_system.identity.domain.model.valueobject.Phone;
import com.bag.complaint_system.identity.infrastructure.adapters.output.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {

  @Mapping(target = "email", source = "user", qualifiedByName = "getEmailValue")
  @Mapping(target = "phone", source = "user", qualifiedByName = "getPhoneValue")
  UserEntity toUserEntity(User user);

  @Mapping(target = "email", source = "userEntity", qualifiedByName = "getEmailValueEntity")
  @Mapping(target = "phone", source = "userEntity", qualifiedByName = "getPhoneValueEntity")
  User toUser(UserEntity userEntity);

  List<User> toUserList(List<UserEntity> userEntities);

  @Named("getEmailValue")
  default String getEmailValue(User user) {
    return user.getEmail().getValue();
  }

  @Named("getPhoneValue")
  default String getPhoneValue(User user) {
    return user.getPhone().getValue();
  }

  @Named("getEmailValueEntity")
  default Email getEmailValueEntity(UserEntity userEntity) {
    return new Email(userEntity.getEmail());
  }

  @Named("getPhoneValueEntity")
  default Phone getPhoneValueEntity(UserEntity userEntity) {
    return new Phone(userEntity.getPhone());
  }
}
