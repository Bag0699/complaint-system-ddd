package com.bag.complaint_system.complaint.application.service;

import com.bag.complaint_system.complaint.application.dto.response.ComplaintResponse;
import com.bag.complaint_system.complaint.application.mapper.ComplaintMapper;
import com.bag.complaint_system.complaint.application.ports.output.ComplaintPersistencePort;
import com.bag.complaint_system.complaint.domain.aggregate.Complaint;
import com.bag.complaint_system.identity.application.ports.output.UserPersistencePort;
import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.shared.exception.RoleAccessDeniedException;
import com.bag.complaint_system.shared.exception.UserNotFoundException;
import com.bag.complaint_system.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetComplaintsByVictimServiceTest {
  @Mock private ComplaintPersistencePort complaintPersistencePort;
  @Mock private UserPersistencePort userPersistencePort;
  @Mock private ComplaintMapper complaintMapper;

  @InjectMocks private GetComplaintsByVictimService getComplaintsByVictimService;

  @Test
  void givenVictimUserAndExistingComplaints_whenExecute_thenReturnComplaintsList() {
    User victimUser = TestUtils.buildUserVictimMock();

    Complaint complaint1 = TestUtils.buildComplaintMock(1L);
    Complaint complaint2 = TestUtils.buildComplaintMock(1L);
    List<Complaint> mockComplaints = List.of(complaint1, complaint2);
    ComplaintResponse expectedResponse = new ComplaintResponse();

    when(userPersistencePort.findById(1L)).thenReturn(Optional.of(victimUser));
    when(complaintPersistencePort.findByVictimId(1L)).thenReturn(mockComplaints);
    when(complaintMapper.toComplaintResponse(any(Complaint.class), anyString()))
        .thenReturn(expectedResponse);

    List<ComplaintResponse> response = getComplaintsByVictimService.execute(1L);

    assertNotNull(response);
    assertEquals(2, response.size());

    verify(userPersistencePort, times(1)).findById(1L);
    verify(complaintPersistencePort, times(1)).findByVictimId(1L);
    verify(complaintMapper, times(2)).toComplaintResponse(any(Complaint.class), anyString());
  }

  @Test
  void givenVictimUserAndNoExistingComplaints_whenExecute_thenReturnEmptyList() {
    User victimUser = TestUtils.buildUserVictimMock();

    when(userPersistencePort.findById(1L)).thenReturn(Optional.of(victimUser));
    when(complaintPersistencePort.findByVictimId(1L)).thenReturn(Collections.emptyList());

    List<ComplaintResponse> response = getComplaintsByVictimService.execute(1L);

    assertNotNull(response);
    assertTrue(response.isEmpty());

    verify(userPersistencePort, times(1)).findById(1L);
    verify(complaintPersistencePort, times(1)).findByVictimId(1L);
    verify(complaintMapper, never()).toComplaintResponse(any(), anyString());
  }

  @Test
  void givenNonExistentVictimUser_whenExecute_thenThrowUserNotFoundException() {

    when(userPersistencePort.findById(1L)).thenReturn(Optional.empty());
    assertThrows(UserNotFoundException.class, () -> getComplaintsByVictimService.execute(1L));

    verify(userPersistencePort, times(1)).findById(1L);
    verify(complaintPersistencePort, never()).findByVictimId(anyLong());
    verify(complaintMapper, never()).toComplaintResponse(any(), anyString());
  }

  @Test
  void givenUserNotVictim_whenExecute_thenReturnThrowRoleAccessDeniedException() {
    User adminUser = TestUtils.buildUserAdminMock();

    when(userPersistencePort.findById(1L)).thenReturn(Optional.of(adminUser));
    assertThrows(RoleAccessDeniedException.class, () -> getComplaintsByVictimService.execute(1L));

    verify(userPersistencePort, times(1)).findById(1L);
    verify(complaintPersistencePort, never()).findByVictimId(anyLong());
    verify(complaintMapper, never()).toComplaintResponse(any(), anyString());
  }
}
