package com.ricardocreates.infra.server.rest.controller.v1.api.administrator;

import com.ricardocreates.application.service.HttpHandlerService;
import com.ricardocreates.domain.service.GetLoginService;
import com.ricardocreates.domain.usecase.administrator.AddAdministratorUseCase;
import com.ricardocreates.domain.usecase.administrator.GetAdministratorUseCase;
import com.ricardocreates.domain.usecase.administrator.RemoveAdministratorUseCase;
import com.ricardocreates.domain.usecase.administrator.RestorePassword;
import com.ricardocreates.domain.usecase.administrator.SendForgotPasswordAdministrator;
import com.ricardocreates.domain.usecase.administrator.UpdateAdministratorUseCase;
import com.ricardocreates.infra.server.rest.controller.v1.api.administrator.mapper.AdministratorDtoMapper;
import com.ricardocreates.domain.entity.administrator.Administrator;
import com.swagger.client.codegen.rest.AdministratorApi;
import com.swagger.client.codegen.rest.model.AdministratorDto;
import com.swagger.client.codegen.rest.model.LoginRequestDto;
import com.swagger.client.codegen.rest.model.LoginResponseDto;
import com.swagger.client.codegen.rest.model.RestorePasswordDto;
import com.swagger.client.codegen.rest.model.SendForgotPasswordDto;
import com.swagger.client.codegen.rest.model.UpdateAdministratorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdministratorController implements AdministratorApi {

    private final GetAdministratorUseCase getAdministratorUseCase;

    private final AddAdministratorUseCase addAdministratorUseCase;

    private final RemoveAdministratorUseCase removeAdministratorUseCase;

    private final UpdateAdministratorUseCase updateAdministratorUseCase;
    private final SendForgotPasswordAdministrator sendForgotPasswordAdministrator;
    private final RestorePassword restorePassword;
    private final GetLoginService userService;

    private final AdministratorDtoMapper administratorDtoMapper;
    private final HttpHandlerService httpHandlerService;

    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Mono<ResponseEntity<AdministratorDto>> addAdministrator(Mono<AdministratorDto> administrator, ServerWebExchange exchange) {
        return administrator.map(administratorDtoMapper::toDomain)
                .flatMap((admin) -> addAdministratorUseCase.add(
                        admin.toBuilder()
                                .build()))
                .map(administratorDtoMapper::fromDomain)
                .map(ResponseEntity::ok);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Mono<ResponseEntity<Flux<AdministratorDto>>> findAdministrator(ServerWebExchange exchange) {
        return Mono.just(getAdministratorUseCase.find(Administrator.builder().build()))
                .map(administratorDtoMapper::fromDomain)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<AdministratorDto>> getAdministrator(String id, ServerWebExchange exchange) {
        return getAdministratorUseCase.get(id)
                .map(administratorDtoMapper::fromDomain)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<LoginResponseDto>> login(Mono<LoginRequestDto> loginRequestDto, ServerWebExchange exchange) {
        return loginRequestDto.map(administratorDtoMapper::toDomain)
                .flatMap(userService::get)
                .map(administratorDtoMapper::fromDomain)
                .map(ResponseEntity::ok);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Mono<ResponseEntity<Void>> removeAdministrator(String id, ServerWebExchange exchange) {
        return removeAdministratorUseCase.remove(id)
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Mono<ResponseEntity<AdministratorDto>> updateAdministrator(Mono<UpdateAdministratorDto> administrator, ServerWebExchange exchange) {
        return administrator.map(administratorDtoMapper::updateAdministratorDtoToDomain)
                .flatMap((admin) -> updateAdministratorUseCase.update(
                        admin.toBuilder()
                                .build()))
                .map(administratorDtoMapper::fromDomain)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Void>> sendForgotPassword(Mono<SendForgotPasswordDto> sendForgotPasswordDto, ServerWebExchange exchange) {
        final String host = httpHandlerService.getHost(exchange.getRequest());
        return sendForgotPasswordDto.map(SendForgotPasswordDto::getEmail)
                .flatMap(email -> sendForgotPasswordAdministrator.sendForgotPassword(host, email))
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Void>> restorePassword(Mono<RestorePasswordDto> restorePasswordDto, ServerWebExchange exchange) {
        return restorePasswordDto
                .flatMap(restorePasswordVar -> restorePassword
                        .restorePassword(restorePasswordVar.getLogin(), restorePasswordVar.getPasswordToken(), restorePasswordVar.getNewPassword()))
                .map(ResponseEntity::ok);
    }
}
