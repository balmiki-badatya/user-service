package com.microops.user.service;

import com.microops.user.dto.UserRequest;
import com.microops.user.entity.User;
import com.microops.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Mono<User> createUser(UserRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .createdAt(LocalDateTime.now())
                .build();
        return Mono.fromCallable(() -> userRepository.save(user))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSuccess(saved -> log.info("User created with id: {}", saved.getId()));
    }

    @Override
    public Mono<User> getUserById(Long id) {
        return Mono.fromCallable(() -> userRepository.findById(id).orElse(null))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSuccess(user -> log.info("Fetched user by id: {}", id));
    }

    @Override
    public Flux<User> getAllUsers() {
        return Flux.fromIterable(userRepository.findAll())
                .subscribeOn(Schedulers.boundedElastic())
                .doOnComplete(() -> log.info("Fetched all users"));
    }

    @Override
    public Mono<Void> deleteUserById(Long id) {
        return Mono.fromRunnable(() -> userRepository.deleteById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSuccess(v -> log.info("Deleted user with id: {}", id))
                .then();
    }
}
