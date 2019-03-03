package com.ygt.flightprovider.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ygt.flightprovider.model.consume.BusinessFlight;
import com.ygt.flightprovider.model.consume.CheapFlight;
import com.ygt.flightprovider.model.dto.FlightResponseDto;
import com.ygt.flightprovider.service.util.FlightResponseDtoMapper;

import reactor.core.publisher.Flux;

@Service
public class FlightAggregationService {

    private final WebClient webClient;

    public FlightAggregationService(WebClient webClient) {
        this.webClient = webClient;
    }

    Flux<FlightResponseDto> retrieveBusinessFlights() {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder.path("business").build())
            .retrieve()
            .bodyToFlux(BusinessFlight.class)
            .map(FlightResponseDtoMapper::map);
    }

    Flux<FlightResponseDto> retrieveCheapFlights() {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder.path("cheap").build())
            .retrieve()
            .bodyToFlux(CheapFlight.class)
            .map(FlightResponseDtoMapper::map);
    }

}
