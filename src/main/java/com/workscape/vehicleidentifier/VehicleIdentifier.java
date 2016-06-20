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
 * Hello world!
 * 
 */
public class VehicleIdentifier {

    private static final String TAG_VEHICLE = "vehicle";
    private static final String TAG_ID = "id";
    private static final String TAG_FRAME = "frame";
    private static final String TAG_MATERIAL = "material";
    private static final String TAG_WHEEL = "wheel";
    private static final String TAG_POSITION = "position";
    private static final String TAG_POWERTRAIN = "powertrain";

    public VehicleReport parseVehiclesXmlFile(String filename) {
        VehicleReport report = null;
        try {
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
			report = parseVehiclesXml(fileInputStream);
        } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
        return report;
	}

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
        VehicleReport report = vehicleIdentifier.parseVehiclesXmlFile(filename);
        System.out.println("Finished processing xml");
        printVehicleReport(report);
    }

    private static void printVehicleReport(VehicleReport report) {
        Map<String, Vehicle.VehicleType> results = report.getIdentificationResults();
        System.out.println("\nVehicles identified, total " + results.size());
        for(String vehicleId : results.keySet()) {
            Vehicle.VehicleType vehicleType = results.get(vehicleId);
            System.out.println(vehicleId + " is " + ((vehicleType == null)? "Unknown" : vehicleType));
        }

        System.out.println("\nSummary of results");
        Map<Vehicle.VehicleType, Integer> summary = report.getSummary();
        for(Vehicle.VehicleType vehicleType : summary.keySet()) {
            System.out.println(((vehicleType == null)? "Unknown" : vehicleType) + " count = " + summary.get(vehicleType));
        }
    }
}
