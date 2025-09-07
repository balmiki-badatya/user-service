package com.microops.user.dto;

import lombok.*;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse<T> {
    private HttpStatusCode status;
    private String errorMessage;
    private T body;
}
