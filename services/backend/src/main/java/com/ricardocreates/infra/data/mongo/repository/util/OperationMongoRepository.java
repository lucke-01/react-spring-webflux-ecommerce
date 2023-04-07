package com.ricardocreates.infra.data.mongo.repository.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Map;
import java.util.Objects;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Utility class to perform Spring MongoDb operations
 */
public class OperationMongoRepository {
    private static final String MONGODB_ID_FIELD = "_id";
    private static final String ID_FIELD = "id";

    private OperationMongoRepository() {
    }

    /**
     * given an object return a Map with all of its properties but nulls ones and ids ones
     *
     * @param obj an object
     * @return a map of objects
     */
    public static Map<String, Object> mapFromObject(Object obj) {
        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Object> dataMap = oMapper.convertValue(obj, Map.class);
        dataMap.values().removeIf(Objects::isNull);
        dataMap.remove(MONGODB_ID_FIELD);
        dataMap.remove(ID_FIELD);
        return dataMap;
    }

    /**
     * given an object return an Update(spring data mongo) with all of its properties but nulls ones and ids ones
     * useful to update documents updating only the wanted fields
     *
     * @param obj an object
     * @return a Update object
     */
    public static Update createUpdateFromObj(Object obj) {
        Map<String, Object> dataMap = mapFromObject(obj);
        Update update = new Update();
        dataMap.forEach(update::set);
        return update;
    }

    public static Update createUpdateFromMap(Map<String, Object> dataMap) {
        Update update = new Update();
        dataMap.forEach(update::set);
        return update;
    }

    /**
     * create a Spring data mongodb Query looking into id field for a given id
     *
     * @param id a id
     * @return a Spring data mongodb Query
     */
    public static Query createQueryFindById(String id) {
        return new Query().addCriteria(where(MONGODB_ID_FIELD).is(id));
    }
}
