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

    public Video match(RichContentItem attachment) {

        String url = attachment.getUrl();
        boolean isEmbed;

        VideoSiteConfiguration site = match(url);

        if(site == null) {
            return null;
        }

        if(!(Strings.isNullOrEmpty(site.getTemplate()))) {
            Pattern sitePattern = Pattern.compile(site.getUrlPattern());
            Matcher pathMatcher = sitePattern.matcher(url);
            pathMatcher.find();
            String id = pathMatcher.group("id");
            url = String.format(site.getTemplate(),id);
        }

        isEmbed = site.getIsEmbed();

        Video video = new Video();

        video.setUrl(url);
        video.setIsEmbed(isEmbed);
        video.setTitle(attachment.getTitle());

        return video;
    }

    public VideoSiteConfiguration match(String url) {

        for(VideoSiteConfiguration site : sites) {
            Pattern sitePattern = Pattern.compile(site.getUrlPattern());
            Matcher pathMatcher = sitePattern.matcher(url);
            if(pathMatcher.find()) {
                return site;
            }
        }
        return null; // nothing matched
    }

}
