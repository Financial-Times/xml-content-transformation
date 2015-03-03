package com.ft.bodyprocessing.richcontent;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * VideoSiteConfiguration
 *
 * @author Simon.Gibbs
 */
public class VideoSiteConfiguration {

    private String urlPattern;
    private String template;
    private boolean isEmbed;

    public VideoSiteConfiguration(@JsonProperty("urlPattern") String urlPattern, @JsonProperty("template") String template, @JsonProperty("isEmbed") boolean isEmbed) {
        this.urlPattern = urlPattern;
        this.template = template;
        this.isEmbed = isEmbed;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public String getTemplate() {
        return template;
    }

    public boolean getIsEmbed() { return isEmbed; }
}
