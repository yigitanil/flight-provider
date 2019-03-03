package com.ygt.flightprovider.controller;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.ygt.flightprovider.model.Page;
import com.ygt.flightprovider.model.dto.FlightResponseDto;
import com.ygt.flightprovider.service.FlightRetrieveService;

import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FlightControllerIT {

    @MockBean
    private FlightRetrieveService flightRetrieveService;

    @Autowired
    protected WebTestClient webTestClient;

    Page<FlightResponseDto> page;

    List<FlightResponseDto> flightResponseDtos;

    LocalDate thirdMarch2019 = LocalDate.of(2019, 3, 3);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
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

    }

    @Test
    public void should_get_business_flights() {
        //given
        String thirdMarch2019 = "2019-03-03";
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();

        multiValueMap.add("departureDate", thirdMarch2019);
        multiValueMap.add("departureTime", "02:00");
        multiValueMap.add("arrivalDate", thirdMarch2019);
        multiValueMap.add("arrivalTime", "23:00");
        multiValueMap.add("departure", "IST");
        multiValueMap.add("arrival", "AMS");
        multiValueMap.add("page", "0");
        multiValueMap.add("count", "1");
        multiValueMap.add("sort", "departureDate");
        multiValueMap.add("sortDirection", "asc");

        //when
        when(flightRetrieveService.retrieveBusiness(ArgumentMatchers.any())).thenReturn(
            Mono.just(new Page<>(0, 2, flightResponseDtos.subList(2, 3))));

        //then

        webTestClient.get()
            .uri(uriBuilder -> uriBuilder.path("/flights/business").queryParams(multiValueMap).build())
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(new ParameterizedTypeReference<Page<FlightResponseDto>>() {
            })
            .value(Page::getCount, Matchers.equalTo(1))
            .value(Page::getPageNumer, Matchers.equalTo(0))
            .value(Page::getTotalCount, Matchers.equalTo(2))
            .value(Page::getContent, Matchers.containsInAnyOrder(flightResponseDtos.subList(2, 3).toArray()));

    }

    @Test
    public void should_get_cheap_flights() {
        //given
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();

        multiValueMap.add("page", "1");
        multiValueMap.add("count", "2");
        multiValueMap.add("sort", "departureDate");
        multiValueMap.add("sortDirection", "asc");

        //when
        when(flightRetrieveService.retrieveCheap(ArgumentMatchers.any())).thenReturn(
            Mono.just(new Page<>(1, 4, flightResponseDtos.subList(2, 4))));

        //then

        webTestClient.get()
            .uri(uriBuilder -> uriBuilder.path("/flights/cheap").queryParams(multiValueMap).build())
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(new ParameterizedTypeReference<Page<FlightResponseDto>>() {
            })
            .value(Page::getCount, Matchers.equalTo(2))
            .value(Page::getPageNumer, Matchers.equalTo(1))
            .value(Page::getTotalCount, Matchers.equalTo(4))
            .value(Page::getContent, Matchers.containsInAnyOrder(flightResponseDtos.subList(2, 4).toArray()));

    }

    @Test
    public void should_get_bad_request_when_page_not_found() {

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();

        multiValueMap.add("page", "0");
        multiValueMap.add("count", "1");
        multiValueMap.add("sort", "invalid");
        multiValueMap.add("sortDirection", "asc");

        webTestClient.get()
            .uri(uriBuilder -> uriBuilder.path("/flights/business").build())
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody()
            .jsonPath("message", Matchers.equalTo("page must not be null"));

    }

    @Test
    public void should_get_bad_request_when_sort_is_invalid() {

        webTestClient.get()
            .uri(uriBuilder -> uriBuilder.path("/flights/business").build())
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody()
            .jsonPath("message", Matchers.contains("Unsupported sorting field"));

    }
}
