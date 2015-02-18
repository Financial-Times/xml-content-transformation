package com.ft.bodyprocessing.xml.eventhandlers;

import com.ft.bodyprocessing.BodyProcessingContext;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class BaseXMLParser<T> {

	private String startElementName;

	protected BaseXMLParser(String startElementName) {
		checkNotNull(startElementName, "The startElementName cannot be null!");
		this.startElementName = startElementName;
	}

	public T parseElementData(StartElement startElement, XMLEventReader xmlEventReader, BodyProcessingContext bodyProcessingContext) throws XMLStreamException {
		T dataBean = createDataBeanInstance();

		try {
			// Use the start element (trigger element) to populate the bean as
			// some types require parsing to start from the starting element.
			populateBean(dataBean, startElement, xmlEventReader, bodyProcessingContext);

			// Check if more data beyond the start element is needed to populate the data bean
			if(doesTriggerElementContainAllDataNeeded()) {
				return dataBean;
			}
			int depth = 1;
			while (xmlEventReader.hasNext()) {
				XMLEvent nextEvent = xmlEventReader.nextEvent();

				if (nextEvent.isStartElement()) {
					StartElement nextStartElement = nextEvent.asStartElement();
					populateBean(dataBean, nextStartElement, xmlEventReader, bodyProcessingContext);
					if(isElementNamed(nextStartElement.getName(), startElementName)) {
						depth++;
					}
				} else if (nextEvent.isEndElement()) {
					// Check if it's the closing element of the start element,
					// in which case exit as we should not continue to parse
					// beyond this element.
					if (isElementNamed(nextEvent.asEndElement().getName(), startElementName)) {
						depth--;
						if(depth==0) {
							break;
						}
					}
				}
			}
		} catch (UnexpectedElementStructureException e) {
			// Something went wrong - read until the end of this element to get
			// rid of the whole thing
			skipUntilMatchingEndTag(startElementName, xmlEventReader);
			// Ensure that the bean returned is not valid for processing.
			return createDataBeanInstance();
		}
		return dataBean;
	}

	public abstract boolean doesTriggerElementContainAllDataNeeded();

	public abstract T createDataBeanInstance();

	protected boolean isElementNamed(QName elementName, String nameToMatch) {
		return elementName.getLocalPart().toLowerCase().equals(nameToMatch.toLowerCase());
	}

	protected abstract void populateBean(T dataBean, StartElement nextStartElement, XMLEventReader xmlEventReader,
										 BodyProcessingContext bodyProcessingContext)
			throws UnexpectedElementStructureException;

	private void skipUntilMatchingEndTag(String nameToMatch, XMLEventReader xmlEventReader) throws XMLStreamException {
		int count = 0;
		while (xmlEventReader.hasNext()) {
			XMLEvent event = xmlEventReader.nextEvent();
			if (event.isStartElement()) {
				StartElement newStartElement = event.asStartElement();
				if (nameToMatch.equals(newStartElement.getName().getLocalPart())) {
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

	protected String parseRawContent(String elementName, XMLEventReader xmlEventReader, StartElement nextStartElement) {
		ElementRawDataParser rawDataParser = new ElementRawDataParser();
		try {
			return rawDataParser.parse(elementName, xmlEventReader, nextStartElement);
		} catch (XMLStreamException e) {
			return null;
		}
	}

	protected String parseRawContent(String elementName, XMLEventReader xmlEventReader) {
		ElementRawDataParser rawDataParser = new ElementRawDataParser();
		try {
			return rawDataParser.parse(elementName, xmlEventReader).trim();
		} catch (XMLStreamException e) {
			return null;
		}
	}
}

