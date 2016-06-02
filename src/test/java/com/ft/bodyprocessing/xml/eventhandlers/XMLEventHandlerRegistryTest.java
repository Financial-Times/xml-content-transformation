package com.ft.bodyprocessing.xml.eventhandlers;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;


@RunWith(value=MockitoJUnitRunner.class)
public class XMLEventHandlerRegistryTest extends BaseXMLEventHandlerTest {

	private XMLEventHandlerRegistry eventHandlerRegistry;
	
	@Mock private XMLEventHandler mockXMLEventHandler;
	@Mock private XMLEventHandler mockXMLEventHandler2;

	@Before
	public void setup() {
		eventHandlerRegistry = new XMLEventHandlerRegistry();
	}
	
	@Test
	public void startElementEventHandlerRegisteredIsReturned() {
		eventHandlerRegistry.registerStartElementEventHandler(mockXMLEventHandler, "a");
		assertEquals(eventHandlerRegistry.getEventHandler(getStartElement("a")), mockXMLEventHandler);
	}

    @Test
    public void startElementEventHandlersRegisteredAreReturned() {
        eventHandlerRegistry.registerEndElementEventHandler(mockXMLEventHandler, "a");
        eventHandlerRegistry.registerEndElementEventHandler(mockXMLEventHandler2, "a");
        final List<XMLEventHandler> l =  eventHandlerRegistry.getEventHandlers(getEndElement("a"));
        assertEquals(2, l.size());
        assertEquals(l.get(0), mockXMLEventHandler);
        assertEquals(l.get(1), mockXMLEventHandler2);
    }
	
	@Test
	public void startElementEventHandlerRegisteredIsReturnedCaseIsIgnored() {
		eventHandlerRegistry.registerStartElementEventHandler(mockXMLEventHandler, "B");
		assertEquals(eventHandlerRegistry.getEventHandler(getStartElement("b")), mockXMLEventHandler);
	}
	
	@Test
	public void startElementEventHandlerRegisteredIsReturnedCaseIsIgnoredOtherDirection() {
		eventHandlerRegistry.registerStartElementEventHandler(mockXMLEventHandler, "c");
		assertEquals(eventHandlerRegistry.getEventHandler(getStartElement("C")), mockXMLEventHandler);
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionFromRegisterStartElementHandlerIfIfNullHandlerSupplied() {
		eventHandlerRegistry.registerStartElementEventHandler(null, "a");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionFromRegisterStartElementHandlerIfNamesNotSupplied() {
		eventHandlerRegistry.registerStartElementEventHandler(mockXMLEventHandler);
	}
	
	@Test
	public void endElementEventHandlerRegisteredIsReturned() {
		eventHandlerRegistry.registerEndElementEventHandler(mockXMLEventHandler, "a");
		assertEquals(eventHandlerRegistry.getEventHandler(getEndElement("a")), mockXMLEventHandler);
	}

    @Test
    public void endElementEventHandlersRegisteredAreReturned() {
        eventHandlerRegistry.registerEndElementEventHandler(mockXMLEventHandler, "a");
        eventHandlerRegistry.registerEndElementEventHandler(mockXMLEventHandler2, "a");
        final List<XMLEventHandler> l =  eventHandlerRegistry.getEventHandlers(getEndElement("a"));
        assertEquals(2, l.size());
        assertEquals(l.get(0), mockXMLEventHandler);
        assertEquals(l.get(1), mockXMLEventHandler2);
    }
	
	@Test
	public void endElementEventHandlerRegisteredIsReturnedCaseIsIgnored() {
		eventHandlerRegistry.registerEndElementEventHandler(mockXMLEventHandler, "B");
		assertEquals(eventHandlerRegistry.getEventHandler(getEndElement("b")), mockXMLEventHandler);
	}
	
	@Test
	public void endElementEventHandlerRegisteredIsReturnedCaseIsIgnoredOtherDirection() {
		eventHandlerRegistry.registerEndElementEventHandler(mockXMLEventHandler, "c");
		assertEquals(eventHandlerRegistry.getEventHandler(getEndElement("C")), mockXMLEventHandler);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionFromRegisterEndElementHandlerIfIfNullHandlerSupplied() {
		eventHandlerRegistry.registerEndElementEventHandler(null, "a");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionFromRegisterEndElementHandlerIfNamesNotSupplied() {
		eventHandlerRegistry.registerEndElementEventHandler(mockXMLEventHandler);
	}
	
	@Test
	public void entityReferenceEventHandlerRegisteredIsReturned() {
		eventHandlerRegistry.registerEntityReferenceEventHandler(mockXMLEventHandler);
		assertEquals(eventHandlerRegistry.getEventHandler(getEntityReference("nbsp")), mockXMLEventHandler);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionFromRegisterEntityReferenceHandlerIfIfNullHandlerSupplied() {
		eventHandlerRegistry.registerEntityReferenceEventHandler(null);
	}
	
	@Test
	public void charactersEventHandlerRegisteredIsReturned() {
		eventHandlerRegistry.registerCharactersEventHandler(mockXMLEventHandler);
		assertEquals(eventHandlerRegistry.getEventHandler(getCharacters("any")), mockXMLEventHandler);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionFromRegisterCharactersHandlerIfIfNullHandlerSupplied() {
		eventHandlerRegistry.registerCharactersEventHandler(null);
	}
	
	@Test
	public void commentsEventHandlerRegisteredIsReturned() {
		eventHandlerRegistry.registerCommentsEventHandler(mockXMLEventHandler);
		assertEquals(eventHandlerRegistry.getEventHandler(getComment("my comment")), mockXMLEventHandler);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionFromRegisterCommentsHandlerIfIfNullHandlerSupplied() {
		eventHandlerRegistry.registerCommentsEventHandler(null);
	}
	
	@Test
	public void defaultEventHandlerRegisteredIsReturned() {
		eventHandlerRegistry.registerDefaultEventHandler(mockXMLEventHandler);
		assertEquals(eventHandlerRegistry.getEventHandler(getCharacters("any")), mockXMLEventHandler);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionFromRegisterDefaultHandlerIfNullHandlerSupplied() {
		eventHandlerRegistry.registerCharactersEventHandler(null);
	}
	
	@Test
	public void startAndEndElementEventHandlerRegisteredIsReturned() {
		eventHandlerRegistry.registerStartAndEndElementEventHandler(mockXMLEventHandler, "a");
		assertEquals(eventHandlerRegistry.getEventHandler(getEndElement("a")), mockXMLEventHandler);
		assertEquals(eventHandlerRegistry.getEventHandler(getStartElement("a")), mockXMLEventHandler);
	}
	
	@Test
	public void startAndEndElementEventHandlerRegisteredIsReturnedCaseIsIgnored() {
		eventHandlerRegistry.registerStartAndEndElementEventHandler(mockXMLEventHandler, "B");
		assertEquals(eventHandlerRegistry.getEventHandler(getEndElement("b")), mockXMLEventHandler);
		assertEquals(eventHandlerRegistry.getEventHandler(getStartElement("b")), mockXMLEventHandler);
	}
	
	@Test
	public void startAndEndElementEventHandlerRegisteredIsReturnedCaseIsIgnoredOtherDirection() {
		eventHandlerRegistry.registerStartAndEndElementEventHandler(mockXMLEventHandler, "c");
		assertEquals(eventHandlerRegistry.getEventHandler(getEndElement("C")), mockXMLEventHandler);
		assertEquals(eventHandlerRegistry.getEventHandler(getStartElement("C")), mockXMLEventHandler);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionFromRegisterStartAndEndElementHandlerIfIfNullHandlerSupplied() {
		eventHandlerRegistry.registerStartAndEndElementEventHandler(null, "a");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionFromRegisterStartAndEndElementHandlerIfNamesNotSupplied() {
		eventHandlerRegistry.registerStartAndEndElementEventHandler(mockXMLEventHandler);
	}
	
	
}
