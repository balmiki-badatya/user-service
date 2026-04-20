package com.microops.user.handler;

import com.microops.user.dto.UserRequest;
import com.microops.user.dto.UserResponse;
import com.microops.user.entity.User;
import com.microops.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserHandler {

    private final UserService userService;

    public Mono<ServerResponse> createUser(ServerRequest serverRequest) {
        log.info("Create user invoked");
        return serverRequest.bodyToMono(UserRequest.class)
                .flatMap(userService::createUser)
                .flatMap(user -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(
                                UserResponse.builder()
                                        .status(HttpStatus.CREATED)
                                        .body(user)
                                        .build()
                        ), UserResponse.class)
                );
    }

    public Mono<ServerResponse> getUserById(ServerRequest serverRequest) {
        Long userId = Long.valueOf(serverRequest.pathVariable("id"));
        log.info("Get user by id invoked for id: {}", userId);
        return userService.getUserById(userId)
                .flatMap(user -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(
                                UserResponse.builder()
                                        .status(HttpStatus.OK)
                                        .body(user)
                                        .build()
                        ), UserResponse.class)
                )
                .switchIfEmpty(ServerResponse
                        .status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(
                                UserResponse.builder()
                                        .status(HttpStatus.NOT_FOUND)
                                        .errorMessage("User not found for id: " + userId)
                                        .build()
                        ), UserResponse.class)
                );
    }

    public Mono<ServerResponse> getAllUsers(ServerRequest serverRequest) {
        log.info("Get all users invoked");
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.getAllUsers(), User.class);
    }

    public Mono<ServerResponse> updateUser(ServerRequest serverRequest) {
        String userId = serverRequest.pathVariable("id");
        log.info("Update user invoked for id: {}", userId);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(
                        UserResponse.builder()
                                .status(HttpStatus.OK)
                                .body("User updated successfully for id: " + userId)
                                .build()
                ), UserResponse.class);
    }

    public Mono<ServerResponse> deleteUser(ServerRequest serverRequest) {
        Long userId = Long.valueOf(serverRequest.pathVariable("id"));
        log.info("Delete user invoked for id: {}", userId);
        return userService.deleteUserById(userId)
                .then(ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(
                                UserResponse.builder()
                                        .status(HttpStatus.OK)
                                        .body("User deleted successfully for id: " + userId)
                                        .build()
                        ), UserResponse.class)
                );
    }

    public Mono<ServerResponse> searchUsers(ServerRequest serverRequest) {
        String query = serverRequest.queryParam("q").orElse("");
        log.info("Search users invoked with query: {}", query);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(
                        UserResponse.builder()
                                .status(HttpStatus.OK)
                                .body("Search results for query: " + query)
                                .build()
                ), UserResponse.class);
    }
}
