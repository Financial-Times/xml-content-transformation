package com.ft.bodyprocessing.xml;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.ft.bodyprocessing.BodyProcessingContext;



public class AddRootNodeBodyProcessorTest {

	private static final String ROOT_NODE_NAME = "wrapperNode";
	private static final BodyProcessingContext bodyProcessingContext = null;
	private static final String BODY = "example body";
	private static final String EXPECTED_WRAPPED_BODY = "<wrapperNode>example body</wrapperNode>";
	private static final String EXPECTED_WRAPPED_NULL_BODY = "<wrapperNode></wrapperNode>";
	private AddRootNodeBodyProcessor bodyProcessor;
	
	@Before
	public void setup() {
		bodyProcessor = new AddRootNodeBodyProcessor(ROOT_NODE_NAME);
	}
	
	@Test
	public void shouldWrapBodyStringInRootNode() {
		String actualWrappedBodyString = bodyProcessor.process(BODY, bodyProcessingContext);
		assertThat("content", actualWrappedBodyString, is(equalTo(EXPECTED_WRAPPED_BODY)));
	}
	
	@Test
	public void shouldTreatNullBodyAsEmpty() {
		String actualWrappedBodyString = bodyProcessor.process(null, bodyProcessingContext);
		assertThat("content", actualWrappedBodyString, is(equalTo(EXPECTED_WRAPPED_NULL_BODY)));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldFailIfRootNodeNameNull() {
		new AddRootNodeBodyProcessor(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldFailIfRootNodeNameEmpty() {
		new AddRootNodeBodyProcessor("");
	}
}
