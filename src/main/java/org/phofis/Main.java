package org.phofis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


import java.io.*;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
        LOGGER.info("Starting");
        if (Simulation.run(new File(args[0]), new File(args[1])))
            LOGGER.info("Simulation finished successfully");
        else
            LOGGER.error("Simulation failed");
    }
}