package com.ft.bodyprocessing.richcontent;

/**
 * Acts as a "value object" to hold basic details of some rich content item before we have worked out what it is, in particular,
 * before we know if it is a video etc.
 *
 * @author Simon.Gibbs
 */
public class RichContentItem {

    private String url;
    private String title;

    public RichContentItem(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }
}
