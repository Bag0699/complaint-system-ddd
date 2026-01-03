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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllComplaintsServiceTest {
  @Mock private ComplaintPersistencePort complaintPersistencePort;
  @Mock private UserPersistencePort userPersistencePort;
  @Mock private ComplaintMapper complaintMapper;

  @InjectMocks private GetAllComplaintsService getAllComplaintsService;



  @Test
  void givenUserAdminAndExistingComplaints_whenExecute_thenReturnComplaintsList() {
    User authAdmin = TestUtils.buildUserAdminMock();

    User victim1 = TestUtils.buildUserVictimMock();
    User victim2 = TestUtils.buildUserVictimMock();
    victim1.setId(2L);
    victim2.setId(3L);

    Complaint complaint1 = TestUtils.buildComplaintMock(2L);
    Complaint complaint2 = TestUtils.buildComplaintMock(3L);
    List<Complaint> mockComplaints = List.of(complaint1, complaint2);
    List<ComplaintResponse> expectedResponse =
        List.of(new ComplaintResponse(), new ComplaintResponse());

    when(userPersistencePort.findById(1L)).thenReturn(Optional.of(authAdmin));
    when(complaintPersistencePort.findAll()).thenReturn(mockComplaints);

    List<User> foundVictims = List.of(victim1, victim2);

    when(userPersistencePort.findAllById(List.of(2L, 3L))).thenReturn(foundVictims);
    when(complaintMapper.toComplaintResponseList(anyList(), anyList()))
        .thenReturn(expectedResponse);

    List<ComplaintResponse> response = getAllComplaintsService.execute(1L);

    assertNotNull(response);
    assertEquals(2, response.size());

    verify(userPersistencePort, times(1)).findById(1L);
    verify(complaintPersistencePort, times(1)).findAll();
    verify(userPersistencePort, times(1)).findAllById(List.of(2L, 3L));
    verify(complaintMapper, times(1)).toComplaintResponseList(anyList(), anyList());
  }

  @Test
  void givenNonExistenUser_whenExecute_thenThrowUserNotFoundException() {

    when(userPersistencePort.findById(1L)).thenReturn(Optional.empty());
    assertThrows(UserNotFoundException.class, () -> getAllComplaintsService.execute(1L));

    verify(complaintPersistencePort, never()).findAll();
    verify(complaintMapper, never()).toComplaintResponseList(anyList(), anyList());
  }

  @Test
  void givenUserNonAdmin_whenExecute_thenThrowRoleAccessDeniedException() {
    User authNonAdmin = TestUtils.buildUserVictimMock();

    when(userPersistencePort.findById(1L)).thenReturn(Optional.of(authNonAdmin));
    assertThrows(RoleAccessDeniedException.class, () -> getAllComplaintsService.execute(1L));

    verify(complaintPersistencePort, never()).findAll();
    verify(complaintMapper, never()).toComplaintResponseList(anyList(), anyList());
  }
}
