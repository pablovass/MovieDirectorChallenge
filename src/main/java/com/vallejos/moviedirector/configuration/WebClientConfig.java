package com.vallejos.moviedirector.configuration;

import io.netty.channel.ChannelOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

/**
 * Configuration class for {@link WebClient}.
 * This class centralizes the configuration of WebClient instances used throughout the application.
 */
@Configuration
public class WebClientConfig {

    /**
     * Creates a pre-configured {@link WebClient.Builder} bean that can be injected
     * across the application to create customized {@link WebClient} instances.
     * <p>
     * This configuration includes:
     * <ul>
     *     <li>A 10-second response timeout.</li>
     *     <li>A 5-second connection timeout.</li>
     * </ul>
     *
     * @return A configured {@link WebClient.Builder} instance.
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(10))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient));
    }
}
