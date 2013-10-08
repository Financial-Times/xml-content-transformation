package com.ft.bodyprocessing.xml.eventhandlers;

import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.writer.BodyWriter;

import javax.xml.namespace.QName;
import javax.xml.stream.events.StartElement;
import org.codehaus.stax2.XMLEventReader2;
import org.codehaus.stax2.ri.evt.StartElementEventImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(value=MockitoJUnitRunner.class)
public class StripElementAndContentsXMLEventHandlerTest extends BaseXMLEventHandlerTest {

	private StripElementAndContentsXMLEventHandler eventHandler;
	
	private StartElement startElement;
	
	@Mock private BodyWriter eventWriter;
	@Mock private XMLEventReader2 mockXmlEventReader;
	@Mock private BodyProcessingContext mockBodyProcessingContext;
	
	@Before
	public void setup() {
		eventHandler = new StripElementAndContentsXMLEventHandler();
	}
	
	@Test
	public void startElementShouldBeStrippedUpTilMatchingEndElement() throws Exception {
		startElement = StartElementEventImpl.construct(null, new QName("pull-quote"), null, null, null);
		//"Some characters<tr/> Some other characters</pull-quote>";
		when(mockXmlEventReader.hasNext()).thenReturn(true, true, true, true, true, false);
		when(mockXmlEventReader.nextEvent()).thenReturn(getCharacters("Some characters"), getStartElement("tr"), getEndElement("tr"), 
				getCharacters(" Some other characters"), getEndElement("pull-quote"));
		when(mockXmlEventReader.peek()).thenReturn(getCharacters("Some characters"), getStartElement("tr"), getEndElement("tr"), 
				getCharacters(" Some other characters"), getEndElement("pull-quote"));
		eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, eventWriter, mockBodyProcessingContext);
		verifyZeroInteractions(eventWriter);
	}
	
	@Test
	public void shouldHandleNestedElements() throws Exception {
		startElement = StartElementEventImpl.construct(null, new QName("pull-quote"), null, null, null);
		//"<table>Some characters<pull-quote></pull-quote> Some other characters</table></pull-quote>";
		when(mockXmlEventReader.hasNext()).thenReturn(true, true, true, true, true, false);
		when(mockXmlEventReader.nextEvent()).thenReturn(getCharacters("Some characters"), getStartElement("pull-quote"), getEndElement("pull-quote"), 
				getCharacters(" Some other characters"), getEndElement("pull-quote"));
		when(mockXmlEventReader.peek()).thenReturn(getCharacters("Some characters"), getStartElement("pull-quote"), getEndElement("pull-quote"),
				getCharacters(" Some other characters"), getEndElement("pull-quote"));
		eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, eventWriter, mockBodyProcessingContext);
		verifyZeroInteractions(eventWriter);
	}

}
