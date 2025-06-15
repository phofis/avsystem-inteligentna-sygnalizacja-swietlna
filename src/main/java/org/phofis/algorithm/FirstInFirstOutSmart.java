package org.phofis.algorithm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.phofis.model.Road;
import org.phofis.model.Vehicle;
import org.phofis.simulation.SimulationState;

import java.util.ArrayList;
import java.util.List;

public class FirstInFirstOutSmart implements Algorithm{
    private static final Logger LOGGER = LogManager.getLogger(FirstInFirstOutSmart.class);

    private boolean isCollision(Road vehicle1start, Road vehicle1end, Road vehicle2start, Road vehicle2end){
        if(vehicle1end == vehicle2end) return true;
        switch (Road.getOrientation(vehicle1start,vehicle1end)){
            case 3:
                // Vehicle 1 is going right
                return false;
            case 2:
                // Vehicle 2 is going across
                if(Road.getOrientation(vehicle2start,vehicle2end) != 1) return false; // No collision if vehicle 2 isn't going left.
                else return true;
            case 1:
                // Vehicle 3 is going left
                if(Road.getOrientation(vehicle2start,vehicle2end) == 3) return false; // No collision if vehicle 2 is going right.
                else return true;
        }
        return true;
    }

    @Override
    public List<String> runSimulationStep(SimulationState sim) {
        Vehicle priorityVehicle = Misc.findNextValidVehicle(sim);
        if (priorityVehicle == null) {
            LOGGER.info("No waiting vehicles, returning empty list");
            return List.of();
        }

        ArrayList<Vehicle> crossingVehicles = new ArrayList<>();

        crossingVehicles.add(sim.getLanes().get(priorityVehicle.getStartRoad()).get(0).get(0)); // Insert vehicle from priorityVehicle's lane

        sim.getLanes().forEach(((road, arrayList) -> {
            if(road != priorityVehicle.getStartRoad() && !arrayList.get(0).isEmpty()){ // Check if vehicles from other lanes may also cross
                boolean flag = true;
                for (Vehicle actualCrossingVehicle: new ArrayList<>(crossingVehicles)) {
                    if (isCollision(actualCrossingVehicle.getStartRoad(), actualCrossingVehicle.getEndRoad(), arrayList.get(0).get(0).getStartRoad(), arrayList.get(0).get(0).getEndRoad())) {
                        flag = false;
                    }
                }
                if(flag)
                    crossingVehicles.add(arrayList.get(0).get(0)); // Adding a vehicle if it isn't colling with any other, already crossing, vehicles
            }
        }));
        ArrayList<String> crossingVehiclesStrings = new ArrayList<>();
        for(Vehicle crossingVehicle : crossingVehicles){
            crossingVehiclesStrings.add(crossingVehicle.getVehicleId());
            sim.getWaitingVehiclesID().remove(crossingVehicle.getVehicleId());
        }
        return crossingVehiclesStrings;
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.SINGLE_LANE;
    }
}
