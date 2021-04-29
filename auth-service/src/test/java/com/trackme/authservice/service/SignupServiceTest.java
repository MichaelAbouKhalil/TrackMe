package com.trackme.authservice.service;

import com.trackme.authservice.Base;
import com.trackme.authservice.repository.RoleRepository;
import com.trackme.authservice.repository.UserRepository;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.constants.ConstantMessages;
import com.trackme.models.enums.PendingRoleEnum;
import com.trackme.models.exception.RoleNotFoundException;
import com.trackme.models.exception.UserAlreadyExistException;
import com.trackme.models.payload.request.signup.SignupRequest;
import com.trackme.models.security.RoleEntity;
import com.trackme.models.security.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SignupServiceTest extends Base {

    @Autowired
    SignupService signupService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    RoleRepository roleRepository;

    SignupRequest validRequest;
    RoleEntity role;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        validRequest = SignupRequest.builder()
                .username("a-username")
                .password("$2y$10$7wgYSzR3P.zdlEm3S5FyuO6uqHSDFlKezzQFZAFyCwcx2TdCePftK")
                .email("test@test.com")
                .role(PendingRoleEnum.PM_PENDING.name())
                .orgId("123")
                .build();

        role = RoleEntity.builder()
                .roleName("ROLE_" + PendingRoleEnum.PM_PENDING.name())
                .build();
    }

    @Test
    public void processSignup_Success() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
        when(roleRepository.findByRoleName(any(String.class))).thenReturn(Optional.of(role));
        when(userRepository.save(any(UserEntity.class))).thenReturn(
                UserEntity.builder()
                        .username(validRequest.getUsername())
                        .email(validRequest.getEmail())
                        .password(validRequest.getPassword())
                        .orgId(validRequest.getOrgId())
                        .role(role)
                        .build());

        CommonResponse commonResponse = signupService.processSignup(validRequest);

        assertTrue(commonResponse.isSuccess());
        assertEquals(HttpStatus.OK.value(), commonResponse.getStatus());
        assertEquals(ConstantMessages.SUCCESS, commonResponse.getMessage());
        assertNull(commonResponse.getError());
        assertNull(commonResponse.getPayload());
    }

    @Test
    public void processSignup_ExistingUsername() {
        when(userRepository.findByUsername(any(String.class)))
                .thenThrow(
                        new UserAlreadyExistException("Username [" + validRequest.getUsername() + "] already exists."));

        UserAlreadyExistException exception = assertThrows(
                UserAlreadyExistException.class,
                () -> signupService.processSignup(validRequest));

        assertEquals("Username [" + validRequest.getUsername() + "] already exists.",
                exception.getMessage());
    }

    @Test
    public void processSignup_ExistingEmail() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        when(userRepository.findByEmail(any(String.class)))
                .thenThrow(
                        new UserAlreadyExistException("Email [" + validRequest.getEmail() + "] already exists."));

        UserAlreadyExistException exception = assertThrows(
                UserAlreadyExistException.class,
                () -> signupService.processSignup(validRequest));

        assertEquals("Email [" + validRequest.getEmail() + "] already exists.",
                exception.getMessage());
    }

    @Test
    public void processSignup_NotExistingRole() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        when(roleRepository.findByRoleName(any(String.class)))
                .thenThrow(
                        new RoleNotFoundException("Requested Role [" + role.getRoleName() + "] not found."));

        RoleNotFoundException exception = assertThrows(
                RoleNotFoundException.class,
                () -> signupService.processSignup(validRequest));

        assertEquals("Requested Role [" + role.getRoleName() + "] not found.",
                exception.getMessage());
    }

    @Test
    public void processSignup_FailedDatabaseSave() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
        when(roleRepository.findByRoleName(any(String.class))).thenReturn(Optional.of(role));

        when(userRepository.save(any(UserEntity.class))).thenReturn(null);

        CommonResponse commonResponse = signupService.processSignup(validRequest);

        assertFalse(commonResponse.isSuccess());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), commonResponse.getStatus());
        assertEquals(ConstantMessages.ERROR, commonResponse.getMessage());
        assertNotNull(commonResponse.getError());
        assertEquals(ConstantMessages.USER_NOT_SAVED, commonResponse.getError().getErrorMessage());
        assertNull(commonResponse.getPayload());
    }

}