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
import com.notelysia.restservices.service.newsapp.UserServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/news-app/user")
@Tag(name = "User Pass Login", description = "API for User Pass Login")
public class UserController {
    private final DecodeString decodeString = new DecodeString();
    private final String verify = "false";
    private final String date = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
    private UserPassLogin userPassLogin;
    private UserInformation userInformation;
    @Autowired
    private UserServices userServices;

    private String getDecode(byte[] data) {
        return this.decodeString.decodeString(data);
    }

    @PostMapping("/register")
    //Create user account
    public ResponseEntity<HashMap<String, String>> registerUser
            (@RequestParam(name = "fullName") String fullName,
             @RequestParam(name = "email") String email,
             @RequestParam(name = "password") String password,
             @RequestParam(name = "nickname") String nickname) {
        int userIdRandom = Integer.parseInt(new RandomNumber().generateRandomNumber());
        String recoveryCode = java.util.UUID.randomUUID().toString();
        //Bcrypt password
        String Salt = BCrypt.gensalt();
        String passwordHash = BCrypt.hashpw(this.getDecode(password.getBytes(StandardCharsets.UTF_8)), Salt);
        this.userPassLogin = new UserPassLogin(userIdRandom, this.getDecode(email.getBytes(StandardCharsets.UTF_8)), passwordHash, Salt, this.getDecode(nickname.getBytes(StandardCharsets.UTF_8)), this.verify, recoveryCode);
        this.userInformation = new UserInformation(userIdRandom, this.getDecode(fullName.getBytes(StandardCharsets.UTF_8)), "not_input", this.date, "not_available");
        this.userServices.saveUser(this.userPassLogin);
        this.userServices.saveInformation(this.userInformation);
        return ResponseEntity.ok().body(new HashMap<>() {{
            this.put("userId", String.valueOf(UserController.this.userPassLogin.getUserId()));
            this.put("fullName", UserController.this.userInformation.getName());
            this.put("email", UserController.this.userPassLogin.getEmail());
            this.put("nickname", UserController.this.userPassLogin.getNickname());
            this.put("verify", UserController.this.userPassLogin.getVerify());
            this.put("recovery", UserController.this.userPassLogin.getRecovery());
            this.put("status", "success");
        }});
    }

    //Verify email from Firebase Authentication, if true, update verify to true
    @PostMapping("/verify-email")
    public ResponseEntity<HashMap<String, String>> verifyEmail(@RequestParam(name = "email") String email) {
        this.userServices.updateVerify("true", this.getDecode(email.getBytes(StandardCharsets.UTF_8)));
        return ResponseEntity.ok().body(new HashMap<>() {
            {
                this.put("email", UserController.this.getDecode(email.getBytes(StandardCharsets.UTF_8)));
                this.put("status", "success");
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
        this.userPassLogin = this.userServices.findByEmailOrNickname(this.getDecode(account.getBytes(StandardCharsets.UTF_8)), this.getDecode(account.getBytes(StandardCharsets.UTF_8)))
                .orElseThrow(() -> new ResourceNotFound("Failed"));
        this.userInformation = this.userServices.findInformationByUserId(String.valueOf(this.userPassLogin.getUserId()))
                .orElseThrow(() -> new ResourceNotFound("Failed"));
        String password_hash = this.userPassLogin.getPassword();
        String salt = this.userPassLogin.getSalt();
        String password_check = BCrypt.hashpw(this.getDecode(password.getBytes(StandardCharsets.UTF_8)), salt);
        if (password_check.equals(password_hash)) {
            userFound.put("userId", String.valueOf(this.userPassLogin.getUserId()));
            userFound.put("email", this.userPassLogin.getEmail());
            userFound.put("nickName", this.userPassLogin.getNickname());
            userFound.put("verify", this.userPassLogin.getVerify());
            userFound.put("fullName", this.userInformation.getName());
            userFound.put("birthday", this.userInformation.getBirthday());
            userFound.put("gender", this.userInformation.getGender());
            userFound.put("avatar", this.userInformation.getAvatar());
            userFound.put("status", "success");
            return ResponseEntity.ok().body(userFound);
        } else {
            userFound.put("status", "fail");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userFound);
        }
    }

    //Sign in with Google account
    //Why gender and birthday not input? Because it is private information about each user.
    // Firebase do not have function to get the user's gender/birthday
    @GetMapping("/sign-in-by-google")
    public ResponseEntity<HashMap<String, String>> signInByGoogle
    (@RequestParam(value = "fullName") String fullName,
     @RequestParam(value = "email") String email,
     @RequestParam(value = "nickName") String nickname,
     @RequestParam(value = "avatar") String avatar) throws ResourceNotFound {
        //First, check if user already have account or not
        //If not, create new account
        HashMap<String, String> userFound = new HashMap<>();
        Optional<UserPassLogin> userPassLoginOption = this.userServices.findByEmailOrNickname(
                this.getDecode(email.getBytes()),
                this.getDecode(nickname.getBytes()));
        if (userPassLoginOption.isPresent()) {
            //Sign in into system
            this.userInformation = this.userServices.findInformationByUserId(
                            String.valueOf(userPassLoginOption.get().getUserId()))
                    .orElseThrow(() -> new ResourceNotFound("Failed"));
            userFound.put("userId", String.valueOf(userPassLoginOption.get().getUserId()));
            userFound.put("email", userPassLoginOption.get().getEmail());
            userFound.put("nickname", userPassLoginOption.get().getNickname());
            userFound.put("verify", userPassLoginOption.get().getVerify());
            userFound.put("fullName", this.userInformation.getName());
            userFound.put("birthday", this.userInformation.getBirthday());
            userFound.put("gender", this.userInformation.getGender());
            userFound.put("avatar", this.userInformation.getAvatar());
            userFound.put("status", "success");
        } else {
            //Save the user into database, then sign in into system
            //In the app will get a popup to create the password for the account
            String userIdRandom = new RandomNumber().generateSSONumber();
            this.userPassLogin = new UserPassLogin(Integer.parseInt(userIdRandom), this.getDecode(email.getBytes()), this.getDecode(nickname.getBytes()), "true");
            this.userInformation = new UserInformation(Integer.parseInt(userIdRandom), this.getDecode(fullName.getBytes()), "not_input", this.date, this.getDecode(avatar.getBytes()));
            this.userServices.saveUser(this.userPassLogin);
            this.userServices.saveInformation(this.userInformation);
            userFound.put("userId", String.valueOf(this.userPassLogin.getUserId()));
            userFound.put("email", this.userPassLogin.getEmail());
            userFound.put("nickname", this.userPassLogin.getNickname());
            userFound.put("verify", this.userPassLogin.getVerify());
            userFound.put("fullName", this.userInformation.getName());
            userFound.put("birthday", this.userInformation.getBirthday());
            userFound.put("gender", this.userInformation.getGender());
            userFound.put("avatar", this.userInformation.getAvatar());
            userFound.put("status", "success");
        }
        return ResponseEntity.ok().body(userFound);
    }

    //Make sure nickname and email is available to use
    @GetMapping("/verify-nickname")
    public ResponseEntity<HashMap<String, String>> CheckNickname(
            @RequestParam(name = "nickname") String nickname,
            @RequestParam(name = "email") String email) {
        HashMap<String, String> resultRespond = new HashMap<>();
        long numberOfNickName = this.userServices.countNickName(this.getDecode(nickname.getBytes(StandardCharsets.UTF_8)),
                this.getDecode(email.getBytes(StandardCharsets.UTF_8)));
        if (numberOfNickName == 0) {
            resultRespond.put("nickName", this.getDecode(nickname.getBytes(StandardCharsets.UTF_8)));
            resultRespond.put("status", "success");
            return ResponseEntity.ok().body(resultRespond);
        } else {
            resultRespond.put("nickName", this.getDecode(nickname.getBytes(StandardCharsets.UTF_8)));
            resultRespond.put("status", "fail");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultRespond);
        }

    }

    //Show recovery code to user
    @GetMapping(value = "/recovery-code", params = {"email"})
    public ResponseEntity<HashMap<String, String>> recoveryCode(
            @RequestParam(name = "email") String email) throws ResourceNotFound {
        this.userPassLogin = this.userServices.findByRecovery(this.getDecode(email.getBytes(StandardCharsets.UTF_8)))
                .orElseThrow(() -> new ResourceNotFound("Failed"));
        return ResponseEntity.ok().body(new HashMap<>() {{
            this.put("recovery", UserController.this.userPassLogin.getRecovery());
        }});
    }

    //Generate recovery code from user
    @PostMapping(value = "/generate-recovery", params = {"userid"})
    public ResponseEntity<HashMap<String, String>> generateRecoveryCode(
            @RequestParam(value = "userId") String userId) {
        String new_recovery_code = java.util.UUID.randomUUID().toString();
        this.userServices.updateRecovery(new_recovery_code, userId);
        return ResponseEntity.ok().body(new HashMap<>() {{
            this.put("recovery", new_recovery_code);
        }});
    }

    //Update password form request forgot password and change password form in settings
    @PostMapping("/change-password")
    public ResponseEntity<HashMap<String, String>> changePassword(
            @RequestParam(name = "userid") String userId,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "oldPass") String oldPass,
            @RequestParam(name = "newPass") String newPass) throws ResourceNotFound {
        this.userPassLogin = this.userServices.findByEmailOrUserid(this.getDecode(email.getBytes(StandardCharsets.UTF_8)),
                        this.getDecode(userId.getBytes(StandardCharsets.UTF_8)))
                .orElseThrow(() -> new ResourceNotFound("Failed"));
        String oldPasswordHash = this.userPassLogin.getPassword();
        String salt = this.userPassLogin.getSalt();
        String passwordCheck = BCrypt.hashpw(this.getDecode(oldPass.getBytes(StandardCharsets.UTF_8)), salt);
        String recoveryCode = java.util.UUID.randomUUID().toString();
        //Bcrypt password
        String Salt = BCrypt.gensalt();
        String newPasswordHash = BCrypt.hashpw(this.getDecode(newPass.getBytes(StandardCharsets.UTF_8)), Salt);
        if (passwordCheck.equals(oldPasswordHash)) {
            this.userServices.updatePassword(newPasswordHash, Salt, recoveryCode, this.getDecode(email.getBytes(StandardCharsets.UTF_8)));
            return ResponseEntity.ok().body(new HashMap<>() {{
                this.put("status", "success");
            }});
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashMap<>() {{
                this.put("status", "fail");
            }});
        }
    }

    //Update user information
    @PostMapping("/update")
    public ResponseEntity<HashMap<String, String>> updateUserInformation(
            @RequestParam(value = "userid") String userid,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "fullName", required = false) String fullName,
            @RequestParam(value = "birthday", required = false) String birthday,
            @RequestParam(value = "gender", required = false) String gender,
            @RequestParam(value = "avatar", required = false) String avatar) {
        HashMap<String, String> resultRespond = new HashMap<>();
        if (username != null) {
            this.userServices.updateNickname(this.getDecode(username.getBytes(StandardCharsets.UTF_8)),
                    this.getDecode(userid.getBytes(StandardCharsets.UTF_8)));
            resultRespond.put("UserName", username);
        }

        if (fullName != null) {
            this.userServices.updateFullName(this.getDecode(fullName.getBytes(StandardCharsets.UTF_8)),
                    this.getDecode(userid.getBytes(StandardCharsets.UTF_8)));
            resultRespond.put("fullName", fullName);
        }

        if (birthday != null) {
            this.userServices.updateBirthday(this.getDecode(birthday.getBytes(StandardCharsets.UTF_8)),
                    this.getDecode(userid.getBytes(StandardCharsets.UTF_8)));
            resultRespond.put("birthday", birthday);
        }

        if (gender != null) {
            this.userServices.updateGender(this.getDecode(gender.getBytes(StandardCharsets.UTF_8)),
                    this.getDecode(userid.getBytes(StandardCharsets.UTF_8)));
            resultRespond.put("gender", gender);
        }

        if (avatar != null) {
            this.userServices.updateAvatar(this.getDecode(avatar.getBytes(StandardCharsets.UTF_8)),
                    this.getDecode(userid.getBytes(StandardCharsets.UTF_8)));
            resultRespond.put("avatar", avatar);
        }

        resultRespond.put("status", "success");
        return ResponseEntity.ok().body(resultRespond);
    }

    //Recovery account with recovery code
    @GetMapping(value = "/recovery", params = {"code"})
    public ResponseEntity<HashMap<String, String>> RecoveryAccount(
            @RequestParam(name = "code") String code) throws ResourceNotFound {
        this.userPassLogin = this.userServices.findByRecovery(this.getDecode(code.getBytes(StandardCharsets.UTF_8)))
                .orElseThrow(() -> new ResourceNotFound("Failed"));
        return ResponseEntity.ok().body(new HashMap<>() {{
            this.put("userId", String.valueOf(UserController.this.userPassLogin.getUserId()));
            this.put("email", UserController.this.userPassLogin.getEmail());
            this.put("nickname", UserController.this.userPassLogin.getNickname());
            this.put("verify", UserController.this.userPassLogin.getVerify());
            this.put("recovery", UserController.this.userPassLogin.getRecovery());
            this.put("status", "success");
        }});
    }
}
