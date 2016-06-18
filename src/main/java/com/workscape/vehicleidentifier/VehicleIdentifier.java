package com.workscape.vehicleidentifier;

import com.sun.deploy.util.StringUtils;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	public static void main(String[] args) {
		System.out.println("Hello World!");

		VehicleIdentifier vehicleIdentifier = new VehicleIdentifier();
		vehicleIdentifier.parseVehiclesXmlFile("vehicles.xml");
	}

	public void parseVehiclesXmlFile(String filename) {
		try {
            File file = new File(getClass().getClassLoader().getResource(filename).getFile());
			parseVehiclesXml(new FileInputStream(file));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

    public List<Vehicle> parseVehiclesXml(InputStream in) throws XMLStreamException {
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader reader = factory.createXMLEventReader(in);

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            printEventType(event.getEventType());

            if(event.getEventType() == XMLEvent.START_ELEMENT) {
                StartElement element = event.asStartElement();
                if(TAG_VEHICLE.equalsIgnoreCase(element.getName().getLocalPart())) {
                    vehicles.add(parseVehicle(element, reader));
                }
            }
        }

        System.out.println("Vehicles=" + Arrays.toString(vehicles.toArray()) + "");
        return vehicles;
    }

    private Vehicle parseVehicle(StartElement startElement, XMLEventReader reader) throws XMLStreamException {
        Vehicle vehicle = new Vehicle();
        boolean inIdTag = false;
        System.out.println("====Vehicle start");

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            printEventType(event.getEventType());
            System.out.println(event);

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

        System.out.println("====Vehicle end");
        return vehicle;
    }

    private Vehicle.Material parseFrame(StartElement startElement, XMLEventReader reader) throws XMLStreamException {
        Vehicle.Material frameMaterial = null;
        boolean inMaterialTag = false;

        System.out.println("====Frame start");

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            printEventType(event.getEventType());
            System.out.println(event);

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

        System.out.println("====Frame end");
        return frameMaterial;
    }

    private Wheel parseWheel(StartElement startElement, XMLEventReader reader) throws XMLStreamException {
        Wheel wheel = new Wheel();
        boolean inPositionTag = false, inMaterialTag = false;
        System.out.println("====Wheel start");

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            printEventType(event.getEventType());
            System.out.println(event);

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

        System.out.println("====Wheel end");
        return wheel;
    }

    private Vehicle.PowerTrain parsePowerTrain(StartElement startElement, XMLEventReader reader) throws XMLStreamException {
        Vehicle.PowerTrain powerTrain = null;
        System.out.println("====Powertrain start");

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            printEventType(event.getEventType());
            System.out.println(event);

            if(event.getEventType() == XMLEvent.START_ELEMENT) {
                String elementName = event.asStartElement().getName().getLocalPart();
                powerTrain = Vehicle.PowerTrain.valueOf(toEnumStr(elementName));
            } else if((event.getEventType() == XMLEvent.END_ELEMENT)
                    && (TAG_POWERTRAIN.equalsIgnoreCase(event.asEndElement().getName().getLocalPart()))){
                break;
            }
        }

        System.out.println("====Powwertrain end");
        return powerTrain;
    }

    private String toEnumStr(String in) {
        return in.toUpperCase().replace(' ', '_');
    }

    public static final String getEventTypeString(int eventType) {
        switch (eventType) {
            case XMLEvent.START_ELEMENT:
                return "START_ELEMENT";

            case XMLEvent.END_ELEMENT:
                return "END_ELEMENT";

            case XMLEvent.PROCESSING_INSTRUCTION:
                return "PROCESSING_INSTRUCTION";

            case XMLEvent.CHARACTERS:
                return "CHARACTERS";

            case XMLEvent.COMMENT:
                return "COMMENT";

            case XMLEvent.START_DOCUMENT:
                return "START_DOCUMENT";

            case XMLEvent.END_DOCUMENT:
                return "END_DOCUMENT";

            case XMLEvent.ENTITY_REFERENCE:
                return "ENTITY_REFERENCE";

            case XMLEvent.ATTRIBUTE:
                return "ATTRIBUTE";

            case XMLEvent.DTD:
                return "DTD";

            case XMLEvent.CDATA:
                return "CDATA";
        }

        return "UNKNOWN_EVENT_TYPE";
    }

    private static void printEventType(int eventType) {
        System.out.print("EVENT TYPE(" + eventType + "):");
        System.out.println(getEventTypeString(eventType));
    }


}
