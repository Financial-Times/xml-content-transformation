package com.ft.bodyprocessing.xml.eventhandlers;

import com.ft.bodyprocessing.writer.BodyWriter;

import javax.xml.stream.events.EntityReference;

public class PlainTextHtmlEntityReferenceEventHandler extends HtmlEntityReferenceEventHandler {

    @Override
    protected void handleUnknownEntity(EntityReference event, BodyWriter eventWriter) {
        eventWriter.write(getEntityAsEscapedUnicode(event));
    }
}
