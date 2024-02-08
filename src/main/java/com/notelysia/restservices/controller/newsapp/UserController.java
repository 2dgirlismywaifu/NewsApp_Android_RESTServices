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
import com.notelysia.restservices.model.dto.newsapp.UserDto;
import com.notelysia.restservices.model.dto.newsapp.UserNameAndEmailDto;
import com.notelysia.restservices.model.entity.newsapp.UserInformation;
import com.notelysia.restservices.model.entity.newsapp.UserLogin;
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
    private final String date = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
    private UserLogin userLogin;
    private UserInformation userInformation;
    @Autowired
    private UserServices userServices;

    private String getDecode(byte[] data) {
        return this.decodeString.decodeString(data);
    }

    @PostMapping("/register")
    //Create user account
    public ResponseEntity<UserDto> registerUser
            (@RequestParam(name = "fullName") String fullName,
             @RequestParam(name = "email") String email,
             @RequestParam(name = "userToken") String userToken,
             @RequestParam(name = "nickname") String nickname) {
        int userIdRandom = Integer.parseInt(new RandomNumber().generateRandomNumber());
        String recoveryCode = java.util.UUID.randomUUID().toString();
        //Bcrypt userToken
        String Salt = BCrypt.gensalt();
        String userTokenHash = BCrypt.hashpw(userToken, Salt);
        String verify = "false";
        this.userLogin = new UserLogin(userIdRandom, email,
                userTokenHash, Salt, nickname, verify, recoveryCode, 0);
        this.userInformation = new UserInformation(userIdRandom, fullName, "not_input", this.date, "not_available", 0);
        this.userServices.saveUser(this.userLogin);
        this.userServices.saveInformation(this.userInformation);

        UserDto userDto = new UserDto();
        userDto.setUserId(String.valueOf(this.userLogin.getUserId()));
        userDto.setEmail(this.userLogin.getEmail());
        userDto.setNickName(this.userLogin.getNickname());
        userDto.setVerify(this.userLogin.getVerify());
        userDto.setFullName(this.userInformation.getName());
        userDto.setBirthday(this.userInformation.getBirthday());
        userDto.setGender(this.userInformation.getGender());
        userDto.setAvatar(this.userInformation.getAvatar());
        userDto.setStatus("success");
        userDto.setTime(String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok().body(userDto);
    }

    //Verify email from Firebase Authentication, if true, update verify to true
    @PostMapping("/verify-email")
    public ResponseEntity<HashMap<String, String>> verifyEmail(@RequestParam(name = "email") String email) {
        this.userServices.updateVerify("true", email);
        return ResponseEntity.ok().body(new HashMap<>() {
            {
                this.put("email", email);
                this.put("status", "success");
            }
        });
    }

    //Sign in with user or email account and userToken
    @GetMapping(value = "/sign-in")
    public ResponseEntity<UserDto> signIn(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "userToken") String userToken)
            throws ResourceNotFound {
        UserDto userDto = new UserDto();
        this.userLogin = this.userServices.findByEmail(this.getDecode(email.getBytes(StandardCharsets.UTF_8)))
                .orElseThrow(() -> new ResourceNotFound("Failed"));
        this.userInformation = this.userServices.findInformationByUserId(String.valueOf(this.userLogin.getUserId()))
                .orElseThrow(() -> new ResourceNotFound("Failed"));
        String userTokenHash = this.userLogin.getUserToken();
        String salt = this.userLogin.getSalt();
        String userTokenCheck = BCrypt.hashpw(this.getDecode(userToken.getBytes()), salt);
        if (userTokenCheck.equals(userTokenHash)) {
            userDto.setUserId(String.valueOf(this.userLogin.getUserId()));
            userDto.setEmail(this.userLogin.getEmail());
            userDto.setNickName(this.userLogin.getNickname());
            userDto.setVerify(this.userLogin.getVerify());
            userDto.setFullName(this.userInformation.getName());
            userDto.setBirthday(this.userInformation.getBirthday());
            userDto.setGender(this.userInformation.getGender());
            userDto.setAvatar(this.userInformation.getAvatar());
            userDto.setStatus("success");
            userDto.setTime(String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.ok().body(userDto);
        } else {
            userDto.setTime(String.valueOf(System.currentTimeMillis()));
            userDto.setEmail(email);
            userDto.setStatus("fail");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userDto);
        }
    }

    //Sign in with Google account
    //Why gender and birthday not input? Because it is private information about each user.
    // Firebase do not have function to get the user's gender/birthday
    @GetMapping("/sign-in-by-google")
    public ResponseEntity<UserDto> signInByGoogle
    (@RequestParam(value = "fullName") String fullName,
     @RequestParam(value = "email") String email,
     @RequestParam(value = "userToken") String userToken,
     @RequestParam(value = "nickName") String nickname,
     @RequestParam(value = "avatar") String avatar) throws ResourceNotFound {
        //First, check if user already have account or not
        //If not, create new account
        UserDto userDto = new UserDto();
        Optional<UserLogin> userPassLoginOption = this.userServices.findByEmail(this.getDecode(email.getBytes()));
        if (userPassLoginOption.isPresent()) {
            String userSalt = userPassLoginOption.get().getSalt();
            String tempUserTokenHash = BCrypt.hashpw(this.getDecode(userToken.getBytes()), userSalt);
            //Sign in into system
            if (userPassLoginOption.get().getUserToken().equals(tempUserTokenHash)) {
                this.userInformation = this.userServices.findInformationByUserId(
                                String.valueOf(userPassLoginOption.get().getUserId()))
                        .orElseThrow(() -> new ResourceNotFound("Failed"));
                userDto.setUserId(String.valueOf(userPassLoginOption.get().getUserId()));
                userDto.setEmail(userPassLoginOption.get().getEmail());
                userDto.setNickName(userPassLoginOption.get().getNickname());
                userDto.setVerify(userPassLoginOption.get().getVerify());
                userDto.setFullName(this.userInformation.getName());
                userDto.setBirthday(this.userInformation.getBirthday());
                userDto.setGender(this.userInformation.getGender());
                userDto.setAvatar(this.userInformation.getAvatar());
                userDto.setStatus("success");
                userDto.setTime(String.valueOf(System.currentTimeMillis()));
            }
        } else {
            //Save the user into database, then sign in into system
            //Also it will create a user_token
            String salt = BCrypt.gensalt();
            int userIdRandom = Integer.parseInt(new RandomNumber().generateRandomNumber());
            String userTokenHash = BCrypt.hashpw(this.getDecode(userToken.getBytes()), salt);
            String recoveryCode = java.util.UUID.randomUUID().toString();
            this.userLogin = new UserLogin(userIdRandom,
                    this.getDecode(email.getBytes()),
                    userTokenHash, salt,
                    this.getDecode(nickname.getBytes()),
                    "true", recoveryCode, 0);
            this.userInformation = new UserInformation(userIdRandom, this.getDecode(fullName.getBytes()), "not_input", "not_input", this.getDecode(avatar.getBytes()), 0);
            this.userServices.saveUser(this.userLogin);
            this.userServices.saveInformation(this.userInformation);
            userDto.setUserId(String.valueOf(this.userLogin.getUserId()));
            userDto.setEmail(this.userLogin.getEmail());
            userDto.setNickName(this.userLogin.getNickname());
            userDto.setVerify(this.userLogin.getVerify());
            userDto.setFullName(this.userInformation.getName());
            userDto.setBirthday(this.userInformation.getBirthday());
            userDto.setGender(this.userInformation.getGender());
            userDto.setAvatar(this.userInformation.getAvatar());
            userDto.setStatus("success");
            userDto.setTime(String.valueOf(System.currentTimeMillis()));
        }
        return ResponseEntity.ok().body(userDto);
    }

    //Make sure nickname and email is available to use
    @GetMapping("/verify-nickname-email")
    public ResponseEntity<HashMap<String, String>> CheckNickname(
            @RequestParam(name = "nickname") String nickname,
            @RequestParam(name = "email") String email) {
        HashMap<String, String> resultRespond = new HashMap<>();
        Optional<UserNameAndEmailDto> countNickNameOrEmail = this.userServices.countNickNameOrEmail(this.getDecode(nickname.getBytes(StandardCharsets.UTF_8)),
                this.getDecode(email.getBytes(StandardCharsets.UTF_8)));
        resultRespond.put("time", String.valueOf(System.currentTimeMillis()));
        if (countNickNameOrEmail.isPresent()) {
            if (countNickNameOrEmail.get().getTotalEmail() > 0) {
                resultRespond.put("status", "used");
                resultRespond.put("email", this.getDecode(email.getBytes(StandardCharsets.UTF_8)));
                return ResponseEntity.ok().body(resultRespond);
            } else if (countNickNameOrEmail.get().getTotalNickName() > 0) {
                resultRespond.put("nickname", this.getDecode(nickname.getBytes(StandardCharsets.UTF_8)));
                resultRespond.put("status", "used");
                return ResponseEntity.ok().body(resultRespond);
            } else {
                resultRespond.put("status", "empty");
                return ResponseEntity.status(HttpStatus.OK).body(resultRespond);
            }
        } else {
            resultRespond.put("status", "empty");
            return ResponseEntity.status(HttpStatus.OK).body(resultRespond);
        }
    }

    //Show recovery code to user
    @GetMapping(value = "/recovery-code", params = {"email"})
    public ResponseEntity<HashMap<String, String>> recoveryCode(
            @RequestParam(name = "email") String email) throws ResourceNotFound {
        this.userLogin = this.userServices.findByRecovery(email)
                .orElseThrow(() -> new ResourceNotFound("Failed"));
        String recoveryCode = this.userLogin.getRecovery();
        if (recoveryCode != null) {
            return ResponseEntity.ok().body(new HashMap<>() {{
                this.put("status", "success");
                this.put("time", String.valueOf(System.currentTimeMillis()));
                this.put("recovery", recoveryCode);

            }});
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashMap<>() {{
                this.put("status", "fail");
                this.put("time", String.valueOf(System.currentTimeMillis()));
                this.put("recovery", "not-found");
            }});
        }
    }

    //Generate recovery code from user
    @PostMapping(value = "/generate-recovery", params = {"userId"})
    public ResponseEntity<HashMap<String, String>> generateRecoveryCode(
            @RequestParam(value = "userId") String userId) {
        String newRecoveryCode = java.util.UUID.randomUUID().toString();
        this.userServices.updateRecovery(newRecoveryCode, userId);
        return ResponseEntity.ok().body(new HashMap<>() {{
            this.put("status", "success");
            this.put("time", String.valueOf(System.currentTimeMillis()));
            this.put("recovery", newRecoveryCode);
        }});
    }

    //Update userToken form request forgot userToken and change userToken form in settings
    @PostMapping("/change-user-token")
    public ResponseEntity<HashMap<String, String>> changeUserToken(
            @RequestParam(name = "userId") String userId,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "newToken") String newToken) throws ResourceNotFound {
        this.userLogin = this.userServices.findByEmailOrUserid(this.getDecode(email.getBytes(StandardCharsets.UTF_8)),
                        this.getDecode(userId.getBytes(StandardCharsets.UTF_8)))
                .orElseThrow(() -> new ResourceNotFound("Failed"));
        String recoveryCode = java.util.UUID.randomUUID().toString();
        String oldUserTokenHash = this.userLogin.getUserToken();
        String salt = this.userLogin.getSalt();
        String userTokenCheck = BCrypt.hashpw(this.getDecode(oldUserTokenHash.getBytes(StandardCharsets.UTF_8)), salt);
        //Bcrypt userToken
        String newSalt = BCrypt.gensalt();
        String newUserTokenHash = BCrypt.hashpw(this.getDecode(newToken.getBytes(StandardCharsets.UTF_8)), newSalt);
        if (userTokenCheck.equals(oldUserTokenHash)) {
            this.userServices.updateUserToken(newUserTokenHash, newSalt, recoveryCode, this.getDecode(email.getBytes(StandardCharsets.UTF_8)));
            return ResponseEntity.ok().body(new HashMap<>() {{
                this.put("status", "success");
                this.put("time", String.valueOf(System.currentTimeMillis()));
                this.put("userToken", "changed");
            }});
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashMap<>() {{
                this.put("status", "fail");
                this.put("time", String.valueOf(System.currentTimeMillis()));
                this.put("userToken", "not-changed");
            }});
        }
    }

    //Update user information
    @PostMapping("/update")
    public ResponseEntity<UserDto> updateUserInformation(
            @RequestParam(value = "userid") String userid,
            @RequestParam(value = "userName", required = false) String username,
            @RequestParam(value = "fullName", required = false) String fullName,
            @RequestParam(value = "birthday", required = false) String birthday,
            @RequestParam(value = "gender", required = false) String gender,
            @RequestParam(value = "avatar", required = false) String avatar) {
        UserDto userDto = new UserDto();
        userDto.setTime(String.valueOf(System.currentTimeMillis()));
        if (username != null && !username.isEmpty()) {
            this.userServices.updateNickname(this.getDecode(username.getBytes(StandardCharsets.UTF_8)),
                    this.getDecode(userid.getBytes(StandardCharsets.UTF_8)));
            userDto.setStatus("success");
            userDto.setNickName(this.getDecode(username.getBytes(StandardCharsets.UTF_8)));
        }

        if (fullName != null && !fullName.isEmpty()) {
            this.userServices.updateFullName(this.getDecode(fullName.getBytes(StandardCharsets.UTF_8)),
                    this.getDecode(userid.getBytes(StandardCharsets.UTF_8)));
            userDto.setStatus("success");
            userDto.setFullName(this.getDecode(fullName.getBytes(StandardCharsets.UTF_8)));
        }

        if (birthday != null && !birthday.isEmpty()) {
            this.userServices.updateBirthday(this.getDecode(birthday.getBytes(StandardCharsets.UTF_8)),
                    this.getDecode(userid.getBytes(StandardCharsets.UTF_8)));
            userDto.setStatus("success");
            userDto.setBirthday(this.getDecode(birthday.getBytes(StandardCharsets.UTF_8)));
        }

        if (gender != null && !gender.isEmpty()) {
            this.userServices.updateGender(this.getDecode(gender.getBytes(StandardCharsets.UTF_8)),
                    this.getDecode(userid.getBytes(StandardCharsets.UTF_8)));
            userDto.setStatus("success");
            userDto.setGender(this.getDecode(gender.getBytes(StandardCharsets.UTF_8)));
        }

        if (avatar != null && !avatar.isEmpty()) {
            this.userServices.updateAvatar(this.getDecode(avatar.getBytes(StandardCharsets.UTF_8)),
                    this.getDecode(userid.getBytes(StandardCharsets.UTF_8)));
            userDto.setStatus("success");
            userDto.setAvatar(this.getDecode(avatar.getBytes(StandardCharsets.UTF_8)));
        }
        return ResponseEntity.ok().body(userDto);
    }

    //Verify Recovery userToken
    @GetMapping(value = "/verify-recovery-code", params = {"code"})
    public ResponseEntity<UserDto> recoveryAccount(
            @RequestParam(name = "code") String code) throws ResourceNotFound {
        UserDto userDto = new UserDto();
        this.userLogin = this.userServices.findByRecovery(this.getDecode(code.getBytes(StandardCharsets.UTF_8)))
                .orElseThrow(() -> new ResourceNotFound("Failed"));
        this.userInformation = this.userServices.findInformationByUserId(String.valueOf(this.userLogin.getUserId()))
                .orElseThrow(() -> new ResourceNotFound("Failed"));
        userDto.setUserId(String.valueOf(this.userLogin.getUserId()));
        userDto.setEmail(this.userLogin.getEmail());
        userDto.setNickName(this.userLogin.getNickname());
        userDto.setVerify(this.userLogin.getVerify());
        userDto.setFullName(this.userInformation.getName());
        userDto.setBirthday(this.userInformation.getBirthday());
        userDto.setGender(this.userInformation.getGender());
        userDto.setAvatar(this.userInformation.getAvatar());
        userDto.setStatus("success");
        userDto.setTime(String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok().body(userDto);
    }
}
