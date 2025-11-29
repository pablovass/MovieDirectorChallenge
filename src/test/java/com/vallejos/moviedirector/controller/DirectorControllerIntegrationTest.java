package com.vallejos.moviedirector.controller;

import com.vallejos.moviedirector.dto.DirectorResponseDto;
import com.vallejos.moviedirector.service.DirectorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@WebFluxTest(DirectorController.class)
@DisplayName("DirectorController Integration Tests")
class DirectorControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private DirectorService directorServiceMock;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public DirectorService directorService() {
            return mock(DirectorService.class);
        }
    }

    @Test
    @DisplayName("should return directors when threshold is valid")
    void getDirectors_shouldReturnDirectors() {

        DirectorResponseDto expectedResponse = new DirectorResponseDto(Arrays.asList("Director A", "Director B"));
        when(directorServiceMock.getDirectorsWithMoreMoviesThan(anyString()))
                .thenReturn(Mono.just(expectedResponse));

        webTestClient.get()
                .uri("/api/directors?threshold=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.directors[0]").isEqualTo("Director A")
                .jsonPath("$.directors[1]").isEqualTo("Director B");
    }

    @Test
    @DisplayName("should return bad request when threshold is not a number")
    void getDirectors_shouldReturnBadRequest_whenThresholdIsNotNumber() {

        when(directorServiceMock.getDirectorsWithMoreMoviesThan("abc"))
                .thenThrow(new IllegalArgumentException("Threshold must be a number"));

        webTestClient.get()
                .uri("/api/directors?threshold=abc")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").exists();
    }

    @Test
    @DisplayName("should return empty list when threshold is negative")
    void getDirectors_shouldReturnEmptyList_whenThresholdIsNegative() {

        DirectorResponseDto expectedResponse = new DirectorResponseDto(Collections.emptyList());
        when(directorServiceMock.getDirectorsWithMoreMoviesThan(anyString()))
                .thenReturn(Mono.just(expectedResponse));

        webTestClient.get()
                .uri("/api/directors?threshold=-1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.directors").isEmpty();
    }
}
