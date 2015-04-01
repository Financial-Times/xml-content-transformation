package com.ft.bodyprocessing.richcontent;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConvertParameters {

    private String startingParameter;
    private String convertedParameter;
    private String conversionTemplate;

    public ConvertParameters(@JsonProperty("startingParameter") String startingParameter,
                             @JsonProperty("convertedParameter") String convertedParameter,
                             @JsonProperty("conversionTemplate") String conversionTemplate) {
        this.startingParameter = startingParameter;
        this.convertedParameter = convertedParameter;
        this.conversionTemplate = conversionTemplate;
    }

    public String getStartingParameter() { return startingParameter; }

    public String getConvertedParameter() { return convertedParameter; }

    public String getConversionTemplate() { return conversionTemplate; }

}
