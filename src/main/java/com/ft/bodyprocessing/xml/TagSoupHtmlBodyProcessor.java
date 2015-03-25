package com.ft.bodyprocessing.xml;

import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.ParserConfigurationException;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.BodyProcessingException;
import com.ft.bodyprocessing.BodyProcessor;
import com.ft.bodyprocessing.Xml;
import org.apache.xalan.xsltc.trax.SAX2DOM;
import org.ccil.cowan.tagsoup.Parser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TagSoupHtmlBodyProcessor implements BodyProcessor {
    @Override
    public String process(String bodyHtml, BodyProcessingContext bodyProcessingContext) throws BodyProcessingException {

		if(bodyHtml==null) {
			throw new BodyProcessingException("Body is null");
		}

		if("".equals(bodyHtml.trim())) {
			return "";
		}

        Document doc = createRectifiedDocument(bodyHtml);
        Element body = (Element) doc.getElementsByTagName("body").item(0);

		return Xml.writeToString(body);
	}



	private Document createRectifiedDocument(String html) throws BodyProcessingException {
        Parser parser = new Parser();
        try {
            SAX2DOM sax2dom = new SAX2DOM();

            parser.setFeature(Parser.namespacesFeature, false);
            parser.setFeature(Parser.namespacePrefixesFeature, false);
            parser.setContentHandler(sax2dom);
            parser.parse(new InputSource(new StringReader(html)));
            return (Document) sax2dom.getDOM();

        } catch (IOException | SAXException | ParserConfigurationException ioe) {
            throw new BodyProcessingException(ioe);
        }
    }
}
