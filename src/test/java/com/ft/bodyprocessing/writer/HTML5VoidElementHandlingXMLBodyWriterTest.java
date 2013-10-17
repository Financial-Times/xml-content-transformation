package com.ft.bodyprocessing.writer;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.codehaus.stax2.XMLOutputFactory2;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ft.bodyprocessing.BodyProcessingException;
import com.google.common.collect.ImmutableMap;



public class HTML5VoidElementHandlingXMLBodyWriterTest {

	private static final String CHARACTERS = "characters";

	private static final String TAG_NAME = "tag";
	// bodyWriter adds auto self closing tag on flush
	private static final String START_TAG_WITH_ATTRIBUTES_AS_STRING = "<tag attribute1=\"value1\" attribute2=\"value2\"/>";
	private static final String START_TAG_AS_STRING = "<tag/>";
	private static final String START_AND_END_TAGS_AS_STRING = "<tag></tag>";

	private static final String ENTITY_REF = "euro";

	private HTML5VoidElementHandlingXMLBodyWriter bodyWriter;

	private XMLOutputFactory2 xmlOutputFactory;
	
	@Before
	public void setup() throws IOException, XMLStreamException {
		xmlOutputFactory = (XMLOutputFactory2) XMLOutputFactory2.newInstance();
		xmlOutputFactory.setProperty(XMLOutputFactory2.P_AUTOMATIC_EMPTY_ELEMENTS, true);	
		bodyWriter = new HTML5VoidElementHandlingXMLBodyWriter(xmlOutputFactory);
	}
	
	@After
	public void teardown() {
		bodyWriter.close();
	}
	
	@Test
	public void shouldWriteCharacters() {
		bodyWriter.write(CHARACTERS);
		bodyWriter.flush();
		String result = bodyWriter.asString();
		assertThat("result", result, is(equalTo(CHARACTERS)));
	}
	
	@Test
	public void shouldWriteStartTagWithSuppliedNameAndAttributes() {
		Map<String, String> attributesAndValues = ImmutableMap.of("attribute1", "value1", "attribute2", "value2");
		bodyWriter.writeStartTag(TAG_NAME, attributesAndValues);
		bodyWriter.flush();
		String result = bodyWriter.asString();
		assertThat("result", result, is(equalTo(START_TAG_WITH_ATTRIBUTES_AS_STRING)));
	}
	
	@Test
	public void shouldWriteStartTagWithSuppliedNameAndNoAttributes() {
		bodyWriter.writeStartTag(TAG_NAME, null);
		bodyWriter.flush();
		String result = bodyWriter.asString();
		assertThat("result", result, is(equalTo(START_TAG_AS_STRING)));
	}
	
	@Test
	public void shouldWriteSeparateEndTagByDefault() {
		bodyWriter.writeStartTag(TAG_NAME, null); // need to have open start tag to be able to write end tag
		bodyWriter.writeEndTag(TAG_NAME);
		bodyWriter.flush();
		String result = bodyWriter.asString();
		assertThat("result", result, is(equalTo(START_AND_END_TAGS_AS_STRING)));
	}
	
	@Test
	public void shouldWriteSelfClosingTagForVoidElements() throws IOException, XMLStreamException {
		List<String> voidElements = Arrays.asList(
				"area", "base", "br", "col", "command", "embed", 
				"hr", "img", "input", "keygen", "link", "meta", 
				"param", "source", "track", "wbr");
		for (String voidElement: voidElements) {
			testSelfClosingTag(voidElement);
		}
		
	}

	private void testSelfClosingTag(String voidElement) throws IOException,
			XMLStreamException {
		BodyWriter otherBodyWriter = new HTML5VoidElementHandlingXMLBodyWriter(xmlOutputFactory);
		otherBodyWriter.writeStartTag(voidElement, null); // need to have open start tag to be able to write end tag
		otherBodyWriter.writeEndTag(voidElement);
		otherBodyWriter.flush();
		String result = otherBodyWriter.asString();
		otherBodyWriter.close();
		assertThat("result", result, is(equalTo("<" + voidElement + "/>")));
	}
	
	@Test
	public void shouldWriteEntityReference() {
		bodyWriter.writeEntityReference(ENTITY_REF);
		bodyWriter.flush();
		String result = bodyWriter.asString();
		assertThat("result", result, is(equalTo("&" + ENTITY_REF + ";")));
	}
	
	@Test
	public void shouldWriteComment() {
		bodyWriter.write(CHARACTERS);
		bodyWriter.flush();
		String result = bodyWriter.asString();
		assertThat("result", result, is(equalTo(CHARACTERS)));
	}
	
	@Test(expected=BodyProcessingException.class)
	public void shouldFailIfWriteUnmatchedEndTag() {
		bodyWriter.writeEndTag(TAG_NAME);
		bodyWriter.flush();
		bodyWriter.asString();
	}
}
