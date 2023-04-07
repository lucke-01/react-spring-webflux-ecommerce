package com.ricardocreates.infra.data.mongo.mapper;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface UtilsMapper {

    default String convertToObjectId(ObjectId id) {
        if (Objects.isNull(id)) {
            return null;
        }
        return id.toString();
    }

    default ObjectId convertToStringId(String id) {
        if (Objects.isNull(id)) {
            return null;
        }
        return new ObjectId(id);
    }
}
