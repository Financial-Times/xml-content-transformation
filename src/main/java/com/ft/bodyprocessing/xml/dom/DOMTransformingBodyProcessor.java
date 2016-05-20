package com.ft.bodyprocessing.xml.dom;

import static javax.xml.xpath.XPathConstants.NODESET;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.BodyProcessingException;
import com.ft.bodyprocessing.BodyProcessor;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;


public class DOMTransformingBodyProcessor
    implements BodyProcessor {
  
  private final Map<String,XPathHandler> handlers;
  
  public DOMTransformingBodyProcessor(Map<String,XPathHandler> handlers) {
    XPath xpath = XPathFactory.newInstance().newXPath();
    for (String path : handlers.keySet()) {
      try {
        xpath.compile(path);
      } catch (XPathExpressionException e) {
        throw new IllegalArgumentException("not a valid XPath: " + path, e);
      }
    }
    
    this.handlers = handlers;
  }
  
  @Override
  public String process(String body, BodyProcessingContext context) throws BodyProcessingException {
    if ((body != null) && !body.trim().isEmpty()) {
      final Document document;
      try {
        final DocumentBuilder documentBuilder = getDocumentBuilder();
        document = documentBuilder.parse(new InputSource(new StringReader(body)));
        
        XPath xpath = XPathFactory.newInstance().newXPath();
        
        for (Map.Entry<String,XPathHandler> en : handlers.entrySet()) {
          String path = en.getKey();
          NodeList nodes = (NodeList)xpath.evaluate(path, document, NODESET);
          en.getValue().handle(nodes);
        }
        
      } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
        throw new BodyProcessingException(e);
      }
      
      body = serializeBody(document);
    }
    
    return body;
  }

  private DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
      final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

      return documentBuilderFactory.newDocumentBuilder();
  }

  private String serializeBody(Document document) {
      DOMSource domSource = new DOMSource(document);
      StringWriter writer = new StringWriter();
      StreamResult result = new StreamResult(writer);

      TransformerFactory tf = TransformerFactory.newInstance();
      try {
          Transformer transformer = tf.newTransformer();
          transformer.setOutputProperty("omit-xml-declaration", "yes");
          transformer.setOutputProperty("standalone", "yes");
          transformer.transform(domSource, result);
          writer.flush();
          String body = writer.toString();
          return body;
      } catch (TransformerException e) {
          throw new BodyProcessingException(e);
      }
  }
}
