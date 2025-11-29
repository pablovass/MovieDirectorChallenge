package com.vallejos.moviedirector.domain;

import lombok.Data;

@Data
public class Movie {
    private String title;
    private String year;
    private String rated;
    private String released;
    private String runtime;
    private String genre;
    private String director;
    private String writer;
    private String actors;

    /**
     * Checks if the movie has a valid director assigned.
     * @return true if the director's name is not null and not empty.
     */
    public boolean hasDirector() {
        return this.director != null && !this.director.trim().isEmpty();
    }
}
