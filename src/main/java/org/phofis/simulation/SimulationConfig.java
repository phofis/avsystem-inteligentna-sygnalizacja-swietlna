package org.phofis.simulation;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.phofis.algorithm.*;

@Getter
@Setter
public class SimulationConfig {
    private static final Logger LOGGER = LogManager.getLogger(SimulationConfig.class);
    private static final Algorithm DEFAULT = new FirstInFirstOut();
    private final Algorithm algorithm;
    private final Integer roadWidth;
    public SimulationConfig(String algorithmName, Integer roadWidth) {
        if(roadWidth<=0){
            LOGGER.error("Invalid road width, setting to 1");
            this.roadWidth = 1;
        }else{
            this.roadWidth = roadWidth;
        }
        switch (algorithmName) {
            case "smart":
                LOGGER.info("Choosing smartFIFO algorithm");
                if(this.roadWidth>1){
                    LOGGER.error("Invalid road width for chosen algorithm");
                    LOGGER.error("Switching to default algorithm");
                    this.algorithm = DEFAULT;
                    break;
                }
                this.algorithm = new FirstInFirstOutSmart();
                break;
            case "dynamic":
                LOGGER.info("Choosing dynamic algorithm");
                this.algorithm = new Dynamic();
                break;
            case "maxThroughput":
                LOGGER.info("Choosing maxThroughput algorithm");
                this.algorithm = new MaxThroughput();
                break;
            case "firstInFirstOut":
                LOGGER.info("Choosing firstInFirstOut algorithm");
                this.algorithm = new FirstInFirstOut();
                break;
            default:
                LOGGER.info("Choosing default algorithm");
                this.algorithm = DEFAULT;
                break;
        }

    }
}
