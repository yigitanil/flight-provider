package com.ygt.flightprovider.service;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ygt.flightprovider.exception.FlightRequestValidationException;
import com.ygt.flightprovider.model.dto.FlightRequestDto;
import com.ygt.flightprovider.model.dto.FlightResponseDto;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FlightRetrieveServiceTest {

    @Mock
    FlightAggregationService flightAggregationService;

    FlightRetrieveService flightRetrieveService;

    @Mock
    FlightRequestDto flightRequestDto;

    Flux<FlightResponseDto> flightResponseDtoFlux;

    List<FlightResponseDto> flightResponseDtos;

    LocalDate thirdMarch2019 = LocalDate.of(2019, 3, 3);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        flightRetrieveService = new FlightRetrieveService(flightAggregationService);
        FlightResponseDto flightResponseDto1 = new FlightResponseDto();
        flightResponseDto1.setDeparture("IST");
        flightResponseDto1.setDepartureDate(thirdMarch2019);
        flightResponseDto1.setDepartureTime(LocalTime.of(8, 0));
        flightResponseDto1.setArrival("ESN");
        flightResponseDto1.setArrivalDate(thirdMarch2019);
        flightResponseDto1.setArrivalTime(LocalTime.of(10, 0));

        FlightResponseDto flightResponseDto2 = new FlightResponseDto();
        flightResponseDto2.setDeparture("IST");
        flightResponseDto2.setDepartureDate(thirdMarch2019);
        flightResponseDto2.setDepartureTime(LocalTime.of(9, 0));
        flightResponseDto2.setArrival("AMS");
        flightResponseDto2.setArrivalDate(thirdMarch2019);
        flightResponseDto2.setArrivalTime(LocalTime.of(12, 30));

        FlightResponseDto flightResponseDto3 = new FlightResponseDto();
        flightResponseDto3.setDeparture("IST");
        flightResponseDto3.setDepartureDate(thirdMarch2019);
        flightResponseDto3.setDepartureTime(LocalTime.of(12, 0));
        flightResponseDto3.setArrival("AMS");
        flightResponseDto3.setArrivalDate(thirdMarch2019);
        flightResponseDto3.setArrivalTime(LocalTime.of(16, 30));

        FlightResponseDto flightResponseDto4 = new FlightResponseDto();
        flightResponseDto4.setDeparture("IST");
        flightResponseDto4.setDepartureDate(thirdMarch2019);
        flightResponseDto4.setDepartureTime(LocalTime.of(6, 0));
        flightResponseDto4.setArrival("JFK");
        flightResponseDto4.setArrivalDate(thirdMarch2019);
        flightResponseDto4.setArrivalTime(LocalTime.of(19, 0));

        flightResponseDtos =
            Arrays.asList(flightResponseDto1, flightResponseDto2, flightResponseDto3, flightResponseDto4);

        flightResponseDtoFlux = Flux.fromIterable(flightResponseDtos);
    }

    @Test
    public void should_return_business_flights_with_all_fileds_not_null() {

        when(flightAggregationService.retrieveBusinessFlights()).thenReturn(flightResponseDtoFlux);
        when(flightRequestDto.getDepartureDate()).thenReturn(thirdMarch2019);
        when(flightRequestDto.getDepartureTime()).thenReturn(LocalTime.of(2, 0));
        when(flightRequestDto.getArrivalDate()).thenReturn(thirdMarch2019);
        when(flightRequestDto.getArrivalTime()).thenReturn(LocalTime.of(23, 0));
        when(flightRequestDto.getDeparture()).thenReturn("IST");
        when(flightRequestDto.getArrival()).thenReturn("AMS");
        when(flightRequestDto.getPage()).thenReturn(0);
        when(flightRequestDto.getCount()).thenReturn(1);
        when(flightRequestDto.getSort()).thenReturn("arrivalDate");
        when(flightRequestDto.getSortDirection()).thenReturn("desc");

        StepVerifier.create(flightRetrieveService.retrieveBusiness(flightRequestDto))
            .expectNextMatches(page -> page.getPageNumer() == 0 && page.getTotalCount() == 2 && page.getCount() == 1
                && page.getContent().get(0).getDepartureDate().compareTo(thirdMarch2019) == 0 && page.getContent()
                .get(0)
                .getArrival()
                .equalsIgnoreCase("AMS"))
            .expectComplete()
            .verify();
    }

    @Test
    public void should_return_cheap_flights_with_all_fileds_not_null() {

        when(flightAggregationService.retrieveCheapFlights()).thenReturn(flightResponseDtoFlux);
        when(flightRequestDto.getDepartureDate()).thenReturn(thirdMarch2019);
        when(flightRequestDto.getDepartureTime()).thenReturn(LocalTime.of(2, 0));
        when(flightRequestDto.getArrivalDate()).thenReturn(thirdMarch2019);
        when(flightRequestDto.getArrivalTime()).thenReturn(LocalTime.of(23, 0));
        when(flightRequestDto.getDeparture()).thenReturn("IST");
        when(flightRequestDto.getArrival()).thenReturn("AMS");
        when(flightRequestDto.getPage()).thenReturn(1);
        when(flightRequestDto.getCount()).thenReturn(1);
        when(flightRequestDto.getSort()).thenReturn("departureDate");
        when(flightRequestDto.getSortDirection()).thenReturn("desc");

        StepVerifier.create(flightRetrieveService.retrieveCheap(flightRequestDto))
            .expectNextMatches(page -> page.getPageNumer() == 1 && page.getTotalCount() == 2 && page.getCount() == 1
                && page.getContent().get(0).getArrival().equalsIgnoreCase("AMS")
                && page.getContent().get(0).getDepartureDate().compareTo(thirdMarch2019) == 0

            )
            .expectComplete()
            .verify();
    }

    @Test
    public void should_return_business_flights_all() {

        when(flightAggregationService.retrieveBusinessFlights()).thenReturn(flightResponseDtoFlux);

        when(flightRequestDto.getPage()).thenReturn(1);
        when(flightRequestDto.getCount()).thenReturn(2);

        StepVerifier.create(flightRetrieveService.retrieveBusiness(flightRequestDto))
            .expectNextMatches(page -> page.getPageNumer() == 1 && page.getTotalCount() == 4 && page.getCount() == 2
                && page.getContent().get(0).getArrival().equalsIgnoreCase("AMS")
                && page.getContent().get(0).getArrivalTime().compareTo(LocalTime.of(12, 30)) == 0)
            .expectComplete()
            .verify();
    }

    @Test(expected = FlightRequestValidationException.class)
    public void should_throw_exception_when_invalid_sort() {

        when(flightAggregationService.retrieveBusinessFlights()).thenReturn(flightResponseDtoFlux);

        when(flightRequestDto.getPage()).thenReturn(0);
        when(flightRequestDto.getCount()).thenReturn(2);
        when(flightRequestDto.getSort()).thenReturn("invalid");

        StepVerifier.create(flightRetrieveService.retrieveBusiness(flightRequestDto)).verifyComplete();
    }

    @Test
    public void retrieveBusiness() {
    }
}