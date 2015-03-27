package com.ft.bodyprocessing.richcontent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Strings;

/**
 * VideoMatcher
 *
 * @author Simon.Gibbs
 */
public class VideoMatcher {

    private List<VideoSiteConfiguration> sites;

    public VideoMatcher(List<VideoSiteConfiguration> sites) {
        this.sites = sites;
    }

    public Video filterVideo(RichContentItem attachment) {

        String url = attachment.getUrl();
        boolean isEmbed;

        VideoSiteConfiguration site = match(url);

        if(site == null) {
            return null;
        }

        if(!(Strings.isNullOrEmpty(site.getTemplate()))) {
            Matcher pathMatcher = startSearchForURLPattern(url, site);
            pathMatcher.find();
            String id = pathMatcher.group("id");
            url = String.format(site.getTemplate(),id);

            if(site.hasParameters()) {

                StringTokenizer queryStringParts = new StringTokenizer(attachment.getUrl(),"?&=");
                Map<String,String> originalParameters = new HashMap<>();
                queryStringParts.nextToken(); // ignore the part before the query string
                while (queryStringParts.hasMoreTokens()) {
                    originalParameters.put(queryStringParts.nextToken(), queryStringParts.nextToken());
                }

                for(String param : site.getRetainedParams()) {
                    if(originalParameters.containsKey(param)) {
                        String embedParamStart = "start";
                        if(originalParameters.containsKey(embedParamStart)) {
                            String transformedValueInSeconds = originalParameters.get(embedParamStart) + "s";
                            originalParameters.remove(embedParamStart);
                            param = "t";
                            originalParameters.put(param, transformedValueInSeconds);
                        }
                        char delimiter = '?';
                        if(url.contains("?")) {
                           delimiter = '&';
                        }
                        url = url + delimiter + param + "=" + originalParameters.get(param);
                    }
                }
            }
        }

        if(site.isForceHTTPS()) {
            url = url.replace("http://","https://");
        }

        isEmbed = site.isEmbedded();

        Video video = new Video();

        video.setUrl(url);
        video.setEmbedded(isEmbed);
        video.setTitle(attachment.getTitle());

        return video;
    }

    private Matcher startSearchForURLPattern(String url, VideoSiteConfiguration site) {
        Pattern sitePattern = Pattern.compile(site.getUrlPattern());
        return sitePattern.matcher(url);
    }

    public VideoSiteConfiguration match(String url) {

        for(VideoSiteConfiguration site : sites) {
            Matcher pathMatcher = startSearchForURLPattern(url, site);
            if(pathMatcher.find()) {
                return site;
            }
        }
        return null; // nothing matched
    }

}
