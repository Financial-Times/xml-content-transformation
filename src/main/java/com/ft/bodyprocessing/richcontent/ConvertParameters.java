package com.ft.bodyprocessing.richcontent;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConvertParameters {

    private String convertFromParameter;
    private String convertedToParameter;
    private String conversionTemplate;

    public ConvertParameters(@JsonProperty("convertFromParameter") String convertFromParameter,
                             @JsonProperty("convertedToParameter") String convertedToParameter,
                             @JsonProperty("conversionTemplate") String conversionTemplate) {
        this.convertFromParameter = convertFromParameter;
        this.convertedToParameter = convertedToParameter;
        this.conversionTemplate = conversionTemplate;
    }

    public String getConvertFromParameter() { return convertFromParameter; }

    public String getConvertedToParameter() { return convertedToParameter; }

    public String getConversionTemplate() { return conversionTemplate; }

}
