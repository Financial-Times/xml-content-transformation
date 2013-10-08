package com.ft.bodyprocessing.xml.eventhandlers;

import static org.mockito.Mockito.verify;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.writer.BodyWriter;
import com.google.common.collect.ImmutableMap;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import org.codehaus.stax2.XMLEventReader2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(value=MockitoJUnitRunner.class)
public class SimpleTransformTagXmlEventHandlerTest  extends BaseXMLEventHandlerTest {

	private SimpleTransformTagXmlEventHandler eventHandler;

	@Mock private BodyWriter      eventWriter;
	@Mock private XMLEventReader2 mockXmlEventReader;
	@Mock private BodyProcessingContext mockBodyProcessingContext;

	@Before
	public void setUp() {
		eventHandler = new SimpleTransformTagXmlEventHandler("span", "class", "underlined");
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorShouldRejectEmptyReplacementElement() {
		new SimpleTransformTagXmlEventHandler("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorShouldRejectNullReplacementElement() {
		new SimpleTransformTagXmlEventHandler(null);
	}

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldRejectUnevenNumberOfAttributes() {
        new SimpleTransformTagXmlEventHandler("p", "class");
    }

	@Test
	public void startElementShouldBeTransformed() throws Exception {
		StartElement startElement = getStartElement("u");
		ImmutableMap<String,String> expectedMap = ImmutableMap.of("class", "underlined");
		eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, eventWriter, mockBodyProcessingContext);

		verify(eventWriter).writeStartTag("span", expectedMap);
	}

	@Test
	public void endElementShouldBeTransformed() throws Exception {
		EndElement endElement = getEndElement("u");

		eventHandler.handleEndElementEvent(endElement, mockXmlEventReader, eventWriter);

		verify(eventWriter).writeEndTag("span");
	}
}
