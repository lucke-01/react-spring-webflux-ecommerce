package com.ricardocreates.infra.data.mongo.mapper.administrator;

import com.ricardocreates.domain.entity.administrator.Administrator;
import com.ricardocreates.domain.entity.administrator.LoginResponse;
import com.ricardocreates.infra.data.mongo.document.administrator.AdministratorDocument;
import com.ricardocreates.infra.data.mongo.mapper.UtilsMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import reactor.core.publisher.Flux;

@Mapper(componentModel = "spring", uses = UtilsMapper.class)
public interface AdministratorDocumentMapper {

    AdministratorDocument fromDomain(Administrator administrator);

    default Flux<Administrator> toDomain(Flux<AdministratorDocument> administratorsDocument) {
        return administratorsDocument.hasElements()
                .flatMapMany(exist -> !exist ? Flux.empty() : administratorsDocument.map(this::toDomain));
    }

    Administrator toDomain(AdministratorDocument administratorEntity);

    @Mapping(target = "roles", expression = "java(administratorDocument.getRoles().stream().map(role->role.toString()).toList())")
    LoginResponse toDomainLogin(AdministratorDocument administratorDocument);

}
