package com.ft.bodyprocessing.xml.eventhandlers;

import com.ft.bodyprocessing.BodyProcessingContext;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

public interface XmlParser<T> {

	T parseElementData(StartElement startElement, XMLEventReader xmlEventReader) throws XMLStreamException;
	void transformFieldContentToStructuredFormat(T dataBean, BodyProcessingContext bodyProcessingContext);
}
