package com.ricardocreates.infra.server.rest.controller.v1.utils;

import com.ricardocreates.application.service.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class HttpUtil {
    private final JWTUtil jwtUtil;

    public String getBearer(ServerWebExchange exchange) {
        var headers = exchange.getRequest().getHeaders();
        var auth = Objects.requireNonNull(headers.get("Authorization")).get(0);
        return auth.replace("Bearer ", "");
    }

    public String getLogin(ServerWebExchange exchange) {
        String token = this.getBearer(exchange);
        return jwtUtil.getUsernameFromToken(token);
    }
}
