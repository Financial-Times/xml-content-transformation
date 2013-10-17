package com.ft.bodyprocessing.xml;

import static com.google.common.base.Preconditions.checkArgument;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.BodyProcessor;
import com.google.common.base.Strings;


public class AddRootNodeBodyProcessor implements BodyProcessor {
	
	private String rootNodeName;
	
	public AddRootNodeBodyProcessor(String rootNodeName){
        checkArgument(!Strings.isNullOrEmpty(rootNodeName), "Root node name should not be null or empty");
		this.rootNodeName = rootNodeName;
	}
	
	@Override
	public String process(String body, BodyProcessingContext bodyProcessingContext) {
		return getStartWrapperNode() + (body == null ? "" : body) + getEndWrapperNode();
	}
	
	private String getStartWrapperNode() {
		return "<" + rootNodeName + ">";
	}
	
	private String getEndWrapperNode() {
		return "</" + rootNodeName + ">";
	}

}
