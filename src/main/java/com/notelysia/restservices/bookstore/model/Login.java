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

package com.notelysia.restservices.bookstore.model;

import com.notelysia.restservices.bookstore.model.CompositeKey.LoginPK;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "Login")
@IdClass(LoginPK.class)
public class Login {
    @Id
    @Column(name = "MaNV")
    private String id;
    @Id
    @Column(name = "Username")
    private String username;
    @Column(name = "Password")
    private String password;
    @Column(name = "AccLevel")
    private int accLevel;
    @Column(name = "RecKey")
    private String recKey;
    @Column(name = "Salt")
    private String salt;

    public Login() {
    }

    public Login(String id, String username, String password, int accLevel, String recKey, String salt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.accLevel = accLevel;
        this.recKey = recKey;
        this.salt = salt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccLevel(int accLevel) {
        this.accLevel = accLevel;
    }

    public void setRecKey(String recKey) {
        this.recKey = recKey;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
