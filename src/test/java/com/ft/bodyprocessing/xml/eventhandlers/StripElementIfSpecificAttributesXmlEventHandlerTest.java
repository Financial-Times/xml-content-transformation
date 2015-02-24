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
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StripElementIfSpecificAttributesXmlEventHandlerTest extends BaseXMLEventHandlerTest {


    private StripElementIfSpecificAttributesXmlEventHandler eventHandler;

    @Mock
    private XMLEventReader2 mockXmlEventReader;
    @Mock
    private BodyWriter eventWriter;
    @Mock
    private BodyProcessingContext mockBodyProcessingContext;
    @Mock
    private XMLEventHandler mockFallbackHandler;

    @Before
    public void setup() {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("class", "morevideo");
        eventHandler = new StripElementIfSpecificAttributesXmlEventHandler(map, mockFallbackHandler);
    }

    @Test
    public void givenTargetedElementShouldConsumeAdditionalEvents() throws XMLStreamException {
        eventHandler.handleStartElementEvent(targetedElementEvent(), mockXmlEventReader, eventWriter, mockBodyProcessingContext);

        verify(mockXmlEventReader).hasNext();
    }


    @Test
    public void givenAPartiallyMatchingElementShouldFallBack() throws XMLStreamException {
        eventHandler.handleStartElementEvent(redHerring(), mockXmlEventReader, eventWriter, mockBodyProcessingContext);

        verify(mockXmlEventReader, never()).hasNext();
        verify(mockFallbackHandler, times(1)).handleStartElementEvent(any(StartElement.class), eq(mockXmlEventReader),
                eq(eventWriter), eq(mockBodyProcessingContext));
    }

    @Test
    public void shouldAlwaysDelegateEndElement() throws XMLStreamException {
        eventHandler.handleEndElementEvent(getEndElement("div"), mockXmlEventReader, eventWriter);

        verify(mockFallbackHandler).handleEndElementEvent(any(EndElement.class), eq(mockXmlEventReader), eq(eventWriter));
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldFailIfTargetedAttrIsNull() {
        eventHandler = new StripElementIfSpecificAttributesXmlEventHandler(null, mockFallbackHandler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailIfTargetedAttrIsEmpty() {
        eventHandler = new StripElementIfSpecificAttributesXmlEventHandler(new HashMap<String, String>(), mockFallbackHandler);
    }


    private StartElement targetedElementEvent() {
        return getStartElementWithAttributes("div", Collections.singletonMap("class", "morevideo"));
    }

    private StartElement redHerring() {
        return getStartElementWithAttributes("div", Collections.singletonMap("class", "lessvideo"));
    }
}
