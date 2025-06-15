import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.phofis.algorithm.FirstInFirstOut;
import org.phofis.algorithm.FirstInFirstOutSmart;
import org.phofis.simulation.SimulationConfig;

public class SimulationConfigTest {
    
    @Test
    void testSmartAlgorithmFallsBackToDefaultWithInvalidRoadWidth() {
        // Test with roadWidth > 1 (invalid for smart algorithm)
        SimulationConfig config = new SimulationConfig("smart", 2);
        
        // Verify that the algorithm falls back to default (FirstInFirstOut)
        assertNotNull(config.getAlgorithm());
        assertTrue(config.getAlgorithm() instanceof FirstInFirstOut);
        assertFalse(config.getAlgorithm() instanceof FirstInFirstOutSmart);
        
        // Verify roadWidth is still set correctly
        assertEquals(2, config.getRoadWidth());
    }
    
    @Test
    void testSmartAlgorithmWorksWithValidRoadWidth() {
        // Test with roadWidth = 1 (valid for smart algorithm)
        SimulationConfig config = new SimulationConfig("smart", 1);
        
        // Verify that the algorithm is set to FirstInFirstOutSmart
        assertNotNull(config.getAlgorithm());
        assertTrue(config.getAlgorithm() instanceof FirstInFirstOutSmart);
        assertFalse(config.getAlgorithm() instanceof FirstInFirstOut);
        
        // Verify roadWidth is set correctly
        assertEquals(1, config.getRoadWidth());
    }
    
    @Test
    void testOtherAlgorithmsUnaffectedByRoadWidth() {
        // Test that other algorithms are not affected by roadWidth
        String[] algorithms = {"firstInFirstOut", "dynamic", "maxThroughput", "unknown"};
        int[] roadWidths = {1, 2, 3, 5};
        
        for (String algorithmName : algorithms) {
            for (int roadWidth : roadWidths) {
                SimulationConfig config = new SimulationConfig(algorithmName, roadWidth);
                
                // All should work regardless of roadWidth
                assertNotNull(config.getAlgorithm(),
                    "Algorithm should not be null for " + algorithmName + " with roadWidth: " + roadWidth);
                assertEquals(roadWidth, config.getRoadWidth(),
                    "RoadWidth should be preserved for " + algorithmName + ": " + roadWidth);
                
                // None of these should be FirstInFirstOutSmart
                assertFalse(config.getAlgorithm() instanceof FirstInFirstOutSmart,
                    algorithmName + " should not be FirstInFirstOutSmart");
            }
        }
    }
    
    @Test
    void testBoundaryConditions() {
        // Test edge cases
        
        // Test with roadWidth = 0 (edge case)
        SimulationConfig configZero = new SimulationConfig("smart", 0);
        assertTrue(configZero.getAlgorithm() instanceof FirstInFirstOutSmart);
        assertEquals(1, configZero.getRoadWidth());
        
        // Test with negative roadWidth (edge case)
        SimulationConfig configNegative = new SimulationConfig("smart", -1);
        assertTrue(configNegative.getAlgorithm() instanceof FirstInFirstOutSmart);
        assertEquals(1, configNegative.getRoadWidth());
        
        // Test with very large roadWidth
        SimulationConfig configLarge = new SimulationConfig("smart", 1000);
        assertTrue(configLarge.getAlgorithm() instanceof FirstInFirstOut);
        assertEquals(1000, configLarge.getRoadWidth());
    }
}