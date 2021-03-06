package com.ft.bodyprocessing.xml.eventhandlers;

import org.codehaus.stax2.XMLInputFactory2;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.StringReader;

public class XMLParserTest {

	protected XMLEventReader createReaderForXml(String xml) throws XMLStreamException {
		XMLInputFactory newInstance = XMLInputFactory2.newInstance();
		StringReader reader = new StringReader(xml);
		return newInstance.createXMLEventReader(reader);
	}

	protected StartElement getStartElement(XMLEventReader xmlEventReader) throws XMLStreamException {
		// move reader past the start document
		xmlEventReader.nextEvent();

		// move reader to the start element
		XMLEvent nextEvent = xmlEventReader.nextEvent();
		return nextEvent.asStartElement();
	}
}
