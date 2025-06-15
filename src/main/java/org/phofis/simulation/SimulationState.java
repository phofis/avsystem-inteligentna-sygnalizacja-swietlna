package org.phofis.simulation;

import lombok.Getter;
import org.phofis.model.Road;
import org.phofis.model.Vehicle;

import java.util.*;

@Getter
public class SimulationState {
    /* Each outer ArrayList represent multiple lanes on every side of the intersection,
     * where inner ArrayLists represent state of single lane.
     */
    private final HashMap<Road,ArrayList<ArrayList<Vehicle>>> lanes;
    private final ArrayDeque<Vehicle> vehiclePriorityQueue;
    private final HashSet<String> waitingVehiclesID;
    public SimulationState (Integer roadWidth) {
        lanes = new HashMap<>();
        for (Road key : List.of(Road.NORTH, Road.EAST, Road.SOUTH, Road.WEST)){
            ArrayList<ArrayList<Vehicle>> list = new ArrayList<>();
            for(int i=0;i<roadWidth;i++){
                list.add(new ArrayList<>());
            }
            lanes.put(key, list);

        }
        this.vehiclePriorityQueue = new ArrayDeque<>();
        this.waitingVehiclesID = new HashSet<>();


    }

    public void addVehicle(Vehicle vehicle) {
        lanes.get(vehicle.getStartRoad()).get(vehicle.getLaneNumber()).add(vehicle);
        if(vehicle.getVehicleId().startsWith("emergency")) {
            vehiclePriorityQueue.addFirst(vehicle);
        } else {
            vehiclePriorityQueue.addLast(vehicle);
        }
        waitingVehiclesID.add(vehicle.getVehicleId());
    }
}