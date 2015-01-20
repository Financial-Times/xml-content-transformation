package com.ft.bodyprocessing.xml.eventhandlers;

import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.writer.BodyWriter;
import com.ft.bodyprocessing.xml.eventhandlers.BaseXMLEventHandler;
import com.ft.bodyprocessing.xml.eventhandlers.XMLEventHandler;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

//TODO - write JUnit tests!
public class RetainWithSpecificClassXMLEventHandler extends BaseXMLEventHandler {

    private String targetedHtmlClass;
    private XMLEventHandler fallbackHandler;

    public RetainWithSpecificClassXMLEventHandler(String targetedHtmlClass, XMLEventHandler fallbackHandler) {
        // TODO - we want to choose which attributes to keep, so also need to pass in a list of valid attribute values (there are examples in other event handlers)
        Preconditions.checkArgument(!Strings.isNullOrEmpty(targetedHtmlClass),"targeted html class name not specified");

        this.targetedHtmlClass = targetedHtmlClass;
        this.fallbackHandler = fallbackHandler;
    }

    @Override
    public void handleEndElementEvent(EndElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter) throws XMLStreamException {
        eventWriter.writeEndTag(event.getName().getLocalPart());
    }

    @Override
    public void handleStartElementEvent(StartElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter,
            BodyProcessingContext bodyProcessingContext) throws XMLStreamException {
        if(!isTargetedClass(event)) {
            fallbackHandler.handleStartElementEvent(event,xmlEventReader,eventWriter,bodyProcessingContext);
            return;
        }
        eventWriter.writeStartTag(event.getName().getLocalPart(),getValidAttributesAndValues(event));
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
