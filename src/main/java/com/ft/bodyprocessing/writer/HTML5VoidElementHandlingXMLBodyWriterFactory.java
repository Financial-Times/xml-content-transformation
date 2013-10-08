package com.ft.bodyprocessing.writer;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.codehaus.stax2.XMLOutputFactory2;


/**
 * A factory for creating HTML5VoidElementHandlingXMLBodyWriters.
 * 
 * Set up to automatically self-close elements, which gives us the behaviour we want in the writer
 * with respect to void elements.
 * 
 * The writer will ensure that non-void elements are NOT self closing.
 * 
 * @author sarah.wells
 *
 */
public class HTML5VoidElementHandlingXMLBodyWriterFactory implements BodyWriterFactory {
	
	private XMLOutputFactory2 xmlOutputFactory = null;
	
	public HTML5VoidElementHandlingXMLBodyWriterFactory(XMLOutputFactory2 xmlOutputFactory) {
		this.xmlOutputFactory = xmlOutputFactory;
		xmlOutputFactory.setProperty(XMLOutputFactory2.P_AUTOMATIC_EMPTY_ELEMENTS, true);	
	}
	
	@Override
	public BodyWriter createBodyWriter() {
		try {
            return new HTML5VoidElementHandlingXMLBodyWriter(xmlOutputFactory);
        } catch (IOException e) {
            throw new RuntimeException();
        } catch (XMLStreamException e) {
            throw new RuntimeException();
        }
	}

}
