package com.vallejos.moviedirector.mapper;

import com.vallejos.moviedirector.domain.Movie;
import com.vallejos.moviedirector.dto.MovieDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MovieMapper Unit Tests")
class MovieMapperTest {

    private MovieMapper mapper = Mappers.getMapper(MovieMapper.class);

    @Test
    @DisplayName("should map MovieDto to Movie domain object correctly")
    void toDomain_shouldMapDtoToDomain() {

        MovieDto dto = new MovieDto();
        dto.setTitle("Test Title");
        dto.setYear("2023");
        dto.setRated("PG-13");
        dto.setReleased("01 Jan 2023");
        dto.setRuntime("120 min");
        dto.setGenre("Action");
        dto.setDirector("Test Director");
        dto.setWriter("Test Writer");
        dto.setActors("Actor 1, Actor 2");

        Movie movie = mapper.toDomain(dto);

        assertNotNull(movie);
        assertEquals("Test Title", movie.getTitle());
        assertEquals("2023", movie.getYear());
        assertEquals("PG-13", movie.getRated());
        assertEquals("01 Jan 2023", movie.getReleased());
        assertEquals("120 min", movie.getRuntime());
        assertEquals("Action", movie.getGenre());
        assertEquals("Test Director", movie.getDirector());
        assertEquals("Test Writer", movie.getWriter());
        assertEquals("Actor 1, Actor 2", movie.getActors());
    }

    @Test
    @DisplayName("should return null when mapping a null MovieDto to Movie")
    void toDomain_shouldReturnNullForNullDto() {
        Movie movie = mapper.toDomain(null);

        assertNull(movie);
    }

    @Test
    @DisplayName("should map Movie domain object to MovieDto correctly")
    void toDto_shouldMapDomainToDto() {

        Movie movie = new Movie();
        movie.setTitle("Another Title");
        movie.setYear("2024");
        movie.setRated("R");
        movie.setReleased("01 Feb 2024");
        movie.setRuntime("150 min");
        movie.setGenre("Drama");
        movie.setDirector("Another Director");
        movie.setWriter("Another Writer");
        movie.setActors("Actor 3, Actor 4");

        MovieDto dto = mapper.toDto(movie);

        assertNotNull(dto);
        assertEquals("Another Title", dto.getTitle());
        assertEquals("2024", dto.getYear());
        assertEquals("R", dto.getRated());
        assertEquals("01 Feb 2024", dto.getReleased());
        assertEquals("150 min", dto.getRuntime());
        assertEquals("Drama", dto.getGenre());
        assertEquals("Another Director", dto.getDirector());
        assertEquals("Another Writer", dto.getWriter());
        assertEquals("Actor 3, Actor 4", dto.getActors());
    }

    @Test
    @DisplayName("should return null when mapping a null Movie to MovieDto")
    void toDto_shouldReturnNullForNullMovie() {

        MovieDto dto = mapper.toDto(null);

        assertNull(dto);
    }
}
