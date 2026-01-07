package com.bag.complaint_system.identity.infrastructure.adapters;

import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.identity.domain.model.valueobject.Email;
import com.bag.complaint_system.identity.infrastructure.adapters.output.persistence.entity.UserEntity;
import com.bag.complaint_system.identity.infrastructure.adapters.output.mapper.UserPersistenceMapper;
import com.bag.complaint_system.identity.infrastructure.adapters.output.persistence.repository.JpaUserRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

  @Mock private JpaUserRepository jpaUserRepository;
  @Mock private UserPersistenceMapper mapper;

  @InjectMocks private UserRepositoryAdapter userRepositoryAdapter;

  @Test
  void givenValidUser_whenSave_thenPersistenceAndReturnMappedUser() {
    User user = TestUtils.buildUserVictimMock();
    UserEntity entity = TestUtils.buildUserEntityVictimMock();

    when(mapper.toUserEntity(any(User.class))).thenReturn(entity);
    when(jpaUserRepository.save(any(UserEntity.class))).thenReturn(entity);
    when(mapper.toUser(any(UserEntity.class))).thenReturn(user);

    User savedUser = userRepositoryAdapter.save(user);

    assertNotNull(savedUser);
    assertEquals(user, savedUser);

    verify(mapper, times(1)).toUserEntity(user);
    verify(jpaUserRepository, times(1)).save(entity);
    verify(mapper, times(1)).toUser(entity);
  }

  @Test
  void givenExistingUserId_whenFindById_thenReturnMappedUser() {
    User userExpected = TestUtils.buildUserVictimMock();
    UserEntity entity = TestUtils.buildUserEntityVictimMock();

    when(jpaUserRepository.findById(anyLong())).thenReturn(Optional.of(entity));
    when(mapper.toUser(entity)).thenReturn(userExpected);

    Optional<User> user = userRepositoryAdapter.findById(1L);

    assertTrue(user.isPresent());
    assertEquals(userExpected, user.get());
    assertEquals(1L, user.get().getId());

    verify(jpaUserRepository, times(1)).findById(1L);
    verify(mapper, times(1)).toUser(entity);
  }

  @Test
  void givenNonExistingUserId_whenFindById_thenReturnEmptyOptional() {

    when(jpaUserRepository.findById(anyLong())).thenReturn(Optional.empty());

    Optional<User> user = userRepositoryAdapter.findById(1L);

    assertAll(() -> assertFalse(user.isPresent()), () -> assertTrue(user.isEmpty()));

    verify(jpaUserRepository, times(1)).findById(1L);
    verify(mapper, never()).toUser(any());
  }

  @Test
  void givenExistingUser_whenFindByEmail_thenReturnMappedUser() {
    User userExpected = TestUtils.buildUserVictimMock();
    UserEntity entity = TestUtils.buildUserEntityVictimMock();
    Email email = new Email("p.garcia@example.com");
    when(jpaUserRepository.findByEmail(any())).thenReturn(Optional.of(entity));
    when(mapper.toUser(entity)).thenReturn(userExpected);

    Optional<User> user = userRepositoryAdapter.findByEmail(email);

    assertTrue(user.isPresent());
    assertEquals(userExpected, user.get());
    assertEquals(1L, user.get().getId());

    verify(jpaUserRepository, times(1)).findByEmail(email.getValue());
    verify(mapper, times(1)).toUser(entity);
  }

  @Test
  void givenNonExistingUser_whenFindByEmail_thenReturnEmptyOptional() {

    Email email = new Email("p.garcia@example.com");
    when(jpaUserRepository.findByEmail(any())).thenReturn(Optional.empty());

    Optional<User> user = userRepositoryAdapter.findByEmail(email);

    assertAll(() -> assertFalse(user.isPresent()), () -> assertTrue(user.isEmpty()));

    verify(jpaUserRepository, times(1)).findByEmail(email.getValue());
    verify(mapper, never()).toUser(any());
  }

  @Test
  void givenExistingEmail_whenExistsByEmail_thenReturnTrue() {
    Email email = new Email("p.garcia@example.com");

    when(jpaUserRepository.existsByEmail(any())).thenReturn(Boolean.TRUE);

    boolean exists = userRepositoryAdapter.existsByEmail(email);

    assertTrue(exists);

    verify(jpaUserRepository, times(1)).existsByEmail(email.getValue());
  }

  @Test
  void givenExistingStudentsActive_whenFindAllActive_thenReturnAllMappedUsers() {

    List<UserEntity> userEntityList =
        Collections.singletonList(TestUtils.buildUserEntityVictimMock());

    List<User> userList = Collections.singletonList(TestUtils.buildUserVictimMock());

    when(jpaUserRepository.findAllByIsActive(anyBoolean())).thenReturn(userEntityList);
    when(mapper.toUserList(anyList())).thenReturn(userList);

    List<User> users = userRepositoryAdapter.findAllActive();

    assertFalse(users.isEmpty());
    assertEquals(1, users.size());

    verify(jpaUserRepository, times(1)).findAllByIsActive(true);
    verify(mapper, times(1)).toUserList(userEntityList);
  }

  @Test
  void givenNonExistingStudentsActive_whenFindAllActive_thenReturnEmptyList() {

    when(jpaUserRepository.findAllByIsActive(anyBoolean())).thenReturn(Collections.emptyList());
    when(mapper.toUserList(Collections.emptyList())).thenReturn(Collections.emptyList());
    List<User> users = userRepositoryAdapter.findAllActive();

    assertNotNull(users);
    assertTrue(users.isEmpty());

    verify(jpaUserRepository, times(1)).findAllByIsActive(true);
    verify(mapper, times(1)).toUserList(Collections.emptyList());
  }

  @Test
  void givenExistingUser_whenFindAlByRole_thenReturnListMappedUser() {
    List<UserEntity> userEntityList =
        Collections.singletonList(TestUtils.buildUserEntityVictimMock());

    List<User> userList = Collections.singletonList(TestUtils.buildUserVictimMock());

    when(jpaUserRepository.findAllByRole(any(UserEntity.RoleEntity.class)))
        .thenReturn(userEntityList);
    when(mapper.toUserList(anyList())).thenReturn(userList);

    List<User> users = userRepositoryAdapter.findAllByRole("ADMIN");

    assertFalse(users.isEmpty());
    assertEquals(1, users.size());

    verify(jpaUserRepository, times(1)).findAllByRole(UserEntity.RoleEntity.ADMIN);
    verify(mapper, times(1)).toUserList(userEntityList);
  }

  @Test
  void givenNonExistingUser_whenFindAlByRole_thenReturnEmptyList() {
    when(jpaUserRepository.findAllByRole(any(UserEntity.RoleEntity.class)))
        .thenReturn(Collections.emptyList());
    when(mapper.toUserList(Collections.emptyList())).thenReturn(Collections.emptyList());

    List<User> users = userRepositoryAdapter.findAllByRole("ADMIN");

    assertNotNull(users);
    assertTrue(users.isEmpty());

    verify(jpaUserRepository, times(1)).findAllByRole(UserEntity.RoleEntity.ADMIN);
    verify(mapper, times(1)).toUserList(Collections.emptyList());
  }

  @Test
  void givenValidUserIds_whenFindAllById_thenReturnMappedUsers() {
    List<UserEntity> userEntityList =
        Collections.singletonList(TestUtils.buildUserEntityVictimMock());

    List<User> userList = Collections.singletonList(TestUtils.buildUserVictimMock());

    when(jpaUserRepository.findAllById(anyList())).thenReturn(userEntityList);
    when(mapper.toUserList(anyList())).thenReturn(userList);

    List<User> users = userRepositoryAdapter.findAllById(Collections.singletonList(1L));

    assertFalse(users.isEmpty());
    assertEquals(1, users.size());

    verify(jpaUserRepository, times(1)).findAllById(Collections.singletonList(1L));
    verify(mapper, times(1)).toUserList(userEntityList);
  }

  @Test
  void givenEmptyUserIds_whenFindAllById_thenReturnEmptyList() {

    when(jpaUserRepository.findAllById(anyList())).thenReturn(Collections.emptyList());
    when(mapper.toUserList(Collections.emptyList())).thenReturn(Collections.emptyList());

    List<User> users = userRepositoryAdapter.findAllById(Collections.emptyList());

    assertNotNull(users);
    assertTrue(users.isEmpty());

    verify(jpaUserRepository, times(1)).findAllById(Collections.emptyList());
    verify(mapper, times(1)).toUserList(Collections.emptyList());
  }

  @Test
  void givenValidUserId_whenDeleteById_thenDeleteUser() {

    doNothing().when(jpaUserRepository).deleteById(anyLong());

    userRepositoryAdapter.deleteById(1L);

    verify(jpaUserRepository, times(1)).deleteById(1L);
  }
}
