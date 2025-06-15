import org.junit.jupiter.api.Test;
import org.phofis.model.Road;
import org.phofis.model.Vehicle;

import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {

    @Test
    void testVehicleCreation() {
        Vehicle vehicle = new Vehicle("vehicle1", Road.SOUTH, Road.NORTH, 1);

        assertEquals("vehicle1", vehicle.getVehicleId());
        assertEquals(Road.SOUTH, vehicle.getStartRoad());
        assertEquals(Road.NORTH, vehicle.getEndRoad());
        assertEquals(1, vehicle.getLaneNumber());
    }

    @Test
    void testVehicleCreationWithNullLaneNumber() {
        Vehicle vehicle = new Vehicle("vehicle2", Road.EAST, Road.WEST, null);

        assertEquals("vehicle2", vehicle.getVehicleId());
        assertEquals(Road.EAST, vehicle.getStartRoad());
        assertEquals(Road.WEST, vehicle.getEndRoad());
        assertEquals(0, vehicle.getLaneNumber());
    }

    @Test
    void testVehicleSetters() {
        Vehicle vehicle = new Vehicle("vehicle3", Road.NORTH, Road.SOUTH, 2);

        vehicle.setVehicleId("newId");
        vehicle.setStartRoad(Road.WEST);
        vehicle.setEndRoad(Road.EAST);
        vehicle.setLaneNumber(3);

        assertEquals("newId", vehicle.getVehicleId());
        assertEquals(Road.WEST, vehicle.getStartRoad());
        assertEquals(Road.EAST, vehicle.getEndRoad());
        assertEquals(3, vehicle.getLaneNumber());
    }
}