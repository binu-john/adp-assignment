package com.workscape.vehicleidentifier;

/**
 * Wheel class
 */
class Wheel {
    Vehicle.WheelPosition position;
    Vehicle.Material material;

    public Wheel() {
    }

    public Wheel(Vehicle.WheelPosition position, Vehicle.Material material) {
        this.position = position;
        this.material = material;
    }

    public Vehicle.WheelPosition getPosition() {
        return position;
    }

    public void setPosition(Vehicle.WheelPosition position) {
        this.position = position;
    }

    public Vehicle.Material getMaterial() {
        return material;
    }

    public void setMaterial(Vehicle.Material material) {
        this.material = material;
    }

    public String toString() {
        return (new StringBuilder())
                .append("{")
                .append("position=").append(this.position).append(", ")
                .append("material=").append(this.material)
                .append("}")
                .toString();
    }
}
