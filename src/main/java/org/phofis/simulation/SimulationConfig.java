package org.phofis.simulation;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.phofis.algorithm.Algorithm;
import org.phofis.algorithm.FirstInFirstOut;
import org.phofis.algorithm.MaxThroughput;

@Getter
@Setter
public class SimulationConfig {
    private static final Logger LOGGER = LogManager.getLogger(SimulationConfig.class);
    private Algorithm currentAlgorithm;
    private final Integer roadWidth;
    public SimulationConfig(String algorithmName, Integer roadWidth) {
        this.roadWidth = roadWidth;
        switch (algorithmName) {
            case "maxThroughput":
                LOGGER.info("Choosing maxThroughput algorithm");
                this.currentAlgorithm = new MaxThroughput();
                break;
            case "firstInFirstOut":
            default:
                LOGGER.info("Choosing firstInFirstOut algorithm");
                this.currentAlgorithm = new FirstInFirstOut();
                break;
        }

    }
}
