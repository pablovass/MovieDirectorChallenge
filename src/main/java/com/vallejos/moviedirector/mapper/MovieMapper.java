package com.vallejos.moviedirector.mapper;

import com.vallejos.moviedirector.domain.Movie;
import com.vallejos.moviedirector.dto.MovieDto;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between {@link MovieDto} and {@link Movie} domain objects.
 * This interface is implemented by MapStruct at compile time.
 * The {@code componentModel = "spring"} attribute makes the generated implementation a Spring Bean,
 * allowing it to be injected into other components.
 */
@Mapper(componentModel = "spring")
public interface MovieMapper {

    /**
     * Converts a {@link MovieDto} to a {@link Movie} domain object.
     *
     * @param dto The Data Transfer Object to convert.
     * @return The corresponding {@link Movie} domain object.
     */
    Movie toDomain(MovieDto dto);

    /**
     * Converts a {@link Movie} domain object to a {@link MovieDto}.
     *
     * @param movie The domain object to convert.
     * @return The corresponding {@link MovieDto}.
     */
    MovieDto toDto(Movie movie);
}
