package com.ricardocreates.infra.server.rest.controller.v1.utils.mapper;

import org.mapstruct.Mapper;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface UtilsDtoMapper {

    String PATTERN_FORMAT = "yyyy-MM-dd HH:mm:ss";

    default Instant convertToInstant(OffsetDateTime offsetDateTime) {
        if (Objects.isNull(offsetDateTime))
            return null;
        return offsetDateTime.toInstant();
    }

    default Instant convertToInstant(String offsetDateTime) {
        if (Objects.isNull(offsetDateTime))
            return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT);
        return LocalDateTime.parse(offsetDateTime, formatter).toInstant(ZoneOffset.UTC);
    }

    default OffsetDateTime convertToOffsetDateTime(Instant instant) {
        if (Objects.isNull(instant))
            return null;
        return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    default String convertToString(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT)
                .withZone(ZoneOffset.UTC);
        if (Objects.isNull(instant))
            return null;
        return formatter.format(instant);
    }
}
