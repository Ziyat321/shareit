package com.practice.shareitziyat.exceptions;

import lombok.Data;

@Data
public class ErrorResponse {
    private final String error;
    private final String description;
}