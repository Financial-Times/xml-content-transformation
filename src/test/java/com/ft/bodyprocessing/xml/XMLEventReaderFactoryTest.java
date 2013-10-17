package com.ft.bodyprocessing.xml;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.any;

import java.io.StringReader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Before;
import org.junit.Test;

;

public class XMLEventReaderFactoryTest {

	private static final String XML = "<p>Test</p>";

	private XMLEventReaderFactory xmlEventReaderFactory;
	
	private XMLInputFactory2 xmlInputFactory;
	
	@Before
	public void setup() {
		xmlInputFactory = mock(XMLInputFactory2.class);
		xmlEventReaderFactory = new XMLEventReaderFactory(xmlInputFactory);
	}
	
	@Test
	public void shouldPadXMLWithRootNode() throws XMLStreamException {
		XMLEventReader xmlEventReader = xmlEventReaderFactory.createXMLEventReader(XML);
		verify(xmlInputFactory).createXMLEventReader(any(StringReader.class));
	}
}
