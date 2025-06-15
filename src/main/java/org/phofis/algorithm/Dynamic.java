package org.phofis.algorithm;

import org.phofis.simulation.SimulationState;

import java.util.List;

public class Dynamic implements Algorithm{
    private static final Integer maxVehicleCount = 10;
    private static final FirstInFirstOut fifo = new FirstInFirstOut();
    private static final MaxThroughput mt = new MaxThroughput();

    @Override
    public List<String> runSimulationStep(SimulationState sim) {
        if(sim.getWaitingVehiclesID().size() > maxVehicleCount * sim.getNorth().size()){
            mt.runSimulationStep(sim);
        } else {
            fifo.runSimulationStep(sim);
        }
        return List.of();
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.MULTIPLE_LANES;
    }
}
