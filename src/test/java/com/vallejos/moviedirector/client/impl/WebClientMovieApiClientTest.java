package com.vallejos.moviedirector.client.impl;

import com.vallejos.moviedirector.configuration.MovieApiProperties;
import com.vallejos.moviedirector.dto.MovieApiResponseDto;
import com.vallejos.moviedirector.dto.MovieDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("WebClientMovieApiClient Unit Tests")
class WebClientMovieApiClientTest {

    @Mock
    private WebClient.Builder webClientBuilder;
    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;
    @Mock
    private MovieApiProperties movieApiProperties;

    private WebClientMovieApiClient webClientMovieApiClient;

    private final String baseUrl = "http://test.api";

    @BeforeEach
    void setUp() {

        when(movieApiProperties.getBaseUrl()).thenReturn(baseUrl);

        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        webClientMovieApiClient = new WebClientMovieApiClient(webClientBuilder, movieApiProperties);

    }

    private void setupWebClientMockChain() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    @DisplayName("Constructor should build WebClient with provided base URL")
    void constructor_shouldBuildWebClientWithBaseUrl() {

        verify(webClientBuilder).baseUrl(baseUrl);
        verify(webClientBuilder).build();

    }

    @Test
    @DisplayName("fetchAllMovies should return movies from a single page response")
    void fetchAllMovies_shouldReturnMoviesFromSinglePage() {
        setupWebClientMockChain();

        MovieDto movie1 = new MovieDto();
        movie1.setTitle("Movie 1");
        MovieDto movie2 = new MovieDto();
        movie2.setTitle("Movie 2");
        List<MovieDto> moviesPage1 = Arrays.asList(movie1, movie2);

        MovieApiResponseDto apiResponsePage1 = new MovieApiResponseDto();
        apiResponsePage1.setPage(1);
        apiResponsePage1.setPerPage(10);
        apiResponsePage1.setTotal(2);
        apiResponsePage1.setTotalPages(1);
        apiResponsePage1.setData(moviesPage1);

        when(responseSpec.bodyToMono(MovieApiResponseDto.class))
                .thenReturn(Mono.just(apiResponsePage1));

        StepVerifier.create(webClientMovieApiClient.fetchAllMovies())
                .expectNext(movie1, movie2)
                .verifyComplete();

        verify(webClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri(any(java.util.function.Function.class));
        verify(requestHeadersSpec, times(1)).retrieve();
        verify(responseSpec, times(1)).bodyToMono(MovieApiResponseDto.class);
    }

    @Test
    @DisplayName("fetchAllMovies should return movies from multiple pages")
    void fetchAllMovies_shouldReturnMoviesFromMultiplePages() {
        setupWebClientMockChain();

        MovieDto movie1 = new MovieDto(); movie1.setTitle("Movie 1");
        MovieDto movie2 = new MovieDto(); movie2.setTitle("Movie 2");
        MovieDto movie3 = new MovieDto(); movie3.setTitle("Movie 3");
        MovieDto movie4 = new MovieDto(); movie4.setTitle("Movie 4");

        MovieApiResponseDto apiResponsePage1 = new MovieApiResponseDto();
        apiResponsePage1.setPage(1);
        apiResponsePage1.setPerPage(2);
        apiResponsePage1.setTotal(4);
        apiResponsePage1.setTotalPages(2);
        apiResponsePage1.setData(Arrays.asList(movie1, movie2));

        MovieApiResponseDto apiResponsePage2 = new MovieApiResponseDto();
        apiResponsePage2.setPage(2);
        apiResponsePage2.setPerPage(2);
        apiResponsePage2.setTotal(4);
        apiResponsePage2.setTotalPages(2);
        apiResponsePage2.setData(Arrays.asList(movie3, movie4));


        when(responseSpec.bodyToMono(MovieApiResponseDto.class))
                .thenReturn(Mono.just(apiResponsePage1))
                .thenReturn(Mono.just(apiResponsePage2));


        StepVerifier.create(webClientMovieApiClient.fetchAllMovies())
                .expectNext(movie1, movie2, movie3, movie4)
                .verifyComplete();


        verify(webClient, times(2)).get();
        verify(requestHeadersUriSpec, times(2)).uri(any(java.util.function.Function.class));
        verify(requestHeadersSpec, times(2)).retrieve();
        verify(responseSpec, times(2)).bodyToMono(MovieApiResponseDto.class);
    }

    @Test
    @DisplayName("fetchAllMovies should handle an empty data array in the response")
    void fetchAllMovies_shouldHandleEmptyDataArray() {
        setupWebClientMockChain();


        MovieApiResponseDto apiResponsePage1 = new MovieApiResponseDto();
        apiResponsePage1.setPage(1);
        apiResponsePage1.setPerPage(10);
        apiResponsePage1.setTotal(0);
        apiResponsePage1.setTotalPages(1);
        apiResponsePage1.setData(Collections.emptyList());

        when(responseSpec.bodyToMono(MovieApiResponseDto.class))
                .thenReturn(Mono.just(apiResponsePage1));


        StepVerifier.create(webClientMovieApiClient.fetchAllMovies())
                .expectNextCount(0)
                .verifyComplete();


        verify(webClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri(any(java.util.function.Function.class));
        verify(requestHeadersSpec, times(1)).retrieve();
        verify(responseSpec, times(1)).bodyToMono(MovieApiResponseDto.class);
    }

    @Test
    @DisplayName("fetchAllMovies should propagate errors from the WebClient")
    void fetchAllMovies_shouldPropagateError() {
        setupWebClientMockChain();


        RuntimeException expectedError = new RuntimeException("API error");
        when(responseSpec.bodyToMono(MovieApiResponseDto.class))
                .thenReturn(Mono.error(expectedError));


        StepVerifier.create(webClientMovieApiClient.fetchAllMovies())
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("API error"))
                .verify();


        verify(webClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri(any(java.util.function.Function.class));
        verify(requestHeadersSpec, times(1)).retrieve();
        verify(responseSpec, times(1)).bodyToMono(MovieApiResponseDto.class);
    }
}
