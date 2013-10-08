package com.ft.bodyprocessing.xml.eventhandlers;

import com.ft.bodyprocessing.writer.BodyWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.stream.XMLEventReader;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class StructuredXmlHtmlEntityReferenceEventHandlerTest extends BaseXMLEventHandlerTest {
    @Mock
       private BodyWriter eventWriter;
       @Mock
       private XMLEventReader mockXmlEventReader;
   
       private XMLEventHandler htmlEntityReferenceEventHandler;
   
       @Before
       public void setUp() throws Exception {
           htmlEntityReferenceEventHandler = new StructuredXmlHtmlEntityReferenceEventHandler();
       }
   
       @Test
       public void testHandleEntityReferenceEventShouldWriteUnEscapedEntity() throws Exception {
           htmlEntityReferenceEventHandler.handleEntityReferenceEvent(getEntityReference("nbsp"), mockXmlEventReader,
                   eventWriter);
           verify(eventWriter).write(String.valueOf('\u00A0'));
       }
   
       @Test
       public void testHandleEntityReferenceEventForUnknownEntity() throws Exception {
           htmlEntityReferenceEventHandler.handleEntityReferenceEvent(getEntityReference("someEntity"),
                   mockXmlEventReader, eventWriter);
           verify(eventWriter).writeEntityReference("someEntity");
       }

}
