package com.bag.complaint_system.complaint.application.mapper;

import com.bag.complaint_system.complaint.application.dto.response.ComplaintDetailResponse;
import com.bag.complaint_system.complaint.application.dto.response.ComplaintResponse;
import com.bag.complaint_system.complaint.domain.aggregate.Complaint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.IntStream;

@Mapper(
    componentModel = "spring",
    uses = {EvidenceMapper.class, AggressorMapper.class})
public interface ComplaintMapper {

  @Mapping(target = "evidenceCount", source = "complaint", qualifiedByName = "getEvidenceCount")
  ComplaintResponse toComplaintResponse(Complaint complaint, String victimName);

  @Mapping(target = "victimName", source = "victimName")
  @Mapping(target = "victimEmail", source = "victimEmail")
  ComplaintDetailResponse toDetailResponse(
      Complaint complaint, String victimName, String victimEmail);

  @Named("getEvidenceCount")
  default int getEvidenceCount(Complaint complaint) {
    return complaint.getEvidenceCount();
  }

  default List<ComplaintResponse> toComplaintResponseList(
      List<Complaint> complaints, List<String> victimNames) {

    return IntStream.range(0, complaints.size())
        .mapToObj(
            index -> {
              Complaint complaint = complaints.get(index);
              String victimName = index < victimNames.size() ? victimNames.get(index) : "Unknow";

              return toComplaintResponse(complaint, victimName);
            })
        .toList();
  }
}
