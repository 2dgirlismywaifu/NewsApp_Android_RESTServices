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

package com.notelysia.newsandroidservices.model;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.HashMap;

@Getter
@Entity
@Table(name = "USER_PASSLOGIN")
public class UserPassLogin {
    @Id
    @Column(name = "user_id")
    private int user_id;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name="salt")
    private String salt;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "verify")
    private String verify;
    @Column(name = "recovery")
    private String recovery;

    public UserPassLogin(int user_id, String email, String password, String salt, String nickname, String verify, String recovery) {
        this.user_id = user_id;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.nickname = nickname;
        this.verify = verify;
        this.recovery = recovery;
    }

    public UserPassLogin() {
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public void setRecovery(String recovery) {
        this.recovery = recovery;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public String getNickname() {
        return nickname;
    }

    public String getVerify() {
        return verify;
    }

    public String getRecovery() {
        return recovery;
    }
}
