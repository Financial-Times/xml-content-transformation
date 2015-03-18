package com.ft.bodyprocessing.xml.eventhandlers;

import java.util.Collections;
import java.util.Map;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.writer.BodyWriter;
import com.ft.bodyprocessing.xml.StAXTransformingBodyProcessor;

public class SimpleTransformBlockElementEventHandler extends BaseXMLEventHandler implements TransformableEvent {


    private StAXTransformingBodyProcessor stAXTransformingBodyProcessor;
    private String replacementElementName;
    private static final String PARAGRAPH_TAG = "p";

    public SimpleTransformBlockElementEventHandler(StAXTransformingBodyProcessor stAXTransformingBodyProcessor, String replacementElementName) {
        this.stAXTransformingBodyProcessor  =  stAXTransformingBodyProcessor;
        this.replacementElementName = replacementElementName;
    }

    @Override
    public void handleStartElementEvent(StartElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter, BodyProcessingContext bodyProcessingContext) throws XMLStreamException {
        boolean pTagOpen = eventWriter.isPTagCurrentlyOpen();
        if (pTagOpen) {
            eventWriter.writeEndTag(PARAGRAPH_TAG);
        }
        String startTagName = event.getName().toString();
        eventWriter.writeStartTag(replacementElementName, noAttributes());

        String nestedProcessedString = stAXTransformingBodyProcessor.process(parseRawContent(startTagName, xmlEventReader), bodyProcessingContext);

        eventWriter.writeRaw(nestedProcessedString);
        eventWriter.writeEndTag(replacementElementName);

        if (pTagOpen) {
            eventWriter.writeStartTag(PARAGRAPH_TAG, noAttributes());
        }
    }

    private Map<String, String> noAttributes() {
        return Collections.emptyMap();
    }

    public String parseRawContent(String elementName, XMLEventReader xmlEventReader) {
        ElementRawDataParser rawDataParser = new ElementRawDataParser();
        try {
            return rawDataParser.parse(elementName, xmlEventReader).trim();
        } catch (XMLStreamException e) {
            return null;
        }
    }

    @Override
    public String getNewElement() {
        return replacementElementName;
    }
}
