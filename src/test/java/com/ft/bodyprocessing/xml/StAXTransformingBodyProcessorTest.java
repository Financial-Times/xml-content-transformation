package com.ft.bodyprocessing.xml;


import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.ft.bodyprocessing.xml.eventhandlers.BaseXMLEventHandler;
import com.ft.bodyprocessing.xml.eventhandlers.StripElementIfSpecificAttributesXmlEventHandler;
import org.junit.Test;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.BodyProcessingException;
import com.ft.bodyprocessing.xml.eventhandlers.RetainXMLEventHandler;
import com.ft.bodyprocessing.xml.eventhandlers.StripXMLEventHandler;
import com.ft.bodyprocessing.xml.eventhandlers.XMLEventHandlerRegistry;

import java.util.Collections;


public class StAXTransformingBodyProcessorTest {

	private static final String BODY = "<p>Text to process with entity ref &euro; in it</p><!-- comment --><p>More text to process.</p><p><?EM-dummyText [Insert news in depth title here]?>Processing instruction is removed by default.</p>";
	private static final String BODY_2 = "<ft-content k='1'></ft-content><ft-content l='2'></ft-content>The rest.<!-- comment --><?EM-dummyText [Insert news in depth title here]?>The rest.";

	private static final String EXPECTED_BODY_CHARACTERS_REMOVED = "<p>&euro;</p><!-- comment --><p></p><p></p>";
	private static final String EXPECTED_BODY_COMMENT_REMOVED = "<p>Text to process with entity ref &euro; in it</p><p>More text to process.</p><p>Processing instruction is removed by default.</p>";
	private static final String EXPECTED_BODY_START_END_TAGS_REMOVED = "Text to process with entity ref &euro; in it<!-- comment -->More text to process.Processing instruction is removed by default.";
    private static final String EXPECTED_BODY_START_END_TAGS_REMOVED_2 = "The rest.<!-- comment -->The rest.";
    private static final String EXPECTED_BODY_ENTITY_REFERENCE_REMOVED = "<p>Text to process with entity ref  in it</p><!-- comment --><p>More text to process.</p><p>Processing instruction is removed by default.</p>";
	private static final String EXPECTED_BODY_PROCESSING_INSTR_REMOVED = "<p>Text to process with entity ref &euro; in it</p><!-- comment --><p>More text to process.</p><p>Processing instruction is removed by default.</p>";

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
    public void shouldHandleStartAndEndElementsWithMultipleEventHandlers() {
        // have to test both together as unbalanced tags results in a BodyProcessingException
        XMLEventHandlerRegistry eventHandlerRegistry = new XMLEventHandlerRegistry() {
            {
                super.registerDefaultEventHandler(new RetainXMLEventHandler());
                super.registerStartAndEndElementEventHandler(new StripElementIfSpecificAttributesXmlEventHandler(Collections.singletonMap("k", "1"), new BaseXMLEventHandler()), "ft-content");
                super.registerStartAndEndElementEventHandler(new StripElementIfSpecificAttributesXmlEventHandler(Collections.singletonMap("l", "2"), new BaseXMLEventHandler()), "ft-content");
            }
        };
        bodyProcessor = new StAXTransformingBodyProcessor(eventHandlerRegistry);
        String processedBody = bodyProcessor.process(BODY_2, new BodyProcessingContext(){});
        assertThat("processedBody", processedBody, is(equalTo(EXPECTED_BODY_START_END_TAGS_REMOVED_2)));
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

    @Test
    public void shouldHandleProcessingInstructionWithCorrectEventHandler() {
        XMLEventHandlerRegistry eventHandlerRegistry = new XMLEventHandlerRegistry() {
            { super.registerDefaultEventHandler(new RetainXMLEventHandler());}
        };
        bodyProcessor = new StAXTransformingBodyProcessor(eventHandlerRegistry);
        String processedBody = bodyProcessor.process(BODY, new BodyProcessingContext(){});
        assertThat("processedBody", processedBody, is(equalTo(EXPECTED_BODY_PROCESSING_INSTR_REMOVED)));
    }

    @Test
    public void shouldHandleWithCorrectEventHandler() {
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
