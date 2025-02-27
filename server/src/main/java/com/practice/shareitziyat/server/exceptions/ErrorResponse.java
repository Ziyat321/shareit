package com.practice.shareitziyat.server.exceptions;

import lombok.Data;

@Data
public class ErrorResponse {
    private final String error;
    private final String description;
}