package com.ft.bodyprocessing.html;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.BodyProcessingException;
import com.ft.bodyprocessing.BodyProcessor;
import com.ft.bodyprocessing.xml.StAXTransformingBodyProcessor;
import com.ft.bodyprocessing.xml.eventhandlers.RetainXMLEventHandler;
import com.ft.bodyprocessing.xml.eventhandlers.XMLEventHandlerRegistry;

/**
 * Html5SelfClosingTagBodyProcessor
 *
 * @author Simon.Gibbs
 */
public class Html5SelfClosingTagBodyProcessor implements BodyProcessor {
    private XMLEventHandlerRegistry echoEventsRegistry;

    public Html5SelfClosingTagBodyProcessor() {
        this.echoEventsRegistry = new XMLEventHandlerRegistry();
        echoEventsRegistry.registerDefaultEventHandler(new RetainXMLEventHandler());
    }

    @Override
    public String process(String body, BodyProcessingContext bodyProcessingContext) throws BodyProcessingException {

        StAXTransformingBodyProcessor processor = new StAXTransformingBodyProcessor(echoEventsRegistry);

        return processor.process(body, bodyProcessingContext);
    }
}
