package com.notelysia.newsandroidservices.models;

public class UserSSO {
    private String user_id;
    private String email;
    private String nickname;
    private String sync_settings;
    private String verify;
    private String status;

    public UserSSO() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSync_settings() {
        return sync_settings;
    }

    public void setSync_settings(String sync_settings) {
        this.sync_settings = sync_settings;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
