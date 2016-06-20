package com.workscape.vehicleidentifier;

import java.util.HashMap;
import java.util.Map;

import com.workscape.vehicleidentifier.VehicleType;

/**
 * This class represents the result of processing the vehicles (vehicles xml)
 */
public class VehicleReport {
    //A map keyed by VehicleId, holding the VehicleType identified for each input Vehicle
    Map<String, VehicleType> identificationResults = new HashMap<String, VehicleType>();

    //Summary results holding the count of vehicles by VehicleType
    Map<VehicleType, Integer> summary = new HashMap<VehicleType, Integer>();

    public Map<String, VehicleType> getIdentificationResults() {
        return identificationResults;
    }

    public void setIdentificationResults(Map<String, VehicleType> identificationResults) {
        this.identificationResults = identificationResults;
    }

    public Map<VehicleType, Integer> getSummary() {
        return summary;
    }

    public void setSummary(Map<VehicleType, Integer> summary) {
        this.summary = summary;
    }

    /**
     * Adds a vehicle and its type to the report
     * @param vehicleId
     * @param vehicleType
     */
    public void addIdentificationResult(String vehicleId, VehicleType vehicleType) {
        removeIdentificationResult(vehicleId);

        //add identification result for given vehicle-id, and increment summary count
        identificationResults.put(vehicleId, vehicleType);

        Integer count = summary.get(vehicleType);
        count = (count == null)? new Integer(1) : new Integer(count.intValue() + 1);
        summary.put(vehicleType, count);
    }

    /**
     * Removes a vehicle from the report
     * @param vehicleId
     */
    public void removeIdentificationResult(String vehicleId) {
        VehicleType existing = identificationResults.get(vehicleId);

        if(existing == null) {
            VehicleType vehicleType = identificationResults.get(vehicleId);
            identificationResults.remove(vehicleId);

            Integer count = summary.get(vehicleType);
            if(count != null) {
                count = new Integer(count.intValue() - 1);
                summary.put(vehicleType, count);
            }
        }
    }
}
