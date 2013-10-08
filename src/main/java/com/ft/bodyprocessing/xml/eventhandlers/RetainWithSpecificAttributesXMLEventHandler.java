package com.ft.bodyprocessing.xml.eventhandlers;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.writer.BodyWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

public class RetainWithSpecificAttributesXMLEventHandler extends BaseXMLEventHandler {

	protected List<String> validAttributes;

	public RetainWithSpecificAttributesXMLEventHandler(String... validAttributes) {
		this.validAttributes = new ArrayList<String>();
		for(String name: validAttributes) {
			this.validAttributes.add(name.toLowerCase());
		}
	}

	@Override
	public void handleEndElementEvent(EndElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter) throws XMLStreamException {
		eventWriter.writeEndTag(event.getName().getLocalPart());
	}

	@Override
	public void handleStartElementEvent(StartElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter,
			BodyProcessingContext bodyProcessingContext) throws XMLStreamException {
		Map<String,String> validAttributesAndValues  = getValidAttributesAndValues(event, validAttributes);
		eventWriter.writeStartTag(event.getName().getLocalPart(), validAttributesAndValues);
	}


}
