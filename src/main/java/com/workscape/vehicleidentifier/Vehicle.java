package com.workscape.vehicleidentifier;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Binu on 6/17/2016.
 */
class Vehicle {

    public enum Material {
        PLASTIC, METAL
    }

    public enum PowerTrain {
        HUMAN, INTERNAL_COMBUSTION, BERNOULLI
    }

    public enum WheelPosition {
        FRONT, REAR, RIGHT_REAR, LEFT_REAR, RIGHT_FRONT, LEFT_FRONT
    }

    public enum VehicleType {
        BIG_WHEEL(Material.PLASTIC, PowerTrain.HUMAN, Material.PLASTIC,
                    new WheelPosition[] {WheelPosition.FRONT, WheelPosition.LEFT_REAR, WheelPosition.RIGHT_REAR}),
        BICYCLE (Material.METAL, PowerTrain.HUMAN, Material.METAL,
                new WheelPosition[] {WheelPosition.FRONT, WheelPosition.REAR}),
        MOTORCYCLE (Material.METAL, PowerTrain.INTERNAL_COMBUSTION, Material.METAL,
                new WheelPosition[] {WheelPosition.FRONT, WheelPosition.REAR}),
        HANG_GLIDER (Material.PLASTIC, PowerTrain.BERNOULLI, null,
                new WheelPosition[] {}),
        CAR (Material.METAL, PowerTrain.INTERNAL_COMBUSTION, null,
                new WheelPosition[] {WheelPosition.RIGHT_FRONT, WheelPosition.LEFT_FRONT, WheelPosition.RIGHT_REAR, WheelPosition.LEFT_REAR}),
        UNKNOWN (null, null, null,
                new WheelPosition[] {});

        private final Material frameMaterial;
        private final PowerTrain powerTrain;
        private final Material wheelMaterial;
        private final WheelPosition[] wheelPositions;

        VehicleType(Material frameMaterial, PowerTrain powerTrain, Material wheelMaterial, WheelPosition[] wheelPositions) {
            this.frameMaterial = frameMaterial;
            this.powerTrain = powerTrain;
            this.wheelMaterial = wheelMaterial;
            this.wheelPositions = wheelPositions;
        }

        public Material getFrameMaterial() {
            return frameMaterial;
        }

        public PowerTrain getPowerTrain() {
            return powerTrain;
        }

        public Material getWheelMaterial() {
            return wheelMaterial;
        }

        public WheelPosition[] getWheelPositions() {
            return wheelPositions;
        }
    }

    private String id;
    private Material frameMaterial;
    private Map<WheelPosition, Wheel> wheels;
    private PowerTrain powerTrain;

    public Vehicle() {
        this.wheels = new HashMap<WheelPosition, Wheel>();
    }

    public Vehicle(String id) {
        this();
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Material getFrameMaterial() {
        return frameMaterial;
    }

    public void setFrameMaterial(Material frameMaterial) {
        this.frameMaterial = frameMaterial;
    }

    public Map<WheelPosition, Wheel> getWheels() {
        return wheels;
    }

    public void setWheels(Map<WheelPosition, Wheel> wheels) {
        this.wheels = wheels;
    }

    public PowerTrain getPowerTrain() {
        return powerTrain;
    }

    public void setPowerTrain(PowerTrain powerTrain) {
        this.powerTrain = powerTrain;
    }

    public void addWheel(Wheel wheel) {
        this.wheels.put(wheel.getPosition(), wheel);
    }

    public String toString() {
        return (new StringBuilder())
                .append("{")
                .append("id=").append(this.id).append(", ")
                .append("frameMaterial=").append(this.frameMaterial).append(", ")
                .append("powerTrain=").append(this.powerTrain).append(", ")
                .append("wheels=").append(Arrays.toString(wheels.values().toArray()))
                .append("}")
                .toString();
    }

    private boolean doesVehicleMatch(VehicleType vehicleType) {
        boolean match = true;

        //check if the vehicle attributes and the number of wheels match
        if((vehicleType.getFrameMaterial() != this.frameMaterial)
                || (vehicleType.getPowerTrain() != this.powerTrain)
                || (vehicleType.getWheelPositions().length != this.wheels.size())) {
            match = false;
        }

        //additionally check for match on the position and material (if specified) of the wheels
        if(match) {
            Material expectedWheelMaterial = vehicleType.getWheelMaterial();

            for (WheelPosition wp : vehicleType.getWheelPositions()) {
                Wheel wheel = this.wheels.get(wp);
                if ((wheel == null)
                        || ((expectedWheelMaterial != null) && (expectedWheelMaterial != wheel.getMaterial()))) {
                    match = false;
                    break;
                }
            }
        }

        return match;
    }

    public VehicleType findVehicleType() {
        VehicleType type = VehicleType.UNKNOWN;

        for (VehicleType vt : VehicleType.values()) {
            if(VehicleType.UNKNOWN == vt) continue;

            if(doesVehicleMatch(vt)) {
                type = vt;
                break;
            }
        }
        return type;
    }


}
