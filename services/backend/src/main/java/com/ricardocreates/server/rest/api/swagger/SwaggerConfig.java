package com.ricardocreates.server.rest.api.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.HttpAuthenticationScheme;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    /**
     * allow to use bearer security in v2
     * just valid in v2 version
     */
    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    /**
     * indicates which URL has security
     *
     * @return a SecurityContext
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    /**
     * indicates which URL has security
     *
     * @return a List of SecurityReference
     */
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        return List.of(new SecurityReference("Authorization", new AuthorizationScope[]{authorizationScope}));
    }

    /**
     * allow to use bearer security in OAS_30
     *
     * @return SecurityScheme
     */
    private SecurityScheme securityScheme() {
        return new HttpAuthenticationScheme("Authorization", "Bearer auth", "http", "bearer", "JWT", new ArrayList<>());
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                //only in v2 version
                //.securitySchemes(List.of(apiKey()))
                .select()
                //.apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.ricardocreates.infra.server.rest.controller"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(List.of(securityScheme()))
                .securityContexts(List.of(securityContext()))
                .apiInfo(new ApiInfoBuilder().title("ecommerce").version("1.0.0").build());
    }

}
