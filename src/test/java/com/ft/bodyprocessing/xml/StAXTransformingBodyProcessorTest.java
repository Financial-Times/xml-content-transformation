package com.ft.bodyprocessing.xml;


import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.BodyProcessingException;
import com.ft.bodyprocessing.xml.eventhandlers.RetainXMLEventHandler;
import com.ft.bodyprocessing.xml.eventhandlers.StripXMLEventHandler;
import com.ft.bodyprocessing.xml.eventhandlers.XMLEventHandlerRegistry;



public class StAXTransformingBodyProcessorTest {

	private static final String BODY = "<p>Text to process with entity ref &euro; in it</p><!-- comment --><p>More text to process</p>";

	private static final String EXPECTED_BODY_CHARACTERS_REMOVED = "<p>&euro;</p><!-- comment --><p></p>";
	private static final String EXPECTED_BODY_COMMENT_REMOVED = "<p>Text to process with entity ref &euro; in it</p><p>More text to process</p>";
	private static final String EXPECTED_BODY_START_END_TAGS_REMOVED = "Text to process with entity ref &euro; in it<!-- comment -->More text to process";
	private static final String EXPECTED_BODY_ENTITY_REFERENCE_REMOVED = "<p>Text to process with entity ref  in it</p><!-- comment --><p>More text to process</p>";
	
	private static final String INVALID_BODY = "<p>Unbalanced xml...";

	private StAXTransformingBodyProcessor bodyProcessor;
	
	@Test
	public void shouldHandleCharactersWithCorrectEventHandler() {
		XMLEventHandlerRegistry eventHandlerRegistry = new XMLEventHandlerRegistry() {
			{ super.registerDefaultEventHandler(new RetainXMLEventHandler());
			  super.registerCharactersEventHandler(new StripXMLEventHandler());}
		};
		bodyProcessor = new StAXTransformingBodyProcessor(eventHandlerRegistry);
		String processedBody = bodyProcessor.process(BODY, new BodyProcessingContext(){});
		assertThat("processedBody", processedBody, is(equalTo(EXPECTED_BODY_CHARACTERS_REMOVED)));
	}
	
	@Test
	public void shouldHandleCommentsWithCorrectEventHandler() {
		XMLEventHandlerRegistry eventHandlerRegistry = new XMLEventHandlerRegistry() {
			{ super.registerDefaultEventHandler(new RetainXMLEventHandler());
			  super.registerCommentsEventHandler(new StripXMLEventHandler());}
		};
		bodyProcessor = new StAXTransformingBodyProcessor(eventHandlerRegistry);
		String processedBody = bodyProcessor.process(BODY, new BodyProcessingContext(){});
		assertThat("processedBody", processedBody, is(equalTo(EXPECTED_BODY_COMMENT_REMOVED)));
	}
	
	@Test
	public void shouldHandleStartAndEndElementsWithCorrectEventHandler() {
		// have to test both together as unbalanced tags results in a BodyProcessingException
		XMLEventHandlerRegistry eventHandlerRegistry = new XMLEventHandlerRegistry() {
			{ super.registerDefaultEventHandler(new RetainXMLEventHandler());
			  super.registerStartAndEndElementEventHandler(new StripXMLEventHandler(), "p");}
		};
		bodyProcessor = new StAXTransformingBodyProcessor(eventHandlerRegistry);
		String processedBody = bodyProcessor.process(BODY, new BodyProcessingContext(){});
		assertThat("processedBody", processedBody, is(equalTo(EXPECTED_BODY_START_END_TAGS_REMOVED)));
	}
	
	@Test
	public void shouldHandleEntityReferenceWithCorrectEventHandler() {
		XMLEventHandlerRegistry eventHandlerRegistry = new XMLEventHandlerRegistry() {
			{ super.registerDefaultEventHandler(new RetainXMLEventHandler());
			  super.registerEntityReferenceEventHandler(new StripXMLEventHandler());}
		};
		bodyProcessor = new StAXTransformingBodyProcessor(eventHandlerRegistry);
		String processedBody = bodyProcessor.process(BODY, new BodyProcessingContext(){});
		assertThat("processedBody", processedBody, is(equalTo(EXPECTED_BODY_ENTITY_REFERENCE_REMOVED)));
	}
	
	@Test(expected = BodyProcessingException.class)
	public void shouldFailForInvalidXML() {
		XMLEventHandlerRegistry eventHandlerRegistry = new XMLEventHandlerRegistry() {
			{ super.registerDefaultEventHandler(new StripXMLEventHandler());}
		};
		bodyProcessor = new StAXTransformingBodyProcessor(eventHandlerRegistry);
		String processedBody = bodyProcessor.process(INVALID_BODY, new BodyProcessingContext(){});
		assertThat("processedBody", processedBody, is(equalTo("")));
		
	}

}
