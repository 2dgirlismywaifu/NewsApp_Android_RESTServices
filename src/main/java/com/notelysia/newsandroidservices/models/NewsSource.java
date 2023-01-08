package com.notelysia.newsandroidservices.models;

public class NewsSource {
    //Instance variables
    private String source_id;
    private String source_name;
    private String source_url;
    private String information;
    private String imgae;

    public NewsSource(String source_id, String source_name, String source_url, String information, String imgae) {
        this.source_id = source_id;
        this.source_name = source_name;
        this.source_url = source_url;
        this.information = information;
        this.imgae = imgae;
    }

    public NewsSource() {
    }
    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public String getSource_name() {
        return source_name;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getImgae() {
        return imgae;
    }

    public void setImgae(String imgae) {
        this.imgae = imgae;
    }
}
