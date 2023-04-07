package com.ricardocreates;

import com.ricardocreates.application.service.JWTUtil;
import com.ricardocreates.domain.entity.administrator.AdminRole;
import com.ricardocreates.domain.entity.administrator.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

/**
 * common test configuration in Spring boot test
 */
public abstract class SpringTestContext {
    protected static final String NO_ROLE = "NONE";

    protected static final String DEFAULT_LOGIN = "admin";
    @Autowired
    private JWTUtil jwtTokenProvider;

    /**
     * load properties in spring programmaticly
     *
     * @param registry - registry
     * @throws IOException exception
     */
    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) throws IOException {
        //give a random available port and set in spring properties
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            registry.add("spring.data.mongodb.port", serverSocket::getLocalPort);
        }
    }

    protected String getDefaultToken() {
        return getDefaultToken(AdminRole.ADMIN);
    }

    protected String getDefaultToken(AdminRole adminRole) {
        return jwtTokenProvider.generateToken(LoginResponse.builder()
                .login(DEFAULT_LOGIN).password("pass").roles(List.of(adminRole.toString())).build());
    }

    protected AdminRole getRoleFromString(String role) {
        return !NO_ROLE.equals(role) ? AdminRole.valueOf(role) : null;
    }

}
