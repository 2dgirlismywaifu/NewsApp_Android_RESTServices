package com.notelysia.newsandroidservices.models;

public class NewsDetails {
    //Instance variables
    private String source_id;
    private String url_type;
    private String url;

    public NewsDetails(String source_id, String url_type, String url) {
        this.source_id = source_id;
        this.url_type = url_type;
        this.url = url;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
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
}
