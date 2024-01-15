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

package com.notelysia.restservices.model.entity.newsapp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_login")
@SecondaryTables({
        @SecondaryTable(name = "sync_subscribe", pkJoinColumns = @PrimaryKeyJoinColumn(name = "user_id")),
        @SecondaryTable(name = "sync_news_favourite", pkJoinColumns = @PrimaryKeyJoinColumn(name = "user_id"))
})
public class UserLogin implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id")
    private int userId;
    @Column(name = "email")
    private String email;
    @Column(name = "user_token")
    private String userToken;
    @Column(name = "salt")
    private String salt;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "verify")
    private String verify;
    @Column(name = "recovery")
    private String recovery;
}
