package com.ricardocreates.domain.entity.exceptions;

public final class ExceptionKeys {

    // keys de errores comunes (genericas)
    public static final String NOT_FOUND = "not_found";

    public static final String SERVICE_UNAVAILABLE = "service_unavailable";

    public static final String BAD_REQUEST = "bad_request";

    public static final String INTERNAL_SERVER_ERROR = "internal_server_error";

    public static final String CONFLICT = "conflict";

    public static final String NOT_UNAUTHORIZED = "unauthorized";

    private ExceptionKeys() {
        super();
    }
}
