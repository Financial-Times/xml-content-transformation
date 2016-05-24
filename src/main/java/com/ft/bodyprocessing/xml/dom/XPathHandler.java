package com.ft.bodyprocessing.xml.dom;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

@FunctionalInterface
public interface XPathHandler {
  public void handle(Document document, NodeList nodes);
}
