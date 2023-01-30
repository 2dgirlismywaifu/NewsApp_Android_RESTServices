package com.notelysia.newsandroidservices.controller;

import com.notelysia.newsandroidservices.AzureSQLConnection;
import com.notelysia.newsandroidservices.RandomNumber;
import com.notelysia.newsandroidservices.models.CheckNickname;
import com.notelysia.newsandroidservices.models.RecoveryCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserInformationController {
    Connection con = null;
    PreparedStatement ps;
    public final String verify = "false";
    public final String user_id_random = new RandomNumber().generateRandomNumber();
    public final String recovery_code = java.util.UUID.randomUUID().toString();
    private final String CREATE_USER = "INSERT INTO USER_PASSLOGIN (user_id, email, password, salt, nickname, verify, recovery) VALUES (?,?,?,?,?,?,?)";
    @RequestMapping(value = "/register", params = {"email", "password", "nickname"},method = RequestMethod.POST)
    //Create user account
    public void createUser
            (@RequestParam(value = "email") String email,
             @RequestParam(value = "password") String password,
             @RequestParam(value = "nickname") String nickname) {
        {
            con = new AzureSQLConnection().getConnection();
            //Bcrypt password
            String Salt = BCrypt.gensalt();
            String password_hash = BCrypt.hashpw(password, Salt);
            try {
                PreparedStatement ps = con.prepareStatement(CREATE_USER);
                ps.setString(1, user_id_random);
                ps.setString(2, email);
                ps.setString(3, password_hash);
                ps.setString(4, Salt);
                ps.setString(5, nickname);
                ps.setString(6, verify);
                ps.setString(7, recovery_code);
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
    //make sure nickname and email is available to use
    @RequestMapping(value = "/checknickname", params = {"nickname", "email"}, method = RequestMethod.GET)
    public ResponseEntity<List<CheckNickname>> checkNickname(@RequestParam(value = "nickname") String nickname, @RequestParam(value = "email") String email) {
        con = new AzureSQLConnection().getConnection();
        List<CheckNickname> checkNicknameList = new ArrayList<>();
        try {
            ps = con.prepareStatement("SELECT * FROM USER_PASSLOGIN WHERE nickname = ? AND email = ?");
            ps.setString(1, nickname);
            ps.setString(2, email);
            ResultSet rs = ps.executeQuery();
            CheckNickname checkNickname = new CheckNickname();
            if (rs.next()) {
                checkNickname.setNickname(rs.getString("nickname"));
                checkNickname.setEmail(rs.getString("email"));
                checkNicknameList.add(checkNickname);
            }
            else {
                checkNickname.setNickname("");
                checkNickname.setEmail("");
                checkNicknameList.add(checkNickname);
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(checkNicknameList);
    }
    //Show recovery code to user
    @RequestMapping(value = "/recoverycode", params = {"email"}, method = RequestMethod.GET)
    public ResponseEntity<List<RecoveryCode>> recoveryCode(@RequestParam(value = "email") String email) {
        con = new AzureSQLConnection().getConnection();
        List<RecoveryCode> recoveryCodeList = new ArrayList<>();
        try {
            ps = con.prepareStatement("SELECT recovery FROM USER_PASSLOGIN WHERE email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            RecoveryCode recoveryCode = new RecoveryCode();
            if (rs.next()) {
                recoveryCode.setRecoverycode(rs.getString(0));
                recoveryCodeList.add(recoveryCode);
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(recoveryCodeList);
    }
    //Generate recovery code from user
    @RequestMapping(value = "/generaterecoverycode", params = {"nickname"}, method = RequestMethod.POST)
    public void generateRecoveryCode(@RequestParam(value = "nickname") String nickname) {
        con = new AzureSQLConnection().getConnection();
        try {
            ps = con.prepareStatement("UPDATE USER_PASSLOGIN SET recovery = ? WHERE nickname = ?");
            ps.setString(1, recovery_code);
            ps.setString(2, nickname);
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //There is another settings form user, but it will write later
    //Other settings is: Sync Settings, Avatar Account, User Information like real name, birthday, gender, etc
}
