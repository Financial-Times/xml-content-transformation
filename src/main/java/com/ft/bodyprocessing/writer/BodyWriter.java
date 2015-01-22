package com.ft.bodyprocessing.writer;

import java.util.Map;


public interface BodyWriter {

	void close();

	void write(String data);

	void writeRaw(String data);

	void writeStartTag(String name, Map<String, String> validAttributesAndValues);

	void writeEndTag(String name);
	
	void writeEntityReference(String name);

	String asString();

	void flush();
	
    boolean isPTagCurrentlyOpen();

	void writeComment(String text);
}
