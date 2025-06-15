package org.phofis.simulation;

import lombok.Getter;
import org.phofis.model.Vehicle;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;

@Getter
public class SimulationState {
    /* Each outer ArrayList represent multiple lanes on every side of intersection,
     * where inner ArrayLists represent state of single lane.
     */
    private final ArrayList<ArrayList<Vehicle>> north, east, south, west;
    private final ArrayDeque<Vehicle> vehiclePriorityQueue;
    private final HashSet<String> waitingVehiclesID;
    public SimulationState (Integer roadWidth) {
        this.north = new ArrayList<>(roadWidth);
        this.east = new ArrayList<>(roadWidth);
        this.south = new ArrayList<>(roadWidth);
        this.west = new ArrayList<>(roadWidth);
        this.vehiclePriorityQueue = new ArrayDeque<>();
        this.waitingVehiclesID = new HashSet<>();

        for (int i = 0; i < roadWidth; i++) {
            north.add(new ArrayList<>());
            east.add(new ArrayList<>());
            south.add(new ArrayList<>());
            west.add(new ArrayList<>());
        }
    }

    public void addVehicle(Vehicle vehicle) {
        switch (vehicle.getStartRoad()) {
            case NORTH:
                north.get(vehicle.getLaneNumber()).add(vehicle);
                break;
            case EAST:
                east.get(vehicle.getLaneNumber()).add(vehicle);
                break;
            case SOUTH:
                south.get(vehicle.getLaneNumber()).add(vehicle);
                break;
            case WEST:
                west.get(vehicle.getLaneNumber()).add(vehicle);
                break;
        }
        if(vehicle.getVehicleId().startsWith("emergency")) {
            vehiclePriorityQueue.addFirst(vehicle);
        } else {
            vehiclePriorityQueue.addLast(vehicle);
        }
        waitingVehiclesID.add(vehicle.getVehicleId());
    }
}
