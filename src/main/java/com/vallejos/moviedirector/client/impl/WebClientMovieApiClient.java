package com.vallejos.moviedirector.client.impl;

import com.vallejos.moviedirector.client.MovieApiClient;
import com.vallejos.moviedirector.configuration.MovieApiProperties;
import com.vallejos.moviedirector.dto.MovieApiResponseDto;
import com.vallejos.moviedirector.dto.MovieDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of {@link MovieApiClient} that uses Spring's {@link WebClient}
 * to fetch movie data from the external API.
 */
@Component
@Qualifier("webClientMovieApiClient")
public class WebClientMovieApiClient implements MovieApiClient {

    private final WebClient webClient;

    /**
     * Constructs the client with a pre-configured WebClient.Builder and API properties.
     *
     * @param webClientBuilder The configured WebClient.Builder, typically provided by a @Bean.
     * @param properties       The configuration properties containing the base URL for the movie API.
     */
    public WebClientMovieApiClient(WebClient.Builder webClientBuilder, MovieApiProperties properties) {
        this.webClient = webClientBuilder.baseUrl(properties.getBaseUrl()).build();
    }

    @Override
    public Flux<MovieDto> fetchAllMovies() {
        return fetchPage(1)
                .expand(response -> {
                    if (response.getPage() < response.getTotalPages()) {
                        return fetchPage(response.getPage() + 1);
                    } else {
                        return Mono.empty();
                    }
                })
                .flatMap(response -> Flux.fromIterable(response.getData()));
    }

    /**
     * Fetches a single page of movie results from the external API.
     *
     * @param page The page number to fetch.
     * @return A {@link Mono} emitting a {@link MovieApiResponseDto} for the requested page.
     */
    private Mono<MovieApiResponseDto> fetchPage(int page) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("page", page).build())
                .retrieve()
                .bodyToMono(MovieApiResponseDto.class);
    }
}
