package com.ricardocreates.application.service;

import com.ricardocreates.domain.entity.administrator.LoginRequest;
import com.ricardocreates.domain.entity.administrator.LoginResponse;
import com.ricardocreates.domain.entity.exceptions.UnauthorizedException;
import com.ricardocreates.domain.repository.administrator.AdministratorRepository;
import com.ricardocreates.domain.service.GetLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class GetLoginImpl implements GetLoginService {

    private final AdministratorRepository administratorMongoRepository;

    private final PBKDF2Encoder passwordEncoder;

    private final JWTUtil jwtUtil;

    @Override
    public Mono<LoginResponse> get(LoginRequest loginRequest) {
        return administratorMongoRepository.getLogin(loginRequest.getLogin())
                .filter(loginResponse -> passwordEncoder.encode(loginRequest.getPassword()).equals(loginResponse.getPassword()))
                .map(loginResponse -> loginResponse.toBuilder().token(jwtUtil.generateToken(loginResponse)).build())
                .switchIfEmpty(Mono.error(new UnauthorizedException("This user or password is incorrect.")));
    }
}

