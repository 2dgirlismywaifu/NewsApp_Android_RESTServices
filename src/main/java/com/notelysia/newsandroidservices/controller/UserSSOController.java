package com.notelysia.newsandroidservices.controller;

import com.notelysia.newsandroidservices.azure.AzureSQLConnection;
import com.notelysia.newsandroidservices.RandomNumber;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
@RestController
public class UserSSOController {
    Connection con = null;
    PreparedStatement ps;
    public final String verify = "true"; //sso always verify
    public String date = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
    private final String CREATE_USER = "INSERT INTO USER_SSO (user_id, email, nickname, verify) VALUES (?,?,?,?)";
    private final String CREATE_USER_INFORMATION = "INSERT INTO USER_SSO_INFORMATION (user_id, name, gender, birthday, avatar) VALUES (?,?,?,?,?)";
    //only user information
    //update user when user login with SSO (nickname, name, avatar)
    private final String UPDATE_USER = "UPDATE USER_SSO SET nickname = ? WHERE user_id = ?";
    private final String UPDATE_USER_AVATAR = "UPDATE USER_SSO_INFORMATION SET avatar =? WHERE user_id = ?";
    private final String UPDATE_USER_NAME = "UPDATE USER_SSO_INFORMATION SET name =? WHERE user_id = ?";
    private final String UPDATE_USER_GENDER = "UPDATE USER_SSO_INFORMATION SET gender = ? WHERE user_id = ?";
    private final String UPDATE_USER_BIRTHDAY = "UPDATE USER_SSO_INFORMATION SET birthday = ? WHERE user_id = ?";
    @RequestMapping(value = "/sso", params = {"fullname","email", "nickname", "avatar"},method = RequestMethod.POST)
    //Create user account
    //Why gender and birthday not input? Because it is private information about each user.
    // Firebase do not have function to get the user's gender/birthdate
    public ResponseEntity <HashMap<String, String>> createUser
            (@RequestParam(value = "fullname") String fullname,
             @RequestParam(value = "email") String email,
             @RequestParam(value = "nickname") String nickname,
             @RequestParam(value = "avatar") String avatar) {
        String user_id_random = new RandomNumber().generateSSONumber();
        String status;
        con = new AzureSQLConnection().getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(CREATE_USER + ";" + CREATE_USER_INFORMATION);
            //CREATE USER_PASSLOGIN
            ps.setString(1, user_id_random);
            ps.setString(2, email);
            ps.setString(3, nickname);
            ps.setString(4, verify);
            //CREATE USER_INFORMATION
            ps.setString(5, user_id_random);
            ps.setString(6, fullname);
            ps.setString(7, "not_input");
            ps.setString(8, date);
            ps.setString(9, avatar);
            int rs = ps.executeUpdate();
            if (rs > 0) {
                status = "success";
            }
            else {
                status = "failed";
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body(new HashMap<String, String>() {{
            put("user_id", user_id_random);
            put("fullname", fullname);
            put("email", email);
            put("nickname", nickname);
            put("verify", verify);
            put("status", status);
            }});
    }
    //Update user information
    @RequestMapping(value = "/sso/update", params = {"user_id", "name", "avatar"}, method = RequestMethod.POST)
    public ResponseEntity <HashMap<String, String>> updateUser(@RequestParam(value = "user_id") String user_id,
                                                              @RequestParam(value = "name") String name,
                                                              @RequestParam(value = "avatar") String avatar) {
        HashMap<String, String> userFound = new HashMap<>();
       con = new AzureSQLConnection().getConnection();
        try {
            ps = con.prepareStatement(UPDATE_USER + ";" + UPDATE_USER_NAME + "; " + UPDATE_USER_AVATAR);
            ps.setString(1, name);
            ps.setString(2, user_id);
            ps.setString(3, name);
            ps.setString(4, user_id);
            ps.setString(5, avatar);
            ps.setString(6, user_id);
            int rs = ps.executeUpdate();
            if (rs > 0) {
                userFound.put("user_id", user_id);
                userFound.put("name", name);
                userFound.put("nickname", name);
                userFound.put("avatar", avatar);
                userFound.put("status", "pass");
            }
            else {
                userFound.put("status", "fail");
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body(userFound);
    }
    @RequestMapping(value = "/sso/count", params = {"account"}, method = RequestMethod.GET)
    public ResponseEntity <HashMap<String, String>> countUser(@RequestParam(value = "account") String account) {
        HashMap<String, String> userFound = new HashMap<>();
       con = new AzureSQLConnection().getConnection();
        try {
            ps = con.prepareStatement("SELECT * FROM USER_SSO, USER_SSO_INFORMATION WHERE email = ? OR nickname = ? AND USER_SSO.user_id = USER_SSO_INFORMATION.user_id");
            ps.setString(1, account);
            ps.setString(2, account);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                userFound.put("user_id", rs.getString("user_id"));
                userFound.put("name", rs.getString("name"));
                userFound.put("birthaday", rs.getString("birthday"));
                userFound.put("gender", rs.getString("gender"));
                userFound.put("avatar", rs.getString("avatar"));
                userFound.put("email", rs.getString("email"));
                userFound.put("nickname", rs.getString("nickname"));
                userFound.put("sync_settings", rs.getString("sync_settings"));
                userFound.put("verify", rs.getString("verify"));
                userFound.put("status", "pass");
            }
            else {
                userFound.put("status", "fail");
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body(userFound);
    }
}
