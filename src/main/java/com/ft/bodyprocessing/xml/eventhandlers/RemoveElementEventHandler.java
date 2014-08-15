package com.ft.bodyprocessing.xml.eventhandlers;

import static com.google.common.base.Preconditions.checkArgument;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.writer.BodyWriter;

public class RemoveElementEventHandler extends BaseXMLEventHandler {
    private final XMLEventHandler fallbackEventHandler;
    private final StartElementMatcher matcher;

    public RemoveElementEventHandler(final XMLEventHandler fallbackEventHandler, final StartElementMatcher matcher) {
        checkArgument(fallbackEventHandler != null, "fallbackEventHandler cannot be null");
        checkArgument(matcher != null, "matcher cannot be null");
        this.fallbackEventHandler = fallbackEventHandler;
        this.matcher = matcher;
    }

    @Override
    public void handleStartElementEvent(final StartElement event, final XMLEventReader xmlEventReader, final BodyWriter eventWriter,
                                        final BodyProcessingContext bodyProcessingContext) throws XMLStreamException {
        if(!matcher.matches(event)) {
            fallbackEventHandler.handleStartElementEvent(event, xmlEventReader, eventWriter, bodyProcessingContext);
        } else {
            final String nameToMatch = event.getName().getLocalPart();
            skipUntilMatchingEndTag(nameToMatch, xmlEventReader);
        }
    }

    @Override // Only called where the start tag used the fallback event handler
    public void handleEndElementEvent(final EndElement event, final XMLEventReader xmlEventReader, final BodyWriter eventWriter) throws XMLStreamException {
        fallbackEventHandler.handleEndElementEvent(event, xmlEventReader, eventWriter);
    }

    public interface StartElementMatcher {
        boolean matches(StartElement element);
    }

    public static StartElementMatcher caselessMatcher(final String attributeName, final String attributeValue) {
        return new StartElementMatcher() {
            @Override
            public boolean matches(final StartElement element) {
                final Attribute channel = element.getAttributeByName(new QName(attributeName));
                return (channel == null || !attributeValue.equalsIgnoreCase(channel.getValue())) ? false : true;
            }
        };
    }

    public static StartElementMatcher attributeNameMatcher(final String attributeName) {
        return new StartElementMatcher() {
            @Override
            public boolean matches(final StartElement element) {
                final Attribute channel = element.getAttributeByName(new QName(attributeName));
                return (channel == null) ? false : true;
            }
        };
    }

}

