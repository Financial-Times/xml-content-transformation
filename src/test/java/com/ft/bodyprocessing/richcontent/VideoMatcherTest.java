package com.ft.bodyprocessing.richcontent;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.mockito.Mock;

/**
 * VideoMatcherTest
 *
 * @author Simon.Gibbs
 */

public class VideoMatcherTest {

    private static final List<String> T = Collections.singletonList("t");
    private static final List<String> START = Collections.singletonList("start");
    private static final List<String> NONE = Collections.emptyList();
    private static final String FROM = "start";
    private static final String TO = "t";
    private static final String NEW_VALUE = "s";
    private static final ConvertParameters CONVERTED_PARAMS = new ConvertParameters(FROM, TO, NEW_VALUE);
    private static final List<ConvertParameters> CONVERTED_PARAMS_LIST = ImmutableList.of(CONVERTED_PARAMS);


    public static List<VideoSiteConfiguration> DEFAULTS = Arrays.asList(
            new VideoSiteConfiguration("https?://www.youtube.com/watch\\?v=(?<id>[A-Za-z0-9_-]+)", null, true, T, null, true),
            new VideoSiteConfiguration("https?://www.youtube.com/embed/(?<id>[A-Za-z0-9_-]+)", "https://www.youtube.com/watch?v=%s", false, START, CONVERTED_PARAMS_LIST, true),
            new VideoSiteConfiguration("https?://youtu.be/(?<id>[A-Za-z0-9_-]+)", "https://www.youtube.com/watch?v=%s", false, T, null, true),
            new VideoSiteConfiguration("https?://vimeo.com/[0-9]+", null, false, NONE, null, false), /* made false for test */
            new VideoSiteConfiguration("https?://video.ft.com/[0-9]+/[\\s]?", null, false, NONE, null, false) /* made false for test */
    );

    private List<VideoSiteConfiguration> videoSiteConfigurationList = DEFAULTS ;

    @Mock private VideoSiteConfiguration videoSiteConfiguration;

    @Test
         public void shouldRewriteYoutubeEmbedsAsLinks() {

        RichContentItem attachment = new RichContentItem("https://www.youtube.com/embed/V8B4CjOkcck","Squirrel is basically Nikki Minoj");

        String result = runMatchAsNormal(attachment);

        assertThat(result, is("https://www.youtube.com/watch?v=V8B4CjOkcck"));
    }

    @Test
    public void shouldRewriteYoutubeEmbedsAsWatchAndConvertStartParamToT() {

        RichContentItem attachment = new RichContentItem("https://www.youtube.com/embed/V8B4CjOkcck?autoplay=0&start=143","Squirrel is basically Nikki Minoj");

        String result = runMatchAsNormal(attachment);

        assertThat(result, is("https://www.youtube.com/watch?v=V8B4CjOkcck&t=143s"));
    }

    @Test
    public void shouldIgnoreTParamFromYoutubeEmbedsWhenTransformedToWatch() {

        RichContentItem attachment = new RichContentItem("https://www.youtube.com/embed/V8B4CjOkcck?t=36s","Squirrel is basically Nikki Minoj");

        String result = runMatchAsNormal(attachment);

        assertThat(result, is("https://www.youtube.com/watch?v=V8B4CjOkcck"));
    }

    @Test
    public void shouldRewriteYoutubeShortUrlsAsLinks() {


        RichContentItem attachment = new RichContentItem("http://youtu.be/V8B4CjOkcck","Squirrel is basically Nikki Minoj");

        String result = runMatchAsNormal(attachment);

        assertThat(result, is("https://www.youtube.com/watch?v=V8B4CjOkcck"));
    }

    @Test
    public void shouldCopyOverWhiteListedParameter() {


        RichContentItem attachment = new RichContentItem("http://youtu.be/V8B4CjOkcck?t=12s","Squirrel is basically Nikki Minoj");

        String result = runMatchAsNormal(attachment);

        assertThat(result, is("https://www.youtube.com/watch?v=V8B4CjOkcck&t=12s"));
    }

    @Test
    public void shouldNotRewriteAVimeoLink() {

        RichContentItem attachment = new RichContentItem("http://vimeo.com/116498390","He Took His Skin Off For Me");

        String result = runMatchAsNormal(attachment);

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

    @Test
    public void shouldForceHttpsWhenConfigured() {


        RichContentItem attachment = new RichContentItem("http://www.youtube.com/watch?v=V8B4CjOkcck&t=12s","Squirrel is basically Nikki Minoj");

        String result = runMatchAsNormal(attachment);

        assertThat(result, is("https://www.youtube.com/watch?v=V8B4CjOkcck&t=12s"));
    }

    @Test
    public void shouldNotForceHttpsWhenNotConfigured() {

        RichContentItem attachment = new RichContentItem("http://vimeo.com/116498390","He Took His Skin Off For Me");

        String result = runMatchAsNormal(attachment);

        assertThat(result, is("http://vimeo.com/116498390"));
    }

    private String runMatchAsNormal(RichContentItem attachment) {
        VideoMatcher matcher = new VideoMatcher(videoSiteConfigurationList);

        Video video = matcher.filterVideo(attachment);

        return video.getUrl();
    }

}
