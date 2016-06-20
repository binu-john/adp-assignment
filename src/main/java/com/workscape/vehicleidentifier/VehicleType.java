package com.workscape.vehicleidentifier;

/**
 * Created by Binu on 6/19/2016.
 */
public enum VehicleType {
    BIG_WHEEL(Vehicle.Material.PLASTIC, Vehicle.PowerTrain.HUMAN, Vehicle.Material.PLASTIC,
            new Vehicle.WheelPosition[] {Vehicle.WheelPosition.FRONT, Vehicle.WheelPosition.LEFT_REAR, Vehicle.WheelPosition.RIGHT_REAR}),
    BICYCLE (Vehicle.Material.METAL, Vehicle.PowerTrain.HUMAN, Vehicle.Material.METAL,
            new Vehicle.WheelPosition[] {Vehicle.WheelPosition.FRONT, Vehicle.WheelPosition.REAR}),
    MOTORCYCLE (Vehicle.Material.METAL, Vehicle.PowerTrain.INTERNAL_COMBUSTION, Vehicle.Material.METAL,
            new Vehicle.WheelPosition[] {Vehicle.WheelPosition.FRONT, Vehicle.WheelPosition.REAR}),
    HANG_GLIDER (Vehicle.Material.PLASTIC, Vehicle.PowerTrain.BERNOULLI, null,
            new Vehicle.WheelPosition[] {}),
    CAR (Vehicle.Material.METAL, Vehicle.PowerTrain.INTERNAL_COMBUSTION, null,
            new Vehicle.WheelPosition[] {Vehicle.WheelPosition.RIGHT_FRONT, Vehicle.WheelPosition.LEFT_FRONT, Vehicle.WheelPosition.RIGHT_REAR, Vehicle.WheelPosition.LEFT_REAR}),
    UNKNOWN (null, null, null,
            new Vehicle.WheelPosition[] {});

    private final Vehicle.Material frameMaterial;
    private final Vehicle.PowerTrain powerTrain;
    private final Vehicle.Material wheelMaterial;
    private final Vehicle.WheelPosition[] wheelPositions;

    VehicleType(Vehicle.Material frameMaterial, Vehicle.PowerTrain powerTrain, Vehicle.Material wheelMaterial, Vehicle.WheelPosition[] wheelPositions) {
        this.frameMaterial = frameMaterial;
        this.powerTrain = powerTrain;
        this.wheelMaterial = wheelMaterial;
        this.wheelPositions = wheelPositions;
    }

    public Vehicle.Material getFrameMaterial() {
        return frameMaterial;
    }

    public Vehicle.PowerTrain getPowerTrain() {
        return powerTrain;
    }

    public Vehicle.Material getWheelMaterial() {
        return wheelMaterial;
    }

    public Vehicle.WheelPosition[] getWheelPositions() {
        return wheelPositions;
    }
}
