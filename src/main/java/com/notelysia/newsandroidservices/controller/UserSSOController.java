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

import com.notelysia.newsandroidservices.config.DecodeString;
import com.notelysia.newsandroidservices.config.RandomNumber;
import com.notelysia.newsandroidservices.jparepo.UserInformationSSORepo;
import com.notelysia.newsandroidservices.jparepo.UserSSORepo;
import com.notelysia.newsandroidservices.model.UserInformationSSO;
import com.notelysia.newsandroidservices.model.UserSSO;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v2")
@Tag(name = "User SSO", description = "API for User SSO")
public class UserSSOController {
    DecodeString decodeString = new DecodeString();
    UserSSO userSSO;
    UserInformationSSO userInformationSSO;
    @Autowired
    UserSSORepo userSSORepo;
    @Autowired
    UserInformationSSORepo userInformationSSORepo;
    private String getDecode (byte[] data) {
        return decodeString.decodeString(data);
    }
    public final String verify = "true"; //sso always verify
    public String date = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
    @PostMapping(value = "/sso", params = {"fullname","email", "nickname", "avatar"})
    //Create user account
    //Why gender and birthday not input? Because it is private information about each user.
    // Firebase do not have function to get the user's gender/birthdate
    public ResponseEntity<HashMap<String, String>> createUser
            (@Parameter(name = "fullname", description = "Encode it to BASE64 before input", required = true)
             @RequestParam(value = "fullname") String fullname,
             @Parameter(name = "email", description = "Encode it to BASE64 before input", required = true)
             @RequestParam(value = "email") String email,
             @Parameter(name = "nickname", description = "Encode it to BASE64 before input", required = true)
             @RequestParam(value = "nickname") String nickname,
             @Parameter(name = "avatar", description = "Encode it to BASE64 before input", required = true)
             @RequestParam(value = "avatar") String avatar) {
        String user_id_random = new RandomNumber().generateSSONumber();
        userSSO = new UserSSO(Integer.parseInt(user_id_random), getDecode(email.getBytes()), getDecode(nickname.getBytes()), verify);
        userInformationSSO = new UserInformationSSO(Integer.parseInt(user_id_random), getDecode(fullname.getBytes()), "not_input", date, getDecode(avatar.getBytes()));
        userSSORepo.save(userSSO);
        userInformationSSORepo.save(userInformationSSO);
        return ResponseEntity.ok().body(new HashMap<>() {{
            put("user_id", String.valueOf(userSSO.getUser_id()));
            put("fullname", userInformationSSO.getName());
            put("email", userSSO.getEmail());
            put("nickname", userSSO.getNickname());
            put("verify", verify);
            put("status", "success");
        }});
    }
    //Update user information
    @PostMapping(value = "/sso/update", params = {"user_id", "name", "avatar"})
    public ResponseEntity <HashMap<String, String>> updateUser(
            @Parameter(name = "user_id", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "user_id") String user_id,
            @Parameter(name = "name", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "name") String name,
            @Parameter(name = "avatar", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "avatar") String avatar) {
        userSSORepo.updateNickname(getDecode(name.getBytes()), getDecode(user_id.getBytes()));
        userInformationSSORepo.updateAvatar(getDecode(avatar.getBytes()), getDecode(user_id.getBytes()));
        return ResponseEntity.ok().body(new HashMap<>(){{
            put("user_id", getDecode(user_id.getBytes()));
            put("nickname", getDecode(name.getBytes()));
            put("avatar", getDecode(avatar.getBytes()));
            put("status", "pass");
        }});
    }
    @GetMapping(value = "/sso/count", params = {"email"})
    public ResponseEntity <HashMap<String, String>> countUser(
            @Parameter(name = "email", description = "Encode it to BASE64 before input") String email) {
        HashMap<String, String> userFound = new HashMap<>();
        if (userSSORepo.countEmail(getDecode(email.getBytes())) > 1
                && userInformationSSORepo.countUserId(getDecode(email.getBytes())) > 1) {
            userFound.put("status", "duplicate");
        }
        else {
            userFound.put("status", "pass");
        }
        return ResponseEntity.ok().body(userFound);
    }
//Update user birthday
    @PostMapping (value = "/sso/birthday/update", params = {"userid", "birthday"})
    public ResponseEntity<HashMap<String, String>> updateSSOBirthday (
            @Parameter(name = "userid", description = "Encode it to BASE64 before input")
            @RequestParam(value = "userid") String userid,
            @Parameter(name = "birthday", description = "Encode it to BASE64 before input")
            @RequestParam(value = "birthday") String birthday) {
        userInformationSSORepo.updateBirthday(getDecode(birthday.getBytes(StandardCharsets.UTF_8)), getDecode(userid.getBytes(StandardCharsets.UTF_8)));
        return ResponseEntity.ok().body(new HashMap<>(){{
            put("status", "pass");
            put("birthday", birthday);
        }});
    }
    //Update user gender
    @PostMapping (value = "/sso/gender/update", params = {"userid", "gender"})
    public ResponseEntity<HashMap<String, String>> updateSSOGender (
            @Parameter(name = "userid", description = "Encode it to BASE64 before input")
            @RequestParam(value = "userid") String userid,
            @Parameter(name = "gender", description = "Encode it to BASE64 before input")
            @RequestParam(value = "gender") String gender) {
        userInformationSSORepo.updateGender(getDecode(gender.getBytes(StandardCharsets.UTF_8)), getDecode(userid.getBytes(StandardCharsets.UTF_8)));
        return ResponseEntity.ok().body(new HashMap<>(){{
            put("status", "pass");
            put("gender", gender);
        }});
    }
}
