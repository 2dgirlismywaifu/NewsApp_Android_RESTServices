package com.notelysia.newsandroidservices.controller;

import com.notelysia.newsandroidservices.AzureSQLConnection;
import com.notelysia.newsandroidservices.RandomNumber;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@RestController
public class UserInformationController {
    Connection con = null;
    PreparedStatement ps;
    public final String verify = "false";
    public final String user_id_random = new RandomNumber().generateRandomNumber();
    private final String CREATE_USER = "INSERT INTO USER_PASSLOGIN (user_id, email, password, nickname, verify) VALUES (?,?,?,?,?)";
    @RequestMapping(value = "/register", params = {"email", "password", "nickname"},method = RequestMethod.POST)
    //Create user account
    public void createUser
            (@RequestParam(value = "email") String email,
             @RequestParam(value = "password") String password,
             @RequestParam(value = "nickname") String nickname) {
        {
            con = new AzureSQLConnection().getConnection();
            try {
                PreparedStatement ps = con.prepareStatement(CREATE_USER);
                ps.setString(1, user_id_random);
                ps.setString(2, email);
                ps.setString(3, password);
                ps.setString(4, nickname);
                ps.setString(5, verify);
                ps.executeUpdate();
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //Verify email from Firebase Authentication, if true, update verify to true
    @RequestMapping(value = "/verify", params = {"email"}, method = RequestMethod.POST)
    public void verifyEmail(@RequestParam(value = "email") String email) {
        con = new AzureSQLConnection().getConnection();
        try {
            ps = con.prepareStatement("UPDATE USER_PASSLOGIN SET verify = 'true' WHERE email = ?");
            ps.setString(1, email);
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //Sign in with user or email account and password
    @RequestMapping(value = "/signin", params = {"account", "password"}, method = RequestMethod.POST)
    public void signIn(@RequestParam(value = "account") String account, @RequestParam(value = "password") String password) {
        con = new AzureSQLConnection().getConnection();
        try {
            ps = con.prepareStatement("SELECT * FROM USER_PASSLOGIN WHERE email = ? OR nickname = ? AND password = ?");
            ps.setString(1, account);
            ps.setString(2, account);
            ps.setString(3, password);
            ps.executeQuery();
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //Update password form request forgot password
    @RequestMapping(value = "/forgotpassword", params = {"email", "password"}, method = RequestMethod.POST)
    public void forgotPassword(@RequestParam(value = "email") String email, @RequestParam(value = "password") String password) {
        con = new AzureSQLConnection().getConnection();
        try {
            ps = con.prepareStatement("UPDATE USER_PASSLOGIN SET password = ? WHERE email = ?");
            ps.setString(1, password);
            ps.setString(2, email);
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //Update password if user want change password form settings, also use old password input to check it avaliable is database or not
    @RequestMapping(value = "/changepassword", params = {"email", "oldpassword", "newpassword"}, method = RequestMethod.POST)
    public void changePassword(@RequestParam(value = "email") String email, @RequestParam(value = "oldpassword") String oldpassword, @RequestParam(value = "newpassword") String newpassword) {
        con = new AzureSQLConnection().getConnection();
        try {
            ps = con.prepareStatement("UPDATE USER_PASSLOGIN SET password = ? WHERE email = ? AND password = ?");
            ps.setString(1, newpassword);
            ps.setString(2, email);
            ps.setString(3, oldpassword);
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //There is another settings form user, but it will write later
    //Other settings is: Sync Settings, Avatar Account, User Information like real name, birthday, gender, etc
}
