package com.ft.bodyprocessing.xml;

import static com.google.common.base.Preconditions.checkArgument;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.ProcessingInstruction;
import javax.xml.stream.events.XMLEvent;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.BodyProcessingException;
import com.ft.bodyprocessing.BodyProcessor;
import com.ft.bodyprocessing.writer.BodyWriter;
import com.ft.bodyprocessing.writer.BodyWriterFactory;
import com.ft.bodyprocessing.writer.HTML5VoidElementHandlingXMLBodyWriterFactory;
import com.ft.bodyprocessing.xml.eventhandlers.XMLEventHandler;
import com.ft.bodyprocessing.xml.eventhandlers.XMLEventHandlerRegistry;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLOutputFactory2;

import java.util.List;


public class StAXTransformingBodyProcessor implements BodyProcessor {

    private BodyWriterFactory bodyWriterFactory = new HTML5VoidElementHandlingXMLBodyWriterFactory((XMLOutputFactory2) XMLOutputFactory2.newInstance());
    private XMLEventHandlerRegistry eventHandlerRegistry;
    private XMLEventReaderFactory xmlEventReaderFactory = new XMLEventReaderFactory((XMLInputFactory2) XMLInputFactory2.newInstance());

    public StAXTransformingBodyProcessor(XMLEventHandlerRegistry eventHandlerRegistry) {
    	checkArgument(eventHandlerRegistry!=null, "eventHandlerRegistry cannot be null");
        this.eventHandlerRegistry = eventHandlerRegistry;
    }

    @Override
    public String process(String body, BodyProcessingContext bodyProcessingContext) throws BodyProcessingException {

        XMLEventReader xmlEventReader = null;
        try {
            xmlEventReader = xmlEventReaderFactory.createXMLEventReader(body);
            
            return doProcess(xmlEventReader, bodyProcessingContext);

        } catch (XMLStreamException e) {
            throw new BodyProcessingException(e);
        } finally {
            if (xmlEventReader != null) {
                try {
                    xmlEventReader.close();
                } catch (XMLStreamException e) {
                    throw new BodyProcessingException(e);
                }
            }
        }
    }

    private String doProcess(XMLEventReader xmlEventReader, BodyProcessingContext bodyProcessingContext) 
            throws XMLStreamException {
        
        BodyWriter bodyWriter = null;
        try {
            bodyWriter = bodyWriterFactory.createBodyWriter();
            
            processEvents(xmlEventReader, bodyProcessingContext, bodyWriter);
            
            return bodyWriter.asString();
        }  finally {
            if (bodyWriter != null) {
                bodyWriter.close();
            }
        }
    }

    private void processEvents(XMLEventReader xmlEventReader, BodyProcessingContext bodyProcessingContext, BodyWriter bodyWriter) 
            throws XMLStreamException {
        while (xmlEventReader.hasNext()) {
            handleEvent(xmlEventReader.nextEvent(), xmlEventReader, bodyProcessingContext, bodyWriter);
        }
    }

    private void handleEvent(XMLEvent event, XMLEventReader xmlEventReader, BodyProcessingContext bodyProcessingContext, BodyWriter bodyWriter) throws XMLStreamException {
        XMLEventHandler eventHandler;
        if (event.isStartElement()) {
            List<XMLEventHandler> eventHandlers = eventHandlerRegistry.getEventHandlers(event.asStartElement());
            for (final XMLEventHandler anEventHandler : eventHandlers) {
                anEventHandler.handleStartElementEvent(event.asStartElement(), xmlEventReader, bodyWriter, bodyProcessingContext);
            }
        } else if (event.isEndElement()) {
            eventHandler = eventHandlerRegistry.getEventHandler(event.asEndElement());
            eventHandler.handleEndElementEvent(event.asEndElement(), xmlEventReader, bodyWriter);
        } else if (event.isCharacters()) {
            eventHandler = eventHandlerRegistry.getEventHandler(event.asCharacters());
            eventHandler.handleCharactersEvent(event.asCharacters(), xmlEventReader, bodyWriter);
        } else if (event.getEventType() == XMLEvent.COMMENT){
        	eventHandler = eventHandlerRegistry.getEventHandler((Comment)event);
        	eventHandler.handleCommentEvent((Comment)event, xmlEventReader, bodyWriter);
    	} else if (event.getEventType() == XMLEvent.ENTITY_REFERENCE) {
    		eventHandler = eventHandlerRegistry.getEventHandler((EntityReference) event);
    		eventHandler.handleEntityReferenceEvent((EntityReference) event, xmlEventReader, bodyWriter);
        } else if (event.getEventType() == XMLEvent.PROCESSING_INSTRUCTION) {
            eventHandler = eventHandlerRegistry.getEventHandler((ProcessingInstruction) event);
            eventHandler.handleProcessingInstructionEvent((ProcessingInstruction) event, xmlEventReader, bodyWriter);
    	} else {
            eventHandler = eventHandlerRegistry.getEventHandler(event);
            eventHandler.handleEvent(event, xmlEventReader, bodyWriter);
        }
    }
    

}
