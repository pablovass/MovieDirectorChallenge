package com.vallejos.moviedirector.service;

import com.vallejos.moviedirector.client.MovieApiClient;
import com.vallejos.moviedirector.domain.Movie;

import com.vallejos.moviedirector.dto.MovieDto;
import com.vallejos.moviedirector.mapper.MovieMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("DirectorService Unit Tests")
class DirectorServiceTest {

    @Mock
    private MovieApiClient movieApiClient;

    @Mock
    private MovieMapper movieMapper;

    @InjectMocks
    private DirectorService directorService;

    @Test
    @DisplayName("should return correct directors for a valid threshold")
    void getDirectorsWithMoreMoviesThan_shouldReturnCorrectDirectors() {
        MovieDto dto1 = new MovieDto(); dto1.setDirector("Director A");
        MovieDto dto2 = new MovieDto(); dto2.setDirector("Director A");
        MovieDto dto3 = new MovieDto(); dto3.setDirector("Director B");
        MovieDto dto4 = new MovieDto(); dto4.setDirector("Director C");
        MovieDto dto5 = new MovieDto(); dto5.setDirector("Director C");
        MovieDto dto6 = new MovieDto(); dto6.setDirector("Director C");

        Movie m1 = new Movie(); m1.setDirector("Director A");
        Movie m2 = new Movie(); m2.setDirector("Director A");
        Movie m3 = new Movie(); m3.setDirector("Director B");
        Movie m4 = new Movie(); m4.setDirector("Director C");
        Movie m5 = new Movie(); m5.setDirector("Director C");
        Movie m6 = new Movie(); m6.setDirector("Director C");

        when(movieApiClient.fetchAllMovies()).thenReturn(Flux.just(dto1, dto2, dto3, dto4, dto5, dto6));
        

        when(movieMapper.toDomain(dto1)).thenReturn(m1);
        when(movieMapper.toDomain(dto2)).thenReturn(m2);
        when(movieMapper.toDomain(dto3)).thenReturn(m3);
        when(movieMapper.toDomain(dto4)).thenReturn(m4);
        when(movieMapper.toDomain(dto5)).thenReturn(m5);
        when(movieMapper.toDomain(dto6)).thenReturn(m6);

        StepVerifier.create(directorService.getDirectorsWithMoreMoviesThan("1"))
                .expectNextMatches(response -> 
                    response.getDirectors().size() == 2 &&
                    response.getDirectors().contains("Director A") &&
                    response.getDirectors().contains("Director C") &&
                    !response.getDirectors().contains("Director B")
                )
                .verifyComplete();
    }
    
    @Test
    @DisplayName("should return empty list when no director meets threshold")
    void getDirectorsWithMoreMoviesThan_shouldReturnEmptyList_whenNoDirectorMeetsThreshold() {
         MovieDto dto1 = new MovieDto(); dto1.setDirector("Director A");
         Movie m1 = new Movie(); m1.setDirector("Director A");
         
         when(movieApiClient.fetchAllMovies()).thenReturn(Flux.just(dto1));
         when(movieMapper.toDomain(dto1)).thenReturn(m1);
         
         StepVerifier.create(directorService.getDirectorsWithMoreMoviesThan("5"))
                .expectNextMatches(response -> response.getDirectors().isEmpty())
                .verifyComplete();
    }

    @Test
    @DisplayName("should return empty list when threshold is negative")
    void getDirectorsWithMoreMoviesThan_shouldReturnEmptyList_whenThresholdIsNegative() {
        StepVerifier.create(directorService.getDirectorsWithMoreMoviesThan("-1"))
                .expectNextMatches(response -> response.getDirectors().isEmpty())
                .verifyComplete();
    }
}
