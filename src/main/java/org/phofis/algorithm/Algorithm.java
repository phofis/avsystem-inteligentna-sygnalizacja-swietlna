package org.phofis.algorithm;

import org.phofis.simulation.SimulationState;

import java.util.List;

public interface Algorithm {
    List<String> runSimulationStep(SimulationState sim);
    AlgorithmType getAlgorithmType();
}
