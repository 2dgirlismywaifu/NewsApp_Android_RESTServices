package com.notelysia.newsandroidservices.models;

public class RSSList {
    private String url_type;
    private String url;
    private String url_image;

    public RSSList() {
    }

    public RSSList(String url_type, String url, String url_image) {
        this.url_type = url_type;
        this.url = url;
        this.url_image = url_image;
    }

    public String getUrl_type() {
        return url_type;
    }

    public void setUrl_type(String url_type) {
        this.url_type = url_type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }
}
