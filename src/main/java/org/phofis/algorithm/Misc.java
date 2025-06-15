package org.phofis.algorithm;

import org.phofis.model.Road;
import org.phofis.model.Vehicle;
import org.apache.logging.log4j.Logger;
import org.phofis.simulation.SimulationState;

import java.util.ArrayList;
import java.util.List;

public class Misc {
    public static void processDirection(List<ArrayList<Vehicle>> lanes, Road targetRoad, List<String> crossingVehicles, Logger LOGGER) {
        for (ArrayList<Vehicle> lane : lanes) {
            if (!lane.isEmpty() && lane.get(0).getEndRoad() == targetRoad) {
                LOGGER.info("Vehicle {} added to crossing list", lane.get(0).getVehicleId());
                crossingVehicles.add(lane.get(0).getVehicleId());
                lane.remove(0);
            }
        }
    }

    public static Vehicle findNextValidVehicle(SimulationState sim) {
        while (!sim.getVehiclePriorityQueue().isEmpty()) {
            Vehicle vehicle = sim.getVehiclePriorityQueue().getFirst();
            if (sim.getWaitingVehiclesID().contains(vehicle.getVehicleId())) {
                return vehicle;
            }
            sim.getVehiclePriorityQueue().removeFirst();
        }
        return null;
    }
}
