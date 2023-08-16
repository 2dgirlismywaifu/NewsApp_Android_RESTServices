/*
 * Copyright By @2dgirlismywaifu (2023) .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.notelysia.newsandroidservices.controller;

import com.notelysia.newsandroidservices.exception.*;
import com.notelysia.newsandroidservices.model.*;
import com.notelysia.newsandroidservices.util.*;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v2")
public class UserPassLoginController {
    DecodeString decodeString = new DecodeString();
    UserPassLogin userPassLogin;
    UserInformation userInformation;
    @Autowired
    UserPassLoginRepo userPassLoginRepo;
    @Autowired
    UserInformationRepo userInformationRepo;
    private final String verify = "false";
    private final String date = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
    private String getDecode (byte[] data) {
        return decodeString.decodeString(data);
    }
//    public final String CREATE_USER = "INSERT INTO USER_PASSLOGIN (user_id, email, password, salt, nickname, verify, recovery) VALUES (?,?,?,?,?,?,?)";
//    public final String CREATE_USER_INFORMATION = "INSERT INTO USER_INFORMATION (user_id, name, gender, birthday, avatar) VALUES (?,?,?,?,?)";
//    ////////Update information////////////////
//    public final String UPDATE_USER_NAME = "UPDATE USER_PASSLOGIN SET nickname = ? WHERE user_id = ?";
//    public final String UPDATE_USER_AVATAR = "UPDATE USER_INFORMATION SET avatar =? WHERE user_id = ?";
//    public final String UPDATE_USER_FULLNAME = "UPDATE USER_INFORMATION SET name =? WHERE user_id = ?";
//    public final String UPDATE_USER_GENDER = "UPDATE USER_INFORMATION SET gender = ? WHERE user_id = ?";
//    public final String UPDATE_USER_BIRTHDAY = "UPDATE USER_INFORMATION SET birthday = ? WHERE user_id = ?";
//    //////////////Update password////////////////
//    public final String UPDATE_USER_PASSWORD = "UPDATE USER_PASSLOGIN SET password = ?, salt = ? WHERE user_id = ?";
//    public final String RECOVERY_ACCOUNT = "SELECT * FROM USER_PASSLOGIN, USER_INFORMATION WHERE recovery = ? AND USER_PASSLOGIN.user_id = USER_INFORMATION.user_id";
    @PostMapping(value = "/register", params = {"fullname","email", "password", "nickname"})
    //Create user account
    //Add description to each parameter
    //Post method not show parameter in swagger, use @RequestBody instead
    public ResponseEntity<HashMap<String, String>> createUser
            (@Parameter(name = "fullname", description = "Encode it to BASE64 before input")
             @RequestParam(name = "fullname") String fullname,
                @Parameter(name = "email", description = "Encode it to BASE64 before input")
             @RequestParam(name = "email") String email,
                @Parameter(name = "password", description = "Encode it to BASE64 before input")
             @RequestParam(name = "password") String password,
                @Parameter(name = "nickname", description = "Encode it to BASE64 before input")
             @RequestParam(name = "nickname") String nickname) {
        int user_id_random = Integer.parseInt(new RandomNumber().generateRandomNumber());
        String recovery_code = java.util.UUID.randomUUID().toString();
        //Bcrypt password
        String Salt = BCrypt.gensalt();
        String password_hash = BCrypt.hashpw(getDecode(password.getBytes(StandardCharsets.UTF_8)), Salt);
        userPassLogin = new UserPassLogin(user_id_random, getDecode(email.getBytes(StandardCharsets.UTF_8)), password_hash, Salt, getDecode(nickname.getBytes(StandardCharsets.UTF_8)), verify, recovery_code);
        userInformation = new UserInformation(user_id_random, getDecode(fullname.getBytes(StandardCharsets.UTF_8)), "not_input", date, "not_available");
        userPassLoginRepo.save(userPassLogin);
        userInformationRepo.save(userInformation);
        return ResponseEntity.ok().body(new HashMap<>() {{
            put("user_id", String.valueOf(userPassLogin.getUser_id()));
            put("fullname", userInformation.getName());
            put("email", userPassLogin.getEmail());
            put("password", userPassLogin.getPassword());
            put("salt", userPassLogin.getSalt());
            put("nickname", userPassLogin.getNickname());
            put("verify", userPassLogin.getVerify());
            put("recovery", userPassLogin.getVerify());
            put("status", "pass");
        }});
    }
    //Verify email from Firebase Authentication, if true, update verify to true
    @PostMapping(value = "/verify", params = {"email"})
    public ResponseEntity<HashMap<String, String>> verifyEmail
    (@Parameter(name = "email", description = "Encode it to BASE64 before input")
            @RequestParam(name = "email") String email
    ) {
        userPassLoginRepo.updateVerify("true", getDecode(email.getBytes(StandardCharsets.UTF_8)));
        return ResponseEntity.ok().body(new HashMap<>(){
            {
                put("email", getDecode(email.getBytes(StandardCharsets.UTF_8)));
                put("status", "pass");
            }
        });
    }
    //Sign in with user or email account and password
    @GetMapping(value = "/signin", params = {"account", "password"})
    public ResponseEntity <HashMap<String, String>> signIn(
            @Parameter(name = "account", description = "Encode it to BASE64 before input") String account,
            @Parameter(name = "password", description = "Encode it to BASE64 before input") String password)
            throws ResourceNotFound {
        HashMap<String, String> userFound = new HashMap<>();
        userPassLogin = userPassLoginRepo.findByEmailOrNickname(getDecode(account.getBytes(StandardCharsets.UTF_8)), getDecode(account.getBytes(StandardCharsets.UTF_8)))
                .orElseThrow(() -> new ResourceNotFound("User not found for this id :: " + account));
        String password_hash = userPassLogin.getPassword();
        String salt = userPassLogin.getSalt();
        String password_check = BCrypt.hashpw(getDecode(password.getBytes(StandardCharsets.UTF_8)), salt);
        if (password_check.equals(password_hash)) {
            userFound.put("user_id", String.valueOf(userPassLogin.getUser_id()));
            userFound.put("email", userPassLogin.getEmail());
            userFound.put("password", userPassLogin.getPassword());
            userFound.put("salt", userPassLogin.getSalt());
            userFound.put("nickname", userPassLogin.getNickname());
            userFound.put("verify", userPassLogin.getVerify());
            userFound.put("recovery", userPassLogin.getVerify());
            userFound.put("status", "pass");
        }
        else {
            userFound.put("status", "fail");
        }
        return ResponseEntity.ok().body(userFound);
    }
    //Update password form request forgot password and change password form in settings
    @PostMapping(value = "/changepassword", params = {"email", "password"})
    public void changepassword(
            @Parameter(name = "email", description = "Encode it to BASE64 before input")
            @RequestParam(name = "email") String email,
            @Parameter(name = "password", description = "Encode it to BASE64 before input")
            @RequestParam(name = "password") String password) {
        String recovery_code = java.util.UUID.randomUUID().toString();
        //Bcrypt password
        String Salt = BCrypt.gensalt();
        String password_hash = BCrypt.hashpw(getDecode(password.getBytes(StandardCharsets.UTF_8)), Salt);
        userPassLoginRepo.updatePassword(password_hash, Salt, recovery_code, getDecode(email.getBytes(StandardCharsets.UTF_8)));
    }

    //Make sure nickname and email is available to use
    @GetMapping(value = "/checknickname", params = {"nickname", "email"})
    public ResponseEntity <HashMap<String, String>> CheckNickname(
            @Parameter(name = "nickname", description = "Encode it to BASE64 before input") String nickname,
            @Parameter(name = "email", description = "Encode it to BASE64 before input") String email) throws ResourceNotFound {
        userPassLogin = userPassLoginRepo.findByNickname(getDecode(nickname.getBytes(StandardCharsets.UTF_8)),
                        getDecode(email.getBytes(StandardCharsets.UTF_8)))
                .orElseThrow(() -> new ResourceNotFound("User not found for this id :: " + nickname));
        return ResponseEntity.ok().body(new HashMap<>() {{
            put("nickname", userPassLogin.getNickname());
            put("email", userPassLogin.getEmail());
        }});
    }
    //Show recovery code to user
    @GetMapping(value = "/recoverycode", params = {"email"})
    public ResponseEntity<HashMap<String, String>> recoveryCode(
            @Parameter(name = "email", description = "Encode it to BASE64 before input") String email) throws ResourceNotFound {
        userPassLogin = userPassLoginRepo.findByRecovery(getDecode(email.getBytes(StandardCharsets.UTF_8)))
                .orElseThrow(() -> new ResourceNotFound("User not found for this id :: " + email));
        return ResponseEntity.ok().body(new HashMap<>() {{
            put("recoverycode", userPassLogin.getRecovery());
        }});
    }
//    //Generate recovery code from user
//    @RequestMapping(value = "/generaterecoverycode", params = {"userid"}, method = RequestMethod.POST)
//    public ResponseEntity <HashMap<String, String>> generateRecoveryCode(@RequestParam(value = "userid") String userid) {
//        con = new AzureSQLConnection().getConnection();
//        String new_recovery_code = java.util.UUID.randomUUID().toString();
//        HashMap<String, String> recoverySuccess = new HashMap<>();
//        try {
//            ps = con.prepareStatement("UPDATE USER_PASSLOGIN SET recovery = ? WHERE user_id = ?");
//            ps.setString(1, new_recovery_code);
//            ps.setString(2, userid);
//            int rs = ps.executeUpdate();
//            if (rs >0 ){
//                recoverySuccess.put("status", "pass");
//                recoverySuccess.put("recoverycode", new_recovery_code);
//            }
//            else
//            {
//                recoverySuccess.put("status", "fail");
//                recoverySuccess.put("recoverycode", "");
//            }
//            con.close();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return ResponseEntity.ok().body(recoverySuccess);
//    }
//    //There is another settings form user, but it will write later
//    //Other settings is: Avatar Account, User Information like real name, birthday, gender, etc
//    //Most important is user can change password
//    //Before allow change password, first check old password is correct or not
//    @RequestMapping(value = "/account/password/check", params = {"userid", "email", "oldpass"}, method = RequestMethod.GET)
//    public ResponseEntity<HashMap<String, String>> checkOldPassword(@RequestParam(value = "userid") String userid,
//                                                                    @RequestParam(value = "email") String email,
//                                                                    @RequestParam(value = "oldpass") String oldpass) {
//        HashMap<String, String> passwordMatch = new HashMap<>();
//        con = new AzureSQLConnection().getConnection();
//        try {
//            ps = con.prepareStatement("SELECT * FROM USER_PASSLOGIN WHERE user_id = ? AND email = ?");
//            ps.setString(1, userid);
//            ps.setString(2, email);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                String password_hash = rs.getString("password");
//                String salt = rs.getString("salt");
//                String password_check = BCrypt.hashpw(oldpass, salt);
//                if (password_check.equals(password_hash)) {
//                    passwordMatch.put("status", "pass");
//                }
//                else {
//                    passwordMatch.put("status", "fail");
//                }
//            }
//            else {
//                passwordMatch.put("status", "fail");
//            }
//            con.close();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return ResponseEntity.ok().body(passwordMatch);
//    }
//    //Update user password
//    @RequestMapping(value = "/account/password/update", params = {"userid", "newpass"}, method = RequestMethod.POST)
//    public ResponseEntity<HashMap<String, String>> updatePassword(@RequestParam(value = "userid") String userid,
//                                                                  @RequestParam(value = "newpass") String newpass) {
//        con = new AzureSQLConnection().getConnection();
//        HashMap<String, String> updateSuccess = new HashMap<>();
//        try {
//            //gen new salt
//            String salt = BCrypt.gensalt();
//            //hash new password
//            String newpass_hash = BCrypt.hashpw(newpass, salt);
//            ps = con.prepareStatement(UPDATE_USER_PASSWORD);
//            ps.setString(1, newpass_hash);
//            ps.setString(2, salt);
//            ps.setString(3, userid);
//            int rs = ps.executeUpdate();
//            if (rs >0 ){
//                updateSuccess.put("newpass", newpass_hash);
//                updateSuccess.put("status", "pass");
//            }
//            else
//            {
//                updateSuccess.put("status", "fail");
//            }
//            con.close();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return ResponseEntity.ok().body(updateSuccess);
//    }
//    //Update user name
//    @RequestMapping (value = "/account/username/update", params = {"userid", "username"}, method = RequestMethod.POST)
//    public ResponseEntity<HashMap<String, String>> updateUserName (@RequestParam(value = "userid") String userid, @RequestParam(value = "username") String username) {
//        con = new AzureSQLConnection().getConnection();
//        HashMap<String, String> updateSuccess = new HashMap<>();
//        try {
//            ps = con.prepareStatement(UPDATE_USER_NAME);
//            ps.setString(1, username);
//            ps.setString(2, userid);
//            int rs = ps.executeUpdate();
//            if (rs >0 ){
//                updateSuccess.put("status", "pass");
//                updateSuccess.put("username", username);
//            }
//            else
//            {
//                updateSuccess.put("status", "fail");
//            }
//            con.close();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return ResponseEntity.ok().body(updateSuccess);
//    }
//    //Update user fullname
//    @RequestMapping (value = "/account/fullname/update", params = {"userid", "fullname"}, method = RequestMethod.POST)
//    public ResponseEntity<HashMap<String, String>> updateUserFullname (@RequestParam(value = "userid") String userid, @RequestParam(value = "fullname") String fullname) {
//        con = new AzureSQLConnection().getConnection();
//        HashMap<String, String> updateSuccess = new HashMap<>();
//        try {
//            ps = con.prepareStatement(UPDATE_USER_FULLNAME);
//            ps.setString(1, fullname);
//            ps.setString(2, userid);
//            int rs = ps.executeUpdate();
//            if (rs >0 ){
//                updateSuccess.put("status", "pass");
//                updateSuccess.put("fullname", fullname);
//            }
//            else
//            {
//                updateSuccess.put("status", "fail");
//            }
//            con.close();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return ResponseEntity.ok().body(updateSuccess);
//    }
//    //Update user birthday
//    @RequestMapping (value = "/account/birthday/update", params = {"userid", "birthday"}, method = RequestMethod.POST)
//    public ResponseEntity<HashMap<String, String>> updateUserBirthday (@RequestParam(value = "userid") String userid, @RequestParam(value = "birthday") String birthday) {
//        con = new AzureSQLConnection().getConnection();
//        HashMap<String, String> updateSuccess = new HashMap<>();
//        try {
//            ps = con.prepareStatement(UPDATE_USER_BIRTHDAY);
//            ps.setString(1, birthday);
//            ps.setString(2, userid);
//            int rs = ps.executeUpdate();
//            if (rs >0 ){
//                updateSuccess.put("status", "pass");
//                updateSuccess.put("birthday", birthday);
//            }
//            else
//            {
//                updateSuccess.put("status", "fail");
//            }
//            con.close();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return ResponseEntity.ok().body(updateSuccess);
//    }
//    //Update user gender
//    @RequestMapping (value = "/account/gender/update", params = {"userid", "gender"}, method = RequestMethod.POST)
//    public ResponseEntity<HashMap<String, String>> updateUserGender (@RequestParam(value = "userid") String userid, @RequestParam(value = "gender") String gender) {
//        con = new AzureSQLConnection().getConnection();
//        HashMap<String, String> updateSuccess = new HashMap<>();
//        try {
//            ps = con.prepareStatement(UPDATE_USER_GENDER);
//            ps.setString(1, gender);
//            ps.setString(2, userid);
//            int rs = ps.executeUpdate();
//            if (rs >0 ){
//                updateSuccess.put("status", "pass");
//                updateSuccess.put("gender", gender);
//            }
//            else
//            {
//                updateSuccess.put("status", "fail");
//            }
//            con.close();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return ResponseEntity.ok().body(updateSuccess);
//    }
//    //Update user avatar
//    @RequestMapping (value = "/account/avatar/update", params = {"userid", "avatar"}, method = RequestMethod.POST)
//    public ResponseEntity<HashMap<String, String>> updateUserAvatar (@RequestParam(value = "userid") String userid, @RequestParam(value = "avatar") String avatar) {
//        con = new AzureSQLConnection().getConnection();
//        HashMap<String, String> updateSuccess = new HashMap<>();
//        //First, upload image to Azure Storage Bolb
//        //Then, get the link of image
//        //Finally, update the link to database
//        try {
//            ps = con.prepareStatement(UPDATE_USER_AVATAR);
//            ps.setString(1, avatar);
//            ps.setString(2, userid);
//            int rs = ps.executeUpdate();
//            if (rs >0 ){
//                updateSuccess.put("status", "pass");
//                updateSuccess.put("avatar", avatar);
//            }
//            else
//            {
//                updateSuccess.put("status", "fail");
//            }
//            con.close();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return ResponseEntity.ok().body(updateSuccess);
//    }
//    //Recovery account with recovery code
//    @RequestMapping(value = "/account/recovery", params = {"code"}, method = RequestMethod.GET)
//    public ResponseEntity <HashMap<String, String>> RecoveryAccount(@RequestParam(value = "code") String code) {
//        HashMap<String, String> userFound = new HashMap<>();
//        con = new AzureSQLConnection().getConnection();
//        try {
//            ps = con.prepareStatement(RECOVERY_ACCOUNT);
//            ps.setString(1, code);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//               userFound.put("user_id", rs.getString("user_id"));
//               userFound.put("name", rs.getString("name"));
//               userFound.put("birthaday", rs.getString("birthday"));
//               userFound.put("gender", rs.getString("gender"));
//               userFound.put("avatar", rs.getString("avatar"));
//               userFound.put("email", rs.getString("email"));
//               userFound.put("password", rs.getString("password"));
//               userFound.put("salt", rs.getString("salt"));
//               userFound.put("nickname", rs.getString("nickname"));
//               userFound.put("verify", rs.getString("verify"));

//               userFound.put("recovery", rs.getString("recovery"));
//               userFound.put("status", "pass");
//            }
//            else {
//                userFound.put("status", "fail");
//            }
//            con.close();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return ResponseEntity.ok().body(userFound);
//    }
}
