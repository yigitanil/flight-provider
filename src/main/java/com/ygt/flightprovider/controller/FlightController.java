package com.ygt.flightprovider.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ygt.flightprovider.model.Page;
import com.ygt.flightprovider.model.dto.FlightRequestDto;
import com.ygt.flightprovider.model.dto.FlightResponseDto;
import com.ygt.flightprovider.service.FlightRetrieveService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/flights")
public class FlightController {

    private final FlightRetrieveService flightRetrieveService;

    public FlightController(FlightRetrieveService flightRetrieveService) {
        this.flightRetrieveService = flightRetrieveService;
    }

    @GetMapping("/business")
    public Mono<Page<FlightResponseDto>> getFlights(@ModelAttribute @Valid FlightRequestDto flightRequestDto) {
        return flightRetrieveService.retrieveBusiness(flightRequestDto);
    }

    @GetMapping("cheap")
    public Mono<Page<FlightResponseDto>> getCheapFlights(@ModelAttribute @Valid FlightRequestDto flightRequestDto) {
        return flightRetrieveService.retrieveCheap(flightRequestDto);
    }
}
