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
    private boolean embedded;

    public VideoSiteConfiguration(@JsonProperty("urlPattern") String urlPattern, @JsonProperty("template") String template, @JsonProperty("embedded") boolean isEmbed) {
        this.urlPattern = urlPattern;
        this.template = template;
        this.embedded = isEmbed;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public String getTemplate() {
        return template;
    }

    public boolean isEmbedded() { return embedded; }
}
