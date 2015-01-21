package com.ft.bodyprocessing.xml.eventhandlers;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.writer.BodyWriter;
import com.google.common.collect.ImmutableMap;
import org.codehaus.stax2.XMLEventReader2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(value=MockitoJUnitRunner.class)
public class RetainWithSpecificClassXMLEventHandlerTest extends BaseXMLEventHandlerTest{

    private RetainWithSpecificClassXMLEventHandler eventHandler;
    private XMLEventHandler fallbackHandler = new RetainWithoutAttributesXMLEventHandler();

    @Mock private XMLEventReader2 mockXmlEventReader;
    @Mock private BodyWriter eventWriter;
    @Mock private BodyProcessingContext mockBodyProcessingContext;

    @Before
    public void setup() throws Exception {
        eventHandler = new RetainWithSpecificClassXMLEventHandler("twitter-tweet", fallbackHandler, "lang");
        Characters text = getCharacters("text");
        when(mockXmlEventReader.peek()).thenReturn(text);
        when(mockXmlEventReader.next()).thenReturn(text);
    }

    @Test
    public void startElementShouldBeOutput() throws Exception {
        Map<String,String> expectedMap = new HashMap<String, String>();
        expectedMap.put("class", "twitter-tweet");
        StartElement startElement = getStartElementWithAttributes("blockquote", expectedMap);
        eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, eventWriter, mockBodyProcessingContext);
        verify(eventWriter).writeStartTag(startElement.getName().getLocalPart(), expectedMap);
    }

    @Test
    public void startElementShouldBeOutputWithSpecificAttributes() throws Exception {
        ImmutableMap<String,String> attributesMap = ImmutableMap.of("class", "twitter-tweet", "lang", "en", "id", "12345");
        ImmutableMap<String,String> expectedMap = ImmutableMap.of("class", "twitter-tweet", "lang", "en");
        StartElement startElement = getStartElementWithAttributes("blockquote", attributesMap);
        eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, eventWriter, mockBodyProcessingContext);
        verify(eventWriter).writeStartTag(startElement.getName().getLocalPart(), expectedMap);
    }

    @Test
    public void endElementShouldByOutput() throws Exception {
        EndElement endElement = getEndElement("blockquote");
        eventHandler.handleEndElementEvent(endElement, mockXmlEventReader, eventWriter);
        verify(eventWriter).writeEndTag(endElement.getName().getLocalPart());
    }
}
