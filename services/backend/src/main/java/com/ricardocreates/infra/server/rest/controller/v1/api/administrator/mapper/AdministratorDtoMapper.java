package com.ricardocreates.infra.server.rest.controller.v1.api.administrator.mapper;

import com.ricardocreates.domain.entity.administrator.Administrator;
import com.ricardocreates.domain.entity.administrator.LoginRequest;
import com.ricardocreates.domain.entity.administrator.LoginResponse;
import com.swagger.client.codegen.rest.model.AdministratorDto;
import com.swagger.client.codegen.rest.model.LoginRequestDto;
import com.swagger.client.codegen.rest.model.LoginResponseDto;
import com.swagger.client.codegen.rest.model.UpdateAdministratorDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import reactor.core.publisher.Flux;

@Mapper(componentModel = "spring")
public interface AdministratorDtoMapper {

    default Flux<AdministratorDto> fromDomain(Flux<Administrator> administrator) {
        return administrator.map(this::fromDomain);
    }

    @Mapping(target = "password", ignore = true)
    AdministratorDto fromDomain(Administrator administrator);

    LoginResponseDto fromDomain(LoginResponse loginResponse);
    
    Administrator toDomain(AdministratorDto administratorDto);

    LoginRequest toDomain(LoginRequestDto loginRequestDto);

    Administrator updateAdministratorDtoToDomain(UpdateAdministratorDto administratorDto);

    LoginResponse administratorToLoginResponse(Administrator administrator);
}
