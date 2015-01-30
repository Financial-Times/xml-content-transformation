package com.ft.bodyprocessing.xml.eventhandlers;

import org.junit.Test;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ElementRawDataParserTest extends XMLParserTest {

	private XMLEventReader xmlEventReader;

	private String element = "some-element";
	private String rawElementContent = "<p></p>some more stuff<table><tr><td>test</td></tr>more text</table>testing 12345";
	private String validXml = "<".concat(element).concat(">").concat(rawElementContent).concat("</").concat(element).concat(">");

	private String rawValidNestWithinTables = "<p></p>some more stuff<table><th><some-element>more text</some-element></th></table>testing 12345";
	private String validXmlWNestedWithinTables = "<".concat(element).concat(">").concat(rawValidNestWithinTables).concat("</").concat(element).concat(">");


	@Test
	public void shouldParseElementContents() throws XMLStreamException {
		xmlEventReader = createReaderForXml(validXml);
		moveReaderToElement(xmlEventReader, element);

		ElementRawDataParser elementRawDataParser = new ElementRawDataParser();
		String actualRawContent = elementRawDataParser.parse("some-element", xmlEventReader);
		assertThat(rawElementContent, equalTo(actualRawContent));
	}

	@Test
	public void shouldParseElementContentsWNestedTableElements() throws XMLStreamException {
		xmlEventReader = createReaderForXml(validXmlWNestedWithinTables);
		moveReaderToElement(xmlEventReader, element);

		ElementRawDataParser elementRawDataParser = new ElementRawDataParser();
		String actualRawContent = elementRawDataParser.parse("some-element", xmlEventReader);
		assertThat(rawValidNestWithinTables, equalTo(actualRawContent));
	}

	@Test
	public void shouldParseWholeElementContentsWNestedTableElements() throws XMLStreamException {
		xmlEventReader = createReaderForXml(validXmlWNestedWithinTables);

		ElementRawDataParser elementRawDataParser = new ElementRawDataParser();
		StartElement startElement = getStartElement(xmlEventReader);
		String actualRawContent = elementRawDataParser.parse("some-element", xmlEventReader, startElement);
		assertThat(validXmlWNestedWithinTables, equalTo(actualRawContent));
	}

	@Test
	public void shouldParseElementContentsIncludingRootElement() throws XMLStreamException {
		xmlEventReader = createReaderForXml(validXml);
		ElementRawDataParser elementRawDataParser = new ElementRawDataParser();

		StartElement startElement = getStartElement(xmlEventReader);
		String actualRawContent = elementRawDataParser.parse("some-element", xmlEventReader, startElement);
		assertThat(validXml, equalTo(actualRawContent));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionWithMismatchStartElementAndEndElementName() throws XMLStreamException {
		xmlEventReader = createReaderForXml(validXml);
		ElementRawDataParser elementRawDataParser = new ElementRawDataParser();

		StartElement startElement = getStartElement(xmlEventReader);
		elementRawDataParser.parse("p", xmlEventReader, startElement);
	}


	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionWithMismatchElementName() throws XMLStreamException {
		xmlEventReader = createReaderForXml(validXml);
		moveReaderToElement(xmlEventReader, element);

		ElementRawDataParser elementRawDataParser = new ElementRawDataParser();
		String rawElementContent = elementRawDataParser.parse("mismatch-element", xmlEventReader);
		assertThat(rawElementContent, equalTo(rawElementContent));
	}

	private void moveReaderToElement(XMLEventReader xmlEventReader, String element) throws XMLStreamException {
		while(xmlEventReader.hasNext()) {
			XMLEvent nextEvent = xmlEventReader.nextEvent();
			if(nextEvent.isStartElement()) {
				if(nextEvent.asStartElement().getName().getLocalPart().equals(element)) {
					break;
				}
			}
		}
	}
}
