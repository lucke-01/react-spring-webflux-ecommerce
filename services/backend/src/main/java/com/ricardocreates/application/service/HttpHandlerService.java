package com.ricardocreates.application.service;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

/**
 * class to help to work with http calls
 */
@Service
public class HttpHandlerService {
    /**
     * returns schema and host example: "https://ricardocreates.com/ecommerce"
     *
     * @param request a request
     * @return a host
     */
    public String getHost(ServerHttpRequest request) {
        return String.format("%s://%s", request.getURI().getScheme(), request.getURI().getHost());
    }
}
