package com.ygt.flightprovider.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ygt.flightprovider.exception.FlightRequestValidationException;
import com.ygt.flightprovider.model.Page;
import com.ygt.flightprovider.model.dto.FlightRequestDto;
import com.ygt.flightprovider.model.dto.FlightResponseDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FlightRetrieveService {

    private final FlightAggregationService flightAggregationService;

    public FlightRetrieveService(FlightAggregationService flightAggregationService) {
        this.flightAggregationService = flightAggregationService;
    }

    private Mono<Page<FlightResponseDto>> retrieve(Flux<FlightResponseDto> flightResponseDtoFlux,
        FlightRequestDto flightRequestDto) {
        Predicate<FlightResponseDto> filter = filter(flightRequestDto);
        Comparator<FlightResponseDto> comparator = getComparator(flightRequestDto);
        return flightResponseDtoFlux.filter(filter)
            .sort(comparator)
            .collectList()
            .map(list -> new Page<>(flightRequestDto.getPage(), list.size(), list.stream()
                .skip(flightRequestDto.getPage() * flightRequestDto.getCount())
                .limit(flightRequestDto.getCount())
                .collect(Collectors.toList())));
    }

    public Mono<Page<FlightResponseDto>> retrieveCheap(FlightRequestDto flightRequestDto) {
        return retrieve(flightAggregationService.retrieveCheapFlights(), flightRequestDto);
    }

    public Mono<Page<FlightResponseDto>> retrieveBusiness(FlightRequestDto flightRequestDto) {
        return retrieve(flightAggregationService.retrieveBusinessFlights(), flightRequestDto);
    }

    private Predicate<FlightResponseDto> filter(FlightRequestDto flightRequestDto) {
        Predicate<FlightResponseDto> filter = flightResponseDto -> true;
        if (!StringUtils.isEmpty(flightRequestDto.getArrival())) {
            filter = filter.and(
                flightResponseDto -> flightResponseDto.getArrival().equalsIgnoreCase(flightRequestDto.getArrival()));
        }
        if (!StringUtils.isEmpty(flightRequestDto.getDeparture())) {
            filter = filter.and(flightResponseDto -> flightResponseDto.getDeparture()
                .equalsIgnoreCase(flightRequestDto.getDeparture()));
        }

        if (Objects.nonNull(flightRequestDto.getArrivalDate())) {
            filter = filter.and(
                flightResponseDto -> flightResponseDto.getArrivalDate().compareTo(flightRequestDto.getArrivalDate())
                    == 0);
        }
        if (Objects.nonNull(flightRequestDto.getArrivalTime())) {
            filter = filter.and(
                flightResponseDto -> flightResponseDto.getArrivalTime().compareTo(flightRequestDto.getArrivalTime())
                    <= 0);
        }

        if (Objects.nonNull(flightRequestDto.getDepartureDate())) {
            filter = filter.and(
                flightResponseDto -> flightResponseDto.getDepartureDate().compareTo(flightRequestDto.getDepartureDate())
                    == 0);
        }
        if (Objects.nonNull(flightRequestDto.getDepartureTime())) {
            filter = filter.and(
                flightResponseDto -> flightResponseDto.getDepartureTime().compareTo(flightRequestDto.getDepartureTime())
                    >= 0);
        }

        return filter;
    }

    private Comparator<FlightResponseDto> getComparator(FlightRequestDto flightRequestDto) {
        Comparator<FlightResponseDto> defaultComparator = Comparator.comparing(
            (FlightResponseDto flightResponseDto) -> LocalDateTime.of(flightResponseDto.getDepartureDate(),
                flightResponseDto.getDepartureTime()));
        if (!StringUtils.isEmpty(flightRequestDto.getSort())) {
            Comparator<FlightResponseDto> comparator;
            if (flightRequestDto.getSort().equalsIgnoreCase("departureDate")) {
                comparator = defaultComparator;
            } else if (flightRequestDto.getSort().equalsIgnoreCase("arrivalDate")) {
                comparator = Comparator.comparing(
                    (FlightResponseDto flightResponseDto) -> LocalDateTime.of(flightResponseDto.getArrivalDate(),
                        flightResponseDto.getArrivalTime()));
            } else {
                throw new FlightRequestValidationException("Unsupported sorting field: " + flightRequestDto.getSort());
            }
            if (!StringUtils.isEmpty(flightRequestDto.getSortDirection()) && flightRequestDto.getSortDirection()
                .equalsIgnoreCase("desc")) {
                comparator = comparator.reversed();
            }
            return comparator;
        }
        return defaultComparator;
    }
}
