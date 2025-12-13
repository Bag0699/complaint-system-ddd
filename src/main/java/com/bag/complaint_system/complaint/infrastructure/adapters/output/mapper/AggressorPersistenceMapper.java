package com.bag.complaint_system.complaint.infrastructure.adapters.output.mapper;

import com.bag.complaint_system.complaint.domain.entity.Aggressor;
import com.bag.complaint_system.complaint.infrastructure.adapters.output.persistence.entity.AggressorEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AggressorPersistenceMapper {

  AggressorEntity toAggressorEntity(Aggressor aggressor);

  Aggressor toAggressor(AggressorEntity aggressorEntity);
}
