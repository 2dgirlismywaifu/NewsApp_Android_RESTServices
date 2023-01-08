package com.notelysia.newsandroidservices.models;

public class NewsAPICountry {
    //53 Countries only used, no China
    private String country_id;
    private String country_code;
    private String country_name;

    public NewsAPICountry(String country_id, String country_code, String country_name) {
        this.country_id = country_id;
        this.country_code = country_code;
        this.country_name = country_name;
    }

    public NewsAPICountry() {
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }
}
