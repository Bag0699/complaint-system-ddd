package com.bag.complaint_system.complaint.application.service;

import com.bag.complaint_system.complaint.application.dto.response.ComplaintDetailResponse;
import com.bag.complaint_system.complaint.application.mapper.ComplaintMapper;
import com.bag.complaint_system.complaint.application.ports.output.ComplaintPersistencePort;
import com.bag.complaint_system.complaint.domain.aggregate.Complaint;
import com.bag.complaint_system.identity.application.ports.output.UserPersistencePort;
import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.shared.exception.ComplaintNotFoundException;
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
class GetComplaintDetailServiceTest {
  @Mock private ComplaintPersistencePort complaintPersistencePort;
  @Mock private UserPersistencePort userPersistencePort;
  @Mock private ComplaintMapper complaintMapper;

  @InjectMocks private GetComplaintDetailService getComplaintDetailService;

  private User mockVictim;
  private User mockAdmin;
  private Complaint mockComplaint;
  private ComplaintDetailResponse expectedResponse;

  @BeforeEach
  void setUp() {
    mockVictim = TestUtils.buildUserVictimMock();
    mockVictim.setId(2L);

    mockAdmin = TestUtils.buildUserAdminMock();

    mockComplaint = TestUtils.buildComplaintMock(2L);
    mockComplaint.setId(1L);

    expectedResponse = ComplaintDetailResponse.builder().id(1L).victimId(2L).build();
  }

  @Test
  void givenUserAdminAndExistingComplaint_whenExecute_thenReturnComplaintDetail() {

    when(complaintPersistencePort.findById(1L)).thenReturn(Optional.of(mockComplaint));
    when(userPersistencePort.findById(1L)).thenReturn(Optional.of(mockAdmin));
    when(userPersistencePort.findById(2L)).thenReturn(Optional.of(mockVictim));
    when(complaintMapper.toDetailResponse(any(Complaint.class), anyString(), anyString()))
        .thenReturn(expectedResponse);

    ComplaintDetailResponse response = getComplaintDetailService.execute(1L, 1L);

    assertNotNull(response);
    assertEquals(expectedResponse.getId(), response.getId());

    verify(complaintPersistencePort, times(1)).findById(1L);
    verify(userPersistencePort, times(1)).findById(1L);
    verify(userPersistencePort, times(1)).findById(2L);
    verify(complaintMapper, times(1)).toDetailResponse(eq(mockComplaint), anyString(), anyString());
  }

  @Test
  void givenOwenUserAndExistingComplaint_whenExecute_thenReturnComplaintDetail() {
    when(complaintPersistencePort.findById(1L)).thenReturn(Optional.of(mockComplaint));
    when(userPersistencePort.findById(2L)).thenReturn(Optional.of(mockVictim));
    when(userPersistencePort.findById(2L)).thenReturn(Optional.of(mockVictim));
    when(complaintMapper.toDetailResponse(any(Complaint.class), anyString(), anyString()))
        .thenReturn(expectedResponse);

    ComplaintDetailResponse response = getComplaintDetailService.execute(1L, 2L);

    assertNotNull(response);
    assertEquals(expectedResponse.getId(), response.getId());
    assertEquals(expectedResponse.getVictimId(), response.getVictimId());

    verify(complaintPersistencePort, times(1)).findById(1L);
    verify(userPersistencePort, times(2)).findById(2L);
    verify(complaintMapper, times(1)).toDetailResponse(eq(mockComplaint), anyString(), anyString());
  }

  @Test
  void givenNonExistentComplaint_whenExecute_thenThrowComplaintNotFoundException() {
    when(complaintPersistencePort.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ComplaintNotFoundException.class, () -> getComplaintDetailService.execute(1L, 1L));

    verify(complaintPersistencePort, times(1)).findById(1L);
    verify(userPersistencePort, never()).findById(anyLong());
    verify(complaintMapper, never())
        .toDetailResponse(any(Complaint.class), anyString(), anyString());
  }

  @Test
  void givenNonExistentAuthUser_whenExecute_thenThrowNotFoundException() {
    when(complaintPersistencePort.findById(1L)).thenReturn(Optional.of(mockComplaint));
    when(userPersistencePort.findById(1L)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> getComplaintDetailService.execute(1L, 1L));

    verify(complaintPersistencePort, times(1)).findById(1L);
    verify(userPersistencePort, times(1)).findById(1L);
    verify(complaintMapper, never())
        .toDetailResponse(any(Complaint.class), anyString(), anyString());
  }

  @Test
  void givenNonAdminNonOwnerUser_whenExecute_thenThrowRoleAccessDeniedException() {
    User authNonOwner = TestUtils.buildUserVictimMock();

    when(complaintPersistencePort.findById(1L)).thenReturn(Optional.of(mockComplaint));
    when(userPersistencePort.findById(1L)).thenReturn(Optional.of(authNonOwner));

    assertThrows(RoleAccessDeniedException.class, () -> getComplaintDetailService.execute(1L, 1L));

    verify(complaintPersistencePort, times(1)).findById(1L);
    verify(userPersistencePort, times(1)).findById(1L);
    verify(complaintMapper, never())
        .toDetailResponse(any(Complaint.class), anyString(), anyString());
  }
}
