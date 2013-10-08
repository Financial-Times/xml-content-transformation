package com.ft.bodyprocessing.xml.eventhandlers;

import static org.springframework.util.Assert.notNull;
import static org.springframework.util.Assert.notEmpty;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class XMLEventHandlerRegistry {

	private XMLEventHandler defaultEventHandler = null;
	private XMLEventHandler charactersEventHandler = null;
	private Map<String, XMLEventHandler> endElementEventHandlers = new HashMap<String,XMLEventHandler>();
	private Map<String, XMLEventHandler> startElementEventHandlers = new HashMap<String,XMLEventHandler>();
	private XMLEventHandler entityReferenceEventHandler;
	private XMLEventHandler commentsEventHandler;
	
	public XMLEventHandlerRegistry() {	
		this.defaultEventHandler = new BaseXMLEventHandler();
	}
	
	public XMLEventHandler getEventHandler(XMLEvent event) {
		return defaultEventHandler;
	}
	
	public XMLEventHandler getEventHandler(StartElement event) {
		XMLEventHandler eventHandler = startElementEventHandlers.get(event.asStartElement().getName().getLocalPart().toLowerCase());
		if (eventHandler == null) {
			eventHandler = (XMLEventHandler) defaultEventHandler;
		}
		return eventHandler;
	}
	
	public XMLEventHandler getEventHandler(EndElement event) {
		XMLEventHandler eventHandler = endElementEventHandlers.get(event.asEndElement().getName().getLocalPart().toLowerCase());
		if (eventHandler == null) {
			eventHandler = (XMLEventHandler) defaultEventHandler;
		}
		return eventHandler;
	}
	
	public XMLEventHandler getEventHandler(EntityReference event) {
		XMLEventHandler eventHandler = entityReferenceEventHandler;
		if (eventHandler == null) {
			eventHandler = (XMLEventHandler) defaultEventHandler;
		}
		return eventHandler;
	}
	
	public XMLEventHandler getEventHandler(Characters event) {
		XMLEventHandler eventHandler = charactersEventHandler;
		if (eventHandler == null) {
			eventHandler = (XMLEventHandler)defaultEventHandler;
		}	
		return eventHandler;
	}
	
	public XMLEventHandler getEventHandler(Comment event) {
		XMLEventHandler eventHandler = commentsEventHandler;
		if (eventHandler == null) {
			eventHandler = (XMLEventHandler)defaultEventHandler;
		}	
		return eventHandler;
	}

	public void registerEndElementEventHandler(XMLEventHandler 
			endElementEventHandler, String... names) {
		notNull(endElementEventHandler, "endElementEventHandler cannot be null");
		notNull(names, "names cannot be null");
		notEmpty(names, "names cannot be empty");
		for(String name: names) {
			endElementEventHandlers.put(name.toLowerCase(), endElementEventHandler);
		}
	}

	public void registerStartElementEventHandler(XMLEventHandler 
			startElementEventHandler, String... names) {
		notNull(startElementEventHandler, "startElementEventHandler cannot be null");
		notNull(names, "names cannot be null");
		notEmpty(names, "names cannot be empty");
		
		for(String name: names) {
			startElementEventHandlers.put(name.toLowerCase(), startElementEventHandler);
		}
	}

	public void registerCharactersEventHandler(XMLEventHandler charactersEventHandler) {
		notNull(charactersEventHandler, "charactersEventHandler cannot be null");
		this.charactersEventHandler = charactersEventHandler;
	}
	
	public void registerCommentsEventHandler(XMLEventHandler commentsEventHandler) {
		notNull(commentsEventHandler, "commentsEventHandler cannot be null");
		this.commentsEventHandler = commentsEventHandler;
	}

    public void registerEntityReferenceEventHandler(XMLEventHandler entityReferenceEventHandler) {
        notNull(entityReferenceEventHandler, "entityReferenceEventHandler cannot be null");
        this.entityReferenceEventHandler = entityReferenceEventHandler;
    }

    public void registerDefaultEventHandler(XMLEventHandler defaultEventHandler) {
		notNull(defaultEventHandler, "defaultEventHandler cannot be null");
		this.defaultEventHandler = defaultEventHandler;
	}

	public void registerStartAndEndElementEventHandler(XMLEventHandler eventHandler,
			String... names) {
		notNull(eventHandler, "eventHandler cannot be null");
		notNull(names, "names cannot be null");
		notEmpty(names, "names cannot be empty");
		for(String name: names) {
			startElementEventHandlers.put(name.toLowerCase(), eventHandler);
			endElementEventHandlers.put(name.toLowerCase(), eventHandler);
		}
		
	}

}
