package com.vallejos.moviedirector.service;

import com.vallejos.moviedirector.client.MovieApiClient;
import com.vallejos.moviedirector.domain.Movie;
import com.vallejos.moviedirector.dto.DirectorResponseDto;
import com.vallejos.moviedirector.mapper.MovieMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class containing the core business logic for director-related operations.
 * This service orchestrates fetching movie data, processing it, and returning the results.
 */
@Service
public class DirectorService {

    private final MovieApiClient movieApiClient;
    private final MovieMapper movieMapper;

    /**
     * Constructs the service with its required dependencies.
     *
     * @param movieApiClient The client to fetch movie data from an external source.
     * @param movieMapper    The mapper to convert between DTOs and domain objects.
     */
    public DirectorService(@Qualifier("webClientMovieApiClient") MovieApiClient movieApiClient, MovieMapper movieMapper) {
        this.movieApiClient = movieApiClient;
        this.movieMapper = movieMapper;
    }

    /**
     * Main orchestration method to get directors who have directed more movies than a given threshold.
     * It handles input validation and coordinates the reactive data flow.
     *
     * @param thresholdStr The threshold value as a string.
     * @return A {@link Mono} emitting a {@link DirectorResponseDto} with the list of directors.
     *         Returns an empty list for negative thresholds.
     * @throws IllegalArgumentException if the threshold is not a valid number.
     */
    public Mono<DirectorResponseDto> getDirectorsWithMoreMoviesThan(String thresholdStr) {
        int threshold = parseAndValidateThreshold(thresholdStr);

        if (threshold < 0) {
            return Mono.just(new DirectorResponseDto(Collections.emptyList()));
        }

        return movieApiClient.fetchAllMovies()
                .map(movieMapper::toDomain)
                .collectList()
                .map(movies -> calculateDirectorResponse(movies, threshold));
    }

    /**
     * Calculates the list of directors who meet the threshold from a given list of movies.
     * This method contains the pure business logic and is designed for easy testing.
     *
     * @param movies    The list of {@link Movie} domain objects.
     * @param threshold The minimum number of movies directed (exclusive).
     * @return A {@link DirectorResponseDto} containing the sorted list of director names.
     */
    private DirectorResponseDto calculateDirectorResponse(List<Movie> movies, int threshold) {
        Map<String, Long> directorCounts = movies.stream()
                .filter(Movie::hasDirector)
                .collect(Collectors.groupingBy(Movie::getDirector, Collectors.counting()));

        List<String> directors = directorCounts.entrySet().stream()
                .filter(entry -> entry.getValue() > threshold)
                .map(Map.Entry::getKey)
                .sorted()
                .toList();

        return new DirectorResponseDto(directors);
    }

    /**
     * Parses and validates the threshold string.
     *
     * @param thresholdStr The threshold value as a string.
     * @return The parsed integer value of the threshold.
     * @throws IllegalArgumentException if the string is not a valid integer.
     */
    private int parseAndValidateThreshold(String thresholdStr) {
        try {
            return Integer.parseInt(thresholdStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Threshold must be a number");
        }
    }
}
