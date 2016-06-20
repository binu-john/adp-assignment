package com.workscape.vehicleidentifier;

import org.junit.Test;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * Unit test for Vehicle.
 */
public class VehicleTest {
    /**
     * Test vehicles.xml
     */
    @Test
    public void testVehicleTypeMatch() {

        //Successful identification of vehicle-type
        Vehicle v1 = new Vehicle("id1");
        v1.setFrameMaterial(Vehicle.Material.METAL);
        v1.setPowerTrain(Vehicle.PowerTrain.INTERNAL_COMBUSTION);
        v1.addWheel(new Wheel(Vehicle.WheelPosition.FRONT, Vehicle.Material.METAL));
        v1.addWheel(new Wheel(Vehicle.WheelPosition.REAR, Vehicle.Material.METAL));
        assertEquals(VehicleType.MOTORCYCLE, v1.findVehicleType());

        //Wheel material does not match, failed to identify vehicle-type
        Vehicle v2 = new Vehicle("id2");
        v2.setFrameMaterial(Vehicle.Material.METAL);
        v2.setPowerTrain(Vehicle.PowerTrain.INTERNAL_COMBUSTION);
        v2.addWheel(new Wheel(Vehicle.WheelPosition.FRONT, Vehicle.Material.PLASTIC));
        v2.addWheel(new Wheel(Vehicle.WheelPosition.REAR, Vehicle.Material.PLASTIC));
        assertEquals(VehicleType.UNKNOWN, v2.findVehicleType());

        //Wheel position does not match, failed to identify vehicle-type
        Vehicle v3 = new Vehicle("id3");
        v3.setFrameMaterial(Vehicle.Material.METAL);
        v3.setPowerTrain(Vehicle.PowerTrain.INTERNAL_COMBUSTION);
        v3.addWheel(new Wheel(Vehicle.WheelPosition.FRONT, Vehicle.Material.METAL));
        v3.addWheel(new Wheel(Vehicle.WheelPosition.LEFT_REAR, Vehicle.Material.METAL));
        assertEquals(VehicleType.UNKNOWN, v3.findVehicleType());

        //Car; Wheeel material is ignored; successful identification
        Vehicle v4 = new Vehicle("id4");
        v4.setFrameMaterial(Vehicle.Material.METAL);
        v4.setPowerTrain(Vehicle.PowerTrain.INTERNAL_COMBUSTION);
        v4.addWheel(new Wheel(Vehicle.WheelPosition.LEFT_FRONT, Vehicle.Material.METAL));
        v4.addWheel(new Wheel(Vehicle.WheelPosition.LEFT_REAR, Vehicle.Material.PLASTIC));
        v4.addWheel(new Wheel(Vehicle.WheelPosition.RIGHT_FRONT, null));
        v4.addWheel(new Wheel(Vehicle.WheelPosition.RIGHT_REAR, null));
        assertEquals(VehicleType.CAR, v4.findVehicleType());

        //Hang Glider; no wheels; successful identification
        Vehicle v5 = new Vehicle("id5");
        v5.setFrameMaterial(Vehicle.Material.PLASTIC);
        v5.setPowerTrain(Vehicle.PowerTrain.BERNOULLI);
        assertEquals(VehicleType.HANG_GLIDER, v5.findVehicleType());

        //Hang Glider; with wheels; failed identification
        Vehicle v6 = new Vehicle("id6");
        v6.setFrameMaterial(Vehicle.Material.PLASTIC);
        v6.setPowerTrain(Vehicle.PowerTrain.BERNOULLI);
        v6.addWheel(new Wheel(Vehicle.WheelPosition.FRONT, Vehicle.Material.METAL));
        assertEquals(VehicleType.UNKNOWN, v6.findVehicleType());
    }

}
