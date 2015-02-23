package com.ft.bodyprocessing.xml.eventhandlers;

import com.ft.bodyprocessing.BodyProcessingContext;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

public interface XmlParser<T> {

	T parseElementData(StartElement startElement, XMLEventReader xmlEventReader, BodyProcessingContext bodyProcessingContext) throws XMLStreamException;

	/**
	 * We are no longer supposed to use this method as it is not fit for our purpose - we do not have any aside assets.
	 * @param dataBean bean containing data of the parsed XML element
	 * @param bodyProcessingContext body processing context
	 */
	@Deprecated
	void transformFieldContentToStructuredFormat(T dataBean, BodyProcessingContext bodyProcessingContext);
}
