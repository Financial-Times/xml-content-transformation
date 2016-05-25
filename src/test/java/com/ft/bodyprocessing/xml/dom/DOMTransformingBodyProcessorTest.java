package com.ft.bodyprocessing.xml.dom;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.BodyProcessingException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Collections;

public class DOMTransformingBodyProcessorTest {
  private static final String BAR = "<bar>A bar element</bar>";
  private static final String BODY = "<body>"
      + "<foo class=\"bar\">A foo element"
      + BAR
      + "</foo>"
      + "<foo class=\"baz\">Another foo element</foo>"
      + "</body>";
  
  private BodyProcessingContext context = mock(BodyProcessingContext.class);
  
  @Test
  public void thatXPathHandlerIsCalled() {
    XPathHandler handler = mock(XPathHandler.class);
    DOMTransformingBodyProcessor processor = new DOMTransformingBodyProcessor(
        Collections.singletonMap("/body/foo/bar", handler));
    
    processor.process(BODY, context);
    
    ArgumentCaptor<Document> captorDoc = ArgumentCaptor.forClass(Document.class);
    ArgumentCaptor<NodeList> captorNodes = ArgumentCaptor.forClass(NodeList.class);
    verify(handler).handle(captorDoc.capture(), captorNodes.capture());
    
    NodeList actual = captorNodes.getValue();
    assertThat(actual.getLength(), equalTo(1));
    Node firstNode = actual.item(0);
    assertThat(firstNode.getTextContent(), equalTo("A bar element"));
    assertThat(firstNode.getOwnerDocument(), equalTo(captorDoc.getValue()));
  }
  
  @Test
  public void thatXPathHandlerIsCalledEvenWhenThereAreNoMatches() {
    XPathHandler handler = mock(XPathHandler.class);
    DOMTransformingBodyProcessor processor = new DOMTransformingBodyProcessor(
        Collections.singletonMap("/foo/bar/baz", handler));
    
    processor.process(BODY, context);
    
    ArgumentCaptor<Document> captorDoc = ArgumentCaptor.forClass(Document.class);
    ArgumentCaptor<NodeList> captor = ArgumentCaptor.forClass(NodeList.class);
    verify(handler).handle(captorDoc.capture(), captor.capture());
    
    NodeList actual = captor.getValue();
    assertThat(actual.getLength(), equalTo(0));
    assertThat(captorDoc.getValue(), notNullValue());
  }
  
  @Test
  public void thatXPathHandlerModifiesDocument() {
    DOMTransformingBodyProcessor processor = new DOMTransformingBodyProcessor(
        Collections.singletonMap("/body/foo/bar", (doc, nodes) -> {
          Node n = nodes.item(0);
          Node parent = n.getParentNode();
          
          Element el = doc.createElement("baz");
          el.setTextContent("Modified by handler");
          
          parent.replaceChild(el, n);
        }));
    
    String actual = processor.process(BODY, context);
    
    assertThat(actual, equalTo(BODY.replace(BAR, "<baz>Modified by handler</baz>")));
  }
  
  @Test
  public void thatBlankDocumentIsHandled() {
    XPathHandler handler = mock(XPathHandler.class);
    DOMTransformingBodyProcessor processor = new DOMTransformingBodyProcessor(
        Collections.singletonMap("/body/foo/bar", handler));
    
    String body = "   \n   ";
    assertThat(processor.process(body, context), equalTo(body));
  }
  
  @Test
  public void thatNullDocumentIsHandled() {
    XPathHandler handler = mock(XPathHandler.class);
    DOMTransformingBodyProcessor processor = new DOMTransformingBodyProcessor(
        Collections.singletonMap("/body/foo/bar", handler));
    
    assertThat(processor.process(null, context), nullValue());
  }
  
  @Test(expected = BodyProcessingException.class)
  public void thatInvalidDocumentIsRejected() {
    XPathHandler handler = mock(XPathHandler.class);
    DOMTransformingBodyProcessor processor = new DOMTransformingBodyProcessor(
        Collections.singletonMap("/body/foo/bar", handler));
    
    processor.process("<foo>", context);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void thatInvalidXPathIsRejected() {
    XPathHandler handler = mock(XPathHandler.class);
    new DOMTransformingBodyProcessor(Collections.singletonMap("$%^&*()", handler));
  }
}
