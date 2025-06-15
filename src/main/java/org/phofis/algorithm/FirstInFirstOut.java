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


    @Override
    public List<String> runSimulationStep(SimulationState sim) {
        Vehicle priorityVehicle = Misc.findNextValidVehicle(sim);
        if (priorityVehicle == null) {
            LOGGER.info("No waiting vehicles, returning empty list");
            return List.of();
        }

        Road start = priorityVehicle.getStartRoad();
        Road end = priorityVehicle.getEndRoad();

        String laneCombo = start + "_TO_" + end;
        LOGGER.info("Choosing {}", laneCombo);

        List<String> crossingVehicles = new ArrayList<>();
        Misc.processDirection(sim.getLanes().get(start), end, crossingVehicles, LOGGER);
        Misc.processDirection(sim.getLanes().get(end), start, crossingVehicles, LOGGER);

        LOGGER.info("Returning list of crossing vehicles");
        crossingVehicles.forEach(sim.getWaitingVehiclesID()::remove);
        return crossingVehicles;
    }


}