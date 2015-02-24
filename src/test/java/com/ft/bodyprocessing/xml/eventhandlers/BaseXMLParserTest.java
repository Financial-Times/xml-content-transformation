package com.ft.bodyprocessing.xml.eventhandlers;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import com.ft.bodyprocessing.BodyProcessingContext;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class BaseXMLParserTest extends XMLParserTest {

	private static final String VALID_XML = "<test><value>abc</value></test>";
	private static final String INVALID_XML = "<test><value>abc</test>";

	private TestParser testParser;
	private XMLEventReader xmlEventReader;

	@Before
	public void setup() {
		testParser = new TestParser();
	}

	@Test
	public void shouldReturnCorrectValue() throws XMLStreamException {
		xmlEventReader = createReaderForXml(VALID_XML);
		StartElement startElement = getStartElement(xmlEventReader);
		TestData testData = testParser.parseElementData(startElement, xmlEventReader, null);

		assertThat(testData.getValue(), equalTo("abc"));
	}

	@Test(expected = XMLStreamException.class)
	public void shouldReturnAppropriateExceptionForInvalidXml() throws XMLStreamException {
		xmlEventReader = createReaderForXml(INVALID_XML);
		StartElement startElement = getStartElement(xmlEventReader);
		testParser.parseElementData(startElement, xmlEventReader, null);
	}

	private class TestData {

		private String value;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	private class TestParser extends BaseXMLParser<TestData> {

		private static final String VALUE_ELEMENT = "value";

		public TestParser() {
			super("test");
		}

		@Override
		public boolean doesTriggerElementContainAllDataNeeded() {
			return false;
		}

		@Override
		public TestData createDataBeanInstance() {
			return new TestData();
		}

		@Override
		protected void populateBean(TestData dataBean, StartElement nextStartElement, XMLEventReader xmlEventReader,
									BodyProcessingContext bodyProcessingContext) throws UnexpectedElementStructureException {
			if (isElementNamed(nextStartElement.getName(), VALUE_ELEMENT)) {
				dataBean.setValue(parseRawContent(VALUE_ELEMENT, xmlEventReader));
			}
		}
	}

}
