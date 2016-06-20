package com.workscape.vehicleidentifier;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Class represents Vehicle and its components
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

    /**
     * Given a VeehicleType, this function checks is this vehicle matches with the same.
     * @param vehicleType
     * @return boolean
     */
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

    /**
     * Iterates through the configured VehicleTypes and finds a match for this vehicle. If match not found, returns VehicleType.UNKNOWN
     * @return VehicleType
     */
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
