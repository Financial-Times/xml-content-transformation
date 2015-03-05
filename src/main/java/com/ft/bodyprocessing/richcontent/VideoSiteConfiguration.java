package com.ft.bodyprocessing.richcontent;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * VideoSiteConfiguration
 *
 * @author Simon.Gibbs
 */
public class VideoSiteConfiguration {

    private String urlPattern;
    private String template;
    private boolean embedded;
    private List<String> retainedParams;
    private boolean forceHTTPS = false;

    public VideoSiteConfiguration(
            @JsonProperty("urlPattern") String urlPattern,
            @JsonProperty("template") String template,
            @JsonProperty("embedded") boolean isEmbed,
            @JsonProperty(value = "retainedParams", required = false) List<String> retainedParams,
            @JsonProperty(value = "forceHTTPS", required = false) boolean forceHTTPS
    ) {
        this.urlPattern = urlPattern;
        this.template = template;
        this.embedded = isEmbed;
        this.forceHTTPS = forceHTTPS;
        this.retainedParams = new ArrayList<>();
        if(retainedParams!=null) {
            this.retainedParams.addAll(retainedParams);
        }

    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public String getTemplate() {
        return template;
    }

    public boolean isEmbedded() { return embedded; }

    public List<String> getRetainedParams() {
        return retainedParams;
    }

    public boolean hasParameters() {
        return !retainedParams.isEmpty();
    }

    public boolean isForceHTTPS() {
        return forceHTTPS;
    }
}
