package com.ygt.flightprovider.model.consume;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessFlight extends BaseFlight<UUID> {

    private String flight;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime departure;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime arrival;
}
