package com.microops.user.config;

import com.microops.user.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouteConfig {

    @Bean
    public RouterFunction<ServerResponse> userRoutes(UserHandler userHandler) {
        return RouterFunctions
                .route(POST("/user/v1/create"), userHandler::createUser)
                .andRoute(GET("/user/v1/all"), userHandler::getAllUsers)
                .andRoute(GET("/user/v1/search"), userHandler::searchUsers)
                .andRoute(GET("/user/v1/{id}"), userHandler::getUserById)
                .andRoute(PUT("/user/v1/{id}"), userHandler::updateUser)
                .andRoute(DELETE("/user/v1/{id}"), userHandler::deleteUser);
    }


}
