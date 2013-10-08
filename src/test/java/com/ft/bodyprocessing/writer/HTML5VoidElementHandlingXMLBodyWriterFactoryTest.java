package com.ft.bodyprocessing.writer;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.StringWriter;

import javax.xml.stream.XMLStreamException;

import org.codehaus.stax2.XMLOutputFactory2;
import org.codehaus.stax2.XMLStreamWriter2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HTML5VoidElementHandlingXMLBodyWriterFactoryTest {

    @Mock private XMLOutputFactory2 mockXMLOutputFactory;
    @Mock private XMLStreamWriter2 mockXMLStreamWriter;

    @Test
    public void testCreation() throws XMLStreamException {
        when(mockXMLOutputFactory.createXMLStreamWriter(isA(StringWriter.class), isA(String.class))).thenReturn(mockXMLStreamWriter);
        
        HTML5VoidElementHandlingXMLBodyWriterFactory bodyWriterFactory = new HTML5VoidElementHandlingXMLBodyWriterFactory(mockXMLOutputFactory);
        BodyWriter actualBodyWriter = bodyWriterFactory.createBodyWriter();
        
        assertTrue(actualBodyWriter instanceof HTML5VoidElementHandlingXMLBodyWriter);
        verify(mockXMLOutputFactory).setProperty(XMLOutputFactory2.P_AUTOMATIC_EMPTY_ELEMENTS, true);
    }
}
