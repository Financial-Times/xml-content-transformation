package com.ft.bodyprocessing.xml.eventhandlers;

import com.ft.bodyprocessing.writer.BodyWriter;
import org.apache.commons.lang.StringEscapeUtils;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EntityReference;

public abstract class HtmlEntityReferenceEventHandler extends BaseXMLEventHandler {

    @Override
    public void handleEntityReferenceEvent(EntityReference event, XMLEventReader xmlEventReader, BodyWriter eventWriter)
            throws
            XMLStreamException {
        String entityAsEscapedUnicode = getEntityAsEscapedUnicode(event);
        String unescapedEntity = StringEscapeUtils.unescapeHtml(entityAsEscapedUnicode);
        if (entityAsEscapedUnicode.equals(unescapedEntity)) {
            handleUnknownEntity(event, eventWriter);
        }
        else {
            eventWriter.write(unescapedEntity);
        }
    }

    protected abstract void handleUnknownEntity(EntityReference event, BodyWriter eventWriter);

    protected String getEntityAsEscapedUnicode(EntityReference event) {
        return "&" + event.getName() + ";";
    }
}
