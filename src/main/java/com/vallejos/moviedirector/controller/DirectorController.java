package com.vallejos.moviedirector.controller;

import com.vallejos.moviedirector.dto.DirectorResponseDto;
import com.vallejos.moviedirector.service.DirectorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * REST controller for handling director-related requests.
 * This controller exposes endpoints for retrieving information about movie directors.
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Directors", description = "API for retrieving director information")
public class DirectorController {

    private final DirectorService directorService;

    /**
     * Constructs the controller and injects the required service.
     *
     * @param directorService The service responsible for the business logic.
     */
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }


    @GetMapping("/directors")
    @Operation(summary = "Get directors with movie count above threshold",
            description = "Returns a list of directors who have directed more movies than the specified threshold.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = DirectorResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid threshold value")
    public Mono<DirectorResponseDto> getDirectors(
            @Parameter(description = "Threshold for number of movies directed")
            @RequestParam(name = "threshold") String thresholdStr) {

        return directorService.getDirectorsWithMoreMoviesThan(thresholdStr);
    }
}
