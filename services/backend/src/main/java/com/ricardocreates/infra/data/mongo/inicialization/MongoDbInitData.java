package com.ricardocreates.infra.data.mongo.inicialization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
@Profile("standalone")
public class MongoDbInitData {
    private final PopulateDataMongodb populateDataMongodb;
    private final ApplicationContext applicationContext;
    private final boolean initializeData;
    private final String initializeDataDirectory;

    public MongoDbInitData(PopulateDataMongodb populateDataMongodb, ApplicationContext applicationContext,
                           @Value("#{new Boolean('${app.options.initializeData}')}") boolean initializeData, @Value("${app.options.initializeDataDirectory}") String initializeDataDirectory) {
        this.populateDataMongodb = populateDataMongodb;
        this.applicationContext = applicationContext;
        this.initializeData = initializeData;
        this.initializeDataDirectory = initializeDataDirectory;
    }

    @PostConstruct
    public void initData() {
        if (initializeData) {
            Resource[] dbInitDir = null;
            log.info("Initializing data");
            try {
                dbInitDir = applicationContext.getResources(initializeDataDirectory);
            } catch (Exception e) {
                log.error("Initializate data error getting data files", e);
            }
            populateDataMongodb.loadMultipleDataIntoMongo(dbInitDir);
            log.info("finished initializing data");
        }
    }


}
