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

        String doc = "<div data-asset-type=\"embed\"><blockquote class=\"twitter-tweet\" lang=\"en\"><p>Fascinating chart - perceptions of female &amp; male leadership advantages<a href=\"http://t.co/VYrmvvaPIl\">http://t.co/VYrmvvaPIl</a> <a href=\"http://t.co/iyrtPRDuJN\">pic.twitter.com/iyrtPRDuJN</a></p>— Conrad Hackett (@conradhackett) <a href=\"https://twitter.com/conradhackett/status/555409369374801920\">January 14, 2015</a></blockquote>\n<script async=\"async\" charset=\"utf-8\" src=\"//platform.twitter.com/widgets.js\"/></div>";

        String expectedDoc = "<blockquote class=\"twitter-tweet\" lang=\"en\"><p>Fascinating chart - perceptions of female &amp; male leadership advantages<a href=\"http://t.co/VYrmvvaPIl\">http://t.co/VYrmvvaPIl</a> <a href=\"http://t.co/iyrtPRDuJN\">pic.twitter.com/iyrtPRDuJN</a></p>— Conrad Hackett (@conradhackett) <a href=\"https://twitter.com/conradhackett/status/555409369374801920\">January 14, 2015</a></blockquote>";


        String result = processor.process(doc, TestBodyProcessingContext.testContext());

        assertThat(result,is(expectedDoc));

    }

}
