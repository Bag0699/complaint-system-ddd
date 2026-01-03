package com.bag.complaint_system.complaint.application.service;

import com.bag.complaint_system.complaint.application.dto.request.CreateComplaintRequest;
import com.bag.complaint_system.complaint.application.dto.response.ComplaintDetailResponse;
import com.bag.complaint_system.complaint.application.mapper.ComplaintMapper;
import com.bag.complaint_system.complaint.application.ports.output.ComplaintPersistencePort;
import com.bag.complaint_system.complaint.domain.aggregate.Complaint;
import com.bag.complaint_system.identity.application.ports.output.UserPersistencePort;
import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.shared.exception.RoleAccessDeniedException;
import com.bag.complaint_system.shared.exception.UserNotFoundException;
import com.bag.complaint_system.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateComplaintServiceTest {

  @Mock private ComplaintPersistencePort complaintPersistencePort;
  @Mock private UserPersistencePort userPersistencePort;
  @Mock private ComplaintMapper complaintMapper;

  @InjectMocks private CreateComplaintService createComplaintService;

  private CreateComplaintRequest request;
  private ComplaintDetailResponse expectedResponse;

  @BeforeEach
  void setUp() {
    request = TestUtils.buildCreateComplaintRequestMock();
    expectedResponse = new ComplaintDetailResponse();
    expectedResponse.setId(1L);
  }

  @Test
  void givenValidVictimAndRequest_whenExecute_thenPersistAndReturnComplaintDetailResponse() {
    User victimUser = TestUtils.buildUserVictimMock();

    when(userPersistencePort.findById(1L)).thenReturn(Optional.of(victimUser));
    when(complaintPersistencePort.save(any(Complaint.class))).thenReturn(mock(Complaint.class));
    when(complaintMapper.toDetailResponse(any(Complaint.class), anyString(), anyString()))
        .thenReturn(expectedResponse);

    ComplaintDetailResponse response = createComplaintService.execute(1L, request);

    assertNotNull(response);
    assertEquals(expectedResponse.getId(), response.getId());

    verify(userPersistencePort, times(1)).findById(1L);
    verify(complaintPersistencePort, times(1)).save(any(Complaint.class));
    verify(complaintMapper, times(1))
        .toDetailResponse(any(Complaint.class), eq("Pedro Garcia"), eq("p.garcia@example.com"));
  }

  @Test
  void givenUserIsNotVictim_whenExecute_thenThrowRoleAccessDeniedException() {
    User adminUser = TestUtils.buildUserAdminMock();

    when(userPersistencePort.findById(1L)).thenReturn(Optional.of(adminUser));
    assertThrows(
        RoleAccessDeniedException.class, () -> createComplaintService.execute(1L, request));

    verify(complaintPersistencePort, never()).save(any(Complaint.class));
    verify(complaintMapper, never())
        .toDetailResponse(any(Complaint.class), anyString(), anyString());
  }

  @Test
  void givenNonExistingUser_whenExecute_thenThrowUserNotFoundException() {
    when(userPersistencePort.findById(anyLong())).thenReturn(Optional.empty());
    assertThrows(UserNotFoundException.class, () -> createComplaintService.execute(1L, request));

    verify(complaintPersistencePort, never()).save(any(Complaint.class));
    verify(complaintMapper, never())
        .toDetailResponse(any(Complaint.class), anyString(), anyString());
  }
}
