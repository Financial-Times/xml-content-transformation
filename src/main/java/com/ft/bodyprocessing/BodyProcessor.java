package com.ft.bodyprocessing;

/**
 * BodyProcessors apply transformations to body text
 */
public interface BodyProcessor {

    /**
     * Processes the supplied body and returns a transformed representation. 
     * @param body The body to process
     * @param bodyProcessingContext Processing the body may cause side effects e.g. addition of assets to the body processing context
     * @return The transformed body
     */
    String process(String body, BodyProcessingContext bodyProcessingContext) throws BodyProcessingException;
    
    // TODO : The interface signature is smelly. Returns a transformed body, but also causes state mutation on BodyProcessingContext
    
}
