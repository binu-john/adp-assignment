package com.workscape.vehicleidentifier;

import com.sun.deploy.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Binu on 6/17/2016.
 */
public class Vehicle {

    public enum VehicleType {
        BIG_WHEEL, BICYCLE, MOTORCYCLE, CAR
    }

    public enum Material {
        PLASTIC, METAL
    }

    public enum PowerTrain {
        HUMAN, INTERNAL_COMBUSTION
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


}
