package org.phofis.simulation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.phofis.model.StepStatus;
import org.phofis.model.StepStatuses;
import org.phofis.model.Vehicle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private static final Logger LOGGER = LogManager.getLogger(Simulation.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static boolean run(File inputJsonFile, File outputJsonFile) {
        LOGGER.info("Starting");
        JsonNode rootNode;
        try {
            rootNode = MAPPER.readTree(inputJsonFile);
        } catch (IOException e) {
            LOGGER.error("Error reading input file");
            return false;
        }

        if (rootNode.get("commands") == null || !rootNode.get("commands").isArray()) {
            LOGGER.error("Invalid commands file");
            return false;
        }

        JsonNode commandsNode = rootNode.get("commands");
        ArrayNode commands = (ArrayNode) commandsNode;
        JsonNode config = commands.get(0);

        SimulationState simulationState;
        SimulationConfig simulationConfig;
        if (config != null && config.get("type") != null && config.get("type").asText().equals("config")) {
            LOGGER.info("Loading configuration from file");
            simulationState = new SimulationState(config.get("roadWidth").asInt());
            simulationConfig = new SimulationConfig(config.get("algorithm").asText(), config.get("roadWidth").asInt());
            commands.remove(0);
        } else {
            LOGGER.info("Default configuration");
            simulationState = new SimulationState(1);
            simulationConfig = new SimulationConfig("firstInFirstOut", 1);
        }

        ArrayList<StepStatus> stepStatuses = new ArrayList<>();

        for (JsonNode command : commands) {
            switch (command.get("type").asText()) {
                case "addVehicle":
                    try {
                        LOGGER.info("Adding vehicle{}", command.asText());
                        simulationState.addVehicle(MAPPER.treeToValue(command, Vehicle.class));
                    } catch (JsonProcessingException e) {
                        LOGGER.error("Error parsing vehicle command", e);
                    }
                    break;
                case "step":
                    LOGGER.info("Step");
                    List<String> leftVehicles = simulationConfig.getAlgorithm().runSimulationStep(simulationState);
                    if (leftVehicles == null) {
                        LOGGER.error("Simulation step failed");
                    }
                    stepStatuses.add(new StepStatus(leftVehicles));
                    break;
                default:
                    LOGGER.error("Unknown command");
                    break;
            }
        }
        try {
            MAPPER.writerWithDefaultPrettyPrinter().writeValue(outputJsonFile, new StepStatuses(stepStatuses));
        } catch (IOException e) {
            LOGGER.error("Error writing output file", e);
        }

        return true;
    }
}
