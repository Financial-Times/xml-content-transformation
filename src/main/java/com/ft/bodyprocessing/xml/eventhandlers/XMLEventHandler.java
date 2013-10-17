package com.ft.bodyprocessing.xml.eventhandlers;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.writer.BodyWriter;


public interface XMLEventHandler {

	public void handleEvent(XMLEvent event, XMLEventReader xmlEventReader, BodyWriter eventWriter) throws XMLStreamException;
	public void handleCharactersEvent(Characters event, XMLEventReader xmlEventReader, BodyWriter eventWriter) throws XMLStreamException;	
	public void handleStartElementEvent(StartElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter, BodyProcessingContext bodyProcessingContext) throws XMLStreamException;
	public void handleEndElementEvent(EndElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter) throws XMLStreamException;
	public void handleCommentEvent(Comment event, XMLEventReader xmlEventReader, BodyWriter eventWriter) throws XMLStreamException;
	public void handleEntityReferenceEvent(EntityReference event, XMLEventReader xmlEventReader, BodyWriter bodyWriter) throws XMLStreamException;

}
