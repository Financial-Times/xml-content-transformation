package com.ft.bodyprocessing.xml.eventhandlers;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.writer.BodyWriter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.StartElement;


public class ReplaceWithStringXMLEventHandler extends BaseXMLEventHandler {
	
	private String replacementString = null;

	public ReplaceWithStringXMLEventHandler(String replacementString) {
		this.replacementString = replacementString;
	}

	@Override
	public void handleEndElementEvent(EndElement event, XMLEventReader xmlEventReader,
			BodyWriter eventWriter) throws XMLStreamException {
		eventWriter.write(replacementString);
	}

	@Override
	public void handleStartElementEvent(StartElement event, XMLEventReader xmlEventReader,
			BodyWriter eventWriter, BodyProcessingContext bodyProcessingContext) throws XMLStreamException {
		eventWriter.write(replacementString);
	}
	
	@Override
	public void handleEntityReferenceEvent(EntityReference event, XMLEventReader xmlEventReader, BodyWriter eventWriter) throws XMLStreamException {
		eventWriter.write(replacementString);	
	}
}
