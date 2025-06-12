package org.phofis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Simulation {
    private static final Logger LOGGER = LogManager.getLogger(Simulation.class);
    public static boolean run(File inputJsonFile, File outputJsonFile) {
        LOGGER.info("Starting");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = mapper.readTree(inputJsonFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonNode commands = rootNode.get("commands");
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        if(commands == null || !commands.isArray()){
            LOGGER.error("Invalid commands file");
            return false;
        }

        for (JsonNode command : commands) {
            switch (command.get("type").asText()) {
                case "addVehicle":
                    try {
                        LOGGER.info("Adding vehicle{}", command.asText());
                        vehicles.add(mapper.treeToValue(command, Vehicle.class));
                    } catch (JsonProcessingException e) {
                        LOGGER.error("Error parsing command line", e);
                    }
                    break;
                case "step":
                    LOGGER.info("Step");
                    break;
            }
        }

        return true;
    }
}
