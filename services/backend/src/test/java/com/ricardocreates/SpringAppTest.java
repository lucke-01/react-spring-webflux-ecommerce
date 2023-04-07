package com.ricardocreates;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class SpringAppTest extends SpringTestContext {

    @Autowired
    private Environment env;


    @Test
    @DisplayName("check if spring is up")
    void contextLoads() {
        //spring is up
        assertThat(env).isNotNull();
    }

}
