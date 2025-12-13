package com.bag.complaint_system.complaint.infrastructure.adapters.output.mapper;

import com.bag.complaint_system.complaint.domain.aggregate.Complaint;
import com.bag.complaint_system.complaint.infrastructure.adapters.output.persistence.entity.ComplaintEntity;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {EvidencePersistenceMapper.class, AggressorPersistenceMapper.class})
public interface ComplaintPersistenceMapper {

  ComplaintEntity toComplaintEntity(Complaint complaint);

  Complaint toComplaint(ComplaintEntity complaintEntity);
}
