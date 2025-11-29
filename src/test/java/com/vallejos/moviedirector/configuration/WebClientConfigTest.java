package com.vallejos.moviedirector.configuration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = WebClientConfig.class)
@DisplayName("WebClientConfig Unit Tests")
class WebClientConfigTest {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Test
    @DisplayName("WebClient.Builder bean should be injected and not null")
    void webClientBuilder_shouldBeInjected() {
        assertNotNull(webClientBuilder, "WebClient.Builder should be injected by Spring context.");

    }
}
