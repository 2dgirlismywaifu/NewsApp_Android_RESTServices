package com.notelysia.newsandroidservices.controller;

import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;

@RestController
public class UserInformationController {
    Connection con = null;
    private final String CREATE_USER = "INSERT INTO USER_PASSLOGIN (user_id, email, password, nickname, verify) VALUES (?,?,?,?,?)";
}
