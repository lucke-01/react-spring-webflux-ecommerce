package com.ricardocreates.domain.entity.exceptions;

public class ServiceUnavailableException extends BaseException {

    public ServiceUnavailableException(final String key, final String description, final Throwable cause) {
        super(key, description, cause);
    }

    public ServiceUnavailableException(final String key, final String description) {
        super(key, description);
    }

    public ServiceUnavailableException(final String key, final Throwable cause) {
        super(key, cause);
    }

    public ServiceUnavailableException(final String key) {
        super(key);
    }

}
