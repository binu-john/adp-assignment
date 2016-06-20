package com.workscape.vehicleidentifier;


import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * Unit test for VehicleIdentifier.
 */
public class VehicleIdentifierTest {

	/**
	 * Test vehicles.xml
	 */
	@Test
	public void testFile_VehiclesXml() {
		VehicleIdentifier app = new VehicleIdentifier();
		VehicleReport report = null;
		try {
			report = app.parseVehiclesXmlFile("vehicles.xml");
		} catch (FileNotFoundException e) {
			fail("FileNotFoundException: XML file not found.");
		} catch (XMLStreamException e) {
			fail("XMLStreamException: XML parsing error.");
		}

		//results
		Map<String, VehicleType> results = report.getIdentificationResults();
		assertEquals(VehicleType.BIG_WHEEL, results.get("vehicle 1"));
		assertEquals(VehicleType.BICYCLE, results.get("vehicle 2"));

		//summary
		Map<VehicleType, Integer> summary = report.getSummary();
		assertEquals(1, summary.get(VehicleType.BIG_WHEEL).intValue());
		assertEquals(1, summary.get(VehicleType.BICYCLE).intValue());
		assertNull(summary.get(VehicleType.MOTORCYCLE));
		assertNull(summary.get(VehicleType.HANG_GLIDER));
		assertNull(summary.get(VehicleType.CAR));
		assertNull(summary.get(VehicleType.UNKNOWN));
	}

	/**
	 * Test vehicles2.xml
	 */
	@Test
	public void testFile_Vehicles2Xml() {
		VehicleIdentifier app = new VehicleIdentifier();
		VehicleReport report = null;
		try {
			report = app.parseVehiclesXmlFile("vehicles2.xml");
		} catch (FileNotFoundException e) {
			fail("FileNotFoundException: XML file not found.");
		} catch (XMLStreamException e) {
			fail("XMLStreamException: XML parsing error.");
		}

		//results
		Map<String, VehicleType> results = report.getIdentificationResults();
		assertEquals(VehicleType.BIG_WHEEL, results.get("vehicle 1"));
		assertEquals(VehicleType.BICYCLE, results.get("vehicle 2"));
		assertEquals(VehicleType.MOTORCYCLE, results.get("vehicle 3"));
		assertEquals(VehicleType.HANG_GLIDER, results.get("vehicle 4"));
		assertEquals(VehicleType.CAR, results.get("vehicle 5"));
		assertEquals(VehicleType.BICYCLE, results.get("vehicle 6"));
		assertEquals(VehicleType.UNKNOWN, results.get("vehicle 7"));

		//summary
		Map<VehicleType, Integer> summary = report.getSummary();
		assertEquals(1, summary.get(VehicleType.BIG_WHEEL).intValue());
		assertEquals(2, summary.get(VehicleType.BICYCLE).intValue());
		assertEquals(1, summary.get(VehicleType.MOTORCYCLE).intValue());
		assertEquals(1, summary.get(VehicleType.HANG_GLIDER).intValue());
		assertEquals(1, summary.get(VehicleType.CAR).intValue());
		assertEquals(1, summary.get(VehicleType.UNKNOWN).intValue());
	}
}
