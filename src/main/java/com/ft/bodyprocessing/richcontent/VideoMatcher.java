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
                if(site.hasRetainedParameters()) {
                    url = rebuildUrl(url, site, originalParameters);
                }

                if(site.hasConvertParameters()) {
                    url = rebuildUrlWithConvertedParams(url, site, originalParameters);
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

    private String rebuildUrlWithConvertedParams(String url, VideoSiteConfiguration site, Map<String, String> originalParameters) {
        for(ConvertParameters convertParameters : site.getConvertParameters()) {
            String param = convertParameters.getConvertFromParameter();
            String paramValue = originalParameters.get(param);
            if(originalParameters.containsKey(param)) {
                param = convertParameters.getConvertedToParameter();
                paramValue = String.format(convertParameters.getConversionTemplate(), paramValue);

                url = appendParameters(url, param, paramValue);
            }
        }
        return url;
    }

    private String rebuildUrl(String url, VideoSiteConfiguration site, Map<String, String> originalParameters) {
        for(String param : site.getRetainedParams()) {
            if(originalParameters.containsKey(param)) {
                String paramValue = originalParameters.get(param);
                url = appendParameters(url, param, paramValue);
            }
        }
        return url;
    }

    private String appendParameters(String url, String param, String paramValue) {
        char delimiter = '?';
        if(url.contains("?")) {
            delimiter = '&';
        }
        StringBuilder stringBuilder = new StringBuilder();
        url = stringBuilder.append(url).append(delimiter).append(param).append("=").append(paramValue).toString();
        return url;
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
