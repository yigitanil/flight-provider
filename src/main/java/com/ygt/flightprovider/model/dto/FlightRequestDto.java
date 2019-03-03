package com.ygt.flightprovider.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightRequestDto {

    private String arrival;
    private String departure;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private LocalDate arrivalDate;
    private LocalTime arrivalTime;
    @NotNull
    private Integer page;
    @NotNull
    private Integer count;
    private String sort;
    private String sortDirection;

    public void setDepartureTime(String departureTime) {
        this.departureTime = LocalTime.parse(departureTime);
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = LocalTime.parse(arrivalTime);
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = LocalDate.parse(departureDate);
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = LocalDate.parse(arrivalDate);
    }

}
