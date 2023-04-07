package com.ricardocreates.domain.entity.exceptions;

public class UnauthorizedException extends BaseException {

    public UnauthorizedException(final String key, final String description, final Throwable cause) {
        super(key, description, cause);
    }

    public UnauthorizedException(final String key, final String description) {
        super(key, description);
    }

    public UnauthorizedException(final String key, final Throwable cause) {
        super(key, cause);
    }

    public UnauthorizedException(final String description) {
        super(ExceptionKeys.NOT_UNAUTHORIZED, description);
    }

}
