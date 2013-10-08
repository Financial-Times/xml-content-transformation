package com.ft.bodyprocessing.xml;

import static com.google.common.base.Preconditions.checkArgument;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.BodyProcessor;


public class AddRootNodeBodyProcessor implements BodyProcessor {
	
	private String rootNodeName;
	
	public AddRootNodeBodyProcessor(String rootNodeName){
        checkArgument(rootNodeName != null, "Root node name should not be null");
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
