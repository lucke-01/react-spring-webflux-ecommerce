package com.ricardocreates.domain.entity.exceptions;

public class ConflictException extends BaseException {

    public ConflictException(final String key, final String description, final Throwable cause) {
        super(key, description, cause);
    }

    public ConflictException(final String key, final String description) {
        super(key, description);
    }

    public ConflictException(final String key, final Throwable cause) {
        super(key, cause);
    }

    public ConflictException(final String description) {
        super(ExceptionKeys.CONFLICT, description);
    }

}
