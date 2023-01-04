package com.notelysia.newsandroidservices.models;

public class ImageInformation {
    //Instance variables
    private String image_id;
    private String source_id;
    private String image;

    public ImageInformation(String image_id, String source_id, String image) {
        this.image_id = image_id;
        this.source_id = source_id;
        this.image = image;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
