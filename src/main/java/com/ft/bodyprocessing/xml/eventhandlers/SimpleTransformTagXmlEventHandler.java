package com.ft.bodyprocessing.xml.eventhandlers;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.writer.BodyWriter;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import org.apache.commons.lang.StringUtils;


public class SimpleTransformTagXmlEventHandler extends BaseXMLEventHandler {

	private String              newElement;
	private Map<String, String> attributesToAdd;

	public SimpleTransformTagXmlEventHandler(String newElement, String... attributesToAdd) {
		if (StringUtils.isEmpty(newElement)) {
			throw new IllegalArgumentException("newElement cannot be empty");
		}
		if (attributesToAdd.length % 2 == 1) {
			throw new IllegalArgumentException("Parameters passed to SimpleTransformTagXmlEventHandler constructor " +
					"must be an even number (pairs of key-values). If you want the last attribute to have an empty " +
					"value pass an empty string.");
		}
		this.newElement = newElement;
		this.attributesToAdd = new HashMap<String, String>();
		for (int i = 0; i < attributesToAdd.length; i += 2) {
			this.attributesToAdd.put(attributesToAdd[i], attributesToAdd[i + 1]);
		}
	}

	@Override
	public void handleStartElementEvent(StartElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter,
			BodyProcessingContext bodyProcessingContext) throws XMLStreamException {
		eventWriter.writeStartTag(newElement, attributesToAdd);
	}

	@Override
	public void handleEndElementEvent(EndElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter) throws XMLStreamException {
		eventWriter.writeEndTag(newElement);
	}

	public String getNewElement() {
		return newElement;
	}

	public Map<String, String> getAttributesToAdd() {
		return attributesToAdd;
	}
}
