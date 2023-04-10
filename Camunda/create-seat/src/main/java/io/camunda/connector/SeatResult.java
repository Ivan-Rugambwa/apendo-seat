package io.camunda.connector;

import io.camunda.connector.retrofit.seat.CreateSeatResponse;
import lombok.Data;

import java.util.Set;

@Data
public class SeatResult {
    private Set<CreateSeatResponse> seatResponses;

}
