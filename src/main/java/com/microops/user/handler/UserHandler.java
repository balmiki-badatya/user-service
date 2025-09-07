package com.microops.user.handler;

import com.microops.user.dto.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UserHandler {
    public Mono<ServerResponse> createUser(ServerRequest serverRequest) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono
                        .just(
                                UserResponse.builder().
                                        status(HttpStatus.CREATED)
                                        .body("User created successfully")
                                        .build()
                        ), UserResponse.class
                );
    }
}
