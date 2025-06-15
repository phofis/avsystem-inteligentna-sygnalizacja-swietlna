package org.phofis.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Vehicle {
    private String vehicleId;
    private Road startRoad;
    private Road endRoad;
    private Integer laneNumber;

    @JsonCreator
    public Vehicle(
            @JsonProperty("vehicleId") String vehicleId,
            @JsonProperty("startRoad") Road startRoad,
            @JsonProperty("endRoad") Road endRoad,
            @JsonProperty("laneNumber") Integer laneNumber
    ) {
        this.vehicleId = vehicleId;
        this.startRoad = startRoad;
        this.endRoad = endRoad;
        this.laneNumber = laneNumber != null ? laneNumber : 0;
    }
}
