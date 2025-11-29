package com.vallejos.moviedirector.client;

import com.vallejos.moviedirector.dto.MovieDto;
import reactor.core.publisher.Flux;

/**
 * Defines the contract for clients that fetch movie data from an external source.
 * This interface abstracts the underlying implementation of the HTTP client,
 * allowing for different implementations (e.g., WebClient, RestTemplate) to be used interchangeably.
 */
public interface MovieApiClient {

    /**
     * Fetches all movies from the external API, handling pagination automatically.
     *
     * @return A {@link Flux} that emits {@link MovieDto} objects.
     *         The stream will complete once all movies from all pages have been emitted.
     *         If an error occurs during the API call, the Flux will signal an error.
     */
    Flux<MovieDto> fetchAllMovies();
}
