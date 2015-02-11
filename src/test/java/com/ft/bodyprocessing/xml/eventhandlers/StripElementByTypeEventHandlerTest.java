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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StripElementByTypeEventHandlerTest extends BaseXMLEventHandlerTest {

    private static final String TYPE = "type";
    private static final String IMAGE_SET_ONTOLOGY = "http://www.ft.com/ontology/content/ImageSet";

    private StripElementByTypeEventHandler eventHandler;

    @Mock private XMLEventReader2 mockXmlEventReader;
    @Mock private BodyWriter eventWriter;
    @Mock private BodyProcessingContext mockBodyProcessingContext;
    @Mock private XMLEventHandler mockFallbackHandler;

    @Before
    public void setup() {
        eventHandler = new StripElementByTypeEventHandler(IMAGE_SET_ONTOLOGY, mockFallbackHandler);
    }

    @Test
    public void givenTargetedElementShouldConsumeAdditionalEvents() throws XMLStreamException {
        eventHandler.handleStartElementEvent(targetedElementEvent(), mockXmlEventReader, eventWriter,
                mockBodyProcessingContext);

        verify(mockXmlEventReader).hasNext();
    }

    @Test
    public void givenAPartiallyMatchingElementShouldFallBack() throws XMLStreamException {
        eventHandler.handleStartElementEvent(notMatchingElementEvent(), mockXmlEventReader, eventWriter,
                mockBodyProcessingContext);

        verify(mockXmlEventReader, never()).hasNext();
        verify(mockFallbackHandler, times(1)).handleStartElementEvent(any(StartElement.class), eq(mockXmlEventReader),
                eq(eventWriter), eq(mockBodyProcessingContext));
    }

    @Test
    public void shouldAlwaysDelegateEndElement() throws XMLStreamException {
        eventHandler.handleEndElementEvent(getEndElement("ft-content"), mockXmlEventReader, eventWriter);

        verify(mockFallbackHandler).handleEndElementEvent(any(EndElement.class), eq(mockXmlEventReader), eq(eventWriter));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailIfTargetedTypeIsNull() {
        eventHandler = new StripElementByTypeEventHandler(null, mockFallbackHandler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailIfTargetedTypeIsEmpty() {
        eventHandler = new StripElementByTypeEventHandler("", mockFallbackHandler);
    }

    private StartElement targetedElementEvent() {
        return getStartElementWithAttributes("ft-content", Collections.singletonMap(TYPE, IMAGE_SET_ONTOLOGY));
    }

    private StartElement notMatchingElementEvent() {
        return getStartElementWithAttributes("ft-content", Collections.singletonMap(TYPE,
                "http://www.ft.com/ontology/content/somethingElse"));
    }
}
