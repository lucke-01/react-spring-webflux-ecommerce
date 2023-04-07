package com.ricardocreates.infra.data.mongo.config;

import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

/**
 * configure mongodb database in memory
 */
@Profile("test")
@Configuration
@Import(EmbeddedMongoAutoConfiguration.class)
public class MongoMemoryBeanConfig {
}
