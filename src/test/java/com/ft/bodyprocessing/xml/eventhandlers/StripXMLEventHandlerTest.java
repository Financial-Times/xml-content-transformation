package com.ft.bodyprocessing.xml.eventhandlers;

import static org.mockito.Mockito.verifyZeroInteractions;

import javax.xml.stream.events.Comment;
import javax.xml.stream.events.EntityReference;

import org.codehaus.stax2.XMLEventReader2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.writer.BodyWriter;

@RunWith(value=MockitoJUnitRunner.class)
public class StripXMLEventHandlerTest extends BaseXMLEventHandlerTest {

	private StripXMLEventHandler eventHandler;
	
	@Mock private XMLEventReader2 mockXmlEventReader;
	@Mock private BodyWriter eventWriter;
	@Mock private BodyProcessingContext mockBodyProcessingContext;
	
	@Before
	public void setup() {
		eventHandler = new StripXMLEventHandler();
	}
	
	@Test
	public void charactersShouldNotBeOutput() throws Exception {
		eventHandler.handleCharactersEvent(getCharacters("test"), mockXmlEventReader, eventWriter);
		verifyZeroInteractions(mockXmlEventReader, eventWriter);
	}
	
	@Test
	public void startElementShouldNotBeOutput() throws Exception {
		eventHandler.handleStartElementEvent(getStartElement("a"), mockXmlEventReader, eventWriter, mockBodyProcessingContext);
		verifyZeroInteractions(mockXmlEventReader, eventWriter);
	}
	
	@Test
	public void endElementShouldNotBeOutput() throws Exception {
		eventHandler.handleEndElementEvent(getEndElement("a"), mockXmlEventReader, eventWriter);
		verifyZeroInteractions(mockXmlEventReader, eventWriter);
	}
	
	@Test
     public void commentShouldNotBeOutput() throws Exception {
        Comment comment = getComment("comment text");
        eventHandler.handleCommentEvent(comment, mockXmlEventReader, eventWriter);
        verifyZeroInteractions(mockXmlEventReader, eventWriter);
    }

    @Test
    public void processingInstructionShouldNotBeOutput() throws Exception {
        eventHandler.handleProcessingInstructionEvent(getProcessingInstruction("EM", "dummyText[Insert news in depth title here"), mockXmlEventReader, eventWriter);
        verifyZeroInteractions(mockXmlEventReader, eventWriter);
    }
		
	@Test
	public void entityReferenceShouldNotBeOutput() throws Exception {
		EntityReference entityReference = getEntityReference("someEntity");
		eventHandler.handleEntityReferenceEvent(entityReference, mockXmlEventReader, eventWriter);
		verifyZeroInteractions(mockXmlEventReader, eventWriter);
	}

}
