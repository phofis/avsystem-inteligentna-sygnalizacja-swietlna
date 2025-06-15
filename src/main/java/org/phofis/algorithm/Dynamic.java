package org.phofis.algorithm;

import org.phofis.model.Road;
import org.phofis.simulation.SimulationState;

import java.util.List;

/*
 * The algorithm switches between fifo and mt algorithm based on number of waiting vehicles.
 */

public class Dynamic implements Algorithm{
    private static final Integer maxVehicleCount = 10;
    private static final FirstInFirstOut fifo = new FirstInFirstOut();
    private static final MaxThroughput mt = new MaxThroughput();

    @Override
    public List<String> runSimulationStep(SimulationState sim) {
        if(sim.getWaitingVehiclesID().size() > maxVehicleCount * sim.getLanes().get(Road.NORTH).size()){
            return mt.runSimulationStep(sim);
        } else {
            return fifo.runSimulationStep(sim);
        }
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.MULTIPLE_LANES;
    }
}
