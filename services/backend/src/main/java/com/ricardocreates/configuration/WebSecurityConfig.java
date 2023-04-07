package com.ricardocreates.configuration;

import com.google.common.collect.ImmutableList;
import com.ricardocreates.application.service.AuthenticationManager;
import com.ricardocreates.application.service.SecurityContextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig {

    @Value("${spring.biller.cors-allowed-origin:}")
    private String corsAllowedOrigin;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private SecurityContextRepository securityContextRepository;

    @Bean
    public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {
        return http
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) ->
                        Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED))
                ).accessDeniedHandler((swe, e) ->
                        Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN))
                ).and()
                .cors().configurationSource(urlBasedCorsConfigurationSource()).and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers("/api/administrator/login").permitAll()
                .pathMatchers("/api/administrator/send-forgot-password").permitAll()
                .pathMatchers("/api/administrator/restore-password").permitAll()
                .pathMatchers("/actuator/info").permitAll()
                .pathMatchers("/actuator/health").permitAll()
                .pathMatchers("/actuator/prometheus").permitAll()
                .pathMatchers("/actuator/metrics").permitAll()
                .pathMatchers("/swagger-ui").permitAll()
                .pathMatchers("/api/swagger-ui").permitAll()
                .pathMatchers("/swagger-ui.html/**").permitAll()
                .pathMatchers("/swagger-ui/**").permitAll()
                .pathMatchers("/swagger-resources/**").permitAll()
                .pathMatchers("/v2/api-docs").permitAll()
                .pathMatchers("/v3/api-docs").permitAll()
                .pathMatchers("/api/administrator/*").hasRole("ADMIN")
                .pathMatchers("/api/*").hasAnyRole("ADMIN", "ADMIN_READ_ONLY", "MANAGEMENT")
                .anyExchange().authenticated()
                .and().build();
    }

    @Bean
    public CorsWebFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfig = createCorsConfiguration();
        source.registerCorsConfiguration("/**", corsConfig);
        return new CorsWebFilter(source);
    }

    private UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource() {
        CorsConfiguration corsConfig = createCorsConfiguration();
        corsConfig.applyPermitDefaultValues();
        UrlBasedCorsConfigurationSource ccs = new UrlBasedCorsConfigurationSource();
        ccs.registerCorsConfiguration("/**", corsConfig);
        return ccs;
    }

    private CorsConfiguration createCorsConfiguration() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(ImmutableList.of(corsAllowedOrigin, "https://ricardocretes.com/",
                "http://localhost:3000/", "https://localhost:3000/"));
        config.setAllowCredentials(true);
        config.setAllowedMethods(ImmutableList.of("GET", "POST", "PUT", "DELETE", "PATCH", "*"));
        config.setAllowedHeaders(ImmutableList.of("Authorization", "Baeldung-Allowed", "*"));
        return config;
    }

}
