package com.ft.bodyprocessing.xml.eventhandlers;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.writer.BodyWriter;
import com.google.common.collect.Lists;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinkTagXMLEventHandler extends BaseXMLEventHandler {

	private static final String A_TAG_NAME = "a";
	private static final QName HREF_ATTRIBUTE = new QName("href");
	private List<QName> validAttributes;

	public LinkTagXMLEventHandler(String... validAttributes) {
		this.validAttributes = Lists.newArrayList();
		for(String name: validAttributes) {
			this.validAttributes.add(new QName(name.toLowerCase()));
		}
	}

	@Override
	public void handleEndElementEvent(EndElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter) throws XMLStreamException {
		eventWriter.writeEndTag(event.getName().getLocalPart());
	}

	@Override
	public void handleStartElementEvent(StartElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter,
			BodyProcessingContext bodyProcessingContext) throws XMLStreamException {
		if (isLink(event)) {
			Attribute hrefAttribute = event.getAttributeByName(HREF_ATTRIBUTE);
			Map<String,String> validAttributesAndValues = new HashMap<String,String>();
			if (hrefAttribute != null) {
				validAttributesAndValues.put(HREF_ATTRIBUTE.getLocalPart(), encodeHref(hrefAttribute.getValue()));
				for (QName attributeName: validAttributes) {
					Attribute attribute = event.getAttributeByName(attributeName);
					if (attribute != null) {
						validAttributesAndValues.put(attributeName.getLocalPart(), attribute.getValue());
					}
				}
			}
			eventWriter.writeStartTag(event.getName().getLocalPart(), validAttributesAndValues);
		} else {
	        throw new XMLStreamException("event must correspond to" + A_TAG_NAME  +" tag");
		}
	}

	private String encodeHref(String value) {
		int paramIndex = value.indexOf('?');
		if (paramIndex >=0) {
			return value.substring(0, paramIndex) + value.substring(paramIndex).replaceAll(" ", "%20");
		}
		return value;
	}

	public boolean isLink(StartElement event) {
        return event.getName().getLocalPart().toLowerCase().equals(A_TAG_NAME);
    }
}
