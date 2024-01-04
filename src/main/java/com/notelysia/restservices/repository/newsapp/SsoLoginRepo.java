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

package com.notelysia.restservices.repository.newsapp;

import com.notelysia.restservices.model.entity.newsapp.UserSSO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SsoLoginRepo extends JpaRepository<UserSSO, Long> {
    //Update nickname user
    @Transactional
    @Modifying
    @Query("UPDATE UserSSO userPassLogin " +
            "SET userPassLogin.nickname = ?1 WHERE userPassLogin.userId = ?2")
    void updateNickname(String nickname, String userId);

    //Count email user to make sure no duplicate email happen
    @Query("SELECT COUNT(userSSO.email) FROM UserSSO userSSO WHERE userSSO.email = ?1")
    long countEmail(String email);
}
