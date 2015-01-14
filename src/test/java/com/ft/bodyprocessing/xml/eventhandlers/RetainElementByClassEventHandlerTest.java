package com.ft.bodyprocessing.xml.eventhandlers;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.writer.BodyWriter;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(value=MockitoJUnitRunner.class)
public class RetainElementByClassEventHandlerTest extends BaseXMLEventHandlerTest {

    private RetainElementByClassEventHandler eventHandler;

    @Mock private XMLEventReader mockXmlEventReader;
    @Mock private BodyWriter mockEventWriter;
    @Mock private BodyProcessingContext mockBodyProcessingContext;
    @Mock private XMLEventHandler mockFallBackHandler;
    @Mock private XMLEvent mockEvent;

    private static final String ELEMENT = "blockquote";
    private String targetedHtmlClass = "twitter-tweet";

    @Before
    public void setUp() throws Exception {
        eventHandler = new RetainElementByClassEventHandler(targetedHtmlClass, mockFallBackHandler);
        when(mockXmlEventReader.hasNext()).thenReturn(true, true, false);
        when(mockXmlEventReader.nextEvent()).thenReturn(mockEvent);
    }

    @Test
    public void startElementShouldBeOutput() throws Exception {

        Map<String,String> expectedMap = Collections.singletonMap("class", "twitter-tweet");
        StartElement startElement = getStartElementWithAttributes("blockquote", expectedMap);
        EndElement endElement = getEndElement(ELEMENT);

        mockWholeElement(startElement, endElement);

        eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockEventWriter).writeStartTag(startElement.getName().getLocalPart(), expectedMap);
    }


    @Test
    public void endElementShouldBeOutput() throws Exception {
        Map<String,String> expectedMap = Collections.singletonMap("class", "twitter-tweet");
        StartElement startElement = getStartElementWithAttributes("blockquote", expectedMap);
        EndElement endElement = getEndElement(ELEMENT);

        mockWholeElement(startElement, endElement);

        eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockEventWriter).writeEndTag(endElement.getName().getLocalPart());
    }

    @Test
    public void shouldFallbackIfClassIsNotTwitterTweet() throws Exception {
        StartElement startElement = getStartElement(ELEMENT);
        EndElement endElement = getEndElement(ELEMENT);

        mockWholeElement(startElement, endElement);

        eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockFallBackHandler).handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
        verify(mockFallBackHandler).handleEndElementEvent(endElement, mockXmlEventReader, mockEventWriter);
    }

    private void mockWholeElement(StartElement startElement, EndElement endElement) {
        when(mockEvent.isStartElement()).thenReturn(true, false);
        when(mockEvent.asStartElement()).thenReturn(startElement);
        when(mockEvent.isEndElement()).thenReturn(false, true);
        when(mockEvent.asEndElement()).thenReturn(endElement);
    }
}
