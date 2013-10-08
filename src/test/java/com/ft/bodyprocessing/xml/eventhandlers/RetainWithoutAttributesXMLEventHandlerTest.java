package com.ft.bodyprocessing.xml.eventhandlers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.writer.BodyWriter;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import org.codehaus.stax2.XMLEventReader2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(value=MockitoJUnitRunner.class)
public class RetainWithoutAttributesXMLEventHandlerTest extends BaseXMLEventHandlerTest {

	private RetainWithoutAttributesXMLEventHandler eventHandler;
	
	@Mock private XMLEventReader2 mockXmlEventReader;
	@Mock private BodyWriter eventWriter;
	@Mock private BodyProcessingContext mockBodyProcessingContext;
	
	@Before
	public void setup() throws Exception {
		eventHandler = new RetainWithoutAttributesXMLEventHandler();
		Characters text = getCharacters("text");
		when(mockXmlEventReader.peek()).thenReturn(text);
		when(mockXmlEventReader.next()).thenReturn(text);
	}
	
	@Test
	public void startElementShouldBeOutput() throws Exception {
		StartElement startElement = getStartElement("a");
		eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, eventWriter, mockBodyProcessingContext);
		verify(eventWriter).writeStartTag(startElement.getName().getLocalPart(), null);
	}
	
	@Test
	public void startElementShouldBeOutputWithoutAttributes() throws Exception {
		Map<String,String> attributesMap = Maps.newHashMap();
		attributesMap.put("key", "value");
		StartElement startElement = getStartElementWithAttributes("a", attributesMap);
		eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, eventWriter, mockBodyProcessingContext);
		verify(eventWriter).writeStartTag(startElement.getName().getLocalPart(), null);
	}
	
	@Test
	public void endElementShouldBeOutput() throws Exception {
		EndElement endElement = getEndElement("a");
		eventHandler.handleEndElementEvent(endElement, mockXmlEventReader, eventWriter);
		verify(eventWriter).writeEndTag(endElement.getName().getLocalPart());
	}

}
