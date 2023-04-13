package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;


/**
 * REST - клиент
 */
@Service
public class RestClient {

    @Value("${app.refresh.url:http://localhost:8888/actuator/busrefresh}")
    private String url;

    private static final Logger log = LoggerFactory.getLogger(RestClient.class);

    public final RestTemplate restTemplate;

    public RestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendRefreshSignal() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{}", headers);
        try {
            restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        } catch (ResourceAccessException e) {
            log.info("Timeout(");
        }
    }

}
