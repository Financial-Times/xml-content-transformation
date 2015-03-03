package com.ft.bodyprocessing.richcontent;

import java.util.List;
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
