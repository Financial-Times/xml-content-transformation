package com.ft.bodyprocessing.xml.eventhandlers;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.writer.BodyWriter;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import java.util.Arrays;
import java.util.List;

/**
 * StripElementByClassEventHandler
 *
 * @author Simon
 */
public class StripElementByClassEventHandler extends BaseXMLEventHandler {

	private String targetedHtmlClass;
	private XMLEventHandler fallbackHandler;

	public StripElementByClassEventHandler(String targetedHtmlClass, XMLEventHandler fallbackHandler) {

		Preconditions.checkArgument(!Strings.isNullOrEmpty(targetedHtmlClass),"targeted html class name not specified");

		this.targetedHtmlClass = targetedHtmlClass;
		this.fallbackHandler = fallbackHandler;
	}

	@Override
	public void handleStartElementEvent(StartElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter, BodyProcessingContext bodyProcessingContext) throws XMLStreamException {

		if(!isTargetedClass(event)) {
			fallbackHandler.handleStartElementEvent(event,xmlEventReader,eventWriter,bodyProcessingContext);
			return;
		}

		skipUntilMatchingEndTag(event.getName().getLocalPart(),xmlEventReader);

	}

	@Override
	public void handleEndElementEvent(EndElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter) throws XMLStreamException {
		fallbackHandler.handleEndElementEvent(event,xmlEventReader,eventWriter);
	}

	private boolean isTargetedClass(StartElement event) {

		Attribute classesAttr = event.getAttributeByName(QName.valueOf("class"));
		if(classesAttr==null) {
			return false;
		}

		List<String> classes = Arrays.asList(classesAttr.getValue().split(" "));

		return classes.contains(targetedHtmlClass);
	}
}
