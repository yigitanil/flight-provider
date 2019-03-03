package com.ygt.flightprovider.service.util;

import com.ygt.flightprovider.model.consume.BusinessFlight;
import com.ygt.flightprovider.model.consume.CheapFlight;
import com.ygt.flightprovider.model.dto.FlightResponseDto;

public class FlightResponseDtoMapper {

    private FlightResponseDtoMapper() {
    }

    public static FlightResponseDto map(CheapFlight cheapFlight) {
        return FlightResponseDto.builder()
            .arrival(cheapFlight.getArrival())
            .departure(cheapFlight.getDeparture())
            .arrivalDate(cheapFlight.getArrivalTime().toLocalDate())
            .arrivalTime(cheapFlight.getArrivalTime().toLocalTime())
            .departureDate(cheapFlight.getDepartureTime().toLocalDate())
            .departureTime(cheapFlight.getDepartureTime().toLocalTime())
            .build();
    }

    public static FlightResponseDto map(BusinessFlight businessFlight) {
        String[] flightSplit = businessFlight.getFlight().split("->");
        return FlightResponseDto.builder()
            .departure(flightSplit[0].trim())
            .arrival(flightSplit.length > 1 ? flightSplit[1].trim() : "")
            .arrivalDate(businessFlight.getArrival().toLocalDate())
            .arrivalTime(businessFlight.getArrival().toLocalTime())
            .departureDate(businessFlight.getDeparture().toLocalDate())
            .departureTime(businessFlight.getDeparture().toLocalTime())
            .build();
    }

}
