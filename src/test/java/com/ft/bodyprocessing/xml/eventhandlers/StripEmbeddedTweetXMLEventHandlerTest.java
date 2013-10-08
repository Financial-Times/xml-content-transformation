package com.ft.bodyprocessing.xml.eventhandlers;

import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Comment;

import org.codehaus.stax2.XMLEventReader2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ft.bodyprocessing.writer.BodyWriter;

@RunWith(value=MockitoJUnitRunner.class)
public class StripEmbeddedTweetXMLEventHandlerTest extends BaseXMLEventHandlerTest {

	StripEmbeddedTweetXMLEventHandler eventHandler;
	
	@Mock private BodyWriter      mockEventWriter;
	@Mock private XMLEventReader2 mockXmlEventReader;
	
	@Before
	public void setUp() {
		eventHandler = new StripEmbeddedTweetXMLEventHandler();
	}
	
	@Test
	public void tweetsShouldBeRemovedFromStartToEndComment() throws XMLStreamException {
		Comment startComment = getComment("tweet id :");
		//"<!-- tweet id: 23423-->Some characters<!-- end of tweet -->";
		when(mockXmlEventReader.hasNext()).thenReturn(true, true, true, false);
		when(mockXmlEventReader.nextEvent()).thenReturn(startComment, getCharacters("Some characters"), 
				getComment("end of tweet"));
		eventHandler.handleComment(startComment, mockXmlEventReader, mockEventWriter);
		verifyZeroInteractions(mockEventWriter);
	}
	
	@Test
	public void tweetsShouldBeRemovedFromStartToEndCommentIgnoringOtherComments() throws XMLStreamException {
		Comment startComment = getComment("tweet id :");
		//"<!-- tweet id: 23423-->Some characters<!-- end of tweet -->";
		when(mockXmlEventReader.hasNext()).thenReturn(true, true, true, false);
		when(mockXmlEventReader.nextEvent()).thenReturn(startComment, getCharacters("Some characters"), 
				getComment("some other comment"), getComment("end of tweet"));
		eventHandler.handleComment(startComment, mockXmlEventReader, mockEventWriter);
		verifyZeroInteractions(mockEventWriter);
	}
	
	@Test
	public void otherCommentsShouldBeRemoved() throws XMLStreamException {
		Comment startComment = getComment("some other comment");
		//"<!-- tweet id: 23423-->Some characters<!-- end of tweet -->";
		when(mockXmlEventReader.hasNext()).thenReturn(true, true, true, false);
		when(mockXmlEventReader.nextEvent()).thenReturn(startComment, getCharacters("Some characters"), 
				getComment("end of tweet"));
		eventHandler.handleComment(startComment, mockXmlEventReader, mockEventWriter);
		verifyZeroInteractions(mockEventWriter);
	}
	
	@Test
	//TODO - what should we do if we don't find a matching end comment? 
	public void mismatchedTweetCommentsShouldFail() throws XMLStreamException {
		Comment startComment = getComment("tweet id :");
		//"<!-- tweet id: 23423-->Some characters<!-- end of tweet -->";
		when(mockXmlEventReader.hasNext()).thenReturn(true, true, true, false);
		when(mockXmlEventReader.nextEvent()).thenReturn(startComment, getCharacters("Some characters"), 
				getComment(" some other comment"));
		eventHandler.handleComment(startComment, mockXmlEventReader, mockEventWriter);
		verifyZeroInteractions(mockEventWriter);
	}
}
