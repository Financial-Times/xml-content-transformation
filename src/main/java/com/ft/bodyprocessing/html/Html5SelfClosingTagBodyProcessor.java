package com.ft.bodyprocessing.html;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.BodyProcessingException;
import com.ft.bodyprocessing.BodyProcessor;
import com.ft.bodyprocessing.xml.StAXTransformingBodyProcessor;
import com.ft.bodyprocessing.xml.eventhandlers.RetainXMLEventHandler;
import com.ft.bodyprocessing.xml.eventhandlers.XMLEventHandlerRegistry;

/**
 * Rewrites arbitrary XML markup to produce HTML 5 style end-tags i.e. to XHTML5
 *
 * @see com.ft.bodyprocessing.writer.HTML5VoidElementHandlingXMLBodyWriter
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

        // The class StAXTransformingBodyProcessor is hard-baked to produce HTML5 via HTML5VoidElementHandlingXMLBodyWriter
        StAXTransformingBodyProcessor processor = new StAXTransformingBodyProcessor(echoEventsRegistry);

        return processor.process(body, bodyProcessingContext);
    }
}
