package com.ft.bodyprocessing.xml.eventhandlers;

import com.ft.bodyprocessing.TestBodyProcessingContext;
import com.ft.bodyprocessing.xml.StAXTransformingBodyProcessor;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class EndToEndStaxHandlerTest {


    StAXTransformingBodyProcessor processor;
    XMLEventHandlerRegistry registry;

    @Before
    public void setup() {
        registry = new XMLEventHandlerRegistry();
        processor = new StAXTransformingBodyProcessor(registry);


    }

    @Test
    public void retainElementByClass() {

        RetainElementByClassEventHandler handler = new RetainElementByClassEventHandler("twitter-tweet",new StripXMLEventHandler());

        registry.registerDefaultEventHandler(new StripXMLEventHandler());
        registry.registerStartAndEndElementEventHandler(handler,"blockquote");

        String doc = "<div data-asset-type=\"embed\"><blockquote class=\"twitter-tweet\" lang=\"en\">" +
        		"<p>Fascinating chart - perceptions of female &amp; male leadership advantages<a href=\"http://t.co/VYrmvvaPIl\">http://t.co/VYrmvvaPIl</a> " +
        		"<a href=\"http://t.co/iyrtPRDuJN\">pic.twitter.com/iyrtPRDuJN</a></p>— Conrad Hackett (@conradhackett) " +
        		"<a href=\"https://twitter.com/conradhackett/status/555409369374801920\">January 14, 2015</a></blockquote>\n" +
        		"<script async=\"async\" charset=\"utf-8\" src=\"//platform.twitter.com/widgets.js\"/></div>";

        String expectedDoc = "<blockquote class=\"twitter-tweet\" lang=\"en\">" +
        		"<p>Fascinating chart - perceptions of female &amp; male leadership advantages<a href=\"http://t.co/VYrmvvaPIl\">http://t.co/VYrmvvaPIl</a> " +
        		"<a href=\"http://t.co/iyrtPRDuJN\">pic.twitter.com/iyrtPRDuJN</a></p>— Conrad Hackett (@conradhackett) " +
        		"<a href=\"https://twitter.com/conradhackett/status/555409369374801920\">January 14, 2015</a></blockquote>";


        String result = processor.process(doc, TestBodyProcessingContext.testContext());

        assertThat(result,is(expectedDoc));

    }

    @Test
    public void retainElementByClassWithMixedContent() {

        RetainElementByClassEventHandler handler = new RetainElementByClassEventHandler("twitter-tweet",new StripXMLEventHandler());

        registry.registerDefaultEventHandler(new StripXMLEventHandler());
        registry.registerStartAndEndElementEventHandler(handler,"blockquote");

        String doc = "<div data-asset-type=\"embed\"><blockquote class=\"twitter-tweet\" lang=\"en\">" +
        		"<p>Learning from Comcast/TWC? AT&amp;T b DirecTV deal includes collar protecting " +
        		"<a href=\"https://twitter.com/search?q=%24DTV&amp;src=ctag\">$DTV</a> shareholders from decline in " +
        		"<a href=\"https://twitter.com/search?q=%24T&amp;src=ctag\">$T</a> stock. (Caps upside, too).</p>&mdash; Liz Hoffman (@lizrhoffman) " +
        		"<a href=\"https://twitter.com/lizrhoffman/statuses/468146880682016769\">May 18, 2014</a></blockquote>" +
        		"<script src=\"//platform.twitter.com/widgets.js\" charset=\"utf-8\"></script></div>";

        String expectedDoc = "<blockquote class=\"twitter-tweet\" lang=\"en\">" +
        		"<p>Learning from Comcast/TWC? AT&amp;T b DirecTV deal includes collar protecting " +
        		"<a href=\"https://twitter.com/search?q=%24DTV&amp;src=ctag\">$DTV</a> shareholders from decline in " +
        		"<a href=\"https://twitter.com/search?q=%24T&amp;src=ctag\">$T</a> stock. (Caps upside, too).</p>&mdash; Liz Hoffman (@lizrhoffman) " +
        		"<a href=\"https://twitter.com/lizrhoffman/statuses/468146880682016769\">May 18, 2014</a></blockquote>";


        String result = processor.process(doc, TestBodyProcessingContext.testContext());

        assertThat(result,is(expectedDoc));

    }

}
