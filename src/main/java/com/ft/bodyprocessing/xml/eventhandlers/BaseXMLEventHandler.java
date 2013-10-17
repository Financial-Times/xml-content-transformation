package com.ft.bodyprocessing.xml.eventhandlers;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.writer.BodyWriter;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class BaseXMLEventHandler implements XMLEventHandler {

	@Override
	public void handleEvent(XMLEvent event, XMLEventReader xmlEventReader, BodyWriter eventWriter) throws XMLStreamException {
		// By default, do nothing
	}

	@Override
	public void handleCharactersEvent(Characters event, XMLEventReader xmlEventReader, BodyWriter eventWriter)
			throws XMLStreamException {
		// By default, do nothing
	}

	@Override
	public void handleStartElementEvent(StartElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter, BodyProcessingContext bodyProcessingContext)
			throws XMLStreamException {
		// By default, do nothing
	}

	@Override
	public void handleEndElementEvent(EndElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter)
			throws XMLStreamException {
		// By default, do nothing
	}

	@Override
	public void handleCommentEvent(Comment event, XMLEventReader xmlEventReader, BodyWriter eventWriter) 
			throws XMLStreamException {
		// By default, do nothing
	}

	@Override
	public void handleEntityReferenceEvent(EntityReference event, XMLEventReader xmlEventReader, BodyWriter bodyWriter)
			throws XMLStreamException {
		// By default, do nothing	
	}
	
	protected void skipUntilMatchingEndTag(String nameToMatch, XMLEventReader xmlEventReader) throws XMLStreamException {
		int count = 0;
		while (xmlEventReader.hasNext()) {
			XMLEvent event = xmlEventReader.nextEvent();
			if (event.isStartElement()) {
				StartElement newStartElement = event.asStartElement();
				if (nameToMatch
						.equals(newStartElement.getName().getLocalPart())) {
					count++;
				}
			}
			if (event.isEndElement()) {
				EndElement endElement = event.asEndElement();
				String localName = endElement.getName().getLocalPart();
				if (nameToMatch.equals(localName)) {
					if (count == 0) {
						return;
					}
					count--;
				}
			}
		}
	}
	
	protected Map<String,String> getValidAttributesAndValues(StartElement event) {
		Map<String,String> validAttributesAndValues = Maps.newHashMap();
		@SuppressWarnings("unchecked")
		Iterator<Attribute> actualAttributesIterator = event.getAttributes();
		while (actualAttributesIterator.hasNext()) {
			Attribute nextAttribute = actualAttributesIterator.next();
			String attributeName = nextAttribute.getName().getLocalPart().toLowerCase();
			validAttributesAndValues.put(attributeName, nextAttribute.getValue());		
		}
		return validAttributesAndValues;
	}
	
	protected Map<String,String> getValidAttributesAndValues(StartElement event, List<String> validAttributes) {
		Map<String,String> validAttributesAndValues = Maps.newHashMap();
		@SuppressWarnings("unchecked")
		Iterator<Attribute> actualAttributesIterator = event.getAttributes();
		while (actualAttributesIterator.hasNext()) {
			Attribute nextAttribute = actualAttributesIterator.next();
			String attributeName = nextAttribute.getName().getLocalPart().toLowerCase();
			if (validAttributes.contains(attributeName)) {
				validAttributesAndValues.put(attributeName, nextAttribute.getValue());
			}			
		}
		return validAttributesAndValues;
	}
	
}
