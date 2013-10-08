package com.ft.bodyprocessing.xml.eventhandlers;



public class StructuredMethodeSourcedBodyXMLEventHandlerRegistry extends XMLEventHandlerRegistry {



	public StructuredMethodeSourcedBodyXMLEventHandlerRegistry() {

		//default is to skip events but leave content - anything not configured below will be handled via this
		super.registerDefaultEventHandler(new StripXMLEventHandler());
		super.registerEndElementEventHandler(new ReplaceWithStringXMLEventHandler("\n"), "p", "h1", "h2", "h3", "h4", "h5", "h6");
		// so that we don't get words running together when these tags are removed
		super.registerEndElementEventHandler(new ReplaceWithStringXMLEventHandler(" "), "br", "li", "ol", "ul");
		super.registerStartElementEventHandler(new ReplaceWithStringXMLEventHandler(" "), "ol", "ul");
		super.registerCharactersEventHandler(new RetainXMLEventHandler());
        super.registerEntityReferenceEventHandler(new PlainTextHtmlEntityReferenceEventHandler());
    }
}
