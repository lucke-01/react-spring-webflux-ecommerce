package com.ricardocreates.domain.entity.administrator;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Builder(toBuilder = true)
@Data
public class Administrator {

    private String id;

    private String login;

    private String password;

    private String email;
    /**
     * random token send to user email and use to restore password
     */
    private String forgetPasswordToken;

    private Instant lastLogin;

    private AdminRole role;

}
