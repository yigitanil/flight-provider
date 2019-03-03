package com.ygt.flightprovider.model.consume;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ygt.json.deserializer.LongToLocalDateTimeDeserializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheapFlight extends BaseFlight<Long> {

    private String departure;
    private String arrival;
    @JsonDeserialize(using = LongToLocalDateTimeDeserializer.class)
    private LocalDateTime departureTime;
    @JsonDeserialize(using = LongToLocalDateTimeDeserializer.class)
    private LocalDateTime arrivalTime;
}
