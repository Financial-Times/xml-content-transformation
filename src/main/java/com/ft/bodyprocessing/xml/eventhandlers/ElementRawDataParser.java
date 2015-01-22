package com.ft.bodyprocessing.xml.eventhandlers;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.StringWriter;

public class ElementRawDataParser {

    /**
     * Extracts the contents of an element until its end element is reached. It assumes that the XMLElementReader is pointing to the start element.
     * @param endElementName
     * @param xmlEventReader
     * @return
     * @throws javax.xml.stream.XMLStreamException
     */
    public String parse(String endElementName, XMLEventReader xmlEventReader) throws XMLStreamException {
        return this.parse(endElementName, xmlEventReader, null);
    }

    /**
     * Parses the contents starting at the next element in the XML Reader until the end tag (endElementName) if found.
     * Should the end element itself need to be included, the startElement should be provided which is used to wrap the contents and
     * an end tag is written to ensure the content parsed is valid XML.
     * @param endElementName
     * @param xmlEventReader
     * @param startElement
     * @return
     * @throws javax.xml.stream.XMLStreamException
     */
	public String parse(String endElementName, XMLEventReader xmlEventReader, StartElement startElement) throws XMLStreamException {
		StringWriter writer = new StringWriter();
        boolean hasReachedEndElement = false;
        ensureEndElementNameMatchesStartElement(endElementName, startElement);
        
		// Wrap with start element based on the startElement
        if(needsRootElementWrapping(startElement)) {
			startElement.writeAsEncodedUnicode(writer);
		}

        int startElements = 1;// default since the starting root element is present already
        
        // Iterate over the xml 
        while(xmlEventReader.hasNext() && !hasReachedEndElement) {
            XMLEvent childEvent = xmlEventReader.nextEvent();

            // Track nested starting elements
            if(isNestedRootStartElement(childEvent, endElementName)) {
                
                startElements++;
            
            } else if(isRootEndElement(childEvent, endElementName)) {
                
                // Check if this ending element belongs to a nested root element
                if(startElements > 1) {
                    
                    startElements--; // cancel-out nested root element
                
                } else { // reached the end of the xml stream that needs parsing
                    hasReachedEndElement = true;
                    if(needsRootElementWrapping(startElement)) {
                        // Wrap end element based on the endElement, if needed
                        childEvent.writeAsEncodedUnicode(writer);
                    }
                    continue;
                }
            }
            // Write the parsed element/characters
            childEvent.writeAsEncodedUnicode(writer);
        }
        
        if(!hasReachedEndElement) {
            throw new IllegalArgumentException(String.format("Element name mismatch, could not find the end element for %s", endElementName));
        }
        return writer.toString();
	}

    
    private boolean isRootEndElement(XMLEvent childEvent, String endElementName) {
        if(childEvent.isEndElement() && isElementNamed(childEvent.asEndElement().getName(), endElementName)) {
            return true;
        }
        return false;
    }

    private boolean isNestedRootStartElement(XMLEvent childEvent, String endElementName) {
        if(childEvent.isStartElement() && isElementNamed(childEvent.asStartElement().getName(), endElementName)) {
            return true;
        }
        return false;
    }

    private void ensureEndElementNameMatchesStartElement(String endElementName, StartElement startElement) {
        String startElementName = parseStartElementName(startElement);
        if(startElementName != null && !startElementName.equals(endElementName.toLowerCase())) {
            throw new IllegalArgumentException("The endElementName and the StartElement must match!");
        }
    }

    private String parseStartElementName(StartElement startElement) {
        if(startElement !=null) {
            return  startElement.getName().getLocalPart().toLowerCase();
        }
        return null;
    }

    private boolean needsRootElementWrapping(StartElement startElement) {
        return startElement != null;
    }

    private boolean isElementNamed(QName elementName, String nameToMatch) {
        return elementName.getLocalPart().toLowerCase().equals(nameToMatch.toLowerCase());
    }

}
