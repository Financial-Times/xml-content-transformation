package com.ft.bodyprocessing.xml.eventhandlers;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.BodyProcessingException;
import com.ft.bodyprocessing.writer.BodyWriter;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.util.Arrays;
import java.util.List;

public class RetainElementByClassEventHandler extends BaseXMLEventHandler {

    private String targetedHtmlClass;
    private XMLEventHandler fallbackHandler;

    public RetainElementByClassEventHandler(String targetedHtmlClass, XMLEventHandler fallbackHandler) {

        Preconditions.checkArgument(!Strings.isNullOrEmpty(targetedHtmlClass), "targeted html class name not specified");

        this.targetedHtmlClass = targetedHtmlClass;
        this.fallbackHandler = fallbackHandler;
    }

    @Override
    public void handleStartElementEvent(StartElement topLevelStartElementEvent, XMLEventReader xmlEventReader, BodyWriter eventWriter, BodyProcessingContext bodyProcessingContext) throws XMLStreamException {

        if(isTargetedClass(topLevelStartElementEvent)) {
            writeStartElement(eventWriter, topLevelStartElementEvent);
        } else {
            fallbackHandler.handleStartElementEvent(topLevelStartElementEvent,xmlEventReader,eventWriter,bodyProcessingContext);
        }

        String nameToMatch = topLevelStartElementEvent.getName().getLocalPart();
        int count = 0;
        while (xmlEventReader.hasNext()) {
            XMLEvent currentEvent = xmlEventReader.nextEvent();
            if (currentEvent.isStartElement()) {
                StartElement newStartElement = currentEvent.asStartElement();
                if (nameToMatch
                        .equals(newStartElement.getName().getLocalPart())) {
                    count++;
                }
                if (isTargetedClass(topLevelStartElementEvent)) {
                    writeStartElement(eventWriter, newStartElement);
                } else {
                    fallbackHandler.handleStartElementEvent(newStartElement, xmlEventReader, eventWriter, bodyProcessingContext);
                }
            }
            if(currentEvent.isCharacters()) {
                eventWriter.write(currentEvent.asCharacters().getData());
            }
            if(currentEvent.isEntityReference()) {
                EntityReference entityReference = (EntityReference) currentEvent;
                eventWriter.writeEntityReference(entityReference.getName());
            }
            if (currentEvent.isEndElement()) {
                EndElement endElement = currentEvent.asEndElement();
                String localName = endElement.getName().getLocalPart();
                if (isTargetedClass(topLevelStartElementEvent)) {
                    eventWriter.writeEndTag(localName);
                } else {
                    fallbackHandler.handleEndElementEvent(endElement, xmlEventReader, eventWriter);
                }
                if (nameToMatch.equals(localName)) {
                    if (count == 0) {
                        return;
                    }
                    count--;
                }
            }
        }
    }

    private void writeStartElement(BodyWriter eventWriter, StartElement newStartElement) {
        eventWriter.writeStartTag(newStartElement.getName().getLocalPart(),getValidAttributesAndValues(newStartElement));
    }

    @Override
    public void handleEndElementEvent(EndElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter) throws XMLStreamException {
        throw new BodyProcessingException("Unexpected call to handleEndElementEvent. Was expected to skip this in handleStartElementEvent");
    }

    private boolean isTargetedClass(StartElement event) {

        Attribute classesAttr = event.getAttributeByName(QName.valueOf("class"));
        if(classesAttr==null) {
            return false;
        }

        List<String> classes = Arrays.asList(classesAttr.getValue().split(" "));

        return classes.contains(targetedHtmlClass);
    }
}
