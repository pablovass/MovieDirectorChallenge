package com.vallejos.moviedirector.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Movie Domain Unit Tests")
class MovieTest {

    @Test
    @DisplayName("hasDirector should return false when director is null")
    void hasDirector_shouldReturnFalse_whenDirectorIsNull() {

        Movie movie = new Movie();
        movie.setDirector(null);

        boolean result = movie.hasDirector();

        assertFalse(result, "hasDirector should be false when director is null");
    }

    @Test
    @DisplayName("hasDirector should return false when director is an empty string")
    void hasDirector_shouldReturnFalse_whenDirectorIsEmpty() {

        Movie movie = new Movie();
        movie.setDirector("");

        boolean result = movie.hasDirector();

        assertFalse(result, "hasDirector should be false when director is an empty string");
    }

    @Test
    @DisplayName("hasDirector should return false when director is a blank string")
    void hasDirector_shouldReturnFalse_whenDirectorIsBlank() {

        Movie movie = new Movie();
        movie.setDirector("   ");


        boolean result = movie.hasDirector();

        assertFalse(result, "hasDirector should be false when director is a blank string");
    }

    @Test
    @DisplayName("hasDirector should return true when director is a valid name")
    void hasDirector_shouldReturnTrue_whenDirectorIsValid() {

        Movie movie = new Movie();
        movie.setDirector("Quentin Tarantino");

        boolean result = movie.hasDirector();

        assertTrue(result, "hasDirector should be true when director is a valid name");
    }
}
