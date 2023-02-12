package com.notelysia.newsandroidservices.controller;

import com.notelysia.newsandroidservices.azure.AzureSQLConnection;
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
import java.util.HashMap;

@RestController
public class UserPassLogin {
    Connection con = null;
    PreparedStatement ps;
    public final String verify = "false";
    public String date = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
    public final String CREATE_USER = "INSERT INTO USER_PASSLOGIN (user_id, email, password, salt, nickname, verify, recovery) VALUES (?,?,?,?,?,?,?)";
    public final String CREATE_USER_INFORMATION = "INSERT INTO USER_INFORMATION (user_id, name, gender, birthday, avatar) VALUES (?,?,?,?,?)";
    ////////Update information////////////////
    public final String UPDATE_USER_NAME = "UPDATE USER_PASSLOGIN SET nickname = ? WHERE user_id = ?";
    public final String UPDATE_USER_AVATAR = "UPDATE USER_INFORMATION SET avatar =? WHERE user_id = ?";
    public final String UPDATE_USER_FULLNAME = "UPDATE USER_INFORMATION SET name =? WHERE user_id = ?";
    public final String UPDATE_USER_GENDER = "UPDATE USER_INFORMATION SET gender = ? WHERE user_id = ?";
    public final String UPDATE_USER_BIRTHDAY = "UPDATE USER_INFORMATION SET birthday = ? WHERE user_id = ?";
    //////////////Update password////////////////
    public final String UPDATE_USER_PASSWORD = "UPDATE USER_PASSLOGIN SET password = ?, salt = ? WHERE user_id = ?";
    public final String RECOVERY_ACCOUNT = "SELECT * FROM USER_PASSLOGIN, USER_INFORMATION WHERE recovery = ? AND USER_PASSLOGIN.user_id = USER_INFORMATION.user_id";
    @RequestMapping(value = "/register", params = {"fullname","email", "password", "nickname"},method = RequestMethod.POST)
    //Create user account
    public ResponseEntity <HashMap<String, String>> createUser
            (@RequestParam(value = "fullname") String fullname,
             @RequestParam(value = "email") String email,
             @RequestParam(value = "password") String password,
             @RequestParam(value = "nickname") String nickname) {
        String user_id_random = new RandomNumber().generateRandomNumber();
        String recovery_code = java.util.UUID.randomUUID().toString();
        String status;
        con = new AzureSQLConnection().getConnection();
        //Bcrypt password
        String Salt = BCrypt.gensalt();
        String password_hash = BCrypt.hashpw(password, Salt);
        try {
            PreparedStatement ps = con.prepareStatement(CREATE_USER + ";" + CREATE_USER_INFORMATION);
            //CREATE USER_PASSLOGIN
            ps.setString(1, user_id_random);
            ps.setString(2, email);
            ps.setString(3, password_hash);
            ps.setString(4, Salt);
            ps.setString(5, nickname);
            ps.setString(6, verify);
            ps.setString(7, recovery_code);
            //CREATE USER_INFORMATION
            ps.setString(8, user_id_random);
            ps.setString(9, fullname);
            ps.setString(10, "not_input");
            ps.setString(11, date);
            ps.setString(12, "not_available");
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
            put("password", password_hash);
            put("salt", Salt);
            put("nickname", nickname);
            put("verify", verify);
            put("recovery", recovery_code);
            put("status", status);
            }});
    }
    //Verify email from Firebase Authentication, if true, update verify to true
    @RequestMapping(value = "/verify", params = {"email"}, method = RequestMethod.POST)
    public ResponseEntity<HashMap<String, String>> verifyEmail(@RequestParam(value = "email") String email) {
        con = new AzureSQLConnection().getConnection();
        try {
            ps = con.prepareStatement("UPDATE USER_PASSLOGIN SET verify = 'true' WHERE email = ?");
            ps.setString(1, email);
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body(new HashMap<String, String>() {{
            put("email", email);
            put("verify", "true");
        }});
    }
    //Sign in with user or email account and password
    @RequestMapping(value = "/signin", params = {"account", "password"}, method = RequestMethod.GET)
    public ResponseEntity <HashMap<String, String>> signIn(@RequestParam(value = "account") String account, @RequestParam(value = "password") String password) {
        HashMap<String, String> userFound = new HashMap<>();
        con = new AzureSQLConnection().getConnection();
        try {
            ps = con.prepareStatement("SELECT * FROM USER_PASSLOGIN, USER_INFORMATION WHERE email = ? OR nickname = ? AND USER_PASSLOGIN.user_id = USER_INFORMATION.user_id");
            ps.setString(1, account);
            ps.setString(2, account);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String password_hash = rs.getString("password");
                String salt = rs.getString("salt");
                String password_check = BCrypt.hashpw(password, salt);
                if (password_check.equals(password_hash)) {
                    userFound.put("user_id", rs.getString("user_id"));
                    userFound.put("name", rs.getString("name"));
                    userFound.put("birthaday", rs.getString("birthday"));
                    userFound.put("gender", rs.getString("gender"));
                    userFound.put("avatar", rs.getString("avatar"));
                    userFound.put("email", rs.getString("email"));
                    userFound.put("password", rs.getString("password"));
                    userFound.put("salt", rs.getString("salt"));
                    userFound.put("nickname", rs.getString("nickname"));
                    userFound.put("verify", rs.getString("verify"));
                    userFound.put("recovery", rs.getString("recovery"));
                    userFound.put("status", "pass");
                }
                else {
                    userFound.put("status", "fail");
                }
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
    public ResponseEntity <HashMap<String, String>> checkNickname(@RequestParam(value = "nickname") String nickname, @RequestParam(value = "email") String email) {
        con = new AzureSQLConnection().getConnection();
        CheckNickname checkNickname = new CheckNickname();
        try {
            ps = con.prepareStatement("SELECT * FROM USER_PASSLOGIN WHERE nickname = ? AND email = ?");
            ps.setString(1, nickname);
            ps.setString(2, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                checkNickname.setNickname(rs.getString("nickname"));
                checkNickname.setEmail(rs.getString("email"));
            }
            else {
                checkNickname.setNickname("");
                checkNickname.setEmail("");
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body(new HashMap<String, String>() {{
            put("nickname", checkNickname.getNickname());
            put("email", checkNickname.getEmail());
            }});
    }
    //Show recovery code to user
    @RequestMapping(value = "/recoverycode", params = {"email"}, method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, String>> recoveryCode(@RequestParam(value = "email") String email) {
        con = new AzureSQLConnection().getConnection();
        RecoveryCode recoveryCode = new RecoveryCode();
        try {
            ps = con.prepareStatement("SELECT recovery FROM USER_PASSLOGIN WHERE email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                recoveryCode.setRecoverycode(rs.getString("recovery"));
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body(new HashMap<String, String>() {{
            put("recoverycode", recoveryCode.getRecoverycode());
        }});
    }
    //Generate recovery code from user
    @RequestMapping(value = "/generaterecoverycode", params = {"userid"}, method = RequestMethod.POST)
    public ResponseEntity <HashMap<String, String>> generateRecoveryCode(@RequestParam(value = "userid") String userid) {
        con = new AzureSQLConnection().getConnection();
        String new_recovery_code = java.util.UUID.randomUUID().toString();
        HashMap<String, String> recoverySuccess = new HashMap<>();
        try {
            ps = con.prepareStatement("UPDATE USER_PASSLOGIN SET recovery = ? WHERE user_id = ?");
            ps.setString(1, new_recovery_code);
            ps.setString(2, userid);
            int rs = ps.executeUpdate();
            if (rs >0 ){
                recoverySuccess.put("status", "pass");
                recoverySuccess.put("recoverycode", new_recovery_code);
            }
            else
            {
                recoverySuccess.put("status", "fail");
                recoverySuccess.put("recoverycode", "");
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body(recoverySuccess);
    }
    //There is another settings form user, but it will write later
    //Other settings is: Avatar Account, User Information like real name, birthday, gender, etc
    //Most important is user can change password
    //Before allow change password, first check old password is correct or not
    @RequestMapping(value = "/account/password/check", params = {"userid", "email", "oldpass"}, method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, String>> checkOldPassword(@RequestParam(value = "userid") String userid,
                                                                    @RequestParam(value = "email") String email,
                                                                    @RequestParam(value = "oldpass") String oldpass) {
        HashMap<String, String> passwordMatch = new HashMap<>();
        con = new AzureSQLConnection().getConnection();
        try {
            ps = con.prepareStatement("SELECT * FROM USER_PASSLOGIN WHERE user_id = ? AND email = ?");
            ps.setString(1, userid);
            ps.setString(2, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String password_hash = rs.getString("password");
                String salt = rs.getString("salt");
                String password_check = BCrypt.hashpw(oldpass, salt);
                if (password_check.equals(password_hash)) {
                    passwordMatch.put("status", "pass");
                }
                else {
                    passwordMatch.put("status", "fail");
                }
            }
            else {
                passwordMatch.put("status", "fail");
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body(passwordMatch);
    }
    //Update user password
    @RequestMapping(value = "/account/password/update", params = {"userid", "newpass"}, method = RequestMethod.POST)
    public ResponseEntity<HashMap<String, String>> updatePassword(@RequestParam(value = "userid") String userid,
                                                                  @RequestParam(value = "newpass") String newpass) {
        con = new AzureSQLConnection().getConnection();
        HashMap<String, String> updateSuccess = new HashMap<>();
        try {
            //gen new salt
            String salt = BCrypt.gensalt();
            //hash new password
            String newpass_hash = BCrypt.hashpw(newpass, salt);
            ps = con.prepareStatement(UPDATE_USER_PASSWORD);
            ps.setString(1, newpass_hash);
            ps.setString(2, salt);
            ps.setString(3, userid);
            int rs = ps.executeUpdate();
            if (rs >0 ){
                updateSuccess.put("newpass", newpass_hash);
                updateSuccess.put("status", "pass");
            }
            else
            {
                updateSuccess.put("status", "fail");
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body(updateSuccess);
    }
    //Update user name
    @RequestMapping (value = "/account/username/update", params = {"userid", "username"}, method = RequestMethod.POST)
    public ResponseEntity<HashMap<String, String>> updateUserName (@RequestParam(value = "userid") String userid, @RequestParam(value = "username") String username) {
        con = new AzureSQLConnection().getConnection();
        HashMap<String, String> updateSuccess = new HashMap<>();
        try {
            ps = con.prepareStatement(UPDATE_USER_NAME);
            ps.setString(1, username);
            ps.setString(2, userid);
            int rs = ps.executeUpdate();
            if (rs >0 ){
                updateSuccess.put("status", "pass");
                updateSuccess.put("username", username);
            }
            else
            {
                updateSuccess.put("status", "fail");
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body(updateSuccess);
    }
    //Update user fullname
    @RequestMapping (value = "/account/fullname/update", params = {"userid", "fullname"}, method = RequestMethod.POST)
    public ResponseEntity<HashMap<String, String>> updateUserFullname (@RequestParam(value = "userid") String userid, @RequestParam(value = "fullname") String fullname) {
        con = new AzureSQLConnection().getConnection();
        HashMap<String, String> updateSuccess = new HashMap<>();
        try {
            ps = con.prepareStatement(UPDATE_USER_FULLNAME);
            ps.setString(1, fullname);
            ps.setString(2, userid);
            int rs = ps.executeUpdate();
            if (rs >0 ){
                updateSuccess.put("status", "pass");
                updateSuccess.put("fullname", fullname);
            }
            else
            {
                updateSuccess.put("status", "fail");
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body(updateSuccess);
    }
    //Update user birthday
    @RequestMapping (value = "/account/birthday/update", params = {"userid", "birthday"}, method = RequestMethod.POST)
    public ResponseEntity<HashMap<String, String>> updateUserBirthday (@RequestParam(value = "userid") String userid, @RequestParam(value = "birthday") String birthday) {
        con = new AzureSQLConnection().getConnection();
        HashMap<String, String> updateSuccess = new HashMap<>();
        try {
            ps = con.prepareStatement(UPDATE_USER_BIRTHDAY);
            ps.setString(1, birthday);
            ps.setString(2, userid);
            int rs = ps.executeUpdate();
            if (rs >0 ){
                updateSuccess.put("status", "pass");
                updateSuccess.put("birthday", birthday);
            }
            else
            {
                updateSuccess.put("status", "fail");
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body(updateSuccess);
    }
    //Update user gender
    @RequestMapping (value = "/account/gender/update", params = {"userid", "gender"}, method = RequestMethod.POST)
    public ResponseEntity<HashMap<String, String>> updateUserGender (@RequestParam(value = "userid") String userid, @RequestParam(value = "gender") String gender) {
        con = new AzureSQLConnection().getConnection();
        HashMap<String, String> updateSuccess = new HashMap<>();
        try {
            ps = con.prepareStatement(UPDATE_USER_GENDER);
            ps.setString(1, gender);
            ps.setString(2, userid);
            int rs = ps.executeUpdate();
            if (rs >0 ){
                updateSuccess.put("status", "pass");
                updateSuccess.put("gender", gender);
            }
            else
            {
                updateSuccess.put("status", "fail");
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body(updateSuccess);
    }
    //Update user avatar
    @RequestMapping (value = "/account/avatar/update", params = {"userid", "avatar"}, method = RequestMethod.POST)
    public ResponseEntity<HashMap<String, String>> updateUserAvatar (@RequestParam(value = "userid") String userid, @RequestParam(value = "avatar") String avatar) {
        con = new AzureSQLConnection().getConnection();
        HashMap<String, String> updateSuccess = new HashMap<>();
        //First, upload image to Azure Storage Bolb
        //Then, get the link of image
        //Finally, update the link to database
        try {
            ps = con.prepareStatement(UPDATE_USER_AVATAR);
            ps.setString(1, avatar);
            ps.setString(2, userid);
            int rs = ps.executeUpdate();
            if (rs >0 ){
                updateSuccess.put("status", "pass");
                updateSuccess.put("avatar", avatar);
            }
            else
            {
                updateSuccess.put("status", "fail");
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body(updateSuccess);
    }
    //Recovery account with recovery code
    @RequestMapping(value = "/account/recovery", params = {"code"}, method = RequestMethod.GET)
    public ResponseEntity <HashMap<String, String>> RecoveryAccount(@RequestParam(value = "code") String code) {
        HashMap<String, String> userFound = new HashMap<>();
        con = new AzureSQLConnection().getConnection();
        try {
            ps = con.prepareStatement(RECOVERY_ACCOUNT);
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
               userFound.put("user_id", rs.getString("user_id"));
               userFound.put("name", rs.getString("name"));
               userFound.put("birthaday", rs.getString("birthday"));
               userFound.put("gender", rs.getString("gender"));
               userFound.put("avatar", rs.getString("avatar"));
               userFound.put("email", rs.getString("email"));
               userFound.put("password", rs.getString("password"));
               userFound.put("salt", rs.getString("salt"));
               userFound.put("nickname", rs.getString("nickname"));
               userFound.put("sync_settings", rs.getString("sync_settings"));
               userFound.put("verify", rs.getString("verify"));
               userFound.put("recovery", rs.getString("recovery"));
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
