package com.microops.user.service;

import com.microops.user.dto.UserRequest;
import com.microops.user.entity.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<User> createUser(UserRequest request);

    Mono<User> getUserById(Long id);

    Flux<User> getAllUsers();

    Mono<Void> deleteUserById(Long id);
}
