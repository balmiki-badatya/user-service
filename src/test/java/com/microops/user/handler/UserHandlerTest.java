package com.microops.user.handler;

import com.microops.user.config.RouteConfig;
import com.microops.user.dto.UserRequest;
import com.microops.user.entity.User;
import com.microops.user.service.UserService;

import org.springframework.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest
@Import({RouteConfig.class, UserHandler.class})
class UserHandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserService userService;

    private User sampleUser() {
        return User.builder()
                .id(1L)
                .username("jdoe")
                .email("jdoe@example.com")
                .firstName("John")
                .lastName("Doe")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createUser_returns201() {
        when(userService.createUser(any(UserRequest.class))).thenReturn(Mono.just(sampleUser()));

        UserRequest request = UserRequest.builder()
                .username("jdoe")
                .email("jdoe@example.com")
                .firstName("John")
                .lastName("Doe")
                .build();

        webTestClient.post().uri("/user/v1/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.status").isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void getUserById_returns200WhenFound() {
        when(userService.getUserById(1L)).thenReturn(Mono.just(sampleUser()));

        webTestClient.get().uri("/user/v1/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo(HttpStatus.OK);
    }

    @Test
    void getUserById_returns404WhenNotFound() {
        when(userService.getUserById(99L)).thenReturn(Mono.empty());

        webTestClient.get().uri("/user/v1/99")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errorMessage").isEqualTo("User not found for id: 99");
    }

    @Test
    void getAllUsers_returns200WithList() {
        when(userService.getAllUsers()).thenReturn(Flux.just(sampleUser()));

        webTestClient.get().uri("/user/v1/all")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .hasSize(1);
    }

    @Test
    void deleteUser_returns200() {
        when(userService.deleteUserById(1L)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/user/v1/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.body").isEqualTo("User deleted successfully for id: 1");
    }
}
