package com.ft.bodyprocessing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Xml
 *
 * @author Simon
 */
public class Xml {

	public static String writeToString(Element body) {
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "no");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(body);
			transformer.transform(source, result);
			return result.getWriter().toString();
		} catch (TransformerException e) {
			throw new BodyProcessingException(e);
		}
	}

    public static Document createDocument(String bodyHtml) {
        try {

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(bodyHtml));
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);

        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new BodyProcessingException(e);
        }

    }
}
