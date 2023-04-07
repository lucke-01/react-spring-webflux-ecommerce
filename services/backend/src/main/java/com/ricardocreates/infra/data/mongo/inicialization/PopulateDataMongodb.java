package com.ricardocreates.infra.data.mongo.inicialization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Profile("standalone")
public class PopulateDataMongodb {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String DATE_FORMAT_REGEX = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z";
    private final ReactiveMongoTemplate mongoTemplate;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public PopulateDataMongodb(ReactiveMongoTemplate mongoTemplate) {
        simpleDateFormat.setLenient(false);
        this.mongoTemplate = mongoTemplate;
    }

    public void removeDataFromMongo(final String collectionName) {
        mongoTemplate.remove(new Query(), collectionName).block();
    }

    public void loadMultipleDataIntoMongo(final Resource[] resources) {
        try {
            if (resources != null) {
                for (Resource resource : resources) {
                    String collectionName = FilenameUtils.removeExtension(resource.getFilename());
                    //reset collection
                    this.removeDataFromMongo(collectionName);
                    //fill collection
                    tryLoadDataIntoMongo(resource, collectionName);
                }
            }
        } catch (Exception e) {
            log.error("Error Initializing data", e);
        }
    }

    public void tryLoadDataIntoMongo(Resource resource, String collectionName) {
        try {
            this.loadDataIntoMongo(resource, collectionName);
        } catch (Exception e) {
            log.error(String.format("error filling collection: %s", collectionName), e);
        }
    }

    public void loadDataIntoMongo(final Resource resource,
                                  final String collectionName) throws IOException {
        List<Document> documentsObject = getDocumentsFromFile(resource);

        documentsObject.forEach(doc -> {
            Object id = doc.get("_id");
            if (id != null) {
                doc.put("_id", new ObjectId(id.toString()));
            }
        });

        for (final Document document : documentsObject) {
            parseDates(document);
            mongoTemplate.insert(document, collectionName).block();
        }
    }

    private List<Document> getDocumentsFromFile(final Resource resource) throws IOException {
        final TypeReference<List<Document>> typeReference = new TypeReference<>() {
        };

        return OBJECT_MAPPER.readValue(resource.getInputStream(), typeReference);
    }

    private void parseDates(final Map<String, Object> document) {
        document.entrySet().stream()
                .filter(entry -> entry.getValue() instanceof String)
                .filter(entry -> ((String) entry.getValue()).matches(DATE_FORMAT_REGEX))
                .forEach(entry -> setValueObjectAsDate(document, entry));

        document.entrySet().stream()
                .filter(entry -> entry.getValue() instanceof Map)
                .forEach(entry -> parseDates((Map<String, Object>) entry.getValue()));

        document.entrySet().stream()
                .filter(entry -> entry.getValue() instanceof ArrayList)
                .forEach(entry -> parseDates((ArrayList) entry.getValue()));
    }

    private void parseDates(final ArrayList<?> documentArray) {
        documentArray.stream()
                .filter(Map.class::isInstance)
                .forEach(entry -> parseDates((Map<String, Object>) entry));
    }

    private void setValueObjectAsDate(final Map<String, Object> document, final Map.Entry<String, Object> entry) {
        try {
            document.put(entry.getKey(), simpleDateFormat.parse((String) entry.getValue()));
        } catch (final ParseException e) {
            log.error("Unexpected parse exception", e);
        }
    }

}