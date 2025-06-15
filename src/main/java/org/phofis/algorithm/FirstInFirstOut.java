package org.phofis.algorithm;

import org.phofis.model.Road;
import org.phofis.model.Vehicle;
import org.phofis.simulation.SimulationState;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/*
 * There are 6 different simulation steps in this algorithm
 * 1. SOUTH -> EAST  and EAST  -> SOUTH
 * 2. SOUTH -> NORTH and NORTH -> SOUTH
 * 3. SOUTH -> WEST  and WEST  -> SOUTH
 * 4. EAST  -> WEST  and WEST  -> EAST
 * 5. WEST  -> NORTH and NORTH -> WEST
 * 6. NORTH -> EAST  and EAST  -> NORTH
 * The algorithm chooses a step based on starting road and ending road of the priorityVehicle,
 * where priorityVehicle is a vehicle, which has been waiting the most, among all waiting vehicles.
 */

public class FirstInFirstOut implements Algorithm {
    private static final Logger LOGGER = LogManager.getLogger(FirstInFirstOut.class);

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.MULTIPLE_LANES;
    }

    private void processDirection(List<ArrayList<Vehicle>> lanes, Road targetRoad, List<String> crossingVehicles) {
        for (ArrayList<Vehicle> lane : lanes) {
            if (!lane.isEmpty() && lane.get(0).getEndRoad() == targetRoad) {
                LOGGER.info("Vehicle {} added to crossing list", lane.get(0).getVehicleId());
                crossingVehicles.add(lane.get(0).getVehicleId());
                lane.remove(0);
            }
        }
    }

    @Override
    public List<String> runSimulationStep(SimulationState sim) {
        if (sim.getVehiclePriorityQueue().isEmpty()) {
            LOGGER.info("No waiting vehicles, returning empty list");
            return List.of();
        }
        Vehicle priorityVehicle = sim.getVehiclePriorityQueue().getFirst();
        while (!sim.getWaitingVehiclesID().contains(priorityVehicle.getVehicleId())) {
            sim.getVehiclePriorityQueue().removeFirst();
            if (sim.getVehiclePriorityQueue().isEmpty()) {
                LOGGER.info("No waiting vehicles, returning empty list");
                return List.of();
            }
            priorityVehicle = sim.getVehiclePriorityQueue().getFirst();
        }

        Road start = priorityVehicle.getStartRoad();
        Road end = priorityVehicle.getEndRoad();
        String laneCombo = start + "_TO_" + end;

        List<String> crossingVehicles = new ArrayList<>();

        switch (laneCombo) {
            case "SOUTH_TO_EAST":
            case "EAST_TO_SOUTH":
                LOGGER.info("Step 1 chosen");
                processDirection(sim.getSouth(), Road.EAST, crossingVehicles);
                processDirection(sim.getEast(), Road.SOUTH, crossingVehicles);
                break;

            case "SOUTH_TO_NORTH":
            case "NORTH_TO_SOUTH":
                LOGGER.info("Step 2 chosen");
                processDirection(sim.getSouth(), Road.NORTH, crossingVehicles);
                processDirection(sim.getNorth(), Road.SOUTH, crossingVehicles);
                break;

            case "SOUTH_TO_WEST":
            case "WEST_TO_SOUTH":
                LOGGER.info("Step 3 chosen");
                processDirection(sim.getSouth(), Road.WEST, crossingVehicles);
                processDirection(sim.getWest(), Road.SOUTH, crossingVehicles);
                break;

            case "EAST_TO_WEST":
            case "WEST_TO_EAST":
                LOGGER.info("Step 4 chosen");
                processDirection(sim.getEast(), Road.WEST, crossingVehicles);
                processDirection(sim.getWest(), Road.EAST, crossingVehicles);
                break;

            case "WEST_TO_NORTH":
            case "NORTH_TO_WEST":
                LOGGER.info("Step 5 chosen");
                processDirection(sim.getWest(), Road.NORTH, crossingVehicles);
                processDirection(sim.getNorth(), Road.WEST, crossingVehicles);
                break;

            case "NORTH_TO_EAST":
            case "EAST_TO_NORTH":
                LOGGER.info("Step 6 chosen");
                processDirection(sim.getNorth(), Road.EAST, crossingVehicles);
                processDirection(sim.getEast(), Road.NORTH, crossingVehicles);
                break;

            default:
                LOGGER.error("Unexpected lane combination chosen by the vehicle");
                break;
        }

        LOGGER.info("Returning list of crossing vehicles");
        crossingVehicles.forEach(sim.getWaitingVehiclesID()::remove);
        return crossingVehicles;
    }
}