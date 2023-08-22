/*
 * Copyright By @2dgirlismywaifu (2023) .
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.notelysia.restservices.newsapp.controller;

import com.notelysia.restservices.config.DecodeString;
import com.notelysia.restservices.config.RandomNumber;
import com.notelysia.restservices.exception.ResourceNotFound;
import com.notelysia.restservices.newsapp.jparepo.UserInformationRepo;
import com.notelysia.restservices.newsapp.jparepo.UserPassLoginRepo;
import com.notelysia.restservices.newsapp.model.UserInformation;
import com.notelysia.restservices.newsapp.model.UserPassLogin;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v2")
@Tag(name = "User Pass Login", description = "API for User Pass Login")
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
                .orElseThrow(() -> new ResourceNotFound("Failed"));
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
                .orElseThrow(() -> new ResourceNotFound("Failed"));
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
                .orElseThrow(() -> new ResourceNotFound("Failed"));
        return ResponseEntity.ok().body(new HashMap<>() {{
            put("recoverycode", userPassLogin.getRecovery());
        }});
    }
    //Generate recovery code from user
    @PostMapping(value = "/generaterecoverycode", params = {"userid"})
    public ResponseEntity <HashMap<String, String>> generateRecoveryCode(
            @Parameter(name = "userid", description = "Encode it to BASE64 before input")
            @RequestParam(value = "userid") String userid) {
        String new_recovery_code = java.util.UUID.randomUUID().toString();
        userPassLoginRepo.updateRecovery(new_recovery_code, userid);
        return ResponseEntity.ok().body(new HashMap<>() {{
            put("recoverycode", new_recovery_code);
            put("status", "pass");
        }});
    }
    //There is another settings form user, but it will write later
    //Other settings is: Avatar Account, User Information like real name, birthday, gender, etc
    //Most important is user can change password
    //Before allow change password, first check old password is correct or not
    @GetMapping(value = "/account/password/check", params = {"userid", "email", "oldpass"})
    public ResponseEntity<HashMap<String, String>> checkOldPassword(
            @Parameter(name = "userid", description = "Encode it to BASE64 before input") String userid,
            @Parameter(name = "email", description = "Encode it to BASE64 before input") String email,
            @Parameter(name = "oldpass", description = "Encode it to BASE64 before input") String oldpass) throws ResourceNotFound {
       userPassLogin = userPassLoginRepo.findByEmailOrUserid(getDecode(email.getBytes(StandardCharsets.UTF_8)), getDecode(userid.getBytes(StandardCharsets.UTF_8)))
               .orElseThrow(() -> new ResourceNotFound("Failed"));
       String password_hash = userPassLogin.getPassword();
       String salt = userPassLogin.getSalt();
       String password_check = BCrypt.hashpw(getDecode(oldpass.getBytes(StandardCharsets.UTF_8)), salt);
       if (password_check.equals(password_hash)) {
           return ResponseEntity.ok().body(new HashMap<>() {{
                    put("status", "pass");
           }});
       }
       else {
           return ResponseEntity.ok().body(new HashMap<>() {{
               put("status", "fail");
           }});
       }
    }
    //Update user name
    @PostMapping (value = "/account/username/update", params = {"userid", "username"})
    public ResponseEntity<HashMap<String, String>> updateUserName (
            @Parameter(name = "userid", description = "Encode it to BASE64 before input")
            @RequestParam(value = "userid") String userid,
            @Parameter(name = "username", description = "Encode it to BASE64 before input")
            @RequestParam(value = "username") String username) {
        userPassLoginRepo.updateNickname(getDecode(username.getBytes(StandardCharsets.UTF_8)), getDecode(userid.getBytes(StandardCharsets.UTF_8)));
        return ResponseEntity.ok().body(new HashMap<>() {{
            put("status", "pass");
            put("username", username);
        }});
    }
    //Update user fullname
    @PostMapping (value = "/account/fullname/update", params = {"userid", "fullname"})
    public ResponseEntity<HashMap<String, String>> updateUserFullname (
            @Parameter(name = "userid", description = "Encode it to BASE64 before input")
            @RequestParam(value = "userid") String userid,
            @Parameter(name = "fullname", description = "Encode it to BASE64 before input")
            @RequestParam(value = "fullname") String fullname) {
        userInformationRepo.updateFullname(getDecode(fullname.getBytes(StandardCharsets.UTF_8)), getDecode(userid.getBytes(StandardCharsets.UTF_8)));
        return ResponseEntity.ok().body(new HashMap<>() {{
            put("status", "pass");
            put("username", fullname);
        }});
    }
    //Update user birthday
    @PostMapping (value = "/account/birthday/update", params = {"userid", "birthday"})
    public ResponseEntity<HashMap<String, String>> updateUserBirthday (
            @Parameter(name = "userid", description = "Encode it to BASE64 before input")
            @RequestParam(value = "userid") String userid,
            @Parameter(name = "birthday", description = "Encode it to BASE64 before input")
            @RequestParam(value = "birthday") String birthday) {
        userInformationRepo.updateBirthday(getDecode(birthday.getBytes(StandardCharsets.UTF_8)), getDecode(userid.getBytes(StandardCharsets.UTF_8)));
        return ResponseEntity.ok().body(new HashMap<>(){{
            put("status", "pass");
            put("birthday", birthday);
        }});
    }
    //Update user gender
    @PostMapping (value = "/account/gender/update", params = {"userid", "gender"})
    public ResponseEntity<HashMap<String, String>> updateUserGender (
            @Parameter(name = "userid", description = "Encode it to BASE64 before input")
            @RequestParam(value = "userid") String userid,
            @Parameter(name = "gender", description = "Encode it to BASE64 before input")
            @RequestParam(value = "gender") String gender) {
        userInformationRepo.updateGender(getDecode(gender.getBytes(StandardCharsets.UTF_8)), getDecode(userid.getBytes(StandardCharsets.UTF_8)));
        return ResponseEntity.ok().body(new HashMap<>(){{
            put("status", "pass");
            put("gender", gender);
        }});
    }
    //Update user avatar
    @PostMapping (value = "/account/avatar/update", params = {"userid", "avatar"})
    public ResponseEntity<HashMap<String, String>> updateUserAvatar (
            @Parameter(name = "userid", description = "Encode it to BASE64 before input")
            @RequestParam(value = "userid") String userid,
            @Parameter(name = "avatar", description = "Encode it to BASE64 before input")
            @RequestParam(value = "avatar") String avatar) {
        userInformationRepo.updateAvatar(getDecode(avatar.getBytes(StandardCharsets.UTF_8)), getDecode(userid.getBytes(StandardCharsets.UTF_8)));
        return ResponseEntity.ok().body(new HashMap<>(){{
            put("status", "pass");
            put("avatar", avatar);
        }});
    }
    //Recovery account with recovery code
    @GetMapping(value = "/account/recovery", params = {"code"})
    public ResponseEntity <HashMap<String, String>> RecoveryAccount(
            @Parameter(name = "code", description = "Encode it to BASE64 before input") String code) throws ResourceNotFound {
    userPassLogin = userPassLoginRepo.findByRecovery(getDecode(code.getBytes(StandardCharsets.UTF_8)))
            .orElseThrow(() -> new ResourceNotFound("Failed"));
    return ResponseEntity.ok().body(new HashMap<>() {{
        put("user_id", String.valueOf(userPassLogin.getUser_id()));
        put("email", userPassLogin.getEmail());
        put("password", userPassLogin.getPassword());
        put("salt", userPassLogin.getSalt());
        put("nickname", userPassLogin.getNickname());
        put("verify", userPassLogin.getVerify());
        put("recovery", userPassLogin.getVerify());
        put("status", "pass");
    }});
    }
}
