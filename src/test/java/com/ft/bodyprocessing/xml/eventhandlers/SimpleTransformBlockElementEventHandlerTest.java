package com.ft.bodyprocessing.xml.eventhandlers;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Map;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.writer.BodyWriter;
import com.ft.bodyprocessing.xml.StAXTransformingBodyProcessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SimpleTransformBlockElementEventHandlerTest extends BaseXMLEventHandlerTest{

    @Mock private StAXTransformingBodyProcessor mockStAXTransformingBodyProcessor;
    @Mock private XMLEventReader mockXmlEventReader;
    @Mock private BodyWriter mockBodyWriter;
    @Mock private BodyProcessingContext mockBodyProcessingContext;

    private SimpleTransformBlockElementEventHandler eventHandler;
    private StartElement startElement;

    private static String TIMELINE_STR =  "<body><timeline><timeline-item>Hello</timeline-item></timeline></body>"; //arbitrary

    @Before
    public void setUp() throws Exception {
        eventHandler = Mockito.spy(new SimpleTransformBlockElementEventHandler(mockStAXTransformingBodyProcessor, "changeToMe"));
        startElement = getCompactStartElement(TIMELINE_STR, "timeline");

        doReturn("<timeline-item>Hello</timeline-item>").when(eventHandler).parseRawContent("timeline", mockXmlEventReader);
        when(mockStAXTransformingBodyProcessor.process("<timeline-item>Hello</timeline-item>", mockBodyProcessingContext)).thenReturn("<timeline-item>Hello</timeline-item>");
    }

    @Test
    public void shouldCloseAndOpenPTagWhenOpen() throws Exception {
        when(mockBodyWriter.isPTagCurrentlyOpen()).thenReturn(true);

        eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockBodyWriter, mockBodyProcessingContext);

        verify(mockBodyWriter).writeEndTag("p");
        verify(mockBodyWriter).writeStartTag("changeToMe", noAttributes());
        verify(mockBodyWriter).writeRaw("<timeline-item>Hello</timeline-item>");
        verify(mockBodyWriter).writeEndTag("changeToMe");
        verify(mockBodyWriter).writeStartTag("p", noAttributes());
    }

    @Test
    public void shouldNotCloseAndOpenPTagsWhenClosed() throws Exception {
        when(mockBodyWriter.isPTagCurrentlyOpen()).thenReturn(false);

        eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockBodyWriter, mockBodyProcessingContext);

        verify(mockBodyWriter, never()).writeEndTag("p");
        verify(mockBodyWriter).writeStartTag("changeToMe", noAttributes());
        verify(mockBodyWriter).writeRaw("<timeline-item>Hello</timeline-item>");
        verify(mockBodyWriter).writeEndTag("changeToMe");
        verify(mockBodyWriter, never()).writeStartTag("p", noAttributes());
    }


    private Map<String, String> noAttributes() {
        return Collections.emptyMap();
    }

}