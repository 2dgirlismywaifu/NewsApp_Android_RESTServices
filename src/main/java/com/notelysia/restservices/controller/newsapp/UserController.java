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

package com.notelysia.restservices.controller.newsapp;

import com.notelysia.restservices.config.DecodeString;
import com.notelysia.restservices.config.RandomNumber;
import com.notelysia.restservices.exception.ResourceNotFound;
import com.notelysia.restservices.model.entity.newsapp.UserInformation;
import com.notelysia.restservices.model.entity.newsapp.UserPassLogin;
import com.notelysia.restservices.model.entity.newsapp.UserSSO;
import com.notelysia.restservices.service.newsapp.UserServices;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v2")
@Tag(name = "User Pass Login", description = "API for User Pass Login")
public class UserController {
    private final DecodeString decodeString = new DecodeString();
    private final String verify = "false";
    private final String date = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
    private UserPassLogin userPassLogin;
    private UserSSO userSSO;
    private UserInformation userInformation;
    @Autowired
    private UserServices userServices;

    private String getDecode(byte[] data) {
        return decodeString.decodeString(data);
    }

    @PostMapping("/register")
    //Create user account
    //Add description to each RequestParam
    //Post method not show RequestParam in swagger, use @RequestBody instead
    public ResponseEntity<HashMap<String, String>> registerUser
            (@RequestParam(name = "fullName") String fullName,
             @RequestParam(name = "email") String email,
             @RequestParam(name = "password") String password,
             @RequestParam(name = "nickname") String nickname) {
        int userIdRandom = Integer.parseInt(new RandomNumber().generateRandomNumber());
        String recovery_code = java.util.UUID.randomUUID().toString();
        //Bcrypt password
        String Salt = BCrypt.gensalt();
        String passwordHash = BCrypt.hashpw(getDecode(password.getBytes(StandardCharsets.UTF_8)), Salt);
        userPassLogin = new UserPassLogin(userIdRandom, getDecode(email.getBytes(StandardCharsets.UTF_8)), passwordHash, Salt, getDecode(nickname.getBytes(StandardCharsets.UTF_8)), verify, recovery_code);
        userInformation = new UserInformation(userIdRandom, getDecode(fullName.getBytes(StandardCharsets.UTF_8)), "not_input", date, "not_available");
        userServices.saveUser(userPassLogin);
        userServices.saveInformation(userInformation);
        return ResponseEntity.ok().body(new HashMap<>() {{
            put("userId", String.valueOf(userPassLogin.getUserId()));
            put("email", userPassLogin.getEmail());
            put("nickname", userPassLogin.getNickname());
            put("verify", userPassLogin.getVerify());
            put("status", "pass");
        }});
    }

    //Verify email from Firebase Authentication, if true, update verify to true
    @PostMapping("/verify")
    public ResponseEntity<HashMap<String, String>> verifyEmail(@RequestParam(name = "email") String email) {
        userServices.updateVerify("true", getDecode(email.getBytes(StandardCharsets.UTF_8)));
        return ResponseEntity.ok().body(new HashMap<>() {
            {
                put("email", getDecode(email.getBytes(StandardCharsets.UTF_8)));
                put("status", "pass");
            }
        });
    }

    //Sign in with user or email account and password
    @GetMapping(value = "/sign-in")
    public ResponseEntity<HashMap<String, String>> signIn(
            @RequestParam(name = "account") String account,
            @RequestParam(name = "password") String password)
            throws ResourceNotFound {
        HashMap<String, String> userFound = new HashMap<>();
        userPassLogin = userServices.findByEmailOrNickname(getDecode(account.getBytes(StandardCharsets.UTF_8)), getDecode(account.getBytes(StandardCharsets.UTF_8)))
                .orElseThrow(() -> new ResourceNotFound("Failed"));
        String password_hash = userPassLogin.getPassword();
        String salt = userPassLogin.getSalt();
        String password_check = BCrypt.hashpw(getDecode(password.getBytes(StandardCharsets.UTF_8)), salt);
        if (password_check.equals(password_hash)) {
            userFound.put("user_id", String.valueOf(userPassLogin.getUserId()));
            userFound.put("email", userPassLogin.getEmail());
            userFound.put("nickname", userPassLogin.getNickname());
            userFound.put("verify", userPassLogin.getVerify());
            userFound.put("status", "pass");
            return ResponseEntity.ok().body(userFound);
        } else {
            userFound.put("status", "fail");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userFound);
        }
    }

    //Update password form request forgot password and change password form in settings
    @PostMapping("/change-password")
    public void changePassword(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "password") String password) {
        String recovery_code = java.util.UUID.randomUUID().toString();
        //Bcrypt password
        String Salt = BCrypt.gensalt();
        String password_hash = BCrypt.hashpw(getDecode(password.getBytes(StandardCharsets.UTF_8)), Salt);
        userServices.updatePassword(password_hash, Salt, recovery_code, getDecode(email.getBytes(StandardCharsets.UTF_8)));
    }

    //Make sure nickname and email is available to use
    @GetMapping("/verify-nickname")
    public ResponseEntity<HashMap<String, String>> CheckNickname(
            @RequestParam(name = "nickname") String nickname,
            @RequestParam(name = "email") String email) throws ResourceNotFound {
        userPassLogin = userServices.findByNickname(getDecode(nickname.getBytes(StandardCharsets.UTF_8)),
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
            @RequestParam(name = "email") String email) throws ResourceNotFound {
        userPassLogin = userServices.findByRecovery(getDecode(email.getBytes(StandardCharsets.UTF_8)))
                .orElseThrow(() -> new ResourceNotFound("Failed"));
        return ResponseEntity.ok().body(new HashMap<>() {{
            put("recovery", userPassLogin.getRecovery());
        }});
    }

    //Generate recovery code from user
    @PostMapping(value = "/generaterecoverycode", params = {"userid"})
    public ResponseEntity<HashMap<String, String>> generateRecoveryCode(
            @RequestParam(value = "userId") String userId) {
        String new_recovery_code = java.util.UUID.randomUUID().toString();
        userServices.updateRecovery(new_recovery_code, userId);
        return ResponseEntity.ok().body(new HashMap<>() {{
            put("recovery", new_recovery_code);
            put("status", "pass");
        }});
    }


    //Other settings is: Avatar Account, User Information like real name, birthday, gender, etc
    //Most important is user can change password
    //Before allow change password, first check old password is correct or not
    @GetMapping("/account/password/check")
    public ResponseEntity<HashMap<String, String>> checkOldPassword(
            @RequestParam(name = "userid") String userId,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "oldPass") String oldPass) throws ResourceNotFound {
        userPassLogin = userServices.findByEmailOrUserid(getDecode(email.getBytes(StandardCharsets.UTF_8)),
                        getDecode(userId.getBytes(StandardCharsets.UTF_8)))
                .orElseThrow(() -> new ResourceNotFound("Failed"));
        String password_hash = userPassLogin.getPassword();
        String salt = userPassLogin.getSalt();
        String password_check = BCrypt.hashpw(getDecode(oldPass.getBytes(StandardCharsets.UTF_8)), salt);
        if (password_check.equals(password_hash)) {
            return ResponseEntity.ok().body(new HashMap<>() {{
                put("status", "pass");
            }});
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashMap<>() {{
                put("status", "fail");
            }});
        }
    }

    //Update user name
    @PostMapping("/account/username/update")
    public ResponseEntity<HashMap<String, String>> updateUserName(
            @RequestParam(value = "userid") String userid,
            @RequestParam(value = "username") String username) {
        userServices.updateNickname(getDecode(username.getBytes(StandardCharsets.UTF_8)),
                getDecode(userid.getBytes(StandardCharsets.UTF_8)));
        return ResponseEntity.ok().body(new HashMap<>() {{
            put("status", "pass");
            put("username", username);
        }});
    }

    //Update user fullName
    @PostMapping("/account/full-name/update")
    public ResponseEntity<HashMap<String, String>> updateUserFullName(
            @RequestParam(value = "userid") String userid,
            @RequestParam(value = "fullname") String fullname) {
        userServices.updateFullName(getDecode(fullname.getBytes(StandardCharsets.UTF_8)),
                getDecode(userid.getBytes(StandardCharsets.UTF_8)));
        return ResponseEntity.ok().body(new HashMap<>() {{
            put("status", "pass");
            put("username", fullname);
        }});
    }

    //Update user birthday
    @PostMapping("/account/birthday/update")
    public ResponseEntity<HashMap<String, String>> updateUserBirthday(
            @RequestParam(value = "userid") String userid,
            @RequestParam(value = "birthday") String birthday) {
        userServices.updateBirthday(getDecode(birthday.getBytes(StandardCharsets.UTF_8)),
                getDecode(userid.getBytes(StandardCharsets.UTF_8)));
        return ResponseEntity.ok().body(new HashMap<>() {{
            put("status", "pass");
            put("birthday", birthday);
        }});
    }

    //Update user gender
    @PostMapping(value = "/account/gender/update")
    public ResponseEntity<HashMap<String, String>> updateUserGender(
            @RequestParam(value = "userid") String userid,
            @RequestParam(value = "gender") String gender) {
        userServices.updateGender(getDecode(gender.getBytes(StandardCharsets.UTF_8)),
                getDecode(userid.getBytes(StandardCharsets.UTF_8)));
        return ResponseEntity.ok().body(new HashMap<>() {{
            put("status", "pass");
            put("gender", gender);
        }});
    }

    //Update user avatar
    @PostMapping(value = "/account/avatar/update")
    public ResponseEntity<HashMap<String, String>> updateUserAvatar(
            @RequestParam(value = "userid") String userid,
            @RequestParam(value = "avatar") String avatar) {
        userServices.updateAvatar(getDecode(avatar.getBytes(StandardCharsets.UTF_8)),
                getDecode(userid.getBytes(StandardCharsets.UTF_8)));
        return ResponseEntity.ok().body(new HashMap<>() {{
            put("status", "pass");
            put("avatar", avatar);
        }});
    }

    //Recovery account with recovery code
    @GetMapping(value = "/account/recovery", params = {"code"})
    public ResponseEntity<HashMap<String, String>> RecoveryAccount(
            @RequestParam(name = "code") String code) throws ResourceNotFound {
        userPassLogin = userServices.findByRecovery(getDecode(code.getBytes(StandardCharsets.UTF_8)))
                .orElseThrow(() -> new ResourceNotFound("Failed"));
        return ResponseEntity.ok().body(new HashMap<>() {{
            put("user_id", String.valueOf(userPassLogin.getUserId()));
            put("email", userPassLogin.getEmail());
            put("password", userPassLogin.getPassword());
            put("salt", userPassLogin.getSalt());
            put("nickname", userPassLogin.getNickname());
            put("verify", userPassLogin.getVerify());
            put("recovery", userPassLogin.getVerify());
            put("status", "pass");
        }});
    }

    //Create user account
    //Why gender and birthday not input? Because it is private information about each user.
    // Firebase do not have function to get the user's gender/birthdate
    @PostMapping("/sso")
    public ResponseEntity<HashMap<String, String>> registerUserSso
    (@RequestParam(value = "fullName") String fullName,
     @RequestParam(value = "email") String email,
     @RequestParam(value = "nickname") String nickname,
     @RequestParam(value = "avatar") String avatar) {
        String user_id_random = new RandomNumber().generateSSONumber();
        userSSO = new UserSSO(Integer.parseInt(user_id_random), getDecode(email.getBytes()), getDecode(nickname.getBytes()), verify);
        userInformation = new UserInformation(Integer.parseInt(user_id_random), getDecode(fullName.getBytes()), "not_input", date, getDecode(avatar.getBytes()));
        userServices.saveSSO(userSSO);
        userServices.saveInformation(userInformation);
        return ResponseEntity.ok().body(new HashMap<>() {{
            put("user_id", String.valueOf(userSSO.getUserId()));
            put("fullname", userInformation.getName());
            put("email", userSSO.getEmail());
            put("nickname", userSSO.getNickname());
            put("verify", verify);
            put("status", "success");
        }});
    }

    //Update user information
    @PostMapping(value = "/sso/update")
    public ResponseEntity<HashMap<String, String>> updateUser(
            @RequestParam(value = "user_id") String user_id,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "avatar") String avatar) {
        userServices.updateNickname(getDecode(name.getBytes()), getDecode(user_id.getBytes()));
        userServices.updateAvatar(getDecode(avatar.getBytes()), getDecode(user_id.getBytes()));
        return ResponseEntity.ok().body(new HashMap<>() {{
            put("user_id", getDecode(user_id.getBytes()));
            put("nickname", getDecode(name.getBytes()));
            put("avatar", getDecode(avatar.getBytes()));
            put("status", "pass");
        }});
    }

    //Update user birthday
    @PostMapping(value = "/sso/birthday/update")
    public ResponseEntity<HashMap<String, String>> updateSSOBirthday(
            @Parameter(name = "userid", description = "Encode it to BASE64 before input")
            @RequestParam(value = "userid") String userid,
            @Parameter(name = "birthday", description = "Encode it to BASE64 before input")
            @RequestParam(value = "birthday") String birthday) {
        userServices.updateBirthday(getDecode(birthday.getBytes(StandardCharsets.UTF_8)), getDecode(userid.getBytes(StandardCharsets.UTF_8)));
        return ResponseEntity.ok().body(new HashMap<>() {{
            put("status", "pass");
            put("birthday", birthday);
        }});
    }

    //Update user gender
    @PostMapping(value = "/sso/gender/update")
    public ResponseEntity<HashMap<String, String>> updateSSOGender(
            @Parameter(name = "userid", description = "Encode it to BASE64 before input")
            @RequestParam(value = "userid") String userid,
            @Parameter(name = "gender", description = "Encode it to BASE64 before input")
            @RequestParam(value = "gender") String gender) {
        userServices.updateGender(getDecode(gender.getBytes(StandardCharsets.UTF_8)), getDecode(userid.getBytes(StandardCharsets.UTF_8)));
        return ResponseEntity.ok().body(new HashMap<>() {{
            put("status", "pass");
            put("gender", gender);
        }});
    }
}
