package com.ft.bodyprocessing.xml.eventhandlers;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class XMLEventHandlerRegistry {

    class NameToHandler {

        private String name;
        private XMLEventHandler handler;

        public NameToHandler(final String name, final XMLEventHandler handler) {
            this.name = name;
            this.handler = handler;
        }

        public String getName() {
            return name;
        }

        public XMLEventHandler getXmlEventHandler() {
            return handler;
        }
    }

	private XMLEventHandler defaultEventHandler = null;
	private XMLEventHandler charactersEventHandler = null;
	private List<NameToHandler> endElementEventHandlers = new ArrayList<>();
	private List<NameToHandler> startElementEventHandlers = new ArrayList<>();
	private XMLEventHandler entityReferenceEventHandler;
	private XMLEventHandler commentsEventHandler;
	
	public XMLEventHandlerRegistry() {	
		this.defaultEventHandler = new BaseXMLEventHandler();
	}
	
	public XMLEventHandler getEventHandler(XMLEvent event) {
		return defaultEventHandler;
	}
	
	public XMLEventHandler getEventHandler(StartElement event) {
        for (final NameToHandler nameToHandler : startElementEventHandlers) {
            if (nameToHandler.getName().equals(event.asStartElement().getName().getLocalPart().toLowerCase())) {
                return nameToHandler.getXmlEventHandler();
            }
        }
        return defaultEventHandler;
	}

    public List<XMLEventHandler> getEventHandlers(StartElement event) {
        final List<XMLEventHandler> matchingHandlers = new ArrayList<>();
        for (final NameToHandler nameToHandler : startElementEventHandlers) {
            if (nameToHandler.getName().equals(event.asStartElement().getName().getLocalPart().toLowerCase())) {
                matchingHandlers.add(nameToHandler.getXmlEventHandler());
            }
        }
        if (!matchingHandlers.isEmpty()) {
            return matchingHandlers;
        }
        return Collections.singletonList(defaultEventHandler);
    }
	
	public XMLEventHandler getEventHandler(EndElement event) {
        for (final NameToHandler nameToHandler : endElementEventHandlers) {
            if (nameToHandler.getName().equals(event.asEndElement().getName().getLocalPart().toLowerCase())) {
                return nameToHandler.getXmlEventHandler();
            }
        }
        return defaultEventHandler;
	}

    public List<XMLEventHandler> getEventHandlers(EndElement event) {
        final List<XMLEventHandler> matchingHandlers = new ArrayList<>();
        for (final NameToHandler nameToHandler : endElementEventHandlers) {
            if (nameToHandler.getName().equals(event.asEndElement().getName().getLocalPart().toLowerCase())) {
                matchingHandlers.add(nameToHandler.getXmlEventHandler());
            }
        }
        if (!matchingHandlers.isEmpty()) {
            return matchingHandlers;
        }
        return Collections.singletonList(defaultEventHandler);
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
			endElementEventHandlers.add(new NameToHandler(name.toLowerCase(), endElementEventHandler));
		}
	}

	public void registerStartElementEventHandler(XMLEventHandler 
			startElementEventHandler, String... names) {
		notNull(startElementEventHandler, "startElementEventHandler cannot be null");
		notNull(names, "names cannot be null");
		notEmpty(names, "names cannot be empty");
		
		for (String name: names) {
			startElementEventHandlers.add(new NameToHandler(name.toLowerCase(), startElementEventHandler));
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
        final String s = "eventHandler cannot be null";
        notNull(eventHandler, s);
		notNull(names, "names cannot be null");
		notEmpty(names, "names cannot be empty");
		for(String name: names) {
			startElementEventHandlers.add(new NameToHandler(name.toLowerCase(), eventHandler));
			endElementEventHandlers.add(new NameToHandler(name.toLowerCase(), eventHandler));
		}
		
	}

    private void notEmpty(String[] names, String message) {
        checkArgument(names!=null && names.length>0, message);
    }

    private void notNull(Object value, String message) {
        checkArgument(value!=null, message);
    }

}
