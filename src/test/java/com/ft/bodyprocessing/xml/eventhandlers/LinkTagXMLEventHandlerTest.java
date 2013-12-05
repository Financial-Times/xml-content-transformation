package com.ft.bodyprocessing.xml.eventhandlers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import javax.xml.stream.events.StartElement;

import org.codehaus.stax2.XMLEventReader2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.writer.BodyWriter;
import com.google.common.collect.ImmutableMap;


@RunWith(value=MockitoJUnitRunner.class)
public class LinkTagXMLEventHandlerTest extends BaseXMLEventHandlerTest {

	private LinkTagXMLEventHandler eventHandler;
	
	@Mock private XMLEventReader2 mockXmlEventReader;
	@Mock private BodyWriter eventWriter;
	@Mock private BodyProcessingContext mockBodyProcessingContext;
	
	@Before
	public void setup() {
		eventHandler = new LinkTagXMLEventHandler();
	}
	
	@Test
	public void shouldRetainHrefAttribute() throws Exception {
		ImmutableMap<String,String> attributesMap = ImmutableMap.of("href", "value");
		StartElement startElement = getStartElementWithAttributes("a", attributesMap);
		eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, eventWriter, mockBodyProcessingContext);
		verify(eventWriter).writeStartTag(startElement.getName().getLocalPart(), attributesMap);
	}
	
	@Test
	public void shouldRetainTitleAttribute() throws Exception {
		ImmutableMap<String,String> attributesMap = ImmutableMap.of("href", "value", "title", "value");
		StartElement startElement = getStartElementWithAttributes("a", attributesMap);
		eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, eventWriter, mockBodyProcessingContext);
		verify(eventWriter).writeStartTag(startElement.getName().getLocalPart(), attributesMap);
	}
	
	@Test
	public void shouldRetainAltAttribute() throws Exception {
		ImmutableMap<String,String> attributesMap = ImmutableMap.of("href", "value", "alt", "value");
		StartElement startElement = getStartElementWithAttributes("a", attributesMap);
		eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, eventWriter, mockBodyProcessingContext);
		verify(eventWriter).writeStartTag(startElement.getName().getLocalPart(), attributesMap);
	}
	
	@Test
	public void shouldRemoveNameAttribute() throws Exception {
		ImmutableMap<String,String> attributesMap = ImmutableMap.of("href", "value", "name", "value");
		StartElement startElement = getStartElementWithAttributes("a", attributesMap);
		eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, eventWriter, mockBodyProcessingContext);	
		ImmutableMap<String,String> expectedAttributesMap = ImmutableMap.of("href", "value");	
		verify(eventWriter).writeStartTag(startElement.getName().getLocalPart(), expectedAttributesMap);
	}
}
