package com.payment.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ApiError {

    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
    private List<String> details;
}
