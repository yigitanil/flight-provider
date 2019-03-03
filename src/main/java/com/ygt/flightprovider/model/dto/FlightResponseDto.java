package com.ygt.flightprovider.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightResponseDto {

    private String departure;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private String arrival;
    private LocalDate arrivalDate;
    private LocalTime arrivalTime;

}
