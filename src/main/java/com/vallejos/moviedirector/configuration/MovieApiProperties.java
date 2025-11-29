package com.vallejos.moviedirector.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "movie.api")
@Data
@Validated
public class MovieApiProperties {

    /**
     * Base URL for the external movie API.
     */
    private String baseUrl;

}
