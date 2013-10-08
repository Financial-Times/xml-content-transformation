package com.ft.bodyprocessing.xml.eventhandlers;

import static org.mockito.Mockito.verify;

import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.StartElement;

import org.codehaus.stax2.XMLEventReader2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.writer.BodyWriter;

@RunWith(value=MockitoJUnitRunner.class)
public class ReplaceWithStringXMLEventHandlerTest extends BaseXMLEventHandlerTest {

	private ReplaceWithStringXMLEventHandler eventHandler;
	
	private EndElement endElement;
	private StartElement startElement;
	private EntityReference entityReference;
	
	@Mock private BodyWriter eventWriter;
	@Mock private XMLEventReader2 mockXmlEventReader;
	@Mock private BodyProcessingContext mockBodyProcessingContext;
	
	@Before
	public void setup() {
		eventHandler = new ReplaceWithStringXMLEventHandler("\n");
	}
	
	@Test
	public void endElementShouldBeReplacedWithSingleNewLine() throws Exception {
		endElement = getEndElement("p");
		eventHandler.handleEndElementEvent(endElement, mockXmlEventReader, eventWriter);
		verify(eventWriter).write("\n");
	}
	
	@Test
	public void startElementShouldBeReplacedWithSingleNewLine() throws Exception {
		startElement = getStartElement("p");
		eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, eventWriter, mockBodyProcessingContext);
		verify(eventWriter).write("\n");
	}
	
	@Test
	public void entityReferenceShouldBeReplacedWithSingleNewLine() throws Exception {
		entityReference = getEntityReference("nbsp");
		eventHandler.handleEntityReferenceEvent(entityReference, mockXmlEventReader, eventWriter);
		verify(eventWriter).write("\n");
	}

}
