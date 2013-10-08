package com.ft.bodyprocessing.regex;


/**
 * Body processor that matches the supplied string patterns against the body, and replaces with empty string.
 */
public class RegexRemoverBodyProcessor extends RegexReplacerBodyProcessor {

	private static final String EMPTY_STRING = "";

	public RegexRemoverBodyProcessor(String stringPattern) {
		super(stringPattern, EMPTY_STRING);
	}
}
