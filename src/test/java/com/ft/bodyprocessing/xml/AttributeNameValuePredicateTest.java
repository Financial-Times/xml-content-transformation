package com.ft.bodyprocessing.xml;

import java.util.Map;

import com.google.common.collect.Maps;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AttributeNameValuePredicateTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void attributeValuesMustNotBeNull() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("constructor param 'name' must not be null");

        new AttributeNameValuePredicate(null);
    }

    @Test
    public void nameMustNotBeNull() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("constructor param 'name' must not be null");
        Map<String,String> attributeValues = Maps.newHashMap();
        attributeValues.put(null, "value");
        new AttributeNameValuePredicate(attributeValues);
    }

    @Test
    public void valueMustNotBeNull() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("constructor param 'value' must not be null");
        Map<String,String> attributeValues = Maps.newHashMap();
        attributeValues.put("name", null);
        new AttributeNameValuePredicate(attributeValues);
    }

}
