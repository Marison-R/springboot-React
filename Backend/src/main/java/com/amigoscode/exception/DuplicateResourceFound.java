package com.amigoscode.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicateResourceFound extends RuntimeException {
    public DuplicateResourceFound(String message) {
        super(message);
    }
}
