package com.ft.bodyprocessing.regex;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class RegexReplacerBodyProcessorTest {

	private RegexReplacerBodyProcessor bodyProcessor = null;
	
	@Before
	public void setup() {
		bodyProcessor = new RegexReplacerBodyProcessor("\n+", "\n");
	}
	
	@Test
	public void twoNewLinesTogetherAreReplacedWithSingleNewLine() {
		String actualTransformedBody = bodyProcessor.process("\n\n", null);
		assertEquals("\n", actualTransformedBody);
	}
	
	@Test
	public void threeNewLinesTogetherAreReplacedWithSingleNewLine() {
		String actualTransformedBody = bodyProcessor.process("\n\n\n", null);
		assertEquals("\n", actualTransformedBody);
	}

	@Test
	public void newLinesInterspersedInTextAreReplacedWithSingleNewLine() {
		String actualTransformedBody = bodyProcessor.process("Text \n\n Text \n Text \n\n", null);
		assertEquals("Text \n Text \n Text \n", actualTransformedBody);
	}

}
