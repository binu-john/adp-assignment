package com.workscape.vehicleidentifier;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the result of processing the vehicles (vehicles xml)
 */
public class VehicleReport {
    //A map keyed by VehicleId, holding the VehicleType identified for each input Vehicle
    Map<String, Vehicle.VehicleType> identificationResults = new HashMap<String, Vehicle.VehicleType>();

    //Summary results holding the count of vehicles by VehicleType
    Map<Vehicle.VehicleType, Integer> summary = new HashMap<Vehicle.VehicleType, Integer>();

    public Map<String, Vehicle.VehicleType> getIdentificationResults() {
        return identificationResults;
    }

    public void setIdentificationResults(Map<String, Vehicle.VehicleType> identificationResults) {
        this.identificationResults = identificationResults;
    }

    public Map<Vehicle.VehicleType, Integer> getSummary() {
        return summary;
    }

    public void setSummary(Map<Vehicle.VehicleType, Integer> summary) {
        this.summary = summary;
    }

    public void addIdentificationResult(String vehicleId, Vehicle.VehicleType vehicleType) {
        removeIdentificationResult(vehicleId);

        //add identification result for given vehicle-id, and increment summary count
        identificationResults.put(vehicleId, vehicleType);

        Integer count = summary.get(vehicleType);
        count = (count == null)? new Integer(1) : new Integer(count.intValue() + 1);
        summary.put(vehicleType, count);
    }

    public void removeIdentificationResult(String vehicleId) {
        Vehicle.VehicleType existing = identificationResults.get(vehicleId);

        if(existing == null) {
            Vehicle.VehicleType vehicleType = identificationResults.get(vehicleId);
            identificationResults.remove(vehicleId);

            Integer count = summary.get(vehicleType);
            if(count != null) {
                count = new Integer(count.intValue() - 1);
                summary.put(vehicleType, count);
            }
        }
    }
}
