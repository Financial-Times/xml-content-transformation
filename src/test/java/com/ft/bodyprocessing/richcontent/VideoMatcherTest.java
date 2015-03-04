package com.ft.bodyprocessing.richcontent;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.Mock;

/**
 * VideoMatcherTest
 *
 * @author Simon.Gibbs
 */

public class VideoMatcherTest {

    private static final List<String> T = Collections.singletonList("t");
    private static final List<String> NONE = Collections.emptyList();

    public static List<VideoSiteConfiguration> DEFAULTS = Arrays.asList(
            new VideoSiteConfiguration("https?://www.youtube.com/watch\\?v=(?<id>[A-Za-z0-9_-]+)", null, true, T),
            new VideoSiteConfiguration("https?://www.youtube.com/embed/(?<id>[A-Za-z0-9_-]+)", "https://www.youtube.com/watch?v=%s", false,T),
            new VideoSiteConfiguration("https?://youtu.be/(?<id>[A-Za-z0-9_-]+)", "https://www.youtube.com/watch?v=%s", false, T),
            new VideoSiteConfiguration("https?://vimeo.com/[0-9]+", null, false, NONE),
            new VideoSiteConfiguration("https?://video.ft.com/[0-9]+/[\\s]?", null, false, NONE)
    );

    private List<VideoSiteConfiguration> videoSiteConfigurationList = DEFAULTS ;

    @Mock private VideoSiteConfiguration videoSiteConfiguration;

    @Test
    public void shouldRewriteYoutubeEmbedsAsLinks() {

        RichContentItem attachment = new RichContentItem("https://www.youtube.com/embed/V8B4CjOkcck","Squirrel is basically Nikki Minoj");

        VideoMatcher matcher = new VideoMatcher(videoSiteConfigurationList);

        Video video = matcher.filterVideo(attachment);

        String result = video.getUrl();

        assertThat(result, is("https://www.youtube.com/watch?v=V8B4CjOkcck"));
    }

    @Test
    public void shouldRewriteYoutubeShortUrlsAsLinks() {


        RichContentItem attachment = new RichContentItem("http://youtu.be/V8B4CjOkcck","Squirrel is basically Nikki Minoj");

        VideoMatcher matcher = new VideoMatcher(videoSiteConfigurationList);

        Video video = matcher.filterVideo(attachment);

        String result = video.getUrl();

        assertThat(result, is("https://www.youtube.com/watch?v=V8B4CjOkcck"));
    }

    @Test
    public void shouldCopyOverWhiteListedParameter() {


        RichContentItem attachment = new RichContentItem("http://youtu.be/V8B4CjOkcck?t=12s","Squirrel is basically Nikki Minoj");

        VideoMatcher matcher = new VideoMatcher(videoSiteConfigurationList);

        Video video = matcher.filterVideo(attachment);

        String result = video.getUrl();

        assertThat(result, is("https://www.youtube.com/watch?v=V8B4CjOkcck&t=12s"));
    }

    @Test
    public void shouldNotRewriteAVimeoLink() {

        RichContentItem attachment = new RichContentItem("http://vimeo.com/116498390","He Took His Skin Off For Me");

        VideoMatcher matcher = new VideoMatcher(videoSiteConfigurationList);

        Video video = matcher.filterVideo(attachment);

        String result = video.getUrl();

        assertThat(result, is("http://vimeo.com/116498390"));
    }

    @Test
    public void shouldNotRewriteAnFTVideoLink() {

        RichContentItem attachment = new RichContentItem("http://video.ft.com/4004982305001/Super-Mario-returns-but-is-it-enough-/editorschoice","Super Mario returns but is it enough?");

        VideoMatcher matcher = new VideoMatcher(videoSiteConfigurationList);

        Video video = matcher.filterVideo(attachment);

        String result = video.getUrl();

        assertThat(result, is("http://video.ft.com/4004982305001/Super-Mario-returns-but-is-it-enough-/editorschoice"));

        String actualTitle =  video.getTitle();
        assertThat(actualTitle, is("Super Mario returns but is it enough?"));
    }

    @Test
    public void shouldAlsoNotTakeOnHTMLEscapingResponsibility() {

        RichContentItem attachment = new RichContentItem("http://video.ft.com/4004982305001/Super-Mario-returns-but-is-it-enough-/editorschoice","Mario & uncertainty");

        VideoMatcher matcher = new VideoMatcher(videoSiteConfigurationList);
        Video video = matcher.filterVideo(attachment);

        String actualTitle =  video.getTitle();
        assertThat(actualTitle, is("Mario & uncertainty"));
    }

    @Test
    public void shouldRejectFTVideoLink() {

        RichContentItem attachment = new RichContentItem("http://video.ft.com/","Super-Mario-returns-but-is-it-enough? & maybe  not");

        VideoMatcher matcher = new VideoMatcher(videoSiteConfigurationList);
        Video video = matcher.filterVideo(attachment);
        assertThat(video, is(nullValue()));
    }


    @Test
    public void shouldFilterOutRandomOtherThings() {

        RichContentItem attachment = new RichContentItem("http://ft.com/data.xls","Excel");

        VideoMatcher matcher = new VideoMatcher(videoSiteConfigurationList);

        Video video = matcher.filterVideo(attachment);

        assertThat(video, nullValue());

    }

}
