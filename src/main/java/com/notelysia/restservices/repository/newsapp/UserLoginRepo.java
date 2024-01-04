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

import com.notelysia.restservices.model.entity.newsapp.UserPassLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserLoginRepo extends JpaRepository<UserPassLogin, Long> {
    //Get password_hash from email and user_id
    @Query("FROM UserPassLogin userPassLogin WHERE userPassLogin.email = ?1 OR userPassLogin.userId = ?2")
    Optional<UserPassLogin> findByEmailOrUserid(String email, String userId);

    //Verify account
    @Transactional
    @Modifying
    @Query("UPDATE UserPassLogin userPassLogin " +
            "SET userPassLogin.verify = ?1 WHERE userPassLogin.email = ?2")
    void updateVerify(String verify, String email);

    //Sigin in account
    @Query("FROM UserPassLogin  userPassLogin, UserInformation userInformation " +
            "WHERE userPassLogin.userId = userInformation.userId AND userPassLogin.email = ?1 OR userPassLogin.nickname = ?2")
    Optional<UserPassLogin> findByEmailOrNickname(String email, String nickname);

    //Update password from reset password
    @Transactional
    @Modifying
    @Query("UPDATE UserPassLogin userPassLogin " +
            "SET userPassLogin.password = ?1, userPassLogin.salt = ?2, userPassLogin.recovery = ?3 " +
            "WHERE userPassLogin.email = ?4")
    void updatePassword(String password, String salt, String recovery, String email);

    //Check nickname
    @Query("FROM UserPassLogin userPassLogin WHERE userPassLogin.nickname = ?1 and userPassLogin.email = ?2")
    Optional<UserPassLogin> findByNickname(String nickname, String email);

    //Get Recovery Code from email
    @Query("FROM UserPassLogin userPassLogin WHERE userPassLogin.email = ?1")
    Optional<UserPassLogin> findByRecovery(String recovery);

    //Update recovery code with user_id
    @Transactional
    @Modifying
    @Query("UPDATE UserPassLogin userPassLogin " +
            "SET userPassLogin.recovery = ?1 WHERE userPassLogin.userId = ?2")
    void updateRecovery(String recovery, String userId);

    //Update nickname user
    @Transactional
    @Modifying
    @Query("UPDATE UserPassLogin userPassLogin " +
            "SET userPassLogin.nickname = ?1 WHERE userPassLogin.userId = ?2")
    void updateNickname(String nickname, String userId);
}
