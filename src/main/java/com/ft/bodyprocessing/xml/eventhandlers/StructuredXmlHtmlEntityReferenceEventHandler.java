package com.ft.bodyprocessing.xml.eventhandlers;

import javax.xml.stream.events.EntityReference;

import com.ft.bodyprocessing.writer.BodyWriter;

public class StructuredXmlHtmlEntityReferenceEventHandler extends HtmlEntityReferenceEventHandler {

    @Override
    protected void handleUnknownEntity(EntityReference event, BodyWriter eventWriter) {
        eventWriter.writeEntityReference(event.getName());
    }
}
