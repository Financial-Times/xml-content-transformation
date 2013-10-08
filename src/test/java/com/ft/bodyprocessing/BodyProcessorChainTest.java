package com.ft.bodyprocessing;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(value=MockitoJUnitRunner.class)
public class BodyProcessorChainTest {
	
	private BodyProcessorChain bodyProcessor;
	
	@Mock private BodyProcessor mockFirstBodyProcessor;
	@Mock private BodyProcessor mockSecondBodyProcessor;
	@Mock private BodyProcessor mockThirdBodyProcessor;
	@Mock private BodyProcessor mockFailingBodyProcessor;
	
	@Mock private BodyProcessingContext mockBodyProcessingContext;
	
	private static final String BODY = "body";
	
	@Before
	public void setup() {
		when(mockFirstBodyProcessor.process(BODY, mockBodyProcessingContext)).thenReturn(BODY);
		when(mockSecondBodyProcessor.process(BODY, mockBodyProcessingContext)).thenReturn(BODY);
		when(mockThirdBodyProcessor.process(BODY, mockBodyProcessingContext)).thenReturn(BODY);
		when(mockFailingBodyProcessor.process(BODY, mockBodyProcessingContext)).thenThrow(new BodyProcessingException("Error"));
	}

	@Test(expected=IllegalArgumentException.class)
	public void cannotCreateWithNullBodyProcessorsList() {
		bodyProcessor = new BodyProcessorChain(null);
	}
	
	@Test
	public void bodyProcessorsAreCalledInSameOrderAsBodyProcessorsList() {
		bodyProcessor = new BodyProcessorChain(Arrays.asList(mockFirstBodyProcessor, mockSecondBodyProcessor, mockThirdBodyProcessor));
		InOrder inOrder = inOrder(mockFirstBodyProcessor, mockSecondBodyProcessor, mockThirdBodyProcessor);
		bodyProcessor.process(BODY, mockBodyProcessingContext);
		inOrder.verify(mockFirstBodyProcessor).process(BODY, mockBodyProcessingContext);
		inOrder.verify(mockSecondBodyProcessor).process(BODY, mockBodyProcessingContext);
		inOrder.verify(mockThirdBodyProcessor).process(BODY, mockBodyProcessingContext);
	}
	
	@Test(expected=BodyProcessingException.class)
	public void whenBodyProcessorThrowsExceptionItGetsPropogated() {
		//TODO - is this the expected behaviour??
		bodyProcessor = new BodyProcessorChain(Arrays.asList(mockFailingBodyProcessor));
		bodyProcessor.process(BODY, mockBodyProcessingContext);
	}
	
	@Test(expected=BodyProcessingException.class)
	public void exceptionThrownWhenBodyIsNull() {
		bodyProcessor = new BodyProcessorChain(Arrays.asList(mockFailingBodyProcessor));
		bodyProcessor.process(null, mockBodyProcessingContext);
	}
	
	@Test(expected=BodyProcessingException.class)
	public void exceptionThrownWhenBodyProcessingContextIsNull() {
		bodyProcessor = new BodyProcessorChain(Arrays.asList(mockFailingBodyProcessor));
		bodyProcessor.process(BODY, null);
	}
}
