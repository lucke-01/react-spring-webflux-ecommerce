package com.ricardocreates.domain.entity.exceptions;

public class BaseException extends RuntimeException {

    private final String key;

    private final String description;

    public BaseException(final String key) {
        this(key, "");
    }

    public BaseException(final String key, final String description) {
        super(key);
        this.key = key;
        this.description = description;
    }

    public BaseException(final String key, final Throwable cause) {
        super(key, cause);
        this.key = key;
        this.description = null;
    }

    public BaseException(final String key, final String description, final Throwable cause) {
        super(key, cause);
        this.key = key;
        this.description = description;
    }

    public static BaseException conflictException(final String errors) {
        return new BaseException(ExceptionKeys.CONFLICT, errors);
    }

    public String getKey() {
        return this.key;
    }

    public String getDescription() {
        return this.description;
    }

}
