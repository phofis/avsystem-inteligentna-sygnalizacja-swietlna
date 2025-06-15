package org.phofis.algorithm;

import org.phofis.model.Road;
import org.phofis.model.Vehicle;
import org.phofis.simulation.SimulationState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/*
 * There are 6 different simulation steps in this algorithm
 * 1. SOUTH -> EAST  and EAST  -> SOUTH
 * 2. SOUTH -> NORTH and NORTH -> SOUTH
 * 3. SOUTH -> WEST  and WEST  -> SOUTH
 * 4. EAST  -> WEST  and WEST  -> EAST
 * 5. WEST  -> NORTH and NORTH -> WEST
 * 6. NORTH -> EAST  and EAST  -> NORTH
 * The algorithm chooses a step based on how many vehicles chose the step.
 */

public class MaxThroughput implements Algorithm {
    private static final Logger LOGGER = LogManager.getLogger(MaxThroughput.class);
    private static final FirstInFirstOut emergencyAlgorithm = new FirstInFirstOut();

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.MULTIPLE_LANES;
    }

    @Override
    public List<String> runSimulationStep(SimulationState sim) {
        if(!sim.getVehiclePriorityQueue().isEmpty() && sim.getVehiclePriorityQueue().getFirst().getVehicleId().startsWith("emergency")){
            return emergencyAlgorithm.runSimulationStep(sim);
        }

        Integer[] numberOfVehiclesChoosingStep = {0, 0, 0, 0, 0, 0};
        ArrayList<ArrayList<Vehicle>> allVehiclesLanes = new ArrayList<>();
        
        sim.getLanes().forEach((road,arrayList)->{
            allVehiclesLanes.addAll(arrayList);
        });

        allVehiclesLanes.forEach((vehicleArrayList) -> {
            if (!vehicleArrayList.isEmpty()) {
                switch (vehicleArrayList.get(0).getStartRoad() + "_TO_" + vehicleArrayList.get(0).getEndRoad()) {
                    case "SOUTH_TO_EAST":
                    case "EAST_TO_SOUTH":
                        numberOfVehiclesChoosingStep[0] += 1;
                        break;

                    case "SOUTH_TO_NORTH":
                    case "NORTH_TO_SOUTH":
                        numberOfVehiclesChoosingStep[1] += 1;
                        break;

                    case "SOUTH_TO_WEST":
                    case "WEST_TO_SOUTH":
                        numberOfVehiclesChoosingStep[2] += 1;
                        break;

                    case "EAST_TO_WEST":
                    case "WEST_TO_EAST":
                        numberOfVehiclesChoosingStep[3] += 1;
                        break;

                    case "WEST_TO_NORTH":
                    case "NORTH_TO_WEST":
                        numberOfVehiclesChoosingStep[4] += 1;
                        break;

                    case "NORTH_TO_EAST":
                    case "EAST_TO_NORTH":
                        numberOfVehiclesChoosingStep[5] += 1;
                        break;
                }
            }
        });

        ArrayList<String> crossingVehicles = new ArrayList<>();


        int tmp = 0, chosenStep = 0;
        for(int i=0;i<6;i++){
            if(numberOfVehiclesChoosingStep[i]>tmp) {
                tmp = numberOfVehiclesChoosingStep[i];
                chosenStep = i;
            }
        }
        switch (chosenStep) {
            case 0:

                LOGGER.info("Step 1 chosen");
                Misc.processDirection(sim.getLanes().get(Road.SOUTH), Road.EAST, crossingVehicles,LOGGER);
                Misc.processDirection(sim.getLanes().get(Road.EAST), Road.SOUTH, crossingVehicles,LOGGER);
                break;

            case 1:
                LOGGER.info("Step 2 chosen");
                Misc.processDirection(sim.getLanes().get(Road.SOUTH), Road.NORTH, crossingVehicles,LOGGER);
                Misc.processDirection(sim.getLanes().get(Road.NORTH), Road.SOUTH, crossingVehicles,LOGGER);
                break;

            case 2:
                LOGGER.info("Step 3 chosen");
                Misc.processDirection(sim.getLanes().get(Road.SOUTH), Road.WEST, crossingVehicles,LOGGER);
                Misc.processDirection(sim.getLanes().get(Road.WEST), Road.SOUTH, crossingVehicles,LOGGER);
                break;

            case 3:
                LOGGER.info("Step 4 chosen");
                Misc.processDirection(sim.getLanes().get(Road.EAST), Road.WEST, crossingVehicles,LOGGER);
                Misc.processDirection(sim.getLanes().get(Road.WEST), Road.EAST, crossingVehicles,LOGGER);
                break;

            case 4:
                LOGGER.info("Step 5 chosen");
                Misc.processDirection(sim.getLanes().get(Road.WEST), Road.NORTH, crossingVehicles,LOGGER);
                Misc.processDirection(sim.getLanes().get(Road.NORTH), Road.WEST, crossingVehicles,LOGGER);
                break;

            case 5:
                LOGGER.info("Step 6 chosen");
                Misc.processDirection(sim.getLanes().get(Road.NORTH), Road.EAST, crossingVehicles,LOGGER);
                Misc.processDirection(sim.getLanes().get(Road.EAST), Road.NORTH, crossingVehicles,LOGGER);
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
