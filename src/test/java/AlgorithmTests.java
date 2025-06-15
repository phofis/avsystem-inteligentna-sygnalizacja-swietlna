import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.phofis.algorithm.*;
import org.phofis.model.Road;
import org.phofis.model.Vehicle;
import org.phofis.simulation.SimulationState;
import java.util.List;

public class AlgorithmTests {

    // FirstInFirstOut Tests
    @Nested
    class FirstInFirstOutTest {
        private FirstInFirstOut algorithm;
        
        @BeforeEach
        void setUp() {
            algorithm = new FirstInFirstOut();
        }
        
        @Test
        void testSingleLane() {
            SimulationState sim = new SimulationState(1);
            Vehicle v1 = new Vehicle("v1", Road.NORTH, Road.SOUTH, 0);
            Vehicle v2 = new Vehicle("v2", Road.SOUTH, Road.NORTH, 0);
            
            sim.addVehicle(v1);
            sim.addVehicle(v2);
            
            List<String> result = algorithm.runSimulationStep(sim);
            
            assertEquals(2, result.size());
            assertTrue(result.contains("v1"));
            assertTrue(result.contains("v2"));
        }
        
        @Test
        void testMultiLane() {
            SimulationState sim = new SimulationState(3);
            Vehicle v1 = new Vehicle("v1", Road.NORTH, Road.SOUTH, 0);
            Vehicle v2 = new Vehicle("v2", Road.NORTH, Road.SOUTH, 1);
            Vehicle v3 = new Vehicle("v3", Road.SOUTH, Road.NORTH, 0);
            
            sim.addVehicle(v1);
            sim.addVehicle(v2);
            sim.addVehicle(v3);
            
            List<String> result = algorithm.runSimulationStep(sim);
            
            assertEquals(3, result.size());
        }
        
        @Test
        void testEmptyQueue() {
            SimulationState sim = new SimulationState(1);
            List<String> result = algorithm.runSimulationStep(sim);
            
            assertTrue(result.isEmpty());
        }
        
        @Test
        void testPriorityVehicleSelection() {
            SimulationState sim = new SimulationState(1);
            Vehicle v1 = new Vehicle("v1", Road.NORTH, Road.EAST, 0);
            Vehicle v2 = new Vehicle("v2", Road.WEST, Road.SOUTH, 0);
            
            sim.addVehicle(v1);
            sim.addVehicle(v2);
            
            List<String> result = algorithm.runSimulationStep(sim);
            
            assertFalse(result.isEmpty());
        }
    }

    // MaxThroughput Tests
    @Nested
    class MaxThroughputTest {
        private MaxThroughput algorithm;
        
        @BeforeEach
        void setUp() {
            algorithm = new MaxThroughput();
        }
        
        @Test
        void testSingleLaneMaxThroughput() {
            SimulationState sim = new SimulationState(1);
            Vehicle v1 = new Vehicle("v1", Road.SOUTH, Road.EAST, 0);
            Vehicle v2 = new Vehicle("v2", Road.EAST, Road.SOUTH, 0);
            Vehicle v3 = new Vehicle("v3", Road.NORTH, Road.WEST, 0);

            sim.addVehicle(v3);
            sim.addVehicle(v1);
            sim.addVehicle(v2);

            
            List<String> result = algorithm.runSimulationStep(sim);
            
            assertFalse(result.isEmpty());
        }
        
        @Test
        void testMultiLaneMaxThroughput() {
            SimulationState sim = new SimulationState(2);
            Vehicle v1 = new Vehicle("v1", Road.SOUTH, Road.EAST, 0);
            Vehicle v2 = new Vehicle("v2", Road.SOUTH, Road.EAST, 1);
            Vehicle v3 = new Vehicle("v3", Road.EAST, Road.SOUTH, 0);
            Vehicle v4 = new Vehicle("v4", Road.WEST, Road.SOUTH, 1);
            
            sim.addVehicle(v1);
            sim.addVehicle(v2);
            sim.addVehicle(v3);
            sim.addVehicle(v4);
            
            List<String> result = algorithm.runSimulationStep(sim);
            
            assertEquals(3, result.size());
        }
        
        @Test
        void testEmergencyVehiclePriority() {
            SimulationState sim = new SimulationState(1);
            Vehicle emergency = new Vehicle("emergency1", Road.NORTH, Road.SOUTH, 0);
            Vehicle regular = new Vehicle("regular1", Road.SOUTH, Road.EAST, 0);
            
            sim.addVehicle(regular);
            sim.addVehicle(emergency);
            
            List<String> result = algorithm.runSimulationStep(sim);
            
            assertTrue(result.contains("emergency1"));
        }
        
        @Test
        void testStepSelection() {
            SimulationState sim = new SimulationState(1);
            Vehicle v1 = new Vehicle("v1", Road.NORTH, Road.EAST, 0);
            Vehicle v2 = new Vehicle("v2", Road.EAST, Road.NORTH, 0);
            Vehicle v3 = new Vehicle("v3", Road.SOUTH, Road.WEST, 0);
            
            sim.addVehicle(v1);
            sim.addVehicle(v2);
            sim.addVehicle(v3);
            
            List<String> result = algorithm.runSimulationStep(sim);
            
            assertEquals(2, result.size());
            assertTrue(result.contains("v1"));
            assertTrue(result.contains("v2"));
        }
    }

    // Dynamic Tests
    @Nested
    class DynamicTest {
        private Dynamic algorithm;
        
        @BeforeEach
        void setUp() {
            algorithm = new Dynamic();
        }
        
        @Test
        void testMultiLaneLowTraffic() {
            SimulationState sim = new SimulationState(2);
            for (int i = 0; i < 5; i++) {
                Vehicle v = new Vehicle("v" + i, Road.NORTH, Road.SOUTH, i % 2);
                sim.addVehicle(v);
            }
            
            List<String> result = algorithm.runSimulationStep(sim);
            
            assertFalse(result.isEmpty());
        }
        
        @Test
        void testSingleLaneHighTraffic() {
            SimulationState sim = new SimulationState(1);
            
            for (int i = 0; i < 15; i++) {
                Road start = Road.values()[i % 4];
                Road end = Road.values()[(i + 1) % 4];
                Vehicle v = new Vehicle("v" + i, start, end, 0);
                sim.addVehicle(v);
            }
            
            List<String> result = algorithm.runSimulationStep(sim);
            
            assertFalse(result.isEmpty());
        }
        
        @Test
        void testMultiLaneHighTraffic() {
            SimulationState sim = new SimulationState(3);
            
            for (int i = 0; i < 35; i++) {
                Road start = Road.values()[i % 4];
                Road end = Road.values()[(i + 2) % 4];
                Vehicle v = new Vehicle("v" + i, start, end, i % 3);
                sim.addVehicle(v);
            }
            
            List<String> result = algorithm.runSimulationStep(sim);
            
            assertFalse(result.isEmpty());
        }
        
        @Test
        void testThresholdBehavior() {
            SimulationState sim = new SimulationState(2);
            
            for (int i = 0; i < 21; i++) {
                Vehicle v = new Vehicle("v" + i, Road.SOUTH, Road.EAST, i % 2);
                sim.addVehicle(v);
            }
            
            List<String> result = algorithm.runSimulationStep(sim);
            
            assertFalse(result.isEmpty());
        }
    }

    @Nested
    class FIFOSmart{
        private FirstInFirstOutSmart algorithm;
        private SimulationState simulationState;

        @BeforeEach
        void setUp() {
            algorithm = new FirstInFirstOutSmart();
            simulationState = new SimulationState(1); // roadWidth = 1 (required for smart algorithm)
        }

        @Test
        void testFourVehiclesGoingRightOneStep() {
            // Create 4 vehicles, each on a different starting road, all going right
            Vehicle vehicleFromNorth = new Vehicle("vehicle1", Road.NORTH, Road.WEST, 0);
            Vehicle vehicleFromEast = new Vehicle("vehicle2", Road.EAST, Road.NORTH, 0);
            Vehicle vehicleFromSouth = new Vehicle("vehicle3", Road.SOUTH, Road.EAST, 0);
            Vehicle vehicleFromWest = new Vehicle("vehicle4", Road.WEST, Road.SOUTH, 0);

            simulationState.addVehicle(vehicleFromNorth);
            simulationState.addVehicle(vehicleFromEast);
            simulationState.addVehicle(vehicleFromSouth);
            simulationState.addVehicle(vehicleFromWest);

            List<String> leftVehicles = algorithm.runSimulationStep(simulationState);

            assertEquals(4, leftVehicles.size());

            // Verify all vehicles have left
            assertTrue(leftVehicles.contains("vehicle1"));
            assertTrue(leftVehicles.contains("vehicle2"));
            assertTrue(leftVehicles.contains("vehicle3"));
            assertTrue(leftVehicles.contains("vehicle4"));

            // Verify waiting vehicles list is updated
            assertEquals(0, simulationState.getWaitingVehiclesID().size());

        }

        @Test
        void testEmptySimulationStep() {
            // Test with no vehicles
            List<String> leftVehicles = algorithm.runSimulationStep(simulationState);

            assertNotNull(leftVehicles);
            assertTrue(leftVehicles.isEmpty());
            assertEquals(0, simulationState.getWaitingVehiclesID().size());
        }

        @Test
        void testSmartAlgorithmWithSingleLaneRequirement() {
            // Verify that the smart algorithm works only with roadWidth = 1
            // This test ensures our setup is correct for the smart algorithm
            assertEquals(1, simulationState.getLanes().get(Road.NORTH).size());
            assertEquals(1, simulationState.getLanes().get(Road.EAST).size());
            assertEquals(1, simulationState.getLanes().get(Road.SOUTH).size());
            assertEquals(1, simulationState.getLanes().get(Road.WEST).size());
        }
    }
}