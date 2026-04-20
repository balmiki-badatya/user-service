package com.microops.user.service;

import com.microops.user.dto.UserRequest;
import com.microops.user.entity.User;
import com.microops.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        userRequest = UserRequest.builder()
                .username("jdoe")
                .email("jdoe@example.com")
                .firstName("John")
                .lastName("Doe")
                .build();

        user = User.builder()
                .id(1L)
                .username("jdoe")
                .email("jdoe@example.com")
                .firstName("John")
                .lastName("Doe")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createUser_savesAndReturnsUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        StepVerifier.create(userService.createUser(userRequest))
                .expectNextMatches(u -> u.getId().equals(1L) && u.getUsername().equals("jdoe"))
                .verifyComplete();

        verify(userRepository).save(any(User.class));
    }

    @Test
    void getUserById_returnsUserWhenFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        StepVerifier.create(userService.getUserById(1L))
                .expectNextMatches(u -> u.getEmail().equals("jdoe@example.com"))
                .verifyComplete();
    }

    @Test
    void getUserById_returnsEmptyWhenNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        StepVerifier.create(userService.getUserById(99L))
                .verifyComplete();
    }

    @Test
    void getAllUsers_returnsAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        StepVerifier.create(userService.getAllUsers())
                .expectNextMatches(u -> u.getId().equals(1L))
                .verifyComplete();
    }

    @Test
    void deleteUserById_completesSuccessfully() {
        doNothing().when(userRepository).deleteById(1L);

        StepVerifier.create(userService.deleteUserById(1L))
                .verifyComplete();

        verify(userRepository).deleteById(1L);
    }
}
