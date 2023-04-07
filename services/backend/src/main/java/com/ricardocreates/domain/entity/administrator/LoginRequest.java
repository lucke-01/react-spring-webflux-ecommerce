package com.ricardocreates.domain.entity.administrator;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class LoginRequest {

  private String login;

  private String password;

}
