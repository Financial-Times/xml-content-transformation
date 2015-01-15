package com.ft.bodyprocessing.writer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;

import org.codehaus.stax2.XMLOutputFactory2;
import org.codehaus.stax2.XMLStreamWriter2;

import com.ft.bodyprocessing.BodyProcessingException;
import com.ft.bodyprocessing.regex.RegexRemoverBodyProcessor;


/**
 * An XML based writer that does NOT self-close elements, except for specific void html5 elements.
 * 
 * Generally in html5, elements should NOT be self closing. i.e. <p></p> is valid but <p/> is not.
 * 
 * For void elements, i.e. ones that can never have any content, it is NOT valid to have 
 * separate start and end elements. e.g. <br> and <br/> are both valid html5, <br></br> is not
 * 
 * For html5, the void element doesn't require a trailing slash but can support it - as we want 
 * valid xml, we will output a trailing slash, i.e. <br/>
 * 
 * @author sarah.wells
 *
 */
public class HTML5VoidElementHandlingXMLBodyWriter implements BodyWriter {
	
	private static final String P_TAG_NAME = "p";

    private static List<String> VOID_ELEMENTS = Arrays.asList(
			"area", "base", "br", "col", "command", "embed", 
			"hr", "img", "input", "keygen", "link", "meta", 
			"param", "source", "track", "wbr");
	
    private StringWriter stringWriter;
    private XMLStreamWriter2 xmlStreamWriter2;

    private boolean isPTagCurrentlyOpen;
	
	public HTML5VoidElementHandlingXMLBodyWriter(XMLOutputFactory2 xmlOutputFactory) throws IOException, XMLStreamException {
        
        this.stringWriter = new StringWriter();
        try {
            xmlStreamWriter2 = xmlOutputFactory.createXMLStreamWriter(stringWriter, "UTF-8");
            xmlStreamWriter2.setProperty(XMLOutputFactory2.P_AUTOMATIC_EMPTY_ELEMENTS, true);
            xmlStreamWriter2.writeStartElement("html");
        } catch (XMLStreamException e) {
            throw new BodyProcessingException(e);
        }
	}

	@Override
	public void flush() {
		try {
		    xmlStreamWriter2.flush();
		} catch (XMLStreamException e) {
			throw new BodyProcessingException(e);
		}
	}

	@Override
	public void close() {
		try {
		    xmlStreamWriter2.close();
		} catch (XMLStreamException e) {
			throw new BodyProcessingException(e);
		}
	}

	@Override
	public void write(String data) {
		try {
		    xmlStreamWriter2.writeCharacters(data);
		} catch (XMLStreamException e) {
			throw new BodyProcessingException(e);
		}
	}

	@Override
	public void writeRaw(String data) {
		try {
			xmlStreamWriter2.writeRaw(data);
		} catch (XMLStreamException e) {
			throw new BodyProcessingException(e);
		}
	}

	@Override
	public void writeStartTag(String name, Map<String, String> validAttributesAndValues) {
		try {
		    xmlStreamWriter2.writeStartElement(name);
			if(isPTAg(name)) {
			    this.isPTagCurrentlyOpen = true;
			}
		    if(validAttributesAndValues != null) {
				for (Entry<String,String> entry: validAttributesAndValues.entrySet()) {
				    xmlStreamWriter2.writeAttribute(entry.getKey(), entry.getValue());
				} 
			}
			
		}catch (XMLStreamException e) {
			throw new BodyProcessingException(e);
		}
	}

	@Override
	public void writeEndTag(String name) {
		try {
			if (!VOID_ELEMENTS.contains(name.toLowerCase())) {
				// this is to force output of a specific end element
			    xmlStreamWriter2.writeCharacters("");
			}	
			xmlStreamWriter2.writeEndElement();
			if(isPTAg(name)){
                this.isPTagCurrentlyOpen = false;
            }
		} catch (XMLStreamException e) {
			throw new BodyProcessingException(e);
		}
	}

	@Override
	public void writeEntityReference(String name) {
		try {
			xmlStreamWriter2.writeEntityRef(name);
		} catch (XMLStreamException e) {
			throw new BodyProcessingException(e);
		}
	}

	@Override
	public void writeComment(String text) {
		try {
			xmlStreamWriter2.writeComment(text);
		} catch (XMLStreamException e) {
			throw new BodyProcessingException(e);
		}	
	}

    @Override
	public String asString() {
	    try {
            xmlStreamWriter2.writeEndElement();
        } catch (XMLStreamException e) {
        	throw new BodyProcessingException(e);
        }
	    this.flush();
	    
	    return removeRootElement(stringWriter.toString());
	}
	
    @Override
    public boolean isPTagCurrentlyOpen() {
        return this.isPTagCurrentlyOpen;
    }
    
    private boolean isPTAg(String name) {
        return name.toLowerCase().equals(P_TAG_NAME);
    }
    
    // TODO: Performance! Check for a more efficient way to remove HTML elements
    private String removeRootElement(String theString) {
        RegexRemoverBodyProcessor removeRootNodeProcessor = new RegexRemoverBodyProcessor("<html/>|<html>|</html>|<html");
        return removeRootNodeProcessor.process(theString, null);
    }

}
