package com.ricardocreates.infra.data.mongo.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

/**
 * Mongodb configuration which will connect to a external (or local) mongodb given host port and database name properties
 */
@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.uri:}")
    private String uri;

    @Value("${spring.data.mongodb.host:}")
    private String host;
    @Value("${spring.data.mongodb.database:ecommerce}")
    private String dbName;

    @Value("${spring.data.mongodb.port:}")
    private String port;

    @Value("${spring.data.mongodb.username:}")
    private String username;

    @Value("${spring.data.mongodb.password:}")
    private String password;

    @Bean
    public MongoClient reactiveMongoClient() {
        if (!uri.isEmpty()) {
            return MongoClients.create(String.format(uri));
        }
        if (username.isEmpty()) {
            return MongoClients.create(String.format("mongodb://%s:%s", host, port));
        }
        return MongoClients.create(String.format("mongodb://%s:%s@%s:%s", username, password, host, port));
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate(MongoClient reactiveMongoClient) {
        return new ReactiveMongoTemplate(reactiveMongoClient, this.dbName);
    }
}
