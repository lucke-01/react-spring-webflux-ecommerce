package com.ricardocreates.domain.entity.exceptions;

public class NotFoundException extends BaseException {

    public NotFoundException(final String key, final String description, final Throwable cause) {
        super(key, description, cause);
    }

    public NotFoundException(final String key, final String description) {
        super(key, description);
    }

    public NotFoundException(final String key, final Throwable cause) {
        super(key, cause);
    }

    public NotFoundException(final String description) {
        super(ExceptionKeys.NOT_FOUND, description);
    }

}
