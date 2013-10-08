package com.ft.bodyprocessing.xml.eventhandlers;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.writer.BodyWriter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.StartElement;

public class StripXMLEventHandler extends BaseXMLEventHandler {

	@Override
	public void handleCharactersEvent(Characters event, XMLEventReader xmlEventReader, BodyWriter eventWriter) throws XMLStreamException {
		//do nothing
	}

	@Override
	public void handleEndElementEvent(EndElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter) throws XMLStreamException {
		//do nothing
	}

	@Override
	public void handleStartElementEvent(StartElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter, 
			BodyProcessingContext bodyProcessingContext) throws XMLStreamException {
		//do nothing
	}
	
	@Override
	public void handleComment(Comment event, XMLEventReader xmlEventReader, BodyWriter eventWriter) throws XMLStreamException {
		// do nothing
	}
	
	@Override
	public void handleEntityReferenceEvent(EntityReference event, XMLEventReader xmlEventReader, BodyWriter bodyWriter) throws XMLStreamException {
		// do nothing	
	}
}
