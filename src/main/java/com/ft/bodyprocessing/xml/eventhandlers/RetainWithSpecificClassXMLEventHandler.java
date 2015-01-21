package com.ft.bodyprocessing.xml.eventhandlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.writer.BodyWriter;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class RetainWithSpecificClassXMLEventHandler extends BaseXMLEventHandler {

    private String targetedHtmlClass;
    private XMLEventHandler fallbackHandler;
    private List<String> validAttributes;

    public RetainWithSpecificClassXMLEventHandler(String targetedHtmlClass, XMLEventHandler fallbackHandler, String... validAttributes) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(targetedHtmlClass),"targeted html class name not specified");
        this.targetedHtmlClass = targetedHtmlClass;
        this.fallbackHandler = fallbackHandler;
        this.validAttributes = new ArrayList<String>();
        for(String name: validAttributes) {
            this.validAttributes.add(name.toLowerCase());
        }
        this.validAttributes.add("class");
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

        Map<String,String> validAttributesAndValues  = getValidAttributesAndValues(event,validAttributes);
        eventWriter.writeStartTag(event.getName().getLocalPart(), validAttributesAndValues);
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
