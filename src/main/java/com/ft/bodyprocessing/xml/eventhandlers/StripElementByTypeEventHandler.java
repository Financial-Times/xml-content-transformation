package com.ft.bodyprocessing.xml.eventhandlers;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.writer.BodyWriter;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

public class StripElementByTypeEventHandler extends BaseXMLEventHandler {

    private static final String TYPE = "type";

    private final String targetedType;
    private final XMLEventHandler fallbackHandler;

    public StripElementByTypeEventHandler(final String targetedType, final XMLEventHandler fallbackHandler) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(targetedType), "targeted element type not specified");
        this.targetedType = targetedType;
        this.fallbackHandler = fallbackHandler;
    }

    @Override
    public void handleStartElementEvent(final StartElement event, final XMLEventReader xmlEventReader,
            final BodyWriter eventWriter, final BodyProcessingContext bodyProcessingContext) throws XMLStreamException {
        if (!isFtContentWithTargetedType(event)) {
            fallbackHandler.handleStartElementEvent(event, xmlEventReader, eventWriter, bodyProcessingContext);
            return;
        }
        skipUntilMatchingEndTag(event.getName().getLocalPart(), xmlEventReader);
    }

    @Override
    public void handleEndElementEvent(final EndElement event, final XMLEventReader xmlEventReader,
            final BodyWriter eventWriter) throws XMLStreamException {
        fallbackHandler.handleEndElementEvent(event, xmlEventReader, eventWriter);
    }

    private boolean isFtContentWithTargetedType(final StartElement event) {
        final Attribute typeAttr = event.getAttributeByName(QName.valueOf(TYPE));
        return typeAttr != null && typeAttr.getValue().equals(targetedType);
    }
}
