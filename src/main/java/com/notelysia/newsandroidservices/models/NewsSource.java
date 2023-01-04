package com.notelysia.newsandroidservices.models;

public class NewsSource {
    //Instance variables
    private String source_id;
    private String source_name;
    private String information;

    public NewsSource(String source_id, String source_name, String information) {
        this.source_id = source_id;
        this.source_name = source_name;
        this.information = information;
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

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
