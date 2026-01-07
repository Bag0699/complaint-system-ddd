package com.bag.complaint_system.complaint.infrastructure.adapters.output.mapper;

import com.bag.complaint_system.complaint.domain.entity.Evidence;
import com.bag.complaint_system.complaint.infrastructure.adapters.output.persistence.entity.EvidenceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EvidencePersistenceMapper {

  EvidenceEntity toEvidenceEntity(Evidence evidence);

  Evidence toEvidence(EvidenceEntity evidenceEntity);
}
