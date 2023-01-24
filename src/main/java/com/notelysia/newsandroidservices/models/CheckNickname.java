package com.notelysia.newsandroidservices.models;

public class CheckNickname {
    private String nickname;
    private String email;

    public CheckNickname(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }
    public CheckNickname() {
    }
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
