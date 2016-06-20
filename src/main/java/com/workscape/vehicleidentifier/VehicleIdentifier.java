package com.workscape.vehicleidentifier;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 * VehicleIdentifier parses an xml file or input stream for Vehicles, identifies the vehicle type of each vehicle,
 * and returns a report.
 */
public class VehicleIdentifier {

    private static final String TAG_VEHICLE = "vehicle";
    private static final String TAG_ID = "id";
    private static final String TAG_FRAME = "frame";
    private static final String TAG_MATERIAL = "material";
    private static final String TAG_WHEEL = "wheel";
    private static final String TAG_POSITION = "position";
    private static final String TAG_POWERTRAIN = "powertrain";

    /**
     * Given the filename (or full file path), reads and parses xml file for Vehicles and returns report
     * @param filename
     * @return VehicleReport
     * @throws FileNotFoundException
     * @throws XMLStreamException
     */
    public VehicleReport parseVehiclesXmlFile(String filename) throws FileNotFoundException, XMLStreamException {
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(filename);
        } catch(FileNotFoundException e) {
            URL resource = getClass().getClassLoader().getResource(filename);
            if(resource == null) {
                throw new FileNotFoundException("ERROR: file not found");
            }
            fileInputStream = new FileInputStream(new File(resource.getFile()));
        }

        return parseVehiclesXml(fileInputStream);
	}

    /**
     * Given the filename (or full file path), reads and parses xml file for Vehicles and returns VehicleReport
     * @param in
     * @return VehicleReport
     * @throws XMLStreamException
     */
    public VehicleReport parseVehiclesXml(InputStream in) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader reader = factory.createXMLEventReader(in);

        Vehicle vehicle;
        VehicleReport report = new VehicleReport();
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();

            if(event.getEventType() == XMLEvent.START_ELEMENT) {
                StartElement element = event.asStartElement();
                if(TAG_VEHICLE.equalsIgnoreCase(element.getName().getLocalPart())) {
                    vehicle = parseVehicle(element, reader);
                    report.addIdentificationResult(vehicle.getId(), vehicle.findVehicleType());
                }
            }
        }

        return report;
    }

    /**
     * This function is called when the Vehicle start-element is encountered. The function reads subsequent elements
     * from the reader util a Vehicle end-element is encountered. The function creates and returns a Vehicle object.
     * @param startElement Vehicle start-element passed to allow function to read attributes (not used now)
     * @param reader xml reader passed to read all xml elements until Vehicle end-element is encountered
     * @return Vehicle
     * @throws XMLStreamException
     */
    private Vehicle parseVehicle(StartElement startElement, XMLEventReader reader) throws XMLStreamException {
        Vehicle vehicle = new Vehicle();
        boolean inIdTag = false;

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();

            if(event.getEventType() == XMLEvent.START_ELEMENT) {
                StartElement element = event.asStartElement();
                if(TAG_FRAME.equalsIgnoreCase(element.getName().getLocalPart())) {
                    vehicle.setFrameMaterial(parseFrame(element, reader));
                } else if(TAG_WHEEL.equalsIgnoreCase(element.getName().getLocalPart())) {
                    vehicle.addWheel(parseWheel(element, reader));
                } else if(TAG_POWERTRAIN.equalsIgnoreCase(element.getName().getLocalPart())) {
                    vehicle.setPowerTrain(parsePowerTrain(element, reader));
                }else if(TAG_ID.equalsIgnoreCase(element.getName().getLocalPart())) {
                    inIdTag = true;
                }
            } else if(event.getEventType() == XMLEvent.END_ELEMENT) {
                EndElement element = event.asEndElement();
                if(TAG_VEHICLE.equalsIgnoreCase(element.getName().getLocalPart())) {
                    break;
                } else if(TAG_ID.equalsIgnoreCase(element.getName().getLocalPart())) {
                    inIdTag = false;
                }
            } else if(inIdTag && event.getEventType() == XMLEvent.CHARACTERS) {
                vehicle.setId(event.asCharacters().getData());
            }
        }

        return vehicle;
    }

    /**
     * This function is called when the Frame start-element is encountered in the Vehicle block. The function returns
     * Vehicle.Material used for the frame.
     * @param startElement Frame start-element passed to allow function to read attributes (not used now)
     * @param reader xml reader passed to read all xml elements until Frame end-element is encountered
     * @return Vehicle.Material
     * @throws XMLStreamException
     */
    private Vehicle.Material parseFrame(StartElement startElement, XMLEventReader reader) throws XMLStreamException {
        Vehicle.Material frameMaterial = null;
        boolean inMaterialTag = false;

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();

            if(event.getEventType() == XMLEvent.START_ELEMENT) {
                StartElement element = event.asStartElement();
                if(TAG_MATERIAL.equalsIgnoreCase(element.getName().getLocalPart())) {
                    inMaterialTag = true;
                }
            } else if(event.getEventType() == XMLEvent.END_ELEMENT) {
                EndElement element = event.asEndElement();
                if(TAG_FRAME.equalsIgnoreCase(element.getName().getLocalPart())) {
                    break;
                } else if(TAG_MATERIAL.equalsIgnoreCase(element.getName().getLocalPart())) {
                    inMaterialTag = false;
                }
            } else if(inMaterialTag && event.getEventType() == XMLEvent.CHARACTERS) {
                String data = event.asCharacters().getData();
                if(data != null) {
                    frameMaterial = Vehicle.Material.valueOf(toEnumStr(data));
                }
            }
        }

        return frameMaterial;
    }

    /**
     * This function is called when the Wheel start-element is encountered in the Vehicle block. The function returns
     * Wheel object.
     * @param startElement Wheel start-element passed to allow function to read attributes (not used now)
     * @param reader xml reader passed to read all xml elements until Wheel end-element is encountered
     * @return Wheel
     * @throws XMLStreamException
     */
    private Wheel parseWheel(StartElement startElement, XMLEventReader reader) throws XMLStreamException {
        Wheel wheel = new Wheel();
        boolean inPositionTag = false, inMaterialTag = false;

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();

            if(event.getEventType() == XMLEvent.START_ELEMENT) {
                StartElement element = event.asStartElement();
                if(TAG_POSITION.equalsIgnoreCase(element.getName().getLocalPart())) {
                    inPositionTag = true;
                } else if(TAG_MATERIAL.equalsIgnoreCase(element.getName().getLocalPart())) {
                    inMaterialTag = true;
                }
            } else if(event.getEventType() == XMLEvent.END_ELEMENT) {
                EndElement element = event.asEndElement();
                if(TAG_WHEEL.equalsIgnoreCase(element.getName().getLocalPart())) {
                    break;
                } else if(TAG_POSITION.equalsIgnoreCase(element.getName().getLocalPart())) {
                    inPositionTag = false;
                } else if(TAG_MATERIAL.equalsIgnoreCase(element.getName().getLocalPart())) {
                    inMaterialTag = false;
                }
            } else if(event.getEventType() == XMLEvent.CHARACTERS) {
                String data = event.asCharacters().getData();
                if(data != null && inMaterialTag) {
                    wheel.setMaterial(Vehicle.Material.valueOf(toEnumStr(data)));
                } else if(data != null && inPositionTag) {
                    wheel.setPosition(Vehicle.WheelPosition.valueOf(toEnumStr(data)));
                }
            }
        }

        return wheel;
    }

    /**
     * This function is called when the PowerTrain start-element is encountered in the Vehicle block. The function returns
     * PowerTrain enum.
     * @param startElement PowerTrain start-element passed to allow function to read attributes (not used now)
     * @param reader xml reader passed to read all xml elements until PowerTrain end-element is encountered
     * @return Vehicle.PowerTrain
     * @throws XMLStreamException
     */
    private Vehicle.PowerTrain parsePowerTrain(StartElement startElement, XMLEventReader reader) throws XMLStreamException {
        Vehicle.PowerTrain powerTrain = null;

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();

            if(event.getEventType() == XMLEvent.START_ELEMENT) {
                String elementName = event.asStartElement().getName().getLocalPart();
                powerTrain = Vehicle.PowerTrain.valueOf(toEnumStr(elementName));
            } else if((event.getEventType() == XMLEvent.END_ELEMENT)
                    && (TAG_POWERTRAIN.equalsIgnoreCase(event.asEndElement().getName().getLocalPart()))){
                break;
            }
        }

        return powerTrain;
    }

    /**
     * Utility function to convert xml data string to enum value
     * @param in
     * @return String
     */
    private String toEnumStr(String in) {
        return in.toUpperCase().replace(' ', '_');
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Main

    public static void main(String[] args) {
        String filename = "vehicles.xml";
        if(args.length > 0) {
            filename = args[0];
        }
        System.out.println("Processing vehicles xml file : " + filename);

        VehicleIdentifier vehicleIdentifier = new VehicleIdentifier();
        VehicleReport report = null;
        try {
            report = vehicleIdentifier.parseVehiclesXmlFile(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

        System.out.println("Finished processing xml");
        printVehicleReport(report);
    }

    private static void printVehicleReport(VehicleReport report) {
        Map<String, VehicleType> results = report.getIdentificationResults();
        System.out.println("\nVehicles identified, total " + results.size());
        for(String vehicleId : results.keySet()) {
            VehicleType vehicleType = results.get(vehicleId);
            System.out.println(vehicleId + " is " + ((vehicleType == null)? "Unknown" : vehicleType));
        }

        System.out.println("\nSummary of results");
        Map<VehicleType, Integer> summary = report.getSummary();
        for(VehicleType vehicleType : summary.keySet()) {
            System.out.println(((vehicleType == null)? "Unknown" : vehicleType) + " count = " + summary.get(vehicleType));
        }
    }
}
