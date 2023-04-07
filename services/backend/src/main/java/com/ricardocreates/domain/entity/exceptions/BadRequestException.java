package com.ricardocreates.domain.entity.exceptions;

public class BadRequestException extends BaseException {

    public BadRequestException(final String key, final String description, final Throwable cause) {
        super(key, description, cause);
    }

    public BadRequestException(final String key, final String description) {
        super(key, description);
    }

    public BadRequestException(final String key, final Throwable cause) {
        super(key, cause);
    }

    public BadRequestException(final String description) {
        super(ExceptionKeys.BAD_REQUEST, description);
    }

}
