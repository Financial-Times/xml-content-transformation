package com.ft.bodyprocessing.xml.eventhandlers;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.writer.BodyWriter;
import org.codehaus.stax2.XMLEventReader2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.StartElement;

import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class StripElementByClassEventHandlerTest extends BaseXMLEventHandlerTest {

	private StripElementByClassEventHandler eventHandler;
	
	@Mock private XMLEventReader2 mockXmlEventReader;
	@Mock private BodyWriter eventWriter;
	@Mock private BodyProcessingContext mockBodyProcessingContext;
	@Mock private XMLEventHandler mockFallbackHandler;

	@Before
	public void setup() {
		eventHandler = new StripElementByClassEventHandler("morevideo",mockFallbackHandler);
	}
	
	@Test
	public void givenTargetedElementShouldConsumeAdditionalEvents() throws XMLStreamException {
		  eventHandler.handleStartElementEvent(targetedElementEvent(),mockXmlEventReader,eventWriter,mockBodyProcessingContext);

		  verify(mockXmlEventReader).hasNext();
	}


	@Test
	public void givenAPartiallyMatchingElementShouldFallBack() throws XMLStreamException {

		eventHandler.handleStartElementEvent(redHerring(),mockXmlEventReader,eventWriter,mockBodyProcessingContext);

		verify(mockXmlEventReader,never()).hasNext();
		verify(mockFallbackHandler,times(1)).handleStartElementEvent(any(StartElement.class),eq(mockXmlEventReader),eq(eventWriter), eq(mockBodyProcessingContext));
	}


	@Test(expected = IllegalArgumentException.class)
	public void shouldFailIfTargetedClassIsNull() {
		eventHandler = new StripElementByClassEventHandler(null,mockFallbackHandler);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailIfTargetedClassIsEmpty() {
		eventHandler = new StripElementByClassEventHandler("",mockFallbackHandler);
	}

	private StartElement targetedElementEvent() {
		return getStartElementWithAttributes("div", Collections.singletonMap("class", "morevideo"));
	}

	private StartElement redHerring() {
		return getStartElementWithAttributes("div", Collections.singletonMap("class", "lessvideo"));
	}

}
